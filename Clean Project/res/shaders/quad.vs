# version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uvs;

uniform mat4 projectedSpace;
uniform mat4 worldSpace;
uniform mat4 viewSpace;

out vec2 uv;

void main()
{
	gl_Position = projectedSpace * viewSpace * worldSpace * vec4(position, 1f);
	uv = uvs;
}