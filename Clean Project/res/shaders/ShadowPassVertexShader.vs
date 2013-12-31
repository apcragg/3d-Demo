# version 330

layout(location = 0) in vec3 position;

uniform mat4 lightSpace;

void main()
{
	vec4 frag_pos = lightSpace * vec4(position, 1f);
	gl_Position = frag_pos;
	
}