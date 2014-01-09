#version 330

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

void main()
{
	vec4 samp = texture(tex, texCoords);
	
	if(samp == vec4(0f, 0f, 0f, 1f))
		discard;
	else
		color = samp;
}