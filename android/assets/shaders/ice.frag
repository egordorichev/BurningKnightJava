#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float a;
uniform float f;
uniform float time;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 c = texture2D(u_texture, v_texCoord.xy);
    c.a = min(a, c.a);

    if (c.r + c.g + c.b < 0.7) {
        gl_FragColor = c;
    } else {
        gl_FragColor = c + vec4(-abs(sin(time / 2.0) / 4.0), 0.25, 0.5, -0.3) * f;
   }
}