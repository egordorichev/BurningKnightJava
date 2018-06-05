#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform float a;
uniform vec2 pos;
uniform vec2 size;
uniform sampler2D u_texture;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
    float x = (pos.x - v_texCoord.x) * cof.x;
    float y = (pos.y - v_texCoord.y) * cof.y;
    float v = round(sin(time * 4.0 + y * 8.0));
    vec2 pos = vec2(
       clamp(v_texCoord.x + v / (cof.x * 16.0), pos.x, pos.x + size.x),
       clamp(v_texCoord.y + round(sin(time * 2.0 + x)) / (cof.y * 24.0), pos.y, pos.y + size.y)
    );

    vec4 color = texture2D(u_texture, pos);

    color.a = min(color.a, a);

    int diff = 1;
    float sum = 0.0;

    for (int x = -diff; x <= diff; x++) {
       for (int y = -diff; y <= diff; y++) {
         vec4 c = texture2D(u_texture, pos + vec2(x, y) * 0.0009);

         if (c.a > 0.0 && c.r > 0.8) {
            sum += c.r;
         }
       }
    }

    float s = 20.0 * (abs(sin(time * 5.0)) / 2.0 + 0.5);
    sum = sum / s;

    gl_FragColor = vec4(max(abs(cos(time / 1.5)) / 2.0, sum + color.r * abs(cos(time * 3.0)) * 2.0), sum * abs(cos(time * 2.0) / 2.5) + color.g, color.b, color.a);
}