# version 330 core

//output color to GL_COLOR_ATTACHMENT0 texture specified in the framebuffer setup
layout(location = 0) out vec3 color;

void main()
{
	//sets the color texture to the linearized (not a real word) depth
	color = vec3(1.0f - (1.0f - gl_FragCoord.z) * 1000f);
}