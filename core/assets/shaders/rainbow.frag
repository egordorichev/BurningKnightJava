#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float a;
uniform float time;
uniform vec2 pos;
uniform vec2 size;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
    float x = (pos.x - v_texCoord.x) * cof.x;
    float y = (pos.y - v_texCoord.y) * cof.y;

    vec4 c = texture2D(u_texture, v_texCoord.xy);
    c.a = min(a, c.a);

    if (c.r + c.g + c.b < 0.7) {
        gl_FragColor = c;
    } else {
        gl_FragColor = vec4(hsv2rgb(vec3(time + (x + y) / 2.0, 1.0, 1.0)), c.a);
   }
}