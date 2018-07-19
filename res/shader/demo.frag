#version 430

uniform vec4 color;
uniform vec3 light_pos;
uniform vec4 Specular;
uniform vec4 Intensity;

in vec3 pos, norm;

layout (location = 0) out vec4 FragColor;

vec4 getColor(){

    vec3 v = normalize(-pos);
    vec3 n = normalize(norm);
    vec3 s = normalize(light_pos.xyz - pos);
    vec3 h = normalize(v+s);

    float len = length(pos);
    float dm = 1/(len*len);


    float sDotN = max(dot(s, n), 0.0);

    return Intensity * (color*(1 + sDotN) + Specular * pow(max(dot(h, n), 0.0), 100)) * dm;

}

void main() {

	FragColor = getColor();
}
