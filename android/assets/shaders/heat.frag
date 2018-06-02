#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform sampler2D u_texture;
uniform vec2 cam;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    float x = v_texCoord.x + cam.x;
    float y = v_texCoord.y + cam.y;
    float v = sin(time * 8.0 + y * 128.0) * 0.0004;
    float u = cos(time * 8.0 + x * 128.0) * 0.0004;

    gl_FragColor = texture2D(u_texture,
       vec2(
           clamp(v_texCoord.x + v, 0.0, 1.0),
           clamp(v_texCoord.y + u, 0.0, 1.0)
       )
   );
}