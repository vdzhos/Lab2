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
    /**prevents the ball from getting stuck in platform*/
    private int timer=0;
    /**Number of users' lives*/
    private int lives = 3;
    /**Number of bricks left*/
    private int bricksQuantity = -1;
    /**for tips*/
    private int tip1;
    private int tip2;
    private int tip3;

    private GOval ball;
    private GRect platform;
    private GRect upperBar;
    private GLabel score;
    private GRect startB;
    private GLabel startL;
    private GLabel tips;



    public void init() {
        this.setSize((int)MAP_WIDTH, (int)MAP_HEIGHT);
        this.setBackground(Color.getHSBColor(30f,29f,25f));
        addMouseListeners();
    }

    /**Arrangement of necessary objects (ball, platform, bricks, upperBar)*/
    private void setup() {
        addBricks();
        addBall(X_START,MAP_HEIGHT/2,DIAM_BALL/2,Color.BLACK);
        addPlatform(X_START,Y_START, PLATFORM_WIDTH, PLATFORM_HEIGHT,Color.BLACK);
        addUpperBar(0,0,MAP_WIDTH,BAR_HEIGHT,Color.LIGHT_GRAY);
        tips=new GLabel("",TIP_X,TIP_Y);
        tips.setFont("Kristen ITC-50");
        while(xVel<0.75 && xVel>-0.75){
            xVel = rgen.nextDouble(-2, 2);
        }
        tip1 = rgen.nextInt(0,25);//for big platform
        tip2 = rgen.nextInt(25,50);//for slower tip
        tip3 = rgen.nextInt(50,75);//for faster tip
        yVel=1.75;
    }

    /**This method creates all the menu buttons*/
    private void menu(){
        createMenuButton("Start", 40,3*MAP_WIDTH/8,MAP_HEIGHT/4,
                MAP_WIDTH/4,MAP_WIDTH/10, new Color(91,168,245), 0);
        createMenuButton("Exit", 40, 2*MAP_WIDTH/5,2*MAP_HEIGHT/5,
                MAP_WIDTH/5,MAP_WIDTH/10, new Color(245, 15, 41), 2);
        bricksQuantity = 100;
        lives = 3;
    }

    /**This method creates a specific menu button*/
    private void createMenuButton(String name,int size, double x, double y, double w, double h, Color color, int num){
        GRect btn = new GRect(x,y,w,h);
        GLabel btn_label = new GLabel(name,x+18,y+size+2);
        Font font_btn = new Font("Segoe UI Semibold", Font.BOLD, (int)(size*MAP_WIDTH/500));
        btn_label.setFont(font_btn);
        btn.setFilled(true);
        btn.setFillColor(new Color(206,206,206));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(num==1){
                    timer = 1;
                    removeAll();
                    setup();
                } else if(num == 2){
                    System.exit(0);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (num == 1) {
                    btn.setFillColor(color);
                }
                else if (num == 2){
                    btn.setFillColor(color);
                }
                else{
                    if(btn.getX()==3*MAP_WIDTH/8){
                        btn.move(-50,0);
                        btn_label.move(-50,0);
                        createLevels(0);
                        btn.setFillColor(color);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (num == 1 && timer == 0) {
                    btn.setFillColor(new Color(206, 206, 206));
                    if(getElementAt(e.getX(),e.getY()) == null){
                        createLevels(1);
                        startB.setFillColor(new Color(206, 206, 206));
                        startB.move(50,0);
                        startL.move(50,0);
                    }
                } else if(num ==2){
                    btn.setFillColor(new Color(206, 206, 206));
                }
            }
        });
        add(btn);
        add(btn_label);
        if(name.equals("Start")){
            startB = btn;
            startL = btn_label;
        }
    }

    private void createLevels(int num){
        if(num == 0){
            createMenuButton("Easy", 25,21*MAP_WIDTH/40, MAP_HEIGHT/4, MAP_WIDTH/4,MAP_WIDTH/15+2,
                    new Color(159, 245, 86), 1);
            createMenuButton("Medium", 25,21*MAP_WIDTH/40, MAP_HEIGHT/4+(MAP_WIDTH/15+2), MAP_WIDTH/4,MAP_WIDTH/15+2,
                    new Color(245, 230, 102), 1);
            createMenuButton("Hard", 25,21*MAP_WIDTH/40, MAP_HEIGHT/4+2*(MAP_WIDTH/15+2), MAP_WIDTH/4,MAP_WIDTH/15+2,
                    new Color(221, 61, 68), 1);

        } else{
            if(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+10) != null &&
                    getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+(MAP_WIDTH/15+2)+10) != null &&
                    getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+2*(MAP_WIDTH/15+2)+10) != null) {
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+10));
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+10));
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+(MAP_WIDTH/15+2)+10));
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+(MAP_WIDTH/15+2)+10));
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+2*(MAP_WIDTH/15+2)+10));
                remove(getElementAt(29*MAP_WIDTH/50,MAP_HEIGHT/4+2*(MAP_WIDTH/15+2)+10));
            }
        }
    }
    public void run() {
        while(true){
            menu();
            pause(500);
            while (xVel == 0) {
                pause(1);
            }
            while (lives > 0 && bricksQuantity > 0) {
                if (timer > 0) timer--;
                moveBall();
                checkForCollision();
                tips();
                pause(DELAY);
            }
            removeAll();
            xVel = 0;
        }
    }
    private boolean flag=true;//for fixing bugs with platform (to use tips() only 1 time)
    private double width = PLATFORM_WIDTH;//for platform collision with walls
    private final double TIP_X=50;//label tips x
    private final double TIP_Y=100;//label tips y

    private void tips(){
//big platform
        if(tip1==100-bricksQuantity&&flag){
            remove(platform);
            width=PLATFORM_WIDTH*2;
            addPlatform(x,Y_START,width,PLATFORM_HEIGHT,Color.BLACK);
            tips.setLabel("Big platform!");
            add(tips);
            flag=false;
        }
       else if(!flag&&tip1==100-bricksQuantity-5){
           width=PLATFORM_WIDTH;
        remove(platform);
            addPlatform(x,Y_START,width,PLATFORM_HEIGHT,Color.BLACK);
            remove(tips);
            flag=true;
       }
       //2x slower
       if(tip2==100-bricksQuantity&&flag){
           xVel=xVel/2;
           yVel=yVel/2;
           tips.setLabel("Slower!");
           add(tips);
           flag=false;
       }
       else if(tip2==100-bricksQuantity-5&&!flag){
           xVel*=2;
           yVel*=2;
           remove(tips);
           flag=true;
       }
       //faster
       if(tip3==100-bricksQuantity&&flag){
           xVel=xVel*2;
           yVel=yVel*2;
           tips.setLabel("Trash!!!");
           add(tips);
           flag=false;
       }
       else if(tip3==100-bricksQuantity-5&&!flag){
           xVel/=2;
           yVel/=2;
           remove(tips);
           flag=true;
       }

    }
    /**This method changes (X) coordinate of the platform as the mouse moves*/
    public void mouseMoved(MouseEvent e){
        if(xVel!=0) {
            if (!(e.getX() + width / 2 > MAP_WIDTH || e.getX() - width / 2 < 0)) {
                platform.setLocation(e.getX() - width / 2, Y_START);
                x = e.getX();
            }
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
            pause(250);
            if(lives>0) {
                ball.setLocation(X_START-DIAM_BALL/2,MAP_HEIGHT/2-DIAM_BALL/2);
                boolean xDirection = rgen.nextBoolean();
                if(!xDirection) {
                    xVel = -xVel;
                }
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
            bricksQuantity--;
            score.setLabel("Score:"+(100 - bricksQuantity));
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
        score = new GLabel("Score:"+(100 - bricksQuantity));
        Font score_font = new Font("Eras Bold ITC", Font.BOLD,(int)(40*MAP_WIDTH/500));
        score.setFont(score_font);
        add(score, MAP_WIDTH/50, 4*MAP_HEIGHT/75);
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

}
