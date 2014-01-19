#version 330

//define blurSize 1f / 1024f
#define blurSize 1f / 2048f
//#define blurSize 1f / 3076f

uniform sampler2D tex;

in vec2 texCoords;
out vec3 color;

float kernalSize = 1.5f;

vec4 blur()
{
	vec4 sum = vec4(0f);
		
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-4.0f * kernalSize * blurSize))) * .05f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-3.0f * kernalSize * blurSize))) * .09f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-2.0f * kernalSize * blurSize))) * .12f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-1.0f * kernalSize * blurSize))) * .15f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (-0.0f * kernalSize * blurSize))) * .16f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (1.0f * kernalSize * blurSize)))  * .15f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (2.0f * kernalSize * blurSize)))  * .12f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (3.0f * kernalSize * blurSize)))  * .09f;
		sum += texture(tex, vec2(texCoords.x, texCoords.y + (4.0f * kernalSize * blurSize)))  * .05f;
				
	return sum;
}

void main()
{
	float b = 0.0f;
	
    b = max(b, texture(tex, vec2(texCoords.x + (blurSize) * (kernalSize), texCoords.y + (blurSize) * (kernalSize))).b);
	b = max(b, texture(tex, vec2(texCoords.x - (blurSize) * (kernalSize), texCoords.y + (blurSize) * (kernalSize))).b);
	b = max(b, texture(tex, vec2(texCoords.x, texCoords.y + (blurSize) * (kernalSize))).b );
	b = max(b, texture(tex, vec2(texCoords.x + (blurSize) * (kernalSize), texCoords.y)).b);
	b = max(b, texture(tex, vec2(texCoords.x - (blurSize) * (kernalSize), texCoords.y)).b );
	b = max(b, texture(tex, vec2(texCoords.x, texCoords.y)).b );
	b = max(b, texture(tex, vec2(texCoords.x + (blurSize) * (kernalSize), texCoords.y - (blurSize) * (kernalSize))).b );
	b = max(b, texture(tex, vec2(texCoords.x - (blurSize) * (kernalSize), texCoords.y - (blurSize) * (kernalSize))).b );
	b = max(b, texture(tex, vec2(texCoords.x, texCoords.y - (blurSize) * (kernalSize))).b );

	color = vec3(blur().xy, b);
}