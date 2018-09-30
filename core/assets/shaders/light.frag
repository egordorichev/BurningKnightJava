#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord);
    float an = (1.0 - color.a * 1.5);
    float vl = (color.r + color.g + color.b);
    gl_FragColor = vec4(color.r * an, color.g * an, color.b * an, (3.0 - vl * 3.0 - (3.0 - vl) * 0.1) / 3.0);
}