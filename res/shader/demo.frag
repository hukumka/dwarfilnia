#version 430

uniform vec4 color;
uniform vec3 light_pos;
uniform vec4 Specular;
uniform vec4 Intensity;

in vec3 pos, norm;
noperspective in vec3 GEdgeDistance;

layout (location = 0) out vec4 FragColor;

vec4 getColor(){

    vec3 v = normalize(-pos);
    vec3 n = normalize(norm);
    vec3 s = normalize(light_pos.xyz - pos);
    vec3 h = normalize(v+s);

    float len = length(pos);
    float dm = 0.1/(len*len);

    float sDotN = max(dot(s, n), 0.0);

    return Intensity * (color*(1 + sDotN) + Specular * dm * pow(max(dot(h, n), 0.0), 100));// * dm;

}

void main() {
    vec4 color = getColor();
    float d = min(GEdgeDistance.x, GEdgeDistance.y);
    d = min(d, GEdgeDistance.z);

    float dst = length(pos);
    float w = 1;
    if (dst > 1)
        w /= dst;

    float mixVal = smoothstep( w - 1, w + 1, d);

    FragColor = mix( vec4(0, 0, 0, 1), color, mixVal );
}
