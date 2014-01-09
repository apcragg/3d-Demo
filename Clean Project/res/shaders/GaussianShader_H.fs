#version 330

#define blurSize 1f / 1024f
//#define blurSize 1f / 3076f

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

vec4 blur()
{
	vec4 sum = vec4(0f);
		
		sum += texture(tex, vec2(texCoords.x + (-4.0f * blurSize), texCoords.y)) * .05f;
		sum += texture(tex, vec2(texCoords.x + (-3.0f * blurSize), texCoords.y)) * .09f;
		sum += texture(tex, vec2(texCoords.x + (-2.0f * blurSize), texCoords.y)) * .12f;
		sum += texture(tex, vec2(texCoords.x + (-1.0f * blurSize), texCoords.y)) * .15f;
		sum += texture(tex, vec2(texCoords.x + (-0.0f * blurSize), texCoords.y)) * .16f;
		sum += texture(tex, vec2(texCoords.x + (1.0f * blurSize), texCoords.y))  * .15f;
		sum += texture(tex, vec2(texCoords.x + (2.0f * blurSize), texCoords.y)) * .12f;
		sum += texture(tex, vec2(texCoords.x + (3.0f * blurSize), texCoords.y))  * .09f;
		sum += texture(tex, vec2(texCoords.x + (4.0f * blurSize), texCoords.y))  * .05f;
			
	return sum;
}

void main()
{
	color = vec4(blur());
}