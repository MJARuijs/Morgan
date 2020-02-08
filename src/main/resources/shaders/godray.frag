#version 450

in vec4 shadowCoords;

uniform sampler2D shadowMap;

uniform int levels;

layout(location = 0) out vec4 outColor;

void main() {

    float distanceFromLight = texture(shadowMap, shadowCoords.xy).r;
    float actualDistance = shadowCoords.z;
    if (actualDistance - 0.005 > distanceFromLight) {
        discard;
    }

    outColor = vec4(0.8, 0.9, 1.0, (1.0 / levels / 4));
    outColor = clamp(outColor, 0.0, 1.0);
}
