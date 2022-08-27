#version 130

in vec2 TexCoords;

out vec4 fragColor;

uniform sampler2D image;
uniform vec4 color;

void main()
{
    fragColor = texture(image, TexCoords);
}