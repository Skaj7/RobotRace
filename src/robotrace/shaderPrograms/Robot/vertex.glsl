// TODO intergrate Phong shader with this shader
attribute vec3 attrTangent; // Application provided tangent space vectors
attribute vec3 attrBitangent; // These vectors are normalized
varying mat3 tangentToEyeMatrix; // Tangent-to-eye matrix used in the fragment shader
varying vec3 N; // Normal vector in view space
varying vec3 vPosition; // Position in view space

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	vPosition = (gl_ModelViewMatrix * gl_Vertex).xyz / gl_Vertex.w;
	gl_TexCoord[0] = gl_MultiTexCoord0;
        gl_FrontColor  = gl_Color;
	N = normalize(gl_NormalMatrix * gl_Normal);
	tangentToEyeMatrix[0] = gl_NormalMatrix * attrTangent;
	tangentToEyeMatrix[1] = gl_NormalMatrix * attrBitangent;
	tangentToEyeMatrix[2] = gl_NormalMatrix * gl_Normal;
}