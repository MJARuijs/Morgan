#version 460

const vec4 waterColor = vec4(0.604, 0.867, 0.851, 1.0);

struct Sun {
    vec4 color;
    vec3 direction;
};

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform sampler2D distortionMap;
uniform sampler2D normalMap;

uniform float waveStrength;
uniform float waveSpeed;
uniform float deltaTime;

uniform float zNear;
uniform float zFar;

uniform Sun sun;
uniform vec3 cameraPosition;

in vec4 clipSpace;
in vec2 texCoords;
in vec3 toCameraVector;
in vec4 worldPosition;

out vec4 outColor;

vec4 calculateSpecular(vec3 normal) {
    vec3 lightDirection = normalize(sun.direction);
    vec3 reflectionVector = 2.0 * dot(lightDirection, normal) * normal - lightDirection;
//    reflectionVector = (reflect(lightDirection, normal));
    float brightness = dot(normalize(reflectionVector), toCameraVector);
    brightness = pow(brightness, 2.0);
    brightness = clamp(brightness, 0.0, 1.0);
    return sun.color * brightness;
}

float linearizeDepth(float depth) {
    return 2.0 * zNear * zFar / (zFar + zNear - (2.0 * depth - 1.0) * (zFar - zNear));
}

void main() {
    vec3 postion = clipSpace.xyz;
    vec2 ndc = ((clipSpace.xy / clipSpace.w) / 2.0) + 0.5;

    vec2 reflectionCoords = vec2(ndc.x, 1.0 - ndc.y);
    vec2 refractionCoords = ndc;

    float moveFactor = deltaTime * waveSpeed;

    vec2 distortedTexCoords = texture(distortionMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 0.1;
    distortedTexCoords = texCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
    vec2 totalDistortion = (texture(distortionMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;

    reflectionCoords += totalDistortion;
    refractionCoords += totalDistortion;

    vec4 reflectionColor = texture(reflectionTexture, reflectionCoords);
    vec4 refractionColor = texture(refractionTexture, refractionCoords);

    vec3 normal = texture(normalMap, distortedTexCoords).rbg * 2.0 - 1.0;
    normal = normalize(normal);

    float fresnelFactor = dot(toCameraVector, normal);
    vec4 specularColor = calculateSpecular(normal);

    float groundDistance = texture(depthTexture, refractionCoords).r;
    groundDistance = linearizeDepth(groundDistance);

    float waterDistance = gl_FragCoord.z;
    waterDistance = linearizeDepth(waterDistance);

    float depthValue = groundDistance - waterDistance;
    float opacity = clamp(depthValue * 2.0, 0.0, 1.0);

    outColor = mix(reflectionColor, refractionColor, fresnelFactor);
    outColor = mix(outColor, waterColor, 0.2);
    outColor.a = opacity;
}
