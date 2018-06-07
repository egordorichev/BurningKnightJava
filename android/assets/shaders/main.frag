#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float heat;
uniform float time;
uniform vec2 shockPos;
uniform float shockTime;
uniform float glitchT;
uniform vec2 transPos;
uniform float transR;
uniform sampler2D u_texture;
uniform vec2 cam;
varying vec2 v_texCoord;
varying vec4 v_color;

float rand(vec2 co){
    return fract(cos(dot(co.xy ,vec2(12.9898, 78.233))) * 43758.5453);
}

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

    vec4 realColor = vec4(0.0);

    if (glitchT > 0.0) {
        vec3 uv = vec3(0.0);
        vec2 uv2 = vec2(0.0);
        vec2 tm = vec2(time);
        vec3 texColor = vec3(0.0);

        if (rand(tm) < 0.7) {
            texColor = texture2D(u_texture, vec2(x, y)).rgb;
        } else {
            texColor = texture2D(u_texture, vec2(x, y) * vec2(rand(tm), rand(tm * 0.99))).rgb;
        }

        float r = rand(tm * 0.001);
        float r2 = rand(tm * 0.1);

        if (v_texCoord.y > rand(vec2(r2)) && v_texCoord.y < r2 + rand(tm * 0.05)){
            if (r < rand(vec2(tm * 0.01))){
                if ((texColor.b + texColor.g + texColor.b) / 3.0 < r * rand(vec2(0.4, 0.5)) * 2.0){
                    uv.r -= sin(v_texCoord.x * r * 0.1 * tm.x) * r * 700.0;
                    uv.g += sin(v_texCoord.y * v_texCoord.x / 2.0 * 0.006 * tm.x) * r * 10.0 * rand(tm * 0.1);
                    uv.b -= sin(v_texCoord.y * v_texCoord.x * 0.5 * tm.x) * sin(v_texCoord.y * v_texCoord.x * 0.1) * r * 20.0;
                    uv2 += vec2(sin(v_texCoord.x * r * 0.1 * tm.x) * r);
                }
            }
        }

        texColor = texture2D(u_texture, vec2(x, y) + uv2).rgb;
        texColor += uv;

        realColor = vec4(texColor, 1.0);
    } else {
        realColor = texture2D(u_texture, vec2(x, y));
    }

    vec2 position = v_texCoord - vec2(0.5);
    float len = length(position);
    float vignette = smoothstep(0.75, 0.75 - 0.45, len);
    realColor.rgb = mix(realColor.rgb, realColor.rgb * vignette, 0.5);

    if (transR < 1.0) {
        float dx = (transPos.x - x) * 1.5;
        float dy = transPos.y - y;
        float d = sqrt(dx * dx + dy * dy);

        if (d < transR) {
            gl_FragColor = realColor;
        } else {
           gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        }
    } else {
        gl_FragColor = realColor;
    }
}