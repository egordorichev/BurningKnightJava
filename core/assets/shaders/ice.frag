#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float a;
uniform float f;
uniform float time;
uniform float poisoned;
uniform float freezed;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 c = texture2D(u_texture, v_texCoord.xy);
    c.a = min(a, c.a);

    if (c.r + c.g + c.b >= 0.7) {
        if (poisoned > 0.5) {
            c += vec4(-0.3, 0.8, -0.3, 0.0) * f;
        }

        if (freezed > 0.5) {
            c += vec4(-abs(sin(time / 2.0) / 4.0), 0.25, 0.5, -0.3) * f;
        }
    }

    gl_FragColor = c;
}