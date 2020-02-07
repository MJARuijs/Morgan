#version 460

layout(location = 0) in vec2 inPosition;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec3 cameraPosition;

out vec4 worldPosition;
out vec2 texCoords;
out vec3 toCameraVector;
out vec4 clipSpace;

void main() {
    worldPosition = model * vec4(inPosition.x, 0.0, inPosition.y, 1.0);
    clipSpace = projection * view * worldPosition;
    texCoords = inPosition / 2.0 + 0.5;
    toCameraVector = normalize(cameraPosition - worldPosition.xyz);

	gl_Position = clipSpace;
}
