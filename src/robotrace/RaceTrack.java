package robotrace;

import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {

    /**
     * The width of one lane. The total width of the track is 4 * laneWidth.
     */
    private final static float laneWidth = 1.22f;
    private final static double quadsPerLane = 100;
    private final static double topTextureRepetitions = 10;
    private final static double sideTextureRepetitionsLengthwise = 100;
    private final static double sideTextureRepetitionsCrosswise = 2;
    private final static double bottomTextureRepetitionsLengthwise = 90;
    private final static double bottomTextureRepetitionsCrosswise = 1.9;
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        
        // show center TODO debug
        /*
        gl.glLineWidth(3.0f);
        gl.glColor3d(90,0,0);
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 100; i++) {
            double t = (double) i / 100;
            Vector centerPoint = getPoint(t);
            Vector tangentVector = getTangent(t);
            Vector nextPoint = centerPoint.add(tangentVector);
            gl.glVertex3d(centerPoint.x, centerPoint.y, centerPoint.z);
            gl.glVertex3d(nextPoint.x, nextPoint.y, nextPoint.z);
        }
        gl.glEnd();
        */
        
        // Load track top texture and make it repeat
        Textures.track.bind(gl);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        // Draw the top of the track
        for (int i = 0; i < 4; i++) {
            gl.glBegin(GL_QUAD_STRIP);
            for (int j = 0; j <= quadsPerLane; j++) {
                // t will go from 0 to 1 in quadsPerLane steps, and then one step where it is 0 again
                double textureT = ((double) j / (double) quadsPerLane);
                double t = ((double) j / (double) quadsPerLane) % (double) 1;
                
                // Find the two points of this iteration
                Vector inner = getPoint(t).add(getInwardVector(t).scale(2 - i));
                Vector outer = getPoint(t).add(getInwardVector(t).scale(1 - i));
                   
                double textureY = (textureT * topTextureRepetitions);
                double innerTextureX = ((double)i/(double)2);
                double outerTextureX = innerTextureX + (double)1/(double)2;
                // Texture the points and glVertex them
                gl.glTexCoord2d(innerTextureX, textureY);
                gl.glVertex3d(inner.x, inner.y, inner.z);
                gl.glTexCoord2d(outerTextureX, textureY);
                gl.glVertex3d(outer.x, outer.y, outer.z);
            }
            gl.glEnd();
        }
        
        // Load track foundation texture and make it repeat
        Textures.brick.bind(gl);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        // Draw the inner sides of the track
        gl.glBegin(GL_QUAD_STRIP);
        for (int j = 0; j <= quadsPerLane; j++) {
            // t will go from 0 to 1 in quadsPerLane steps, and then one step where it is 0 again
            double textureT = ((double) j / (double) quadsPerLane);
            double t = ((double) j / (double) quadsPerLane) % (double) 1;

            // Find the innermost point on the top of the track
            Vector innerTop = getPoint(t).add(getInwardVector(t).scale(2));
            // Find the point 2 meters down
            Vector innerBottom = innerTop.add(new Vector(0,0,-2));
            double textureX = (textureT*sideTextureRepetitionsLengthwise);
            double topTextureY = 1*sideTextureRepetitionsCrosswise;
            double bottomTextureY = 0;
            // Texture the points and glVertex them
            gl.glTexCoord2d(textureX, bottomTextureY);
            gl.glVertex3d(innerBottom.x, innerBottom.y, innerBottom.z);
            gl.glTexCoord2d(textureX, topTextureY);
            gl.glVertex3d(innerTop.x, innerTop.y, innerTop.z);
        }
        gl.glEnd();
        
        // Draw the outer sides of the track
        gl.glBegin(GL_QUAD_STRIP);
        for (int j = 0; j <= quadsPerLane; j++) {
            // t will go from 0 to 1 in quadsPerLane steps, and then one step where it is 0 again
            double textureT = ((double) j / (double) quadsPerLane);
            double t = ((double) j / (double) quadsPerLane) % (double) 1;

            // Find the outermost point on the top of the track
            Vector outerTop = getPoint(t).subtract(getInwardVector(t).scale(2));
            // Find the point 2 meters down
            Vector outerBottom = outerTop.add(new Vector(0,0,-2));
            double textureX = (textureT*sideTextureRepetitionsLengthwise);
            double topTextureY = 1*sideTextureRepetitionsCrosswise;
            double bottomTextureY = 0;
            // Texture the points and glVertex them
            gl.glTexCoord2d(textureX, bottomTextureY);
            gl.glVertex3d(outerBottom.x, outerBottom.y, outerBottom.z);
            gl.glTexCoord2d(textureX, topTextureY);
            gl.glVertex3d(outerTop.x, outerTop.y, outerTop.z);
        }
        gl.glEnd();
        
        // Draw the bottom of the track
        for (int i = 0; i < 4; i++) {
            gl.glBegin(GL_QUAD_STRIP);
            for (int j = 0; j <= quadsPerLane; j++) {
                // t will go from 0 to 1 in quadsPerLane steps, and then one step where it is 0 again
                double textureT = ((double) j / (double) quadsPerLane);
                double t = ((double) j / (double) quadsPerLane) % (double) 1;
                
                // Find the two points of this iteration
                Vector inner = getPoint(t).add(getInwardVector(t).scale(2 - i)).add(new Vector(0,0,-2));
                Vector outer = getPoint(t).add(getInwardVector(t).scale(1 - i)).add(new Vector(0,0,-2));
                   
                double textureY = (textureT * bottomTextureRepetitionsLengthwise);
                double innerTextureX = ((double)i/(double)2)*bottomTextureRepetitionsCrosswise;
                double outerTextureX = (innerTextureX + (double)1/(double)2)*bottomTextureRepetitionsCrosswise;
                // Texture the points and glVertex them
                gl.glTexCoord2d(innerTextureX, textureY);
                gl.glVertex3d(inner.x, inner.y, inner.z);
                gl.glTexCoord2d(outerTextureX, textureY);
                gl.glVertex3d(outer.x, outer.y, outer.z);
            }
            gl.glEnd();
        }
    }

    /**
     * Returns the center of a lane at 0 <= t < 1. Use this method to find the
     * position of a robot on the track.
     * Lanes are 0-indexed
     */
    public Vector getLanePoint(int lane, double t) {
        return getPoint(t).add(getInwardVector(t).scale(1.5 - lane));
    }

    /**
     * Returns the tangent of a lane at 0 <= t < 1. Use this method to find the
     * orientation of a robot on the track.
     * Lanes are 0-indexed
     */
    public Vector getLaneTangent(int lane, double t) {
        return getTangent(t);
    }

//    public void draw(GL2 gl, GLU glu, GLUT glut) {
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
//    
//    }
//    
//    /**
//     * Returns the center of a lane at 0 <= t < 1.
//     * Use this method to find the position of a robot on the track.
//     */
//    public Vector getLanePoint(int lane, double t){
//        Vector V = new Vector(0.0,0.0,1.0).cross(getTangent(t));    //normal vector
//            V = V.normalized();
//            return new Vector(getPoint(t).x+(-1.5*laneWidth+lane*laneWidth)*V.x, getPoint(t).y+(-1.5*laneWidth+lane*laneWidth)*V.y, 1);
//        return Vector.O;
//    }
//    
//    /**
//     * Returns the tangent of a lane at 0 <= t < 1.
//     * Use this method to find the orientation of a robot on the track.
//     */
//    public Vector getLaneTangent(int lane, double t){
//        return getTangent(t);
//
//    }
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
    
    // Returns a normal vector on the test track at 0 <= t < 1, pointing outward
    protected abstract Vector getNormal(double t);
    
    // Returns a normal vector on the test track at 0 <= t < 1 of length laneWidth, pointing inward
    protected abstract Vector getInwardVector(double t);
}
