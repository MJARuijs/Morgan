#version 450

const int samples = 3;
const float samplesPerPixel = (samples * 2.0 + 1.0) * (samples * 2.0 + 1.0);

in vec4 shadowCoords;
flat in vec4 passAmbientColor;
flat in vec4 passDiffuseColor;

uniform sampler2D shadowMap;
uniform vec2 shadowMapSize;
uniform bool sunShaftRender;

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec4 outColor2;

void main() {

    float lightFactor = 1.0;

    if (!sunShaftRender) {
        float horizontalPixelSize = 1.0 / shadowMapSize.x;
        float verticalPixelSize = 1.0 / shadowMapSize.y;

        float shadowValue = 0.0;

        for (int x = -samples; x < samples; x++) {
            for (int y = -samples; y < samples; y++) {
                 float distanceFromLight = texture(shadowMap, shadowCoords.xy + vec2(x * horizontalPixelSize, y * verticalPixelSize)).r;
                 float actualDistance = shadowCoords.z;
                 if (actualDistance - 0.005 > distanceFromLight) {
                    shadowValue += 1.0;
                 }
            }
        }

        shadowValue /= samplesPerPixel;
        lightFactor = 1.0 - (shadowValue * shadowCoords.w);
    }

    outColor = passAmbientColor + passDiffuseColor * lightFactor;
    outColor = clamp(outColor, 0.0, 1.0);
    outColor2 = vec4(1.0, 1.0, 1.0, 1.0);
}

