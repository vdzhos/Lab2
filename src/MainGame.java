import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.event.*;
import java.awt.*;

public class MainGame extends GraphicsProgram {

    private RandomGenerator rgen = RandomGenerator.getInstance();
    /**The width of the window*/
    private static final double MAP_WIDTH = 500;
    /**The height of the window*/
    private static final double MAP_HEIGHT = 700;
    /**The width of the platform*/
    private static final int PLATFORM_WIDTH =100;
    /**The height of the platform*/
    private static final int PLATFORM_HEIGHT =10;
    /**The diameter of the ball*/
    private static final double DIAM_BALL = 15;
    /**Starting (X) coordinate for the ball and the platform*/
    private static final double X_START = MAP_WIDTH/2;
    /**Starting (Y) coordinate for platform */
    private static final double Y_START = 600;
    /**Delay between different objects' moves*/
    private static final double DELAY = 3;

    /**speed of the ball (X)*/
    private double xVel = 0;
    /**speed of the ball (Y)*/
    private double yVel = 1.75;
    /**coordinate X of the mouse*/
    private int x=0;
    /**prevents the ball from getting stuck in platform*/
    private int timer=0;
    /**Number of users' lives*/
    private int lives = 3;

    private GOval ball;
    private GRect platform;

    public void init() {
        this.setSize((int)MAP_WIDTH, (int)MAP_HEIGHT);
        this.setBackground(Color.getHSBColor(30f,29f,25f)); //той жовтий колір, що був, ну прям задуже яскравий, треба якийсь приглушеніший, щоб очі не різало
        addMouseListeners();
    }

    /**Arrangement of necessary objects (ball, platform, bricks)*/
    private void setup() {
        addBricks();
        addBall(X_START,MAP_HEIGHT/2,DIAM_BALL/2,Color.BLACK);
        addPlatform(X_START,Y_START, PLATFORM_WIDTH, PLATFORM_HEIGHT,Color.BLACK);
        while(xVel<0.75 && xVel>-0.75){
            xVel = rgen.nextDouble(-2, 2);
        }
    }

    public void run(){
        setup();
        while(x==0){pause(1);} // while mouse is out of window, program sleeps
        while(lives > 0) {
            if(timer > 0) timer--;
            moveBall();
            checkForCollision();
            pause(DELAY);
        }

    }
    public void mouseMoved(MouseEvent e){
        platform.setLocation(e.getX()- PLATFORM_WIDTH /2,Y_START);
        x=e.getX();
    }

    /**This method creates the bricks for this game*/
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

    /**This method moves the ball*/
    private void moveBall() {
        ball.move(xVel,yVel);
    }

    /** This method checks for collisions*/
    private void checkForCollision() {
        if(ball.getY()<DIAM_BALL/2){  //for ceiling
            yVel = -yVel;
        } else if((ball.getX()>getWidth()-DIAM_BALL)||(ball.getX()<DIAM_BALL/2)){  //for walls
            xVel = -xVel;
        } else if(ball.getY()-5>MAP_HEIGHT){
            lives--;
            if(lives>0) {
                ball.setLocation(platform.getX() + PLATFORM_WIDTH / 2, platform.getY() - DIAM_BALL * 2);
                yVel = -yVel;
            }
        }
        collisionWithBricks();
    }

    /**This method checks for collisions with bricks and platform. And if one happened it changes the ball's speed to opposite.*/
    private void collisionWithBricks(){
        if(bricksRemover(ball.getX()+DIAM_BALL/2,ball.getY()-1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2+MAP_WIDTH/100,ball.getY()-1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2-MAP_WIDTH/100,ball.getY()-1,0)){
            yVel = -yVel;
        } else if(bricksRemover(ball.getX()+DIAM_BALL/2,ball.getY()+DIAM_BALL+1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2+MAP_WIDTH/100,ball.getY()+DIAM_BALL+1,0) ||
                bricksRemover(ball.getX()+DIAM_BALL/2-MAP_WIDTH/100,ball.getY()+DIAM_BALL+1,0)){
            yVel = -yVel;
        } else if(bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2,-1) ||
                bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2 + MAP_WIDTH/100,-1) ||
                bricksRemover(ball.getX()-1,ball.getY()+DIAM_BALL/2 - MAP_WIDTH/100,-1)){
            xVel = -xVel;
        } else if(bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2,1) ||
                bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2 + MAP_WIDTH/100,1) ||
                bricksRemover(ball.getX()+DIAM_BALL+1,ball.getY()+DIAM_BALL/2 - MAP_WIDTH/100,1)){
            xVel = -xVel;
        }
    }

    /**This method removes the bricks, if the ball crashes into it (or changes the ball's speed to opposite if the collision happened with platform)*/
    private boolean bricksRemover(double xCoord, double yCoord, int num){
        if(getElementAt(xCoord, yCoord) != null && getElementAt(xCoord, yCoord) != platform){
            remove(getElementAt(xCoord, yCoord));
            return true;
        } else if(getElementAt(xCoord, yCoord) == platform && timer==0){
            if(xVel*num>0){
                xVel = -xVel;
            }
            yVel=-yVel;
            timer = 50;
            return false;
        }
        return false;
    }

    /**This method creates a platform, which users will use to beat back the ball*/
    private void addPlatform(double x, double y, double w,double h, Color color){
        platform = new GRect (x-w/2, y-h/2, w, h);
        platform.setFilled(true);
        platform.setColor(color);
        add(platform);
    }
    /**This method creates a ball*/
    private void addBall(double x, double y, double r, Color color){
        ball = new GOval (x-r, y-r, 2*r, 2*r);
        ball.setFilled(true);
        ball.setColor(color);
        add(ball);
    }

}
