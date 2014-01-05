# version 330 core

//output color to GL_COLOR_ATTACHMENT0 texture specified in the framebuffer setup
layout(location = 0) out vec3 color;
layout(location = 1) out float depth;

void main()
{
	//sets the color texture to the linearized (not a real word) depth
	color = vec3(gl_FragCoord.z *  gl_FragCoord.w);
	depth = gl_FragCoord.z;
}