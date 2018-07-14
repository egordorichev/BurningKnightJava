uniform mat4 u_projTrans;


uniform vec4 u_textureSizes;
uniform vec4 u_sampleProperties;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    v_color = a_color;
    v_color.a = v_color.a * (255.0 / 254.0);

    vec2 uvSize = u_textureSizes.xy;
    float upscale = u_textureSizes.z;

    v_texCoord.x = a_texCoord0.x + (u_sampleProperties.z / upscale) / uvSize.x;
    v_texCoord.y = a_texCoord0.y + (u_sampleProperties.w / upscale) / uvSize.y;

    gl_Position = u_projTrans * a_position;
}