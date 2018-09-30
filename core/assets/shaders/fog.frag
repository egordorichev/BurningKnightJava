#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
uniform float cx;
uniform float cy;
uniform float sx;
uniform float sy;
uniform float szx;
uniform float szy;
uniform float time;
uniform float tx;
uniform float ty;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    float x = mod(v_texCoord.x + cx - sx + tx * time, szx) + sx;
    float y = mod(v_texCoord.y + cy - sy + ty * time, szy) + sy;
    vec4 color = texture2D(u_texture, vec2(x, y));
    //float mod = sin(time * 100.0 + color.r) * 0.5 + 0.5;
    //gl_FragColor = vec4(color.r * mod + (1.0 - color.r) * (1.0 - mod));


    float md = cos(time * 100.0 + texture2D(u_texture, vec2(
        mod(v_texCoord.x + cx - sx, szx * 2.0) * 0.5 + sx,
        mod(v_texCoord.y + cy - sy, szy * 2.0) * 0.5 + sy
    )).r * 10.0) * 0.5 + 0.5;

    float v = color.r * md;

    gl_FragColor = vec4(v, v, v, color.r * 0.5);
}