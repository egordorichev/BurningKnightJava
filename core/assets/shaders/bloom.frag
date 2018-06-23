#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float a;
uniform float r;
uniform float g;
uniform float b;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

vec4 effect(vec4 colour) {
  float samples = 5.0;
  float quality = 0.0015;

  vec4 source = texture2D(u_texture, v_texCoord);
  vec4 sum = vec4(0.0);
  float diff = (samples - 1.0) / 2.0;
  vec2 sizeFactor = vec2(1.0) * quality;

  for (float x = -diff; x <= diff; x++) {
    for (float y = -diff; y <= diff; y++) {
      sum += texture2D(u_texture, v_texCoord + vec2(x, y) * sizeFactor);
    }
  }

  return ((sum / (samples * samples))) * colour;
}

void main() {
    gl_FragColor = effect(vec4(r, g, b, a));
}