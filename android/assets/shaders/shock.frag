#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    float x = v_texCoord.x;
    float y = v_texCoord.y;
    float dx = (0.5 - x) * 1.5;
    float dy = 0.5 - y;
    float d = sqrt(dx * dx + dy * dy) * 2.0 - time * 4.0;

    float v = max(0.0, (cos(d) / 2.0 - 0.45)) / 4.0;
    float a = atan(dy, dx);

    gl_FragColor = texture2D(u_texture,
       vec2(
           clamp(v_texCoord.x + v * cos(a), 0.0, 1.0),
           clamp(v_texCoord.y + v * sin(a), 0.0, 1.0)
       )
    );
}