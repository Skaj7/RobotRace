package robotrace;

import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static robotrace.ShaderPrograms.*;
import static robotrace.Textures.*;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards; (Not required in this assignment)
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the folder textures. 
 * These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * Textures.track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
    
    private final double[] robotTArray;
        
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
                
        );
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
              
        );
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
              
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
                
        );
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[2];
        
        // Track 1
        raceTracks[0] = new ParametricTrack();
        
        // Track 2
        float g = 3.5f;
        raceTracks[1] = new BezierTrack(
                
            new Vector[] {}
       
        );
        
        // Initialize the terrain
        terrain = new Terrain();
        
        // How far each robot has run. Initially 0 for all robots
        robotTArray = new double[4];
        for(int i = 0; i < 4; i ++) {
            robotTArray[i] = 0;
        }
        
        // Position robots based on robotTArray
        robots[0].position = raceTracks[gs.trackNr].getLanePoint(0, robotTArray[0]);
        robots[1].position = raceTracks[gs.trackNr].getLanePoint(1, robotTArray[1]);
        robots[2].position = raceTracks[gs.trackNr].getLanePoint(2, robotTArray[2]);
        robots[3].position = raceTracks[gs.trackNr].getLanePoint(3, robotTArray[3]);
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        
	// Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
	// Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");
        
        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");
        
    }
   
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Add light source
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0f,0f,0f,1f}, 0);
               
        // Update the view according to the camera mode and robot of interest.
        int lastRobot = 0;
        for(int i = 1; i < 4; i ++) {
            if (robotTArray[i] < robotTArray[lastRobot]) {
                lastRobot = i;
            } 
        }
        camera.update(gs, robots[lastRobot]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");
        
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        // Update the robots' t values
        for(int i = 0; i < 4; i ++) {
            robotTArray[i] = (0.01*((i+3)%4) + 1)*gs.tAnim/(double)8;
        }
        
        // Position robots
        robots[0].position = raceTracks[gs.trackNr].getLanePoint(0, robotTArray[0]);
        robots[1].position = raceTracks[gs.trackNr].getLanePoint(1, robotTArray[1]);
        robots[2].position = raceTracks[gs.trackNr].getLanePoint(2, robotTArray[2]);
        robots[3].position = raceTracks[gs.trackNr].getLanePoint(3, robotTArray[3]);
        
        // Orient robots
        robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(0, robotTArray[0]);
        robots[1].direction = raceTracks[gs.trackNr].getLaneTangent(1, robotTArray[1]);
        robots[2].direction = raceTracks[gs.trackNr].getLaneTangent(2, robotTArray[2]);
        robots[3].direction = raceTracks[gs.trackNr].getLaneTangent(3, robotTArray[3]);
        
        // Draw robots
        gl.glUseProgram(robotShader.getProgramID()); 
        robots[0].draw(gl, glu, glut, gs.tAnim);
        robots[1].draw(gl, glu, glut, gs.tAnim);
        robots[2].draw(gl, glu, glut, gs.tAnim);
        robots[3].draw(gl, glu, glut, gs.tAnim);
        
        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut);
        reportError("terrain:");
        
        // Draw trees
        gl.glUseProgram(robotShader.getProgramID());
        drawTrees();
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        //Origin
         gl.glPushMatrix();
            gl.glColor3d(90,90,0);
            glut.glutSolidSphere(0.2, 50, 50);
        gl.glPopMatrix();
        gl.glPushMatrix();
            //Z-axis
            gl.glColor3d(0,0,90);
            drawArrow();
        gl.glPopMatrix();
        gl.glPushMatrix();
            //X-axis
            gl.glColor3d(90,0,0);
            gl.glRotated(90.0, 0, 1, 0);
            drawArrow();
        gl.glPopMatrix();
        gl.glPushMatrix();
            //Y-axis
            gl.glColor3d(0,90,0);
            gl.glRotated(-90.0, 1, 0, 0);
            drawArrow();
        gl.glPopMatrix();
        gl.glDisable(GL_BLEND);
    }
    
    /**
     * Draws a single arrow
     */
    public void drawArrow() { 
        glut.glutSolidCylinder(0.1, 1.5, 100, 100);
        gl.glTranslated(0, 0, 1.5);
        glut.glutSolidCone(0.175, 0.35, 100, 100);
    }
 
    /**
     * Drawing hierarchy example.
     * 
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively. 
     * 
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which 
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     * 
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    private void drawHierarchy() {
        gl.glColor3d(gs.sliderC, gs.sliderD, gs.sliderE);
        gl.glPushMatrix(); 
            gl.glScaled(1, 0.5, 0.5);
            glut.glutSolidCube(1);
            gl.glScaled(1, 2, 2);
            gl.glTranslated(0.5, 0, 0);
            gl.glRotated(gs.sliderA * -90.0, 0, 1, 0);
            drawSecond();
        gl.glPopMatrix();
    }
    
    private void drawSecond() {
        gl.glTranslated(0.5, 0, 0);
        gl.glScaled(1, 0.5, 0.5);
        glut.glutSolidCube(1);
        gl.glScaled(1, 2, 2);
        gl.glTranslated(0.5, 0, 0);
        gl.glRotated(gs.sliderB * -90.0, 0, 1, 0);
        drawThird();
    }
    
    private void drawThird() {
        gl.glTranslated(0.5, 0, 0);
        gl.glScaled(1, 0.5, 0.5);
        glut.glutSolidCube(1);
    }
    
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
    
    
    private void drawTrees() {
        drawTree(2, 2, 1);
        drawTree(4.4f, -1, 0.9f);
        drawTree(18, 10, 1.4f);
        drawTree(17, 5, 1.2f);
        drawTree(16, -3, 1.1f);
        drawTree(15, 13, 1.3f);
        drawTree(-18, 10, 1f);
        drawTree(-2, -2, 1);
        drawTree(-3, 4, 1.2f);
    }
    private void drawTree(float x, float y, float scale) {
        gl.glPushMatrix(); 
        gl.glTranslated(x, y, 0);
        gl.glScaled(scale, scale, scale);
        // Draw tree trunk
        Material material = Material.WOOD;
        gl.glMaterialfv(GL_FRONT,GL_DIFFUSE, material.diffuse,0);
        gl.glMaterialfv(GL_FRONT,GL_SPECULAR, material.specular,0);
        gl.glMaterialfv(GL_FRONT,GL_SHININESS, new float[] {material.shininess},0);
        glut.glutSolidCylinder(0.7, 1.3, 10, 5);
        gl.glTranslated(0, 0, 1.1);
        
        // Draw tree top
        material = Material.LEAF;
        gl.glMaterialfv(GL_FRONT,GL_DIFFUSE, material.diffuse,0);
        gl.glMaterialfv(GL_FRONT,GL_SPECULAR, material.specular,0);
        gl.glMaterialfv(GL_FRONT,GL_SHININESS, new float[] {material.shininess},0);
        glut.glutSolidCone(2, 3, 10, 5);
        glut.glutSolidCone(2, 0, 10, 5);
        gl.glTranslated(0, 0, 1.5);
        gl.glScaled(0.8, 0.8, 0.8);
        glut.glutSolidCone(2, 3, 10, 5);
        glut.glutSolidCone(2, 0, 10, 5);
        gl.glTranslated(0, 0, 1.5);
        gl.glScaled(0.8, 0.8, 0.8);
        glut.glutSolidCone(2, 3, 10, 5);
        glut.glutSolidCone(2, 0, 10, 5);
        gl.glPopMatrix();
    }
}
