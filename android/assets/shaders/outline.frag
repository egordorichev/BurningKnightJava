#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec3 u_color;
uniform float u_a;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
   vec4 color = texture2D(u_texture, v_texCoord.xy);

   gl_FragColor = vec4(u_color, min(color.a, u_a));
}