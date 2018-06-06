#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord);

    if (sin(time / 3.0 + (v_texCoord.y - v_texCoord.x) * 10.0) > 0.999) {
        color.r = 1.0;
        color.g = 1.0;
        color.b = 1.0;
    }

    gl_FragColor = color;
}