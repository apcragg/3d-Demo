# version 330 core

layout(location = 0) out float depth;

void main()
{
	//depth = 1.0f - (1.0f - gl_FragCoord.z) * 1000f;
	depth = gl_FragCoord.z;
	gl_FragDepth = gl_FragCoord.z;
}