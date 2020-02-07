#version 450

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec2 inTextureCoord;
layout(location = 2) in vec3 inNormal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec4 waterPlane;
uniform mat4 shadowMatrix;
uniform vec3 cameraPosition;
uniform float shadowDistance;

const float transitionDistance = 10.0;

out vec4 worldPosition;
out vec4 shadowCoords;
out vec2 passTextureCoord;
out vec3 passNormal;

void main() {

    passTextureCoord = inTextureCoord;
    passNormal = mat3(model) * inNormal;

    worldPosition = model * vec4(inPosition, 1.0);

    gl_ClipDistance[0] = dot(worldPosition, waterPlane);
    shadowCoords = shadowMatrix * worldPosition;
    vec3 toCameraVector = cameraPosition - worldPosition.xyz;
    float toCameraDistance = length(toCameraVector);
    float distance = toCameraDistance - (shadowDistance - transitionDistance);
    distance /= transitionDistance;
    shadowCoords.w = clamp(distance, 0.0, 1.0);
    gl_Position = projection * view * worldPosition;
}
