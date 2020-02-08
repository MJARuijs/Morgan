#version 450

layout(location = 0) in vec2 position;

out vec2 passTexCoords;

void main() {
    passTexCoords = (position + 1.0) / 2.0;
    gl_Position = vec4(position, 0, 1);
}
