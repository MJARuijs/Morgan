#version 450

layout(location = 0) in vec2 inTexCoords;

out vec2 passTexCoords;

void main() {
    passTexCoords = (inTexCoords + 1.0) / 2.0;
    vec2 position = inTexCoords;
    gl_Position = vec4(position, 0.0, 1.0);
}
