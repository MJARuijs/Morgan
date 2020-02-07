#version 450 core

in vec3 texCoord;

uniform samplerCube sampler;

out vec4 color;

void main() {
    color = texture(sampler, texCoord);
}
