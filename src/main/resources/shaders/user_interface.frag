#version 450 core

uniform sampler2D sampler;
uniform vec4 color;
uniform bool hasTexture;
uniform bool isHovered;
uniform bool isClicked;
uniform bool mixOutputColor;
uniform vec4 mixColor;
uniform bool depthTexture;

in vec2 passTexCoords;

out vec4 outColor;

float linearizeDepth(float depth) {
    float zNear = 0.01;
    float zFar = 1000.0;
    return 2.0 * zNear * zFar / (zFar + zNear - (2.0 * depth - 1.0) * (zFar - zNear));
}

void main() {

    if (depthTexture && hasTexture) {
        float depth = linearizeDepth(texture(sampler, passTexCoords).x) / 50.0;
        outColor = vec4(depth, depth, depth, 1.0);
    } else if (hasTexture) {
        outColor = texture(sampler, passTexCoords);
    } else {
        outColor = color;
    }

    if (isClicked) {
        outColor = mix(outColor, vec4(0.2, 0.2, 0.2, 1.0), 0.6);
    } else if (isHovered) {
        outColor = mix(outColor, vec4(0.1, 0.1, 0.1, 1.0), 0.1);
    }

    if (mixOutputColor) {
        outColor = mix(outColor, vec4(mixColor.xyz, outColor.a), 0.9);
    }
}
