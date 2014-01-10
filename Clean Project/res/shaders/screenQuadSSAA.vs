#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;

out vec2 texCoords;

uniform float scale0;
uniform float scale1;

void main()
{
	gl_Position = vec4(scale0 * position.xy + scale1, 0f, 1f);
	
	texCoords = uv;
}