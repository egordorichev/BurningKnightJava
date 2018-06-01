#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform vec2 pos;
uniform vec2 size;
uniform vec2 tpos;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
    float x = (pos.x - v_texCoord.x) * cof.x;
    float y = (pos.y - v_texCoord.y) * cof.y;
    float v = sin(time * 4.0 + y * 8.0) / (cof.x * 16.0);
    float m = (time / (cof.x * 2.0));


    m -= floor(m / (size.x * 4.0)) * size.x * 4.0;

    vec4 color = texture2D(u_texture,
        vec2(
            clamp(v_texCoord.x + v, pos.x, pos.x + size.x) + m,
            v_texCoord.y
        )
    );

    vec4 white = texture2D(u_texture2,
        vec2(
            v_texCoord.x - pos.x + tpos.x,
            v_texCoord.y - pos.y + tpos.y
        )
    );

    gl_FragColor = vec4(color.rgb, white.a);
}