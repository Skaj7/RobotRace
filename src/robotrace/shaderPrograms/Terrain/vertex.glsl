// simple vertex shader

varying vec3 R;
varying float Reflect;
//varying bool shading;

float height(float x, float y){

	float A = 0.6 * cos(0.3 * x + 0.2 * y) + 0.4 * cos(x - 0.5 * y);

/*
	if (A < -1.0){
		A=-1.0;
	}
	if(A>1.0) {
		A=1.0;
	}
*/

	return A;
}

float random( vec2 p )
{
    vec2 K1 = vec2(
        23.14069263277926, // e^pi (Gelfond's constant)
         2.665144142690225 // 2^sqrt(2) (Gelfondâ€“Schneider constant)
    );
    return (fract( cos( dot(p,K1) ) * 12345.6789 )*2)-1;
}

vec4 color(float height){
	vec4 C;
	C.w =1;
	if(height < 0.0){
		C.b = 50;
		//return C;
	}else if (height < 0.5){
		C.r = 50;
		C.g = 50;
	}else{
		C.g = 50;
	}
	return C;
}


void main()
{
	// N = the normal vector from the vertex in view space 
	vec3 N = normalize(gl_NormalMatrix * gl_Normal);
	// I = the incident vector from the camera to the vertex in view space
	vec3 I = (gl_ModelViewMatrix * gl_Vertex).xyz / gl_Vertex.w; 
	// R = the reflection vector in view space 
	R = I - 2.0*dot(I, N)*N, 1.0;

	//R = -R;
	// Transform R back to model space
	R = (gl_ModelViewMatrixInverse * vec4(R, 0.0)).xyz;


 	//vec3 position = (gl_ModelViewProjectionMatrix * gl_Vertex).xyz / gl_Vertex.w ;
	
	vec3 offset; 
	offset.xy = vec2(gl_Vertex.x, gl_Vertex.y);//, height(gl_Vertex.x, gl_Vertex.y));
	if(offset.x != -20 && offset.x != 20 && offset.y != -20 && offset.y != 20){
            offset.z= random(offset.xy);
            gl_FrontColor  = color(offset.z);
            Reflect=0;
	} else{
            gl_FrontColor  = vec4(220,220,220,0.2);
            Reflect=1;
        }

	//gl_Position    = V;
	
	gl_TexCoord[0] = gl_MultiTexCoord0;

	//p = p + offset(p) * gl_Normal;

	gl_Position = gl_ModelViewProjectionMatrix * vec4(offset,1);
}


/*
void main()
{

	// N = the normal vector from the vertex in view space 
	vec3 N = normalize(gl_NormalMatrix * gl_Normal);
	// I = the incident vector from the camera to the vertex in view space
	vec3 I = (gl_ModelViewMatrix * gl_Vertex).xyz / gl_Vertex.w; 
	// R = the reflection vector in view space 
	R = I - 2.0*dot(I, N)*N, 1.0;
	// Transform R back to model space
	R = (gl_ModelViewMatrixInverse * vec4(R, 0.0)).xyz;


	//vertex shader output	
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor  = gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;
}
*/