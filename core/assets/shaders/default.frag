#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
   gl_FragColor = vec4(texture2D(u_texture, v_texCoord.xy).rgba);
}