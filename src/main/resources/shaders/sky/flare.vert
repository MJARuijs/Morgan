#version 450

layout(location = 0) in vec2 inPosition;

uniform vec4 transformation;
uniform float aspectRatio;

out vec2 passTexCoords;

void main() {
    passTexCoords = (inPosition + 1.0) / 2.0;

    vec2 position = inPosition * transformation.zw + transformation.xy;
    position.x = position.x * 2.0 - 1.0;
    position.x /= aspectRatio;
    position.y = position.y * -2.0 + 1.0;

    gl_Position = vec4(position, 0, 1);
}
