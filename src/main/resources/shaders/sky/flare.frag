#version 450

in vec2 passTexCoords;

uniform sampler2D sampler;
uniform float brightness;

out vec4 outColor;

void main() {
    outColor = texture(sampler, passTexCoords);
    outColor.a *= brightness;
}
