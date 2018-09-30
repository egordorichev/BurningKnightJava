#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord);
    float an = (1.0 - color.a);
    float vl = (color.r + color.g + color.b) / 3.0;
    gl_FragColor = vec4(color.r * an, color.g * an, color.b * an, 0.8 - vl * 1.0 - (1.0 - vl) * 0.05);
}