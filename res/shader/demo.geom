#version 430

layout ( triangles ) in;
layout ( triangle_strip, max_vertices = 3) out;

in vec3 position[3];
out vec3 pos, norm;

void main() {

    vec3 normal = normalize(cross(position[1]-position[0], position[2]-position[0]));


    for (int i = 0; i < 3; i++){
        pos = position[i];
        norm = normal;
        gl_Position = gl_in[i].gl_Position;
        EmitVertex();
    }

    EndPrimitive();

}