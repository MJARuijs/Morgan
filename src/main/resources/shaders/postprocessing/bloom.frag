#version 450

in vec2 texCoord;

uniform sampler2D originalSampler;
uniform sampler2D blurredSampler;

out vec4 color;

void main() {

    vec4 original = texture(originalSampler, texCoord);
    vec4 blurred = texture(blurredSampler, texCoord);

    color = clamp(original + blurred, 0.0, 1.0);
}