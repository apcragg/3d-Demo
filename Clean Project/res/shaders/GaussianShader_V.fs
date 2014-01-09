#version 330

#define blurSize 1f / 1024f
//#define blurSize 1f / 3076f

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

vec4 blur()
{
	vec4 sum = vec4(0f);
		
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-4.0f * blurSize))) * .05f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-3.0f * blurSize))) * .09f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-2.0f * blurSize))) * .12f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-1.0f * blurSize))) * .15f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-0.0f * blurSize))) * .16f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (1.0f * blurSize)))  * .15f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (2.0f * blurSize)))  * .12f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (3.0f * blurSize)))  * .09f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (4.0f * blurSize)))  * .05f;
			
	return sum;
}

void main()
{
	color = blur();
}