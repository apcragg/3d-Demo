#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uvMap;
layout(location = 2) in vec3 normalPos;
layout(location = 3) in vec3 tangentPos;

uniform mat4 projectedSpace;
uniform mat4 worldSpace;
uniform mat4 viewSpace;

out vec3 pos;
out vec3 object_normal;
out vec2 uvs;
out vec3 worldPos;
out mat3 tbnMatrix;

void main()
{
	pos = (worldSpace * vec4(position, 1f)).xyz;
	uvs = uvMap;
	object_normal = normalize(worldSpace * vec4(normalPos, 0.0f)).xyz;
	
	gl_Position =  (projectedSpace * viewSpace * vec4(pos, 1f));
	
	//Benny's code. See engine.main.OBJLoader in the calculateTangnet() method for his github link.
	
	vec3 tangent = (worldSpace * vec4(tangentPos, 0f)).xyz;
	vec3 n = object_normal;
	
	vec3 t = normalize(tangent);
	t = normalize(t - dot(t, n) * n);
	
	vec3 bitangent = cross(t, n);
	
	tbnMatrix = mat3(t, bitangent, n);
}