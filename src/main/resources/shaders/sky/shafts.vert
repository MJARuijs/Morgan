#version 460

layout(location = 0) in vec3 inPosition;

out vec3 position;

void main() {
    position = inPosition;
}
