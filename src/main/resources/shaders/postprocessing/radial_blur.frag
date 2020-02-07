#version 450

in vec2 passTexCoords;

uniform sampler2D sampler;
uniform vec2 position;
out vec4 outColor;

const float exposure = 0.0034;
const float decay = 1.0;
const float density = 1.84;
const float weight = 3.65;

void main() {
    vec2 pixelPosition = passTexCoords - position;
    pixelPosition *= 1.0 / 75.0 * density;

    vec2 textureCoord = passTexCoords;
    vec4 totalColor = vec4(0.0);
    float illuminationDecay = 1.0;

    for (int i = 0; i < 75; i++) {
        textureCoord -= pixelPosition;
        vec4 color = texture(sampler, textureCoord);
        color *= illuminationDecay * weight;
        totalColor += color;

        illuminationDecay *= decay;
    }

    outColor = totalColor;
    outColor *= exposure;

}
