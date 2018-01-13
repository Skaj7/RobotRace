// simple vertex shader
varying vec3 N; // Normal vector in view space
varying vec3 vPosition; // Vertex position in view space

void main()
{
    N = normalize(gl_NormalMatrix * gl_Normal);
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;      // model view transform
    gl_FrontColor = gl_Color;
    vPosition = (gl_ModelViewMatrix * gl_Vertex).xyz / gl_Vertex.w;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}