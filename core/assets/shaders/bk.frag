#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform float a;
uniform float white;
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
    vec2 ps = vec2(
       clamp(v_texCoord.x + v / (cof.x * 16.0), pos.x, pos.x + size.x),
       clamp(v_texCoord.y + round(sin(time * 2.0 + x)) / (cof.y * 24.0), pos.y, pos.y + size.y)
    );

    vec4 color = texture2D(u_texture, ps);

    color.a = min(color.a, a);

    float xx = (ps.x - pos.x) * cof.x;
    float yy = (ps.y - pos.y) * cof.y;

    float dx = (xx - 0.5);
    float dy = (yy + 0.5);
    float d = sqrt(dx * dx + dy * dy);

    float sum = max(0.0, 1.41 - d * 2.0 + cos(time * 1.4));

    color.r = min(1.0, sum + color.r);
    color.g = min(1.0, sum + color.g);
    color.b = min(1.0, sum + color.b);

    color = vec4(1.0, (abs(cos(time * 2.0) / 2.5)) + color.g, color.b * 0.5, color.a);

    if (white > 0.5) {
        color.r += 0.7;
        color.g += 0.4;
        color.b += 0.4;
    }

    gl_FragColor = color;
}