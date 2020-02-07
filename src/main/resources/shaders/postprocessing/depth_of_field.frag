#version 460

in vec2 passTexCoords;

uniform sampler2D colorTexture;
uniform sampler2D blurredTexture;
uniform sampler2D depthTexture;
uniform float zNear;
uniform float zFar;

layout(location = 0) out vec4 outColor;

float linearizeDepth(float depth) {
    return 2.0 * zNear * zFar / (zFar + zNear - (2.0 * depth - 1.0) * (zFar - zNear));
}

void main() {

    float offset = 0.2;

    float centerDepth = texture(depthTexture, vec2(0.5, 0.5)).r;
    centerDepth = linearizeDepth(centerDepth).r / 10.0;

    float totalDepth = centerDepth * 0.36;

    float depth = texture(depthTexture, vec2(0.5, 0.5) + vec2(offset, 0)).r;
    totalDepth += 0.16 * linearizeDepth(depth) / 10.0;

    depth = texture(depthTexture, vec2(0.5, 0.5) + vec2(-offset, 0)).r;
    totalDepth += 0.16 * linearizeDepth(depth) / 10.0;

    depth = texture(depthTexture, vec2(0.5, 0.5) + vec2(0, -offset)).r;
    totalDepth += 0.16 * linearizeDepth(depth) / 10.0;

    depth = texture(depthTexture, vec2(0.5, 0.5) + vec2(0, -offset)).r;
    totalDepth += 0.16 * linearizeDepth(depth) / 10.0;

    depth = texture(depthTexture, passTexCoords).r;
    depth = linearizeDepth(depth) / 10.0;


//    totalDepth /= 5.0;

    if (abs(depth - totalDepth) > 0.2 && totalDepth < 0.9) {
        outColor = texture(blurredTexture, passTexCoords);
    } else {
        outColor = texture(colorTexture, passTexCoords);
    }

//    outColor = vec4(depth, depth, depth, 1.0);
//    outColor = texture(depthTexture, passTexCoords);
}
