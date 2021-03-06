
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
uniform vec2 pos;
uniform vec2 epos;
uniform vec2 bpos;
uniform float burnStep;
uniform vec2 size;
uniform vec2 tpos;
uniform float spread;
uniform float a;
uniform float spreadStep;
uniform float water;
uniform float speed;
uniform float activated;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    if (activated < 0.5) {
        gl_FragColor = texture2D(u_texture, v_texCoord.xy);
        return;
    }

    if (spread > 0.5) {
        vec4 map = texture2D(u_texture2,
            vec2(
                v_texCoord.x - pos.x + tpos.x,
                v_texCoord.y - pos.y + tpos.y
            )
        );

        if (map.a == 1.0 && map.r <= spreadStep) {
            vec4 edge = texture2D(u_texture2,
                vec2(
                    v_texCoord.x - pos.x + epos.x,
                    v_texCoord.y - pos.y + epos.y
                )
            );

            if (edge.a == 1.0 && edge.r == 1.0 && edge.g == 0.0 && edge.b == 0.0) {
                gl_FragColor = texture2D(u_texture, v_texCoord.xy);
            } else {
                gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
            }
        } else {
            gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
        }
    } else if (water > 0.5) {
        vec4 edge = texture2D(u_texture2,
            vec2(
                v_texCoord.x - pos.x + tpos.x,
                v_texCoord.y - pos.y + tpos.y
            )
        );

        if (edge.a == 1.0 && edge.r == 1.0 && edge.g == 0.0 && edge.b == 0.0) {
            vec2 cof = vec2(1.0 / size.x, 1.0 / size.y);
            float m = (time * speed / (cof.y * 2.0));
            m -= floor(m / (size.y * 4.0) + 1.0) * (size.y * 4.0);
            gl_FragColor = texture2D(u_texture, vec2(v_texCoord.x, v_texCoord.y - m));
            gl_FragColor.a = a;
        } else {
            gl_FragColor = edge;
        }
    } else {
        /*if (burnStep > 0.0) {
            vec4 burn = texture2D(u_texture2,
                vec2(
                    v_texCoord.x - pos.x + bpos.x,
                    v_texCoord.y - pos.y + bpos.y
                )
            );

            if (map.a == 1.0 && map.r > spreadStep) {
                gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
                return;
            }
        }*/

        vec4 edge = texture2D(u_texture2,
            vec2(
                v_texCoord.x - pos.x + tpos.x,
                v_texCoord.y - pos.y + tpos.y
            )
        );

       if (edge.a == 1.0 && edge.r == 1.0 && edge.g == 0.0 && edge.b == 0.0) {
            gl_FragColor = texture2D(u_texture, vec2(v_texCoord.x, v_texCoord.y));
       } else {
            gl_FragColor = edge;
       }
    }
}