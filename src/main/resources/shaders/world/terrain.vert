#version 450

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec3 inNormal;
layout(location = 2) in vec4 inColor;

struct AmbientLight {
    vec4 color;
};

struct DirectionalLight {
    vec4 color;
    vec3 direction;
};

uniform AmbientLight ambient;
uniform DirectionalLight sun;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec4 waterPlane;
uniform bool sunShaftRender;

uniform vec3 cameraPosition;
uniform mat4 shadowMatrix;
uniform float shadowDistance;

const float transitionDistance = 10.0;

out vec4 shadowCoords;
flat out vec4 passAmbientColor;
flat out vec4 passDiffuseColor;

vec4 computeAmbientColor() {
    return ambient.color * inColor;
}

vec4 computeDirectionalColor(vec3 position) {

    // Diffuse
    vec3 lightDirection = normalize(sun.direction);
    vec3 normal = normalize(inNormal);

    float brightness = clamp(dot(lightDirection, normal), 0.0, 1.0);

    vec4 diffuseColor = brightness * inColor * sun.color;

    // Specular
//    vec3 reflectionVector = 2 * (dot(lightDirection, normal)) * normal - lightDirection;
//    vec3 toCameraVector = normalize(cameraPosition - position);
//
//    vec3 specularColor = inColor.xyz * sun.color * clamp(pow(dot(reflectionVector, toCameraVector), 20.0f), 0.0, 1.0);

    return diffuseColor;
}

void main() {
    vec4 worldPosition = model * vec4(inPosition, 1.0);

    if (sunShaftRender) {
        passAmbientColor = vec4(0);
        passDiffuseColor = vec4(0);
        shadowCoords = vec4(0);
    } else {
        passAmbientColor = computeAmbientColor();
        passDiffuseColor = computeDirectionalColor(worldPosition.xyz);

        shadowCoords = shadowMatrix * worldPosition;

        vec3 toCameraVector = cameraPosition - worldPosition.xyz;
        float toCameraDistance = length(toCameraVector);
        float distance = toCameraDistance - (shadowDistance - transitionDistance);
        distance /= transitionDistance;
        shadowCoords.w = clamp(1.0 - distance, 0.0, 1.0);
    }

    if (waterPlane.w > worldPosition.y) {
        gl_ClipDistance[0] = dot(waterPlane, worldPosition);
    } else {
        gl_ClipDistance[0] = dot(worldPosition, worldPosition);
    }

    gl_Position = projection * view * worldPosition;
}

