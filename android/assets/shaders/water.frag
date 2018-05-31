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
    float v = sin(time * 4.0 + y * 8.0);
    // float u = sin(time * 4.0 + x * 8.0);

    vec4 color = texture2D(u_texture,
        vec2(
            clamp(v_texCoord.x + v / (cof.x * (32.0 + sin(v_texCoord.y * 32.0) * 16.0)), pos.x, pos.x + size.x),
            v_texCoord.y // clamp(v_texCoord.y + u / (cof.y * 32.0), pos.y, pos.y + size.y)
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