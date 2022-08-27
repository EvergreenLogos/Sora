#version 130

in vec4 position;
in vec2 texcoord;

uniform float offsetX;
uniform float offsetY;
uniform float zoomFactor;

out vec2 TexCoords;

void main()
{
	TexCoords = position.zw;
    gl_Position = vec4((position.x/zoomFactor)-offsetX, (position.y/zoomFactor)-offsetY, 0, 1);
}