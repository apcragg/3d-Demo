# version 330 core

//output color to GL_COLOR_ATTACHMENT0 texture specified in the framebuffer setup
layout(location = 0) out vec2 color;

void main()
{
	//sets the color texture to the linearized (not a real word) depth
	color = vec2(gl_FragCoord.z, gl_FragCoord.z * gl_FragCoord.z);
	gl_FragDepth = gl_FragCoord.z;
}