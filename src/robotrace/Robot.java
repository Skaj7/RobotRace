package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material) {
        this.material = material;
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim){
        
        gl.glPushMatrix();
            //skybox reflection texture for the gold and silver robot
            Textures.brick.disable(gl);
            Textures.sky.bind(gl);
            
            gl.glMaterialfv(GL_FRONT,GL_DIFFUSE, material.diffuse,0);
            gl.glMaterialfv(GL_FRONT,GL_SPECULAR, material.specular,0);
            gl.glMaterialfv(GL_FRONT,GL_SHININESS, new float[] {material.shininess},0);   
            
            gl.glTranslated(position.x, position.y, position.z);

            // Orient robots based on direction
            // Without rotation, they look along (0, 1, 0). We must rotate such that this becomes the direction vector
            double angleBetween = Math.toDegrees(Math.acos(direction.dot(new Vector(0, 1, 0))));
            gl.glRotated(angleBetween, 0, 0, position.y > 0 ? 1 : -1);
            
            torso(gl, glut);
            head(gl, glut,tAnim);
            leftArm(gl,glut, tAnim);
            rightArm(gl,glut, tAnim);
            leftLeg(gl,glut, tAnim);
            rightLeg(gl,glut, tAnim);

        gl.glPopMatrix();
       
    }
    
    public void head(GL2 gl,GLUT glut,float tAnim){
        gl.glPushMatrix();
            double angle = Math.cos(tAnim*6)*2;

            gl.glRotated(angle, 0, 1, 0);
            gl.glTranslated(0, 0, 1.95);
            glut.glutSolidSphere(0.2, 50, 50);
            gl.glTranslated(0, 0, -0.3);
            glut.glutSolidCylinder(0.1, 0.2, 50, 50);
            
        gl.glPopMatrix();
    }
    
    public void torso(GL2 gl,GLUT glut){
        gl.glPushMatrix();
            gl.glTranslated(0, 0, 1.35);
            gl.glScaled(0.5, 0.3, 0.8);
            glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }
    
        public void leftLeg(GL2 gl,GLUT glut, float tAnim){
        gl.glPushMatrix();
            gl.glTranslated(-0.15, 0, 0);
            thigh(gl,glut,Math.sin(tAnim*3));
            gl.glTranslated(0.15, 0, 0);
        gl.glPopMatrix();
    }

    public void rightLeg(GL2 gl,GLUT glut, float tAnim){
        gl.glPushMatrix();
            gl.glTranslated(0.15, 0, 0);
            thigh(gl,glut,Math.cos(tAnim*3));
            gl.glTranslated(-0.15, 0, 0);
        gl.glPopMatrix();
    }
    
    public void thigh(GL2 gl,GLUT glut, double d){
        //Hip + thigh
        //angle of movement
        double angle = d*100;
        gl.glTranslated(0, 0, 0.9);
        
        if(angle < 0){
            angle=angle*-1;
        }

        angle=angle-20;
        gl.glRotated(angle, 1, 0, 0);

        glut.glutSolidSphere(0.12, 50, 50);
        gl.glTranslated(0, 0, -0.2);
        gl.glScaled(0.15, 0.15, 0.45);
        glut.glutSolidCube(1f);
        gl.glScaled(1/0.15, 1/0.15, 1/0.45);
        
        //Knee + shin
        angle = d*160;
        shin(gl,glut,angle);
    }
    public void shin(GL2 gl,GLUT glut, double angle){
        //Knee + shin
        gl.glTranslated(0, 0, -0.25);
        
        //angle of movement
        if(angle > 0){
            angle=angle*-1;
        }
        angle=angle+80;
        if(angle > 0){
            angle=angle*-1;
        }
       
        gl.glRotated(angle, 1, 0, 0);
        glut.glutSolidSphere(0.1, 50, 50);
        gl.glTranslated(0, 0, -0.2);
        gl.glScaled(0.15, 0.15, 0.45);
        glut.glutSolidCube(1f);
        gl.glScaled(1/0.15, 1/0.15, 1/0.45);
        
        //foot
        foot(gl,glut);
    }
    public void foot(GL2 gl,GLUT glut){
        //foot
        gl.glTranslated(0, 0.05, -0.25);
        gl.glScaled(0.1, 0.15, 0.1);
        glut.glutSolidSphere(1, 50, 50);
    }
    
    
    
    
    public void leftArm(GL2 gl,GLUT glut, float tAnim){
        gl.glPushMatrix();
            gl.glTranslated(-0.33, 0, 0);
            upperArm(gl,glut,Math.sin(tAnim*3));
            gl.glTranslated(0.33, 0, 0);
        gl.glPopMatrix();
    }
    
    public void rightArm(GL2 gl,GLUT glut,float tAnim){
        gl.glPushMatrix();
            gl.glTranslated(0.33, 0, 0);
            upperArm(gl,glut,Math.cos(tAnim*3));
            gl.glTranslated(-0.33, 0, 0);
        gl.glPopMatrix();
    }
    
    public void upperArm(GL2 gl,GLUT glut,double d){
        //sholder+ upperarm
        //angle of movement
        double angle = d*40;
        
        gl.glTranslated(0, 0, 1.65);
        
        if(angle > 0){
            angle=angle*-1;
        }
        angle=angle+10;
        gl.glRotated(angle, 1, 0, 0);
        
        glut.glutSolidSphere(0.1,50,50);        

        gl.glTranslated(0, 0, -0.22);
        gl.glScaled(0.1, 0.1, 0.3);
        glut.glutSolidCube(1f);
        gl.glScaled(10, 10, 1/0.3);
        
        //elbow + forearm + hand
        //angle of movement
        angle = d*5;
        forearm(gl, glut, angle);
    }
    public void forearm(GL2 gl,GLUT glut,double angle){
        
        //elbow + forearm + hand
        gl.glTranslated(0, 0, -0.15);
        
        gl.glRotated(90+angle, 1, 0, 0);
        glut.glutSolidSphere(0.07,50,50);

        gl.glTranslated(0, 0, -0.15);
        gl.glScaled(0.1, 0.1, 0.3);
        glut.glutSolidCube(1f);
        gl.glScaled(10, 10, 1/0.3);

        hand(gl,glut);
    }
    
    public void hand(GL2 gl,GLUT glut){
        gl.glTranslated(0, 0.05, -0.2);
        gl.glScaled(0.1, 0.15, 0.1);
        glut.glutSolidSphere(1, 50, 50);
    }
}
