varying vec3 N;
varying vec3 vPosition;
uniform sampler2D track;

// Returns a vec4 with the rgba values to give to gl_FragColor, using the Phong model
vec4 shading(vec3 pos, vec3 N, gl_LightSourceParameters light, gl_MaterialParameters mat, vec4 texture) {
	// Vector L from light source toward the vertex
	vec3 L = normalize(pos - light.position.xyz);
	// Vector R, the reflection direction of L as it bounces off the vertex
	vec3 R = normalize(reflect(L, N));
	// Vector C from vertex toward camera
	vec3 C = normalize(-pos);
	// Angle theta between -L and the normal vector
	float cosTheta = dot(normalize(-L), normalize(N));
	// Clamp the angle to get diffuse strength
	float diffuseStrength = clamp(cosTheta, 0, 1);
	// Angle alpha between R and C
	float cosAlpha = dot(R, C);
	// Specular strength
	float specularStrength = clamp(cosAlpha, 0, 1);	

	// EMISSION
	vec4 emission = mat.emission;
	
	// AMBIENT
	vec4 ambient = light.ambient * texture;
	
	// DIFFUSE	
	vec4 diffuse = light.diffuse * texture * diffuseStrength;
	
	// SPECULAR	
        vec4 specular = light.specular * texture * pow(specularStrength, mat.shininess);

	return emission + ambient + diffuse + specular;
}


void main() {
    // On the form (vec4 ambient, vec4 diffuse, vec4 specular, vec4 position, ...)
    gl_LightSourceParameters light = gl_LightSource[0];  
    // On the form (vec4 emission, vec4 ambient, vec4 diffuse, vec4 specular, float shininess)
    gl_MaterialParameters mat = gl_FrontMaterial;

    vec4 texture = texture2D(track,gl_TexCoord[0].st);
    vec4 shade = shading(vPosition, N, light, mat, texture);

    gl_FragColor = shade; 
}