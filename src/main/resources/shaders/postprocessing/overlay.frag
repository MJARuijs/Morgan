#version 450

uniform sampler2D sampler;
uniform sampler2D overlay;

in vec2 passTexCoords;

out vec4 outColor;

void main() {
    vec4 originalColor = texture(sampler, passTexCoords);
    vec4 overlayColor = texture(overlay, passTexCoords);
    outColor = vec4(originalColor.xyz + overlayColor.xyz, 1.0);
}
