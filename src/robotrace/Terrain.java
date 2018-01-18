package robotrace;

import static com.jogamp.opengl.GL.GL_BLEND;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL2.*;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {
    
    public Terrain() {
        
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        gl.glEnable(GL_BLEND);
        gl.glPushMatrix();
        //draw quad strips to use in the shader
        for(int i = -20; i < 20; i+=1){
            gl.glBegin(GL_QUAD_STRIP);
            for(int j = -20; j < 21; j+=1){
                gl.glVertex3d(j, i, 0);
                gl.glVertex3d(j, i+1, 0);
            }
            gl.glEnd(); 
        }
        gl.glPopMatrix();
        
        
        Textures.brick.disable(gl);
        Textures.sky.bind(gl);

        gl.glPushMatrix();
            //water surface, made transparent and grey. With the cubemapping skybox reflection.
            gl.glBegin(GL_QUAD_STRIP);
                gl.glVertex3d(-20, 20, 0);
                gl.glVertex3d(-20, -20, 0);
                gl.glVertex3d(20, 20, 0);
                gl.glVertex3d(20, -20, 0);
            gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL_BLEND);
    }
}
    
    

