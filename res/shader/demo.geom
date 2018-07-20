#version 430

layout ( triangles ) in;
in vec3 position[3];
uniform mat4 ViewportMatrix;



layout ( triangle_strip, max_vertices = 3) out;
out vec3 pos, norm;
noperspective out vec3 GEdgeDistance;


void main() {


    vec3 normal = normalize(cross(position[1]-position[0], position[2]-position[0]));


    vec3 p0 = vec3(ViewportMatrix * (gl_in[0].gl_Position / gl_in[0].gl_Position.w));
    vec3 p1 = vec3(ViewportMatrix * (gl_in[1].gl_Position / gl_in[1].gl_Position.w));
    vec3 p2 = vec3(ViewportMatrix * (gl_in[2].gl_Position / gl_in[2].gl_Position.w));

    // Находим высоты треугольника
    float a = length(p1 - p2);
    float b = length(p2 - p0);
    float c = length(p0 - p1);
    float alpha = acos( (b*b + c*c - a*a) / (2.0*b*c) );
    float beta  = acos( (a*a + c*c - b*b) / (2.0*a*c) );
    float ha = abs( c * sin(beta) );
    float hb = abs( c * sin(alpha));
    float hc = abs( b * sin(alpha));

    // Выкидываем треугольник вместе со всеми данными
    GEdgeDistance = vec3(ha, 0, 0);
    pos = position[0];
    norm = normal;
    gl_Position = gl_in[0].gl_Position;
    EmitVertex();

    GEdgeDistance = vec3(0, hb, 0);
    pos = position[1];
    norm = normal;
    gl_Position = gl_in[1].gl_Position;
    EmitVertex();

    GEdgeDistance = vec3(0, 0, hc);
    pos = position[2];
    norm = normal;
    gl_Position = gl_in[2].gl_Position;
    EmitVertex();

    EndPrimitive();

}