#version 450

uniform sampler2D sampler;
uniform float strength;

in vec2 passTexCoords;

out vec4 outColor;

void main() {
    vec4 color = texture(sampler, passTexCoords);
    float brightness = (color.r * 0.2126 + color.g * 0.7152 + color.b * 0.0722);
    outColor = color * pow(brightness, 1.0 / strength);
}
