#version 450

const vec4 waterColor = vec4(0.604, 0.867, 0.851, 1.0);

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform float zNear;
uniform float zFar;

in vec4 actualPosition;
in vec4 distortedPosition;
in vec3 passNormal;
in vec4 diffuseColor;
in vec4 specularColor;
in vec3 toCameraVector;

out vec4 outColor;

float depthFactor(float waterDepth) {
    float factor = smoothstep(0.0, 5.0, waterDepth);
    return factor * 0.7;
}

vec2 convertToNDC(vec4 position) {
    vec2 ndc = (position.xy / position.w) / 2.0 + 0.5;
    return clamp(ndc, 0.002, 0.998);
}

float linearizeDepth(float depth) {
    return 2.0 * zNear * zFar / (zFar + zNear - (2.0 * depth - 1.0) * (zFar - zNear));
}

void main() {
    vec2 actualTexCoords = convertToNDC(actualPosition);
    vec2 distordedTexCoords = convertToNDC(distortedPosition);
    vec2 reflectionCoords = vec2(actualTexCoords.x, 1.0 - actualTexCoords.y);

    float depth = texture(depthTexture, distordedTexCoords).r;
    float lakeBottomDepth = linearizeDepth(depth);
    float waterDistance = linearizeDepth(gl_FragCoord.z);
    float waterDepth = lakeBottomDepth - waterDistance;
    float opacity = clamp(waterDepth * 2.0, 0.0, 1.0);

    vec4 reflectionColor = texture(reflectionTexture, reflectionCoords);
    reflectionColor = mix(reflectionColor, waterColor, 0.4);

    vec4 refractionColor = texture(refractionTexture, actualTexCoords);
    refractionColor = mix(refractionColor, waterColor, depthFactor(waterDepth));

    // Fresnel
    float fresnelFactor = dot(passNormal, toCameraVector);

    outColor = mix(reflectionColor, refractionColor, fresnelFactor);
    outColor = mix(outColor, waterColor, 0.25);
    outColor = outColor * diffuseColor + specularColor;
    outColor.a = opacity;
}
