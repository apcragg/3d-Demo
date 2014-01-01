#version 330
#define MAX_POINT_LIGHTS 64
#define MAX_SPOT_LIGHTS 8

layout(location = 0) out vec3 colorOUT;

in vec3 world_pos;
in vec3 object_normal;
in vec2 object_uvs;
in vec4 shadowCoord;
in mat3 tbnMatrix;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct AmbientLight
{
    BaseLight base;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

struct PointLight
{
	BaseLight base;
	vec3 pos;
	vec3 coefficient;
};

struct SpotLight
{
	float angle;
	vec3 direction;
	vec3 position;
	BaseLight base;
};

//Light uniforms
uniform DirectionalLight mainLight;
uniform AmbientLight ambient;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform int plNum;
uniform int slNum;

//textures
uniform sampler2D diffuseTex;
uniform sampler2D normalTex;
uniform sampler2D parallaxTex;
uniform sampler2D shadowTex;
uniform float textureScale;
uniform int parallaxMapping;

//materials
uniform int normalMap;
uniform int specExp;
uniform float specIntensity;
uniform vec3 specColor;

//Camera uniforms
in vec3 camera_Pos;

vec2 uvCoords = object_uvs / textureScale;
vec3 totalSpec = vec3(0f, 0f, 0f);
vec3 normal;
float attenuation = 1f;
float fogFalloff = 150f;

vec4 color;

vec4 textureColor()
{
    return texture2D(diffuseTex, uvCoords);
}

vec4 calculateSpecular(BaseLight base, vec3 direction)
{
    //cos of the angle of incidence. If it is greater than 90 we shouldn't see
	// any specular so we set specular to 0.
	
	float cosAI = clamp(dot(normal, -direction), 0f, 1f);
	
	vec3 viewVector = normalize(camera_Pos - world_pos);
	vec3 halfAngle = normalize(viewVector - direction);
	
	//cosine of the angle between the half angle and normal. Is 1 if it is a perfect reflection.
	float spec = dot(normal, halfAngle);

	//raises the specular factor to the specularExponent value from the material. Clamps it to positive values
	spec = pow(spec, specExp);
	spec = spec > 0 ? spec : 0.0f;
	spec = cosAI != 0.0 ? spec : 0;

    //end specular
	
	return vec4(specColor * spec * specIntensity * attenuation * base.color * cosAI, 1f);
}

vec4 calculateLight(BaseLight base, vec3 direction, int spec)
{
    float directionalStrength = clamp(dot(normal, -direction), 0f, 1f);
    vec4 textureColor = textureColor();

    vec4 finalDiffuse = vec4((directionalStrength * textureColor  * base.intensity * vec4(base.color, 1f)).xyz, 1f);
	
	if(spec == 1)
		totalSpec += calculateSpecular(base, direction).xyz;

    return finalDiffuse;
}

vec4 calculatePointLights(PointLight p)
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);

	vec3 direction = world_pos - p.pos;
		
	float distance = length(direction);
		
	float attenFactor = 1f / (((p.coefficient.x) + (distance * p.coefficient.y) + (p.coefficient.z * pow(distance, 2))) + .00001);
	
	attenuation = pow(attenFactor, .25);
		
	totalLight += (calculateLight(p.base, normalize(direction), 1) * attenFactor);
	
	return clamp(totalLight, 0f, 1f);
}

vec4 pointLightsLoop()
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	
	for(int i = -1; i < plNum; i++)
	{
		totalLight += calculatePointLights(pointLights[i]);
	}
	
	return clamp(totalLight, 0f, 1f);
}

vec4 calculateSpotLights(SpotLight s)
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	float spotFactor = dot(normalize(world_pos - s.position), normalize(s.direction));
	
	if((spotFactor > s.angle))
	{
		totalLight +=  (calculateLight(s.base, s.direction, 0) / length(world_pos - s.position)) 
						* (1.0 - (1.0 - spotFactor)/(1.0 - s.angle));;
	}
	
	return totalLight;
}

vec4 spotLightLoop()
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	
	for(int i = -1; i  < slNum; i++)
	{
		totalLight += calculateSpotLights(spotLights[i]);
	}
	
	return totalLight;
}

vec4 calculateFog()
{
	float distance = length(camera_Pos - world_pos);
	
	if(distance > fogFalloff)
	{
		distance = distance - fogFalloff;
		return vec4(1f - (1f / pow(2.71828, distance * .033f * .0033f * distance)));
	}
	else
		return vec4(0);
}

void calculateNormals()
{
	if(normalMap == 1)
	{
		normal = tbnMatrix * (2f * texture2D(normalTex, uvCoords).xyz - 1f);
	}
}

void calculateParallax()
{
	if(parallaxMapping == 1)
	{
		float heightSample = texture2D(parallaxTex, uvCoords.xy).x;		
		float hsb = .05f * heightSample - 0.03f;
		
		uvCoords = uvCoords + (normalize(camera_Pos - world_pos) * tbnMatrix).xy * hsb ;
	}	
	normalize(object_normal);
}

float calculateShadows()
{
	float visibility = 1.0;
	float bias = .001f;
	
	vec2 uvCoord = shadowCoord.xy / shadowCoord.w;
	float z  = (shadowCoord.z - bias) / shadowCoord.w;
	
	if (texture(shadowTex, uvCoord).x  <  z)
	{
		visibility = 0.0;
	}
		
		return visibility;
}

void main()
{
	//pre comp fixes
	uvCoords.y = -uvCoords.y;
	
	normal = normalize(object_normal);
	calculateParallax();
	calculateNormals();
	
	//shadow mapping
	float s_factor = calculateShadows();

	//main lighting
	vec4 lightColor 		= calculateLight(mainLight.base, mainLight.direction, 1);
	vec4 ambientColor 		= vec4(ambient.base.color * ambient.base.intensity, 1f) * textureColor();
	vec4 pointLightColor 	= pointLightsLoop();
	vec4 spotLightColor 	= spotLightLoop() * s_factor;
	vec4 fogColor 			= calculateFog();
	
	//final color addition
 	color = lightColor + ambientColor + vec4(totalSpec, 1f) + pointLightColor + spotLightColor + fogColor;
	
	//gamma
	color.x = pow(color.x, 1f/1.8f);
	color.y = pow(color.y, 1f/1.8f);
	color.z = pow(color.z, 1f/1.8f);
	
	colorOUT = color.xyz;
	
	float depth = gl_FragCoord.z;
	depth = 1.0f - (1.0f - depth) * 1000f;
	
	//color = color * .00001f + vec4(vec3(depth), 1f);
}

