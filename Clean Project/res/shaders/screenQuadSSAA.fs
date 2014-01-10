#version 330
#define offsetY 1f / 1080f
#define offsetX 1f / 1920f

uniform sampler2D tex;

in vec2 texCoords;
out vec4 color;

void main()
{
	vec4 sum = vec4(0f);
	
	sum += texture(tex, vec2(texCoords.x - (offsetX * 0.75f), texCoords.y + (offsetY * 0.75f))) * .125f;
	sum += texture(tex, vec2(texCoords.x - (offsetX * 0.75f), texCoords.y - (offsetY * 0.75f))) * .125f;
	sum += texture(tex, vec2(texCoords.x - (offsetX * 0.25f), texCoords.y + (offsetY * 0.25f))) * .125f;
	sum += texture(tex, vec2(texCoords.x - (offsetX * 0.25f), texCoords.y - (offsetY * 0.25f))) * .125f;
	sum += texture(tex, vec2(texCoords.x + (offsetX * 0.25f), texCoords.y + (offsetY * 0.25f))) * .125f;
	sum += texture(tex, vec2(texCoords.x + (offsetX * 0.25f), texCoords.y - (offsetY * 0.25f))) * .125f;
	sum += texture(tex, vec2(texCoords.x + (offsetX * 0.75f), texCoords.y + (offsetY * 0.75f))) * .125f;
	sum += texture(tex, vec2(texCoords.x + (offsetX * 0.75f), texCoords.y - (offsetY * 0.75f))) * .125f;
	
	if(sum == vec4(0f, 0f, 0f, 1f))
		discard;
	else
		color = sum;
}