#version 450

layout(location = 0) in vec2 inPosition;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform float size;
out vec2 passTexCoords;

void main() {
    passTexCoords = (inPosition + 1.0) / 2.0;
    gl_Position = projection * view * model * vec4(size * inPosition, 0.9999, 1);
}
