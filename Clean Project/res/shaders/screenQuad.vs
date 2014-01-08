#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;

out vec2 texCoords;

void main()
{
	gl_Position = vec4(.5f * position.xy + .5f, 0f, 1f);
	
	texCoords = uv;
}