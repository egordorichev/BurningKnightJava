#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 pos;
uniform vec2 tpos;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 color = texture2D(u_texture, vec2(v_texCoord.x, v_texCoord.y));

    vec4 white = texture2D(u_texture2,
        vec2(
            v_texCoord.x - pos.x + tpos.x,
            v_texCoord.y - pos.y + tpos.y
        )
    );

    gl_FragColor = vec4(color.rgb, white.a);
}