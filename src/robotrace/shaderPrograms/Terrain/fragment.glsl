
uniform sampler2D cubemap; // pick up texture map installed in ShaderMaker 

varying float Reflect;
varying vec3 R; // vector from object vertex to eye reflected in the normal 
float Q=1.0/4.0; // quarter 
float T=1.0/3.0; // third 

// Given methods to create homogeneous matrices 
mat3 Translate(vec2 t) 
{ 
	return mat3(vec3(1,0,0), vec3(0,1,0), vec3(t.x,t.y,1));
} 
mat3 Scale(vec2 s) 
{ 
	return mat3(vec3(s.x,0,0),vec3(0,s.y,0),vec3(0,0,1)); 
} 
mat3 Mat(vec2 v0, vec2 v1, vec2 t) 
{ 
	return mat3(vec3(v0,0),vec3(v1,0),vec3(t,1)); 
} 

vec2 computeTexCoords(vec3 R) {
	vec3 texCoords;
	vec2 translateTo;
	// used to get rid of the noise on the texture.
	float n = 0.9999;
	// C is where a half-line from the origin toward R intersects the unit square
	vec3 C = R/max(max(abs(R.x), abs(R.y)), abs(R.z));
	
	/*
	Get the correct texCoords by case distiction. And fill in the correct translateTo
	values to translate the coordinates to the right square on the texture map. Some 
	values are negative to rotate the texture.
	*/
	if (C.x >= n){
		texCoords = vec3(C.z, -C.y, 1.0);
		translateTo = vec2(2.0*Q,T);
	}else if (C.x <= -n){
		texCoords = vec3(-C.z, -C.y, 1.0);
		translateTo = vec2(0.0,T);
	}else if (C.y >= n){
		texCoords = vec3(C.x, -C.z, 1.0);
		translateTo = vec2(Q,0.0);
	}else if (C.y <= -n){
		texCoords = vec3(C.x, C.z, 1.0);
		translateTo = vec2(Q,2.0*T);
	}else if (C.z >= n){
		texCoords = vec3(-C.x, -C.y, 1.0);
		translateTo = vec2(3.0*Q,T);
	}else if (C.z <= -n){
		texCoords = vec3(C.x, C.y, 1.0);
		translateTo = vec2(Q,T);
	}
	//translate to origin.
	mat3 translate = Translate(vec2(1.0,1.0)); 
	/*	
	scale coordinates to the right size and translate it to the right position 
	using translateTo. Here we use excersise 2.1 and 2.2 to calculate the scale.
	*/
	mat3 ScaleAndTranslate = Mat(vec2(Q/2.0,0.0),vec2(0.0,T/2.0),translateTo);
	
	texCoords = ScaleAndTranslate*translate*texCoords;	

	return texCoords.xy;
}

void main(void) { 
        if (Reflect == 1){
            vec3 color = texture2D(cubemap, computeTexCoords(R)).xyz;
            gl_FragColor = vec4(color,0.3);
       } else {
           gl_FragColor = gl_Color;
        }
}