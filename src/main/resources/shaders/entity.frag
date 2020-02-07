#version 450

struct AmbientLight {
    vec4 color;
};

struct DirectionalLight {
    vec4 color;
    vec3 direction;
};

struct PointLight {
    vec4 color;
    vec3 position;
};

struct Material {
    vec4 diffuseColor;
    vec4 specularColor;
    float shininess;
    sampler2D sampler;
    bool textured;
};

const int samples = 4;
const float samplesPerPixel = (samples * 2.0 + 1.0) * (samples * 2.0 + 1.0);

in vec4 worldPosition;
in vec2 passTextureCoord;
in vec3 passNormal;
in vec4 shadowCoords;

uniform AmbientLight ambient;
uniform DirectionalLight sun;
uniform PointLight pointlights[2];
uniform Material material;

uniform bool sunShaftRender;
uniform mat4 view;

uniform vec3 cameraPosition;
uniform vec2 shadowMapSize;
uniform sampler2D shadowMap;

layout(location = 0) out vec4 outColor;
//layout(location = 1) out vec4 outColor1;

vec4 computeAmbientColor() {
    if (material.textured) {
        return texture(material.sampler, passTextureCoord) * ambient.color;
    }
    return ambient.color * material.diffuseColor;
}

vec4 computeDirectionalColor() {

    // Diffuse
    vec3 lightDirection = normalize(sun.direction);
    vec3 normal = normalize(passNormal);

    float brightness = clamp(dot(lightDirection, normal), 0.0, 1.0);

    vec4 diffuseColor;
    if (material.textured) {
        diffuseColor = texture(material.sampler, passTextureCoord) * sun.color * brightness;
    } else {
        diffuseColor = brightness * material.diffuseColor * sun.color;
    }

    // Specular
    vec3 position = worldPosition.xyz;
    vec3 reflectionVector = 2 * (dot(lightDirection, normal)) * normal - lightDirection;
    vec3 toCameraVector = normalize(cameraPosition - position);

    vec4 specularColor;
    if (material.textured) {
        specularColor = texture(material.sampler, passTextureCoord) * sun.color * clamp(pow(dot(reflectionVector, toCameraVector), material.shininess), 0.0, 1.0);
    } else {
        specularColor = material.specularColor * sun.color * clamp(pow(dot(reflectionVector, toCameraVector), material.shininess), 0.0, 1.0);
    }

    return diffuseColor;
}

void main() {

    if (sunShaftRender) {
        outColor = vec4(0, 0, 0, 1);
    } else {
        vec4 ambientColor = computeAmbientColor();
        vec4 directionalColor = computeDirectionalColor();

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

        outColor = ambientColor + directionalColor * lightFactor;
        outColor = clamp(outColor, 0.0, 1.0);
//        outColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
}
