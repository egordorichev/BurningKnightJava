#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform vec2 pos;
uniform vec2 size;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
    float x = (pos.x - v_texCoord.x) * cof.x;
    float y = (pos.y - v_texCoord.y) * cof.y;
    float v = round(sin(time * 4.0 + y * 8.0));

    vec4 color = texture2D(u_texture,
        vec2(
            clamp(v_texCoord.x + v / (cof.x * 24.0), pos.x, pos.x + size.x),
            clamp(v_texCoord.y + round(sin(time * 4.0 + x)) / (cof.y * 24.0), pos.y, pos.y + size.y)
        )
    );

    if (sin(time + y * (32.0 + cos(time / 2.0) * 10.0)) + cos(time * 3.0 + y * 64.0) > abs(sin(time * 1.5 + y * 48.0) * 0.2 + 0.8)) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
    } else {
        gl_FragColor = color;
    }
}