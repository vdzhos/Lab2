import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;
public class MainGame extends GraphicsProgram {

    private RandomGenerator rgen = RandomGenerator.getInstance();
    /**The width of the window*/
    private static final double MAP_WIDTH = 500;
    /**The height of the window*/
    private static final double MAP_HEIGHT = 750;
    /**The width of the platform*/
    private static final double PLATFORM_WIDTH =100;
    /**The height of the platform*/
    private static final double PLATFORM_HEIGHT =10;
    /**The diameter of the ball*/
    private static final double DIAM_BALL = 15;
    /**Starting (X) coordinate for the ball and the platform*/
    private static final double X_START = MAP_WIDTH/2;
    /**Starting (Y) coordinate for platform */
    private static final double Y_START = 6*MAP_HEIGHT/7;
    /**Delay between different objects' moves*/
    private static final double DELAY = 3;
    /**The height of the upper bar, where quantity of lives is displayed*/
    private static final double BAR_HEIGHT = MAP_HEIGHT/15;

    /**speed of the ball (X)*/
    private double xVel = 0;
    /**speed of the ball (Y)*/
    private double yVel = 1.75;
    /**coordinate X of the mouse*/
    private int x=0;
    /**coordinate Y of the mouse*/
    private int y=0;
    /**prevents the ball from getting stuck in platform*/
    private int timer=0;
    /**Number of users' lives*/
    private int lives = 3;
    /**Number of bricks left*/
    private int bricksQuantity = 100;
    /** for fixing bugs*/
    private int menu=0;
    /** score*/
    private int score=0;

    private GOval ball;
    private GRect platform;
    private GRect upperBar;
    private GLabel start;
    private GRect startR;
    private GLabel lose;
    private GLabel scoreL;

    private int X_B=150;//button x
    private int Y_B=400;//button y
    public void init() {
        this.setSize((int)MAP_WIDTH, (int)MAP_HEIGHT);
        this.setBackground(Color.getHSBColor(5f,5f,300f)); //оцей, ніби, норм
        addMouseListeners();
    }

    /**Arrangement of necessary objects (ball, platform, bricks, upperBar)*/
    private void setup() {
        addBricks();
        addBall(X_START,MAP_HEIGHT/2,DIAM_BALL/2,Color.BLACK);
        addPlatform(X_START,Y_START, PLATFORM_WIDTH, PLATFORM_HEIGHT,Color.BLACK);
        addUpperBar(0,0,MAP_WIDTH,BAR_HEIGHT,Color.LIGHT_GRAY);
        while(xVel<0.75 && xVel>-0.75){
            xVel = rgen.nextDouble(-2, 2);
        }
        score = 0;
        scoreL = new GLabel("Score:"+score);
        scoreL.setFont("Copperplate Gothic Bold-30");
        add(scoreL,50,40);
        menu=1;
    }
    private void menu(){
        start = new GLabel("Start");
        start.setFont("Copperplate Gothic Bold-40");
        add(filledRectS(X_B,Y_B+3,160,35,Color.GRAY));
        add(start,X_B+13,Y_B);
        start.sendToFront();
    }
    private void exitMenu(){
        removeAll();
        lose = new GLabel("You lost!");
        lose.setFont("Kristen ITC-50");
        lose.setVisible(false);
        add(lose,125,250 );
            if(lives==0){
    lose.setVisible(true);
    }
    }
    public void run(){
        menu();
        while(menu==0){pause(1);}// while menu is opened
        while(lives > 0 && bricksQuantity>0) {
            if(timer > 0) timer--;
            moveBall();
            checkForCollision();
            pause(DELAY);
        }
       exitMenu();
    }


    /**This method changes (X) coordinate of the platform as the mouse moves*/
    public void mouseMoved(MouseEvent e){
        x=e.getX();
        y=e.getY();
        if(menu==0){
            if (x > X_B && x < X_B + 160 && y > Y_B - 35 && y < Y_B) {
                startR.setFilled(true);
            }
            else {
                startR.setFilled(false);
            }
        }
        else if(menu==1){
            if (!(x + PLATFORM_WIDTH / 2 > MAP_WIDTH || x - PLATFORM_WIDTH / 2 < 0)) {
                platform.setLocation(e.getX() - PLATFORM_WIDTH / 2, Y_START);
            }
        }
    }
    public void mouseClicked(MouseEvent e){
        if(startR.isFilled()&&menu==0){
            start.setVisible(false);
            startR.setVisible(false);
            setup();
        }
    }

    /**This method creates the bricks for this game*/
    private void addBricks(){
        for(double i = MAP_HEIGHT/10 + 50; i < MAP_HEIGHT/10+0.2*MAP_HEIGHT + 50; i += 0.02*MAP_HEIGHT){
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
        if(ball.getY()<=Math.abs(yVel)+BAR_HEIGHT){  //for ceiling
            yVel = -yVel;
        }
        if((ball.getX()>=MAP_WIDTH-DIAM_BALL)||(ball.getX()<=-Math.abs(yVel))){  //for walls
            xVel = -xVel;
        }
        if(ball.getY()-5>MAP_HEIGHT){
            lives--;
            minusLife();
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
    private boolean bricksRemover(double xCord, double yCord, int num){
        if(getElementAt(xCord, yCord) != null && getElementAt(xCord, yCord) != platform){
            remove(getElementAt(xCord, yCord));
            score+=1;
            bricksQuantity--;
            scoreL.setLabel("Score:"+score);
            return true;
        } else if(getElementAt(xCord, yCord) == platform && timer==0){
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

    /**This method adds upperBar and all its components (hearts, )*/
    private void addUpperBar(double x, double y, double w,double h, Color color){
        upperBar = new GRect(x,y,w,h);
        upperBar.setFilled(true);
        upperBar.setFillColor(color);
        upperBar.setColor(color);
        add(upperBar);
        add(new GImage("Heart.png"),MAP_WIDTH-129,7*BAR_HEIGHT/50);
        add(new GImage("Heart.png"),MAP_WIDTH-87,7*BAR_HEIGHT/50);
        add(new GImage("Heart.png"),MAP_WIDTH-45,7*BAR_HEIGHT/50);
    }

    /**This method changes a red heart for a white heart, when users lose the ball*/
    private void minusLife(){
        if(lives==2){
            remove(getElementAt(MAP_WIDTH-25,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-45,7*BAR_HEIGHT/50);
        } else if(lives==1){
            remove(getElementAt(MAP_WIDTH-67,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-87,7*BAR_HEIGHT/50);
        } else if (lives == 0){
            remove(getElementAt(MAP_WIDTH-109,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-129,7*BAR_HEIGHT/50);
        }
    }
    /** Draws rectangle (for a button)*/
    private GRect filledRectS(int x, int y, int w,int h, Color color){
        startR = new GRect (x, y-h, w, h);
        startR.setColor(color);
        startR.sendToFront();
        return startR;
    }

}
