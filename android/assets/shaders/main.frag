#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float heat;
uniform float time;
uniform vec2 shockPos;
uniform float shockTime;
uniform sampler2D u_texture;
uniform vec2 cam;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    float x = v_texCoord.x;
    float y = v_texCoord.y;

    if (shockTime >= 0.0 && shockTime < 1.5) {
        float dx = (shockPos.x - x) * 1.5;
        float dy = shockPos.y - y;
        float d = sqrt(dx * dx + dy * dy) * 2.0 - shockTime * 4.0;

        float v = max(0.0, (cos(d) / 2.0 - 0.45)) / 3.0;
        float a = atan(dy, dx);

        x = clamp(v_texCoord.x + v * cos(a), 0.0, 1.0);
        y = clamp(v_texCoord.y + v * sin(a), 0.0, 1.0);
    }

    if (heat > 0.0) {
        float xx = v_texCoord.x + cam.x;
        float yy = v_texCoord.y + cam.y;
        float v = sin(-time * 8.0 + yy * 128.0 + xx * 64.0) * 0.0006;
        float u = cos(time * 8.0 + xx * 128.0 + yy * 32.0) * 0.0006;

        x = clamp(x + v, 0.0, 1.0);
        y = clamp(y + u, 0.0, 1.0);
    }

    gl_FragColor = texture2D(u_texture, vec2(x, y));
}