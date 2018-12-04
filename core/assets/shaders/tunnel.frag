#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform float sign;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
   float y = v_texCoord.y + (time * sign * 16.0);
   y -= floor(y);
   gl_FragColor = texture2D(u_texture, vec2(v_texCoord.x, y));
}