#version 450

layout(location = 0) in vec2 inPosition;
layout(location = 1) in vec2 inTexCoords;

out vec2 passTexCoords;

void main() {
    passTexCoords = (inPosition + 1.0) / 2.0;
    passTexCoords.y = 1.0 - passTexCoords.y;
    vec2 position = inPosition.xy;
    gl_Position =  vec4(position.xy, 0.0, 1.0);
}
