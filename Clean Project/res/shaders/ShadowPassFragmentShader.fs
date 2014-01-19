# version 330 core

//output color to GL_COLOR_ATTACHMENT0 texture specified in the framebuffer setup
layout(location = 0) out vec3 color;

void main()
{
	//sets the color texture to the linearized (not a real word) depth
	float depth = gl_FragCoord.z;
	//depth = (2 * depth) - 1.0f;
	
	float moment1 = depth;
	float moment2 = depth * depth;
	
	float dx = dFdx(depth);
	float dy = dFdy(depth);
		moment2 += 0.25*(dx*dx+dy*dy) ;
	
	color = vec3(moment1, moment2, moment1);
	gl_FragDepth = gl_FragCoord.z;
}