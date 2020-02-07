#version 450

uniform sampler2D sampler;

in vec2 passTexCoords;

out vec4 outColor;

void main() {
    outColor = texture(sampler, passTexCoords);
}