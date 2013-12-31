# version 330

uniform sampler2D tex;

in vec2 uv;

out vec4 color;

void main()
{
	color = texture(tex, uv);
	
	color = vec4(uv, 0f, 1f);
	
	//if(color == vec4(0,0,0,0))
		//color = vec4(1,0,0,1);
}