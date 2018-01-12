package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
//    /**
//     * Draws this track, based on the control points.
//     */
//    public void draw(GL2 gl, GLU glu, GLUT glut) {
//        
//    }
//    
//    /**
//     * Returns the center of a lane at 0 <= t < 1.
//     * Use this method to find the position of a robot on the track.
//     */
//    public Vector getLanePoint(int lane, double t){
//
//        return Vector.O;
//
//    }
//    
//    /**
//     * Returns the tangent of a lane at 0 <= t < 1.
//     * Use this method to find the orientation of a robot on the track.
//     */
//    public Vector getLaneTangent(int lane, double t){
//        
//        return Vector.O;
//
//    }
    
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        //top of the track
//        ShaderPrograms.trackShader.useProgram(gl);
//        Textures.track.enable(gl);
//        Textures.track.bind(gl);
//        gl.glPushMatrix();
//        gl.glBegin(GL_TRIANGLE_STRIP);
//        
//        for(double i = 0; i < 1.002; i+=0.001){
//            Vector V = new Vector(0.0,0.0,1.0).cross(getTangent(i));
//            V = V.normalized();
//            gl.glNormal3d(0,0,1);
//            gl.glTexCoord2d(0, i * 20 % 1);
//            gl.glVertex3d(getPoint(i).x+2*laneWidth*V.x, getPoint(i).y+2*laneWidth*V.y, 1);
//            gl.glNormal3d(0,0,1);
//            gl.glTexCoord2d(1, i * 20 % 1);
//            gl.glVertex3d(getPoint(i).x-2*laneWidth*V.x, getPoint(i).y-2*laneWidth*V.y, 1);
//            
//        }
//        gl.glEnd();
//        gl.glPopMatrix();
//        Textures.track.disable(gl);
//        
//        //sides of the track
//        Textures.brick.enable(gl);
//        Textures.brick.bind(gl);
//        gl.glPushMatrix();
//        gl.glBegin(GL_TRIANGLE_STRIP);
//        //outside
//        for(double i = 0; i < 1.002f; i+=0.001){
//            Vector V = new Vector(0.0,0.0,1.0).cross(getTangent(i));
//            V = V.normalized();
//            gl.glNormal3d(V.x,V.y,V.z);
//            gl.glTexCoord2d(i * 20 % 1, 0);
//            gl.glVertex3d(getPoint(i).x+2*laneWidth*V.x, getPoint(i).y+2*laneWidth*V.y, 1);
//            gl.glNormal3d(V.x,V.y,V.z);
//            gl.glTexCoord2d(i * 20 % 1, 1);
//            gl.glVertex3d(getPoint(i).x+2*laneWidth*V.x, getPoint(i).y+2*laneWidth*V.y, -1);
//        }
//        
//        gl.glEnd();
//        gl.glPopMatrix();
//        
//        
//        gl.glPushMatrix();
//        gl.glBegin(GL_TRIANGLE_STRIP);
//        //inside
//        for(double i = 0; i < 1.002; i+=0.001){
//            Vector V = new Vector(0.0,0.0,1.0).cross(getTangent(i));
//            V = V.normalized();
//            gl.glNormal3d(-V.x,-V.y,-V.z);
//            gl.glTexCoord2d(i * 20 % 1, 0);
//            gl.glVertex3d(getPoint(i).x-2*laneWidth*V.x, getPoint(i).y-2*laneWidth*V.y, 1);
//            gl.glNormal3d(-V.x,-V.y,-V.z);
//            gl.glTexCoord2d(i * 20 % 1, 1);
//            gl.glVertex3d(getPoint(i).x-2*laneWidth*V.x, getPoint(i).y-2*laneWidth*V.y, -1);
//        }
//        
//        gl.glEnd();
//        
//        gl.glPopMatrix();
//        Textures.brick.disable(gl);
    
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){
//        Vector V = new Vector(0.0,0.0,1.0).cross(getTangent(t));    //normal vector
//            V = V.normalized();
//            return new Vector(getPoint(t).x+(-1.5*laneWidth+lane*laneWidth)*V.x, getPoint(t).y+(-1.5*laneWidth+lane*laneWidth)*V.y, 1);
        return Vector.O;
    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        return getTangent(t);

    }
    
    
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
