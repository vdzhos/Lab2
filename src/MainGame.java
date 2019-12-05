import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.event.*;
import java.awt.*;

public class MainGame extends GraphicsProgram {
    private static final int MAP_WIDTH = 500;
    private static final int MAP_HEIGHT = 700;
    private static final int WIDTH=100;
    private static final int HEIGHT=10;

    private static final int DIAM_BALL = 15;
    private static final int DELAY = 10;
    private static final double X_START = MAP_WIDTH/2;
    private static final double Y_START = 500;
    private static final double Y_PLATFORM_START=Y_START+DIAM_BALL/2+HEIGHT/2+1;
    private double xVel = 1;//speed of x ball
    private double yVel = -6;//speed

    private int x=0; //mouse scan x
    private int y=0; //mouse scan y

    private GOval ball;
    private GRect platform;

    RandomGenerator rgen = RandomGenerator.getInstance();

    //setup
    private void setup() {
        addMouseListeners();
        this.setSize(MAP_WIDTH, MAP_HEIGHT);
        this.setBackground(Color.getHSBColor(25f,29f,30f));
        addBricks();
        addBall(X_START,Y_START,DIAM_BALL/2,Color.BLACK);
        addPlatform(X_START,Y_PLATFORM_START,WIDTH,HEIGHT,Color.BLACK);
    }

    public void run(){
        setup();
        while(x==0&&y==0){
            if(!(x==0)||!(y==0))break;//
            pause(1);
        }
        while(true) {
            moveBall();
            checkForCollision();
            pause(DELAY);
        }

    }
    public void mouseMoved(MouseEvent e){
        x=e.getX();
        y=e.getY();
        remove(platform);
        addPlatform(x,Y_PLATFORM_START,100,10,Color.BLACK);
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

    // checking for collision
    private void checkForCollision() {
        if(ball.getY()<DIAM_BALL/2)yVel = -yVel;//for ceiling
        if((ball.getX()>getWidth()-DIAM_BALL)||(ball.getX()<DIAM_BALL/2))xVel = -xVel;//for walls
        if(ball.getY()>getHeight()-DIAM_BALL){
            //todo minus life
        }
        if((ball.getX()+DIAM_BALL>x-WIDTH/2&&ball.getX()+DIAM_BALL<x+WIDTH/2)&&
                (ball.getY()+DIAM_BALL>Y_PLATFORM_START-HEIGHT/2&&ball.getY()+DIAM_BALL<Y_PLATFORM_START+HEIGHT)){
            yVel=-yVel;
        }

    }

    //This method creates a platform, which users will use to beat back the ball
    public void addPlatform(double x, double y, double w,double h, Color color){
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
