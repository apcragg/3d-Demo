#version 330

#define blurSize 1f / 1024f
//#define blurSize 1f / 3076f

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

vec2 blur()
{
	vec2 sum = vec2(0f);
		
		sum += texture(tex, vec2(texCoords.x + (-4.0f * blurSize), texCoords.y)).xy * .05f;
		sum += texture(tex, vec2(texCoords.x + (-3.0f * blurSize), texCoords.y)).xy * .09f;
		sum += texture(tex, vec2(texCoords.x + (-2.0f * blurSize), texCoords.y)).xy * .12f;
		sum += texture(tex, vec2(texCoords.x + (-1.0f * blurSize), texCoords.y)).xy * .15f;
		sum += texture(tex, vec2(texCoords.x + (-0.0f * blurSize), texCoords.y)).xy * .16f;
		sum += texture(tex, vec2(texCoords.x + (1.0f * blurSize), texCoords.y)).xy  * .15f;
		sum += texture(tex, vec2(texCoords.x + (2.0f * blurSize), texCoords.y)).xy  * .12f;
		sum += texture(tex, vec2(texCoords.x + (3.0f * blurSize), texCoords.y)).xy  * .09f;
		sum += texture(tex, vec2(texCoords.x + (4.0f * blurSize), texCoords.y)).xy  * .05f;
			
	return sum;
}

void main()
{
	color = vec4(blur(), .5f, 1f);
}