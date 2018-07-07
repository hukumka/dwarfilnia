#version 430

layout (location = 0) in vec3 VertexPosition;

out vec3 position;
uniform mat4 MVP, ModelViewMatrix;

void main() {

    position = (ModelViewMatrix * vec4(VertexPosition, 1.0)).xyz;

    gl_Position = MVP * vec4(VertexPosition, 1.0);

}
