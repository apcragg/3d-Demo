#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;

uniform mat4 projectedSpace;
uniform mat4 worldSpace;
uniform mat4 viewSpace;
uniform int displacementMaping;

uniform sampler2d displacementMap;
uniform float displacementFactor;

out vec3 world_pos;
out vec3 object_normal;
out vec2 object_uvs;
out mat3 tbnMatrix;

//Benny's code. See engine.main.OBJLoader in the calculateTangnet() method for his github link.
mat3  calculateTBN()
{
	vec3 wolrd_tangent = (worldSpace * vec4(tangent, 0f)).xyz;	
	vec3 t = normalize(tangent);
	
	t = normalize(t - dot(t, object_normal) * object_normal);
	
	vec3 bitangent = cross(t, object_normal);
	
	return mat3(t, bitangent, object_normal);
}

void calculateDisplacement()
{
	if(displacementMapping == 1)
	{
		vec3 mapColor = texture2D(uv, displacementMap).xyz;
		float dFactor = 1.0f - length(mapColor);
		
		world_pos = position + normalize(object_normal) * dFactor *  displacementFactor;
	}
	else
	{
		world_pos = position;
	}
}

void main()
{
	calculateDisplacement();

	world_pos = (worldSpace * vec4(position, 1f)).xyz;
	object_uvs = uv;
	object_normal = normalize(worldSpace * vec4(normal, 0.0f)).xyz;
	
	gl_Position =  (projectedSpace * viewSpace * vec4(world_pos, 1f));
	
	tbnMatrix = calculateTBN();
}
