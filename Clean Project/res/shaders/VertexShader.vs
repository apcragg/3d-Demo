#version 330
#define MAX_SHADOW_SPOT_LIGHTS 4

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;

//space matricies
uniform mat4 projectedSpace;
uniform mat4 worldSpace;
uniform mat4 viewSpace;
uniform mat4 lightSpace[MAX_SHADOW_SPOT_LIGHTS];

//shadow uniforms
uniform int s2Num;

//displacement mapping
uniform sampler2D displacementTex;
uniform float displacementFactor;
uniform int displacementMapping;
uniform vec3 cameraPos;

vec3 temp_World_pos;
mat4 biasMatrix = mat4(
0.5, 0.0, 0.0, 0.0,
0.0, 0.5, 0.0, 0.0,
0.0, 0.0, 0.5, 0.0,
0.5, 0.5, 0.5, 1.0
);

out vec3 world_pos;
out vec3 object_normal;
out vec2 object_uvs;
out mat3 tbnMatrix;
out vec3 camera_Pos;
out vec4 shadowCoord[MAX_SHADOW_SPOT_LIGHTS];
flat out int s2Num_;

//Benny's code. See engine.main.OBJLoader in the calculateTangnet() method for his github link.
mat3  calculateTBN()
{
	vec3 wolrd_tangent = (worldSpace * vec4(tangent, 0f)).xyz;	
	vec3 t = normalize(tangent);
	
	t = normalize(t - dot(t, object_normal) * object_normal);
	
	vec3 bitangent = cross(t, object_normal);
	
	return mat3(t, bitangent, object_normal);
}

// displacement mapping
vec3 calculateDisplacement()
{
	vec3 tempPos;
	
	//get the texture and put it into -1.0f -> 1.0f space
	vec4 dv = 2f * texture2D(displacementTex, uv) - 1f;
	
	if(abs(dv.x) < .05f || abs(dv.w) < .05f)
		dv = vec4(0f);
	
	float dFactor = 0.30*dv.x + 0.59*dv.y + 0.11*dv.z;
		
	tempPos = position + (normalize(normal) * dFactor * displacementFactor); // * displacementFactor;
	
	if(displacementMapping != 1)
	{
		tempPos = position;
	}
	
	return tempPos;
}

void main()
{
	//displacement mapping
	temp_World_pos = calculateDisplacement();
	
	//into world space
	world_pos = (worldSpace * vec4(temp_World_pos, 1f)).xyz;
	
	//pass through uvs
	object_uvs = uv;
	
	//puts the normals into world space
	object_normal = normalize(worldSpace * vec4(normal, 0.0f)).xyz;
	
	gl_Position =  ((projectedSpace * viewSpace) * vec4(world_pos, 1f));

	tbnMatrix = calculateTBN();	
	camera_Pos = cameraPos;
	
	//shadow mappinh
	shadowCoord[0] = (biasMatrix * lightSpace[0]) * vec4(world_pos, 1f);
	shadowCoord[1] = (biasMatrix * lightSpace[1]) * vec4(world_pos, 1f);
	shadowCoord[2] = (biasMatrix * lightSpace[2]) * vec4(world_pos, 1f);
	shadowCoord[3] = (biasMatrix * lightSpace[3]) * vec4(world_pos, 1f);

	s2Num_ = s2Num;
}
