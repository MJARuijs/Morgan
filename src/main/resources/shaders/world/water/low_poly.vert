#version 450

const float PI = 3.1415926535897932384626433832795;

const float waveLength = 1.0;
const float waveAmplitude = 0.2;

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec4 inNeighbours;

struct DirectionalLight {
    vec4 color;
    vec3 direction;
};

uniform DirectionalLight sun;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec3 cameraPosition;
uniform float deltaTime;

out vec4 actualPosition;
out vec4 distortedPosition;
out vec3 passNormal;
out vec4 diffuseColor;
out vec4 specularColor;
out vec3 toCameraVector;

float generateHeight(vec4 position) {
    float radiansX = ((mod(position.x+position.z*position.x*0.2, waveLength)/waveLength) + deltaTime * mod(position.x * 0.8 + position.z, 1.5)) * 2.0 * PI;
    float radiansZ = ((mod(0.1 * (position.z*position.x +position.x*position.z), waveLength)/waveLength) + deltaTime * 2.0 * mod(position.x , 2.0) ) * 2.0 * PI;
    return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

vec3 calculateNormal(vec3 a, vec3 b, vec3 c) {
    vec3 v1 = b - a;
    vec3 v2 = c - a;
    vec3 cross = cross(v1, v2);
    return normalize(cross);
}

vec4 calculateDiffuse(vec3 normal) {
    vec3 lightDirection = normalize(sun.direction);
    float brightness = dot(normal, lightDirection);
    brightness = clamp(brightness, 0.0, 1.0);
    return sun.color * brightness;
}

vec4 calculateSpecular(vec3 position, vec3 normal) {
    vec3 lightDirection = normalize(sun.direction);
    vec3 reflectionVector = 2 * dot(lightDirection, normal) * normal - lightDirection;
    vec3 toCameraVector = normalize(cameraPosition - position);
    float brightness = dot(reflectionVector, toCameraVector);
    brightness = pow(brightness, 100.0);
    brightness = clamp(brightness, 0.0, 1.0);
    return sun.color * brightness;
}

void main() {
    vec3 currentPosition = vec3(inPosition.x, 0, inPosition.z);
    vec3 neighbour1 = currentPosition + vec3(inNeighbours.x, 0.0, inNeighbours.y);
    vec3 neighbour2 = currentPosition + vec3(inNeighbours.z, 0.0, inNeighbours.w);

    actualPosition = projection * view * model * vec4(currentPosition, 1.0);

    currentPosition.y = generateHeight(vec4(currentPosition, 1.0)) / 4.0f;
    neighbour1.y = generateHeight(vec4(neighbour1, 1.0)) / 4.0f;
    neighbour2.y = generateHeight(vec4(neighbour2, 1.0)) / 4.0f;

    distortedPosition = projection * view * model * vec4(currentPosition, 1.0);

    passNormal = calculateNormal(currentPosition, neighbour1, neighbour2);
    toCameraVector = normalize(cameraPosition - currentPosition);

    diffuseColor = calculateDiffuse(passNormal);
    specularColor = calculateSpecular(currentPosition, passNormal);
    gl_Position = distortedPosition;
}
