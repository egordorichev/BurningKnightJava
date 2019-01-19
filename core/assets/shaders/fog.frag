#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
uniform float cx;
uniform float cy;
uniform float time;
uniform float tx;
uniform float ty;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    float x = mod(v_texCoord.x + cx + tx * time, 1.0);
    float y = mod(v_texCoord.y + cy + ty * time, 1.0);
    vec4 color = texture2D(u_texture, vec2(x, y));

    float md = cos(time * 100.0 + texture2D(u_texture, vec2(
        mod(v_texCoord.x + cx, 2.0) * 0.5,
        mod(v_texCoord.y + cy, 2.0) * 0.5
    )).r * 10.0) * 0.5 + 0.5;

    float v = color.r * md;

    gl_FragColor = vec4(v, v, v, color.r * 0.4);
}