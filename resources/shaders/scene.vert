#version 330
layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 textureCoordinate;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
//uniform float useFakeLightning;

out vec3 Normal;
out vec3 FragPos;
out vec2 fragTextureCoordinate;

// fog coba
out float visibility;
const float density = 0.007;
const float gradient = 1.5;

//model sama spt transformation matrix
void main() {
    vec4 positionRelativeToCam = view * model * vec4(position, 1.0);
    gl_Position = projection * positionRelativeToCam;

//    vec3 actualNormal = normal;
//    if (useFakeLightning > 0.5){
//        actualNormal = vec3(0.0, 1.0, 0.0);
//    }

    FragPos = vec3(model * vec4(position, 1.0));
    Normal = normalize(model * vec4(normal, 0.0)).xyz;
    fragTextureCoordinate = textureCoordinate;


    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance*density), gradient *1.0));
    //supaya tetep 0 - 1
    visibility = clamp(visibility, 0.0, 1.0);
}