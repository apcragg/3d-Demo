#version 330

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

void main()
{
	color = vec4(texture(tex, texCoords).xy, .5f, 1f);
}