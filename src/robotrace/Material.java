package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (
            
        new float[] {0.34615f, 0.3143f, 0.0903f,	1},
        new float[] {0.797357f, 0.723991f, 0.208006f, 1},
        83.2f

    ),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
            
        new float[] {0.2775f, 0.2775f, 0.2775f,	1},
        new float[] {0.773911f,	0.773911f, 0.773911f, 1},
        89.6f

    ),

    /** 
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
            
        new float[] {190/255f, 140/255f, 0, 1},
        new float[] {255/255f, 140/255f, 0/255f, 1},
        43f

    ),

    /**
     * Wood material properties.
     * Modify the default values to make it look like Wood.
     */
    WOOD (

        new float[] {96/255f, 72/255f, 0, 1},
        new float[] {96/255f, 72/255f, 0/255f, 1},
        25f

    );

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
