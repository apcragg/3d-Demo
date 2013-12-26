#version 120

attribute vec3 position;

varying vec3 pos;

void main()
{
	pos = position;
	gl_Position = vec4(position, 1f);
}