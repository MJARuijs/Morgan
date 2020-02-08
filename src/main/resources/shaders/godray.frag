#version 450

const int samples = 4;
const float samplesPerPixel = (samples * 2.0 + 1.0) * (samples * 2.0 + 1.0);

in vec4 worldPosition;
in vec2 passTextureCoord;
in vec3 passNormal;
in vec4 shadowCoords;

uniform mat4 view;

uniform vec3 cameraPosition;
uniform vec2 shadowMapSize;
uniform sampler2D shadowMap;
uniform sampler2D depthTexture;

uniform int levels;

layout(location = 0) out vec4 outColor;

void main() {

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
    float lightFactor = 1.0 - (shadowValue * shadowCoords.w);

    outColor = vec4(1.0, 1.0, 1.0, (1.0 / levels) * lightFactor);
    outColor = clamp(outColor, 0.0, 1.0);
}
