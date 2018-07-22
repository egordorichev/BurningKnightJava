#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 size;
uniform vec2 pos;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

vec4 eff() {
  vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
  float x = (v_texCoord.x - pos.x) * cof.x;
  float y = (v_texCoord.y - pos.y) * cof.y;
  vec4 source = texture2D(u_texture, v_texCoord);

  float v = max(0.0, y - 0.2 + cos(x * 6.293) / 16.0);

  source.r = max(0.0, source.r - v);
  source.g = max(0.0, source.g - v);
  source.b = max(0.0, source.b - v);

  return source;
}

void main() {
    gl_FragColor = eff();
}