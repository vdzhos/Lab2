import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.event.*;
import java.awt.*;

public class MainGame extends GraphicsProgram {

    private RandomGenerator rgen = RandomGenerator.getInstance();
    private static final int MAP_WIDTH = 500;
    private static final int MAP_HEIGHT = 700;
    private static final int WIDTH=100;
    private static final int HEIGHT=10;

    private static final int DIAM_BALL = 15;
    private static final int DELAY = 10;
    private static final double X_START = MAP_WIDTH/2;
    private static final double Y_START = 600;
    private static final double Y_PLATFORM_START=Y_START+DIAM_BALL/2+HEIGHT/2+1;
    private double xVel = rgen.nextDouble(1,5);//speed of x ball
    private double yVel = rgen.nextDouble(-6,-4);//speed

    private int x=0; //mouse scan x
    private int timer=0;

    private GOval ball;
    private GRect platform;

    public void init() {
        this.setSize(MAP_WIDTH, MAP_HEIGHT);
        this.setBackground(Color.getHSBColor(25f,29f,30f));
        addMouseListeners();
    }

    private void setup() {
        addBricks();
        addBall(X_START,Y_START,DIAM_BALL/2,Color.BLACK);
        addPlatform(X_START,Y_PLATFORM_START,WIDTH,HEIGHT,Color.BLACK);
    }

    public void run(){
        setup();
        while(x==0){pause(1);} // while mouse is out of window, program sleeps
        while(true) {
            moveBall();
            checkForCollision();
            if(timer > 0) timer--;
            pause(DELAY);
        }

    }
    public void mouseMoved(MouseEvent e){
        platform.setLocation(e.getX()-WIDTH/2,Y_PLATFORM_START);
        x=e.getX();
    }

    // This method creates the bricks for this game
    public void addBricks(){
        for(double i = MAP_HEIGHT/10; i < MAP_HEIGHT/10+0.2*MAP_HEIGHT; i += 0.02*MAP_HEIGHT){
            Color color = rgen.nextColor();
            for(double j = MAP_WIDTH*0.01; j < MAP_WIDTH; j += 0.099*MAP_WIDTH){
                GRect brick = new GRect(j,i,0.089*MAP_WIDTH,0.015*MAP_HEIGHT);
                brick.setFilled(true);
                brick.setFillColor(color);
                add(brick);
            }
        }
    }

    //moving ball
    private void moveBall() {
        ball.move(xVel,yVel);
    }

    // checking for collisions
    private void checkForCollision() {
        if(ball.getY()<DIAM_BALL/2){  //for ceiling
            yVel = -yVel;
        } else if((ball.getX()>getWidth()-DIAM_BALL)||(ball.getX()<DIAM_BALL/2)){  //for walls
            xVel = -xVel;
        } else if(ball.getY()+DIAM_BALL>MAP_HEIGHT){
            ball.setLocation(platform.getX()+WIDTH/2,platform.getY()-DIAM_BALL*2);
            yVel = -yVel;
        }
        if(timer==0){
            collisionWithBricks();
        }
    }

    private void collisionWithBricks(){
        if(bricksRemover(ball.getX()+DIAM_BALL/2,ball.getY()-1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2+MAP_WIDTH/100,ball.getY()-1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2-MAP_WIDTH/100,ball.getY()-1,0)){
            yVel = -yVel;
            timer=2;
        } else if(bricksRemover(ball.getX()+DIAM_BALL/2,ball.getY()+DIAM_BALL+1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2+MAP_WIDTH/100,ball.getY()+DIAM_BALL+1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2-MAP_WIDTH/100,ball.getY()+DIAM_BALL+1,0)){
            yVel = -yVel;
            timer=2;
        } else if(bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2,-1) ||
                bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2 + MAP_WIDTH/100,-1) ||
                bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2 - MAP_WIDTH/100,-1)){
            xVel = -xVel;
            timer=2;
        } else if(bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2,1) ||
                bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2 + MAP_WIDTH/100,1) ||
                bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2 - MAP_WIDTH/100,1)){
            xVel = -xVel;
            timer=2;
        }
    }

    private boolean bricksRemover(double xCoord, double yCoord, int num){
        if(getElementAt(xCoord, yCoord) != null && getElementAt(xCoord, yCoord) != platform){
            remove(getElementAt(xCoord, yCoord));
            return true;
        } else if(getElementAt(xCoord, yCoord) == platform && timer==0){
            if(xVel*num>0){
                xVel = -xVel;
            }
            yVel=-yVel;
            timer = 5;
            return false;
        }
        return false;
    }

    //This method creates a platform, which users will use to beat back the ball
    private void addPlatform(double x, double y, double w,double h, Color color){
        platform = new GRect (x-w/2, y-h/2, w, h);
        platform.setFilled(true);
        platform.setColor(color);
        add(platform);
    }
    //This method creates a ball
    private void addBall(double x, double y, double r, Color color){
        ball = new GOval (x-r, y-r, 2*r, 2*r);
        ball.setFilled(true);
        ball.setColor(color);
        add(ball);
    }

}
