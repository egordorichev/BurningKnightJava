#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float a;
uniform float r;
uniform float g;
uniform float b;
uniform float remove;
uniform vec2 size;
uniform vec2 pos;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

vec4 eff(vec4 colour) {
  vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
  float x = (v_texCoord.x - pos.x) * cof.x;
  float y = (v_texCoord.y - pos.y) * cof.y;
  vec4 source = texture2D(u_texture, v_texCoord);

  if (remove < 0.5) {
      float dx = (x - 0.5);
      float dy = (y - 0.5);
      float d = sqrt(dx * dx + dy * dy);

      float v = max(0.0, 1.41 - d * 3.0);

      source.g = min(1.0, v + source.g);
      source.b = min(1.0, v + source.b);
      source.r = min(1.0, v + source.r);
  }

  return colour * source;
}

void main() {
    gl_FragColor = eff(vec4(r, g, b, a));
}