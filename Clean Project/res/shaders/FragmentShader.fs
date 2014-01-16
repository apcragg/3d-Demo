#version 330
#define MAX_POINT_LIGHTS 64
#define MAX_SPOT_LIGHTS 8
#define MAX_SHADOW_SPOT_LIGHTS 4


layout(location = 0) out vec4 color;

in vec3 world_pos;
in vec3 object_normal;
in vec2 object_uvs;
in vec4 shadowCoord[MAX_SHADOW_SPOT_LIGHTS];
in mat3 tbnMatrix;
flat in int s2Num_;

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
uniform SpotLight shadowSpotLights[MAX_SHADOW_SPOT_LIGHTS];
uniform int plNum;
uniform int slNum;


//textures
uniform sampler2D diffuseTex;
uniform sampler2D normalTex;
uniform sampler2D parallaxTex;
uniform sampler2DShadow shadowTex0;
uniform sampler2DShadow shadowTex1;
uniform sampler2DShadow shadowTex2;
uniform sampler2DShadow shadowTex3;
uniform float textureScale;
uniform int parallaxMapping;

//materials
uniform int normalMap;
uniform int specExp;
uniform float specIntensity;
uniform vec3 specColor;

//Camera uniforms
in vec3 camera_Pos;
uniform float gamma;

vec2 uvCoords = object_uvs / textureScale;
vec3 totalSpec = vec3(0f, 0f, 0f);
vec3 normal;
float attenuation = 1f;
float fogFalloff = 200f;

//shadow mapping
const vec2 poissonDisk[] = vec2[](
vec2(-0.06470083f, -0.9226869f),
vec2(0.2496478f, -0.4083893f),
vec2(-0.4978239f, -0.595479f),
vec2(-0.2198737f, -0.3370214f),
vec2(-0.649868f, -0.2171402f),
vec2(0.7954451f, 0.071216f),
vec2(0.4748782f, 0.3046738f),
vec2(-0.2104412f, 0.2523279f),
vec2(-0.004335199f, 0.6138465f),
vec2(-0.6704991f, 0.4099813f),
vec2(-0.5012408f, 0.8232943f),
vec2(0.4141222f, -0.865628f),
vec2(0.2112032f, 0.9613814f),
vec2(0.6426348f, 0.716343f),
vec2(-0.9788542f, 0.1075208f),
vec2(-0.1468241f, 0.9574707f)
);

vec4 textureColor()
{
    return texture2D(diffuseTex, uvCoords);
}

float random(vec3 seed, int i)
{
	float dot_product = dot(vec4(seed, i), vec4(12.984f,78.23f,45.164f,94.673f));
    return (sin(dot_product) * 43758.54f);
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
	
	return vec4(specColor * spec * specIntensity * base.color * cosAI, 1f);
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

vec4 calculateSpotLights(SpotLight s, vec2 visibility)
{
	visibility = clamp(visibility, 0f, 1f);

	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	float spotFactor = dot(normalize(world_pos - s.position), normalize(s.direction));
	
	if((spotFactor > s.angle))
	{
		totalLight +=  ((calculateLight(s.base, s.direction, 0) / length(world_pos - s.position)) 
						* (1.0 - (1.0 - spotFactor)/(1.0 - s.angle)) * visibility.x);
		totalSpec += (calculateSpecular(s.base, s.direction).xyz * (1.0 - (1.0 - spotFactor)/(1.0 - s.angle)) * vec3(1f, 1f, 1f)) * visibility.y;
	}
	
	return totalLight;
}

vec4 spotLightLoop()
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	
	for(int i = -1; i  < slNum; i++)
	{
		totalLight += calculateSpotLights(spotLights[i], vec2(1));
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
		normal = normalize(tbnMatrix * (2f * texture2D(normalTex, uvCoords).xyz - 1f)); 
	}
}

// some weird negative shit going on on here. Because of the way parallax mapping works it
// doesn't like having it's texture flipped upside down without having too it's eye vec flipped. 
// In turn this means the normal y component needs to be flipped as well.

void calculateParallax()
{
	if(parallaxMapping == 1)
	{
		float heightSample = texture2D(parallaxTex, uvCoords).x;                
        float hsb = (0.05f * heightSample) - 0.03f;
                
        uvCoords = uvCoords  + (((normalize(camera_Pos - world_pos) * tbnMatrix).xy) * hsb);
	}	
	
}

float shadowLookup(int samp, vec2 shadowUV, float z)
{		
	//shadowUV /= shadowCoord[samp].w; //only use for perspective shadow maps
	shadowUV = clamp(shadowUV, 0.0001f, .9999f);
	vec2 offset;
	float factor = 0.0f;
	
	int count = 0;
	
	for(float x = -0.5f; x <= 0.5f; x += 1.0f)
	{
		for(float y = -0.5f; y <= 0.5f; y += 1.0f)
		{
			offset.x = (1f/3076) * x;
			offset.y = (1f/3076) * y;
			
			if(samp == 0) factor += (1.0f - texture(shadowTex0, vec3(shadowUV + offset, z - .0005f)));
			if(samp == 1) factor += (1.0f - texture(shadowTex1, vec3(shadowUV + offset, z - .0005f)));
			if(samp == 2) factor += (1.0f - texture(shadowTex2, vec3(shadowUV + offset, z - .0005f)));
			if(samp == 3) factor += (1.0f - texture(shadowTex3, vec3(shadowUV + offset, z - .0005f)));
			
			count ++;
		}
	}
	
	return factor / count;
}

vec4 calculateShadows(SpotLight s, int i)
{
	vec2 visibility = vec2(1.0f);
	
	float cosTheta = clamp(dot(normal, normalize(-s.direction)), 0f, 1f);
		if(cosTheta < 0.025f) return calculateSpotLights(s, vec2(0.05f, 0.0f));
		
	float bias = .001f * tan(acos(cosTheta));	
	bias = clamp(bias, 0.0f, 0.01f);
	
	vec2 uvCoord = shadowCoord[i].xy;
	float z  = (shadowCoord[i].z - bias);// / shadowCoord[i].w;
	
	//if the texel is outside the shadowmap's view don't cast a shadow and early exit
	vec2 uvTest = uvCoord;
	if(uvTest.x > 1f || uvTest.y > 1f || uvTest.x < 0f || uvTest.y < 0f) return calculateSpotLights(s, vec2(1.0f, 1.0f)); 
	
	/*
	*Old code that might still be useful later.
	
	int sCount = 0;
	int lCount = 0;
	
	/for(int j = 0; j < 8; j++)
	{			
		int index = int(8*random(floor(world_pos.xyz*1000.0), j))%8;
	
		float factor = shadowLookup(i, (uvCoord + (poissonDisk[index]/1400f)), z);
		
		if(factor == 1.0f) sCount++; if(sCount == 2 && j == 1){ return calculateSpotLights(s, vec2(.15f, 0f)); }
		if(factor == 0.0f) lCount++; if(lCount == 2 && j == 1){ return calculateSpotLights(s, vec2(1.0f, 1.0f)); } 
	
		visibility.x -= factor * (1f / 8) * .95;
		visibility.y -= factor * (1f / 8);
	}	
	
	* End old code
	*/
	
	//early returns
	
	float factor = shadowLookup(i, uvCoord, z);
	visibility.x -= factor * .95;
	visibility.y -= factor;
		
	return calculateSpotLights(s, visibility);
}

vec4 shadowSpotLightLoop()
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	
	for(int i = -1; i  < s2Num_; i++)
		totalLight += calculateShadows(shadowSpotLights[i], i);
	
	return totalLight;
}

void main()
{
	//pre comp fixes
	uvCoords.y = -uvCoords.y;
	normal = normalize(object_normal);
	
	//prepare normals and uvs from normal/parallax maps
	calculateParallax();
	calculateNormals();
	
	//shadow mapping
	vec4 shadowSpotLightColor = shadowSpotLightLoop();

	//main lighting
	vec4 lightColor 		= calculateLight(mainLight.base, mainLight.direction, 1);
	vec4 ambientColor 		= vec4(ambient.base.color * ambient.base.intensity, 1f) * textureColor();
	vec4 pointLightColor 	= pointLightsLoop();
	vec4 spotLightColor 	= spotLightLoop();
	vec4 fogColor 			= calculateFog();
	
	//final color addition
 	color = lightColor + ambientColor + vec4(totalSpec, 1f) + pointLightColor + spotLightColor + shadowSpotLightColor + fogColor + ambientColor;
	
	//gamma
	color.x = pow(color.x, 1f/gamma);
	color.y = pow(color.y, 1f/gamma);
	color.z = pow(color.z, 1f/gamma);
}

