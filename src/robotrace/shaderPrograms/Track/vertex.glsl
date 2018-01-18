varying vec3 N; // Normal vector in view space
varying vec3 vPosition; // Position in view space

void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    vPosition = (gl_ModelViewMatrix * gl_Vertex).xyz / gl_Vertex.w;
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor  = gl_Color;
    N = normalize(gl_NormalMatrix * gl_Normal);
}