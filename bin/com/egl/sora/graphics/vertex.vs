#version 330 core
layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tex;

uniform float offsetX;
uniform float offsetY;
uniform float zoomFactor;

out vec2 texout;

void main()
{
    gl_Position = vec4(zoomFactor*(pos.x-offsetX), zoomFactor*(pos.y-offsetY), 0, 1);
    
    texout = tex;
}