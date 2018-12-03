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
   float v = max(0.0, v_texCoord.x * 1.0 + 1.0);
   float x = v_texCoord.x + (time * sign * 3.0) * (1.0 - v);
   x -= floor(x);
   float y = 0.5 + (v_texCoord.y - 0.5) * v;
   y -= floor(y);
   gl_FragColor = texture2D(u_texture, vec2(x * v, y));
}