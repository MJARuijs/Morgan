#version 450 core

layout(location = 0) in vec3 position;

uniform mat4 projection;
uniform mat4 view;

out vec3 texCoord;

void main() {
    texCoord = position;
	gl_Position = projection * mat4(mat3(view)) * vec4(position, 1.0);
	gl_Position = gl_Position.xyww;
}