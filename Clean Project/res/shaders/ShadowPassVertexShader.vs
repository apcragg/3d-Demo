# version 330

layout(location = 0) in vec3 position;

uniform mat4 lightSpace;
uniform mat4 worldSpace;

void main()
{
	vec4 frag_pos = (lightSpace * worldSpace) * vec4(position, 1f);
	gl_Position = frag_pos;
	
}