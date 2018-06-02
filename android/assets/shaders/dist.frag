#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform vec2 pos;
uniform vec2 size;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
    float x = (pos.x - v_texCoord.x) * cof.x;
    float y = (pos.y - v_texCoord.y) * cof.y;
    float v = sin(time * 4.0 + y * 16.0) / (cof.x * 48.0);
    float u = cos(time * 3.0 + y * 16.0) / (cof.x * 48.0);

    vec4 color =
    gl_FragColor = texture2D(u_texture,
       vec2(
           clamp(v_texCoord.x + v, pos.x, pos.x + size.x),
           clamp(v_texCoord.y + u, pos.y, pos.y + size.y)
       )
   );
}