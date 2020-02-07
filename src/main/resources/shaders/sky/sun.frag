#version 450

in vec2 passTexCoords;

uniform sampler2D sunTexture;

out vec4 outColor;

void main() {
    outColor = texture(sunTexture, passTexCoords);
}
