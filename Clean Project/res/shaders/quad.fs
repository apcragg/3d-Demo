# version 330

//quad uv coords
in vec2 uv;

//pre sampled depth texture
uniform sampler2D tex;

//output to the quad
out vec4 color;

void main()
{
	vec2 uvCoord = vec2(1.0f - uv.x, 1.0f - uv.y);

	//samples the texture
	vec4 temp = texture(tex, uvCoord);
	
	//puts the depth in a more linear space
	float factor = temp;//1.0f - (1.0f - temp.x) * 1000f;
	color = vec4(vec3(factor), 0f);
	
	//replace all texels without a sampled depth with the color red
	if(temp == vec4(1,1,1,1))
		color = vec4(1,0,0,1);
}