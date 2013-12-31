# version 330

uniform sampler2D tex;

in vec2 uv;

out vec4 color;

void main()
{
	color = vec4(1, 0, 0, 1);//texture(tex, uv);
	
	//if(color == vec4(0,0,0,0))
		//color = vec4(1,0,0,1);
}