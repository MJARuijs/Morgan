#version 460

const int size = 5;

layout (points) in;
layout (line_strip, max_vertices = 256) out;

//struct Pixel {
//    float depth;
//};
//
//layout(std430, binding = 0) buffer pixelBuffer {
//    Pixel pixels[];
//};

in vec3 position[1];

uniform mat4 projection;
uniform mat4 view;

uniform mat4 inverseProjection;
uniform mat4 inverseView;

uniform vec3 sunDirection;
uniform vec3 dimensions;
uniform vec3 translation;

uniform sampler2D depthTexture;

void main() {

    for (int x = -size; x < size; x++) {
        for (int y = -size; y < size; y++) {

            vec2 ndc = vec2(0.0);
            ndc.x = (2.0 * (position[0].x + x) / 500.0) - 1.0;
            ndc.y = (1.0 - (2.0 * (position[0].y + y) / 500.0));

            vec2 offset = vec2(x, y);
            vec2 texCoords = (position[0].xy + offset) / 500.0;

            float depth = texture(depthTexture, texCoords).r;

            if (depth < 1.0) {
                vec4 clipSpace = vec4(ndc, -1.0, 1.0);
                vec4 eyeSpace = inverseProjection * clipSpace;
                eyeSpace.z = 0.0;
                eyeSpace.w = 1.0;
                vec3 worldSpace = (inverseView * eyeSpace).xyz;
                worldSpace.y *= -1.0;
                worldSpace.y += translation.y * 2.0;

                gl_Position = projection * view * vec4(worldSpace, 1.0);
                EmitVertex();

                vec3 endPoint = vec3(worldSpace.xyz);
                endPoint -= normalize(sunDirection) * depth * dimensions;

                gl_Position = projection * view * vec4(endPoint, 1.0);
                EmitVertex();
                EndPrimitive();
            }
        }
    }
}
