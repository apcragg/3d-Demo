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

void calculateParallax()
{
	if(parallaxMapping == 1)
	{
		float heightSample = texture2D(parallaxTex, uvCoords).x;                
        float hsb = (0.05f * heightSample) - 0.03f;
                
        uvCoords = uvCoords  + (((normalize(camera_Pos - world_pos) * tbnMatrix).xy) * hsb);
	}	
	
}

/*
float linstep(float mi, float ma, float v)  
{
	float f = clamp((v - mi) / (ma - mi), 0f, 1f);
	
	return f;
}  

float ReduceLightBleeding(float p_max, float Amount)  
{  
  // Remove the [0, Amount] tail and linearly rescale (Amount, 1].  
   return linstep(Amount, 1, p_max);  
}  

float chebyshevUpperBound( float di, int i)
	{
		vec3 moments;
	
		// We retrive the two moments previously stored (depth and depth*depth)
		if(i == 0) moments = (texture(shadowTex0, vec2(shadowCoord[i].xy))).xyz;
		if(i == 1) moments = (texture(shadowTex1, vec2(shadowCoord[i].xy))).xyz;
		if(i == 2) moments = (texture(shadowTex2, vec2(shadowCoord[i].xy))).xyz;
		if(i == 3) moments = (texture(shadowTex3, vec2(shadowCoord[i].xy))).xyz;
		
		// Surface is fully lit. as the current fragment is before the light occluder
		if (di <= moments.x + .001f)
			return 1.0 ;
	
		// The fragment is either in shadow or penumbra. We now use chebyshev's upperBound to check
		// How likely this pixel is to be lit (p_max)
		float variance = moments.y - (moments.x*moments.x);
		variance = max(variance,0.002);
	
		float d = di - moments.x;
		float p_max = variance / (variance + d*d);
		
		if(di >= moments.z  +.001f)
		{
			return 0.0f;
		}
	
		return ReduceLightBleeding(p_max, .45f);  
	}
*/

float shadowLookup(int samp, vec2 shadowUV, float z)
{		
	shadowUV = clamp(shadowUV, 0.0001f, .9999f);
	
	float factor = 0.0f;
	
			if(samp == 0) factor += (1.0f - texture(shadowTex0, vec3(shadowUV, z - .005f)));
			if(samp == 1) factor += (1.0f - texture(shadowTex1, vec3(shadowUV , z - .0005f)));
			if(samp == 2) factor += (1.0f - texture(shadowTex2, vec3(shadowUV, z - .0005f)));
			if(samp == 3) factor += (1.0f - texture(shadowTex3, vec3(shadowUV, z - .0005f)));

	return factor;
}


vec4 calculateShadows(SpotLight s, int i)
{
	vec2 visibility = vec2(1.0f);
	vec2 uvCoord = shadowCoord[i].xy;
	
	//Biases the sampling based on the angle of intersection between the light and fragment
	float cosTheta = clamp(dot(normal, normalize(-s.direction)), 0f, 1f);
		if(cosTheta < 0.025f) return calculateSpotLights(s, vec2(0.05f, 0.0f));
		
	float bias = .001f * tan(acos(cosTheta));	
		bias = clamp(bias, 0.0f, 0.01f);
	
	float z  = (shadowCoord[i].z - bias);
	
	//if the texel is outside the shadowmap's view don't cast a shadow and early exit
	vec2 uvTest = uvCoord;
	if(uvTest.x > 1f || uvTest.y > 1f || uvTest.x < 0f || uvTest.y < 0f) return calculateSpotLights(s, vec2(1.0f, 1.0f)); 
	
	int sCount = 0, lCount = 0;
	
	for(int j = 0; j < 8; j++)
	{			
		int index = int(8 * random(floor(world_pos.xyz * 1000.0), j)) % 8;
	
		float factor = shadowLookup(i, (uvCoord + (poissonDisk[index]/800f)), z);
		
		if(factor == 1.0f) sCount++; if(sCount == 2 && j == 1){ return calculateSpotLights(s, vec2(.15f, 0f)); }
		if(factor == 0.0f) lCount++; if(lCount == 2 && j == 1){ return calculateSpotLights(s, vec2(1.0f, 1.0f)); } 
	
		visibility.x -= factor * (1f / 8) * .95;
		visibility.y -= factor * (1f / 8);
	}	
		
	return calculateSpotLights(s, visibility);
}

vec4 shadowSpotLightLoop()
{
	vec4 totalLight = vec4(0f, 0f, 0f, 1f);
	
	for(int i = -1; i  < s2Num_; i++)
	{
		totalLight += calculateShadows(shadowSpotLights[i], i);
		//totalLight += calculateSpotLights(shadowSpotLights[i], vec2(chebyshevUpperBound(shadowCoord[i].z, i)));
	}
	
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

