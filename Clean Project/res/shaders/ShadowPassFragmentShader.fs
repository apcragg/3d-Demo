# version 330 core

layout(location = 0) out vec3 depth;

void main()
{
	depth = vec3(1.0f - (1.0f - gl_FragCoord.z) * 1000f);
	//depth = gl_FragCoord.z;
	gl_FragDepth = gl_FragCoord.z;
}