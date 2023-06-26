#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 textureCoordinate;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 Normal;
out vec3 FragPos;
out vec2 fragTextureCoordinate;
void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    FragPos = vec3(model * vec4(position, 1.0));
    Normal = normalize(model * vec4(normal, 0.0)).xyz;
//    fragTextureCoordinate = textureCoordinate * 100.0;
    fragTextureCoordinate = textureCoordinate;
}