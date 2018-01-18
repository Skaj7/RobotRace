varying vec3 N;
varying vec3 vPosition;
uniform sampler2D track;

// Returns a vec4 with the rgba values to give to gl_FragColor, using the Phong model
vec4 shading(vec3 pos, vec3 N, gl_LightSourceParameters light, gl_MaterialParameters mat, vec3 color) {
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
	vec4 ambient = light.ambient * mat.ambient;
	
	// DIFFUSE	
	vec4 diffuse = light.diffuse * mat.diffuse * diffuseStrength;
	
	// SPECULAR
	//vec4 specular = light.specular * mat.specular * pow(specularStrength, mat.shininess);
	
        vec4 specular = light.specular * mat.specular * pow(specularStrength, mat.shininess);

	return emission + ambient + diffuse + specular;
}


void main() {
    // On the form (vec4 ambient, vec4 diffuse, vec4 specular, vec4 position, ...)
    gl_LightSourceParameters light = gl_LightSource[0];  
    // On the form (vec4 emission, vec4 ambient, vec4 diffuse, vec4 specular, float shininess)
    gl_MaterialParameters mat = gl_FrontMaterial;

    gl_FragColor = texture2D(track, vec2(gl_TexCoord[0].s, gl_TexCoord[0].t));
    //color = texture2D(track, vec2(gl_TexCoord[0].s, gl_TexCoord[0].t)).xyz;
    //gl_FragColor = shading(vPosition, N, light, mat, color);
}