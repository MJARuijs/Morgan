#version 450

in vec2 passTexCoords;

uniform sampler2D gradient;

out vec4 outColor;

void main() {
    outColor = texture(gradient, passTexCoords);
}
