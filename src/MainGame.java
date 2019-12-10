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
    /**Number of rows*/
    private static final int NROWS = 10;
    /**Number of bricks in a row*/
    private static final int NBRICKS = 10;
    /**The height of the bricks*/
    private static final double BRICK_HEIGHT = 12;
    /**The distance between bricks (X) coord*/
    private static final double DIST_BETWEEN_X = 5;
    /**The distance between bricks (Y) coord*/
    private static final double DIST_BETWEEN_Y = 3;


    /**speed of the ball (X)*/
    private double xVel = 0;
    /**speed of the ball (Y)*/
    private double yVel = 1.75;

    /**coordinate X of the mouse*/
    private int x=0;
    /**prevents the ball from getting stuck in platform*/
    private int timer=0;
    /**Number of users' lives*/
    private int NTURNS;
    /**Number of bricks left*/
    private int bricksQuantity = -1;
    /**for tips*/
    private int bonusTimer = 0;
    private int NBonuses = 5;
    private int currentBonus = 0;
    private double bonusChanceCoef = 1;

    private final double TIP_X=185;//label tips x
    private final double TIP_Y=40;//label tips y

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
        tips.setFont("Kristen ITC-35");
        while(xVel<0.75 && xVel>-0.75){
            xVel = rgen.nextDouble(-2, 2);
        }
        yVel=1.75;
    }

    /**This method creates all the menu buttons*/
    private void menu(){
        createMenuButton("Start", 40,3*MAP_WIDTH/8,MAP_HEIGHT/4,
                MAP_WIDTH/4,MAP_WIDTH/10, new Color(91,168,245), 0);
        createMenuButton("Exit", 40, 2*MAP_WIDTH/5,2*MAP_HEIGHT/5,
                MAP_WIDTH/5,MAP_WIDTH/10, new Color(245, 15, 41), 2);
        bricksQuantity = NROWS*NBRICKS;
        NTURNS = 3;
        currentBonus=0;
        bonusTimer=0;
        NBonuses = 5;
        bonusChanceCoef=1;
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
            while (xVel == 0) {
                pause(1);
            }
            while (NTURNS > 0 && bricksQuantity > 0) {
                if (timer > 0) timer--;
                if (bonusTimer > 0) bonusTimer--;
                moveBall();
                checkForCollision();
                if(bonusTimer==0) {
                    tipsDisactivate();
                }
                pause(DELAY);
            }
            removeAll();
            xVel = 0;
        }
    }

    private void bonusSetup(){
        double a = NROWS*NBRICKS-bricksQuantity;
        double b = NROWS*NBRICKS*5;
        boolean bool = rgen.nextBoolean(a/(b*bonusChanceCoef));
        System.out.println(a/(b*bonusChanceCoef));
        if(bool && NBonuses>0 && currentBonus==0) {
            bonusChanceCoef+=0.5;
            if(bonusTimer==0){
                currentBonus = rgen.nextInt(1, 5); // i1 - amount of types of bonuses
            }
            tipsActivate();
            if(bonusTimer==0) {
                NBonuses--;
                bonusTimer = 2700;
            }
        }
    }

    private void tipsActivate() {
        if (currentBonus == 1) {                   //big platform
            remove(platform);
            addPlatform(x, Y_START, PLATFORM_WIDTH * 2, PLATFORM_HEIGHT, Color.BLACK);
            tips.setLabel("Big platform!");
            tips.setFont("Kristen ITC-25");
            add(tips);
        } else if (currentBonus == 2) {              //slower
            xVel = xVel / 2;
            yVel = yVel / 2;
            tips.setLabel("Slower!");
            tips.setFont("Kristen ITC-35");
            add(tips);
        } else if (currentBonus == 3) {              //faster
            xVel = xVel * 2;
            yVel = yVel * 2;
            tips.setLabel("Trash!!!");
            tips.setFont("Kristen ITC-35");
            add(tips);
        }
        else if(currentBonus == 4){                 //small platform
            remove(platform);
            addPlatform(x, Y_START, PLATFORM_WIDTH / 2, PLATFORM_HEIGHT, Color.BLACK);
            tips.setLabel("Small platform!");
            tips.setFont("Kristen ITC-23");
            add(tips);
        }
        else if(currentBonus==5){
            plusLife();                             //plus life
            NTURNS++;
        }
    }

    private void tipsDisactivate(){
        if(currentBonus==1||currentBonus==4){
            remove(platform);
            addPlatform(x,Y_START,PLATFORM_WIDTH,PLATFORM_HEIGHT,Color.BLACK);
            remove(tips);
            currentBonus=0;
        }else if(currentBonus==2) {
            xVel *= 2;
            yVel *= 2;
            remove(tips);
            currentBonus=0;
        }else if(currentBonus==3){
            xVel/=2;
            yVel/=2;
            remove(tips);
            currentBonus=0;
        }
    }

    /**This method changes (X) coordinate of the platform as the mouse moves*/
    public void mouseMoved(MouseEvent e){
        if(xVel!=0) {
            if (!(e.getX() + platform.getWidth() / 2 > MAP_WIDTH || e.getX() - platform.getWidth() / 2 < 0)) {
                platform.setLocation(e.getX() - platform.getWidth() / 2, Y_START);
                x = e.getX();
            }
        }
    }

    /**This method creates the bricks for this game*/
    private void addBricks(){
        for(double i = MAP_HEIGHT/10 + BAR_HEIGHT; i < MAP_HEIGHT/10 + BAR_HEIGHT + NROWS*(DIST_BETWEEN_Y+BRICK_HEIGHT);
            i += DIST_BETWEEN_Y+BRICK_HEIGHT){
            Color color = rgen.nextColor();
            for(double j = DIST_BETWEEN_X; j < MAP_WIDTH; j += ((MAP_WIDTH-DIST_BETWEEN_X)/NBRICKS)){
                GRect brick = new GRect(j,i,((MAP_WIDTH-DIST_BETWEEN_X)/NBRICKS) - DIST_BETWEEN_X,BRICK_HEIGHT);
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
            NTURNS--;
            minusLife();
            pause(250);
            if(NTURNS >0) {
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
            bonusSetup();
            score.setLabel("Score:"+(NROWS*NBRICKS - bricksQuantity));
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
        score = new GLabel("Score:"+(NROWS*NBRICKS - bricksQuantity));
       // Font score_font = new Font("Eras Bold ITC", Font.BOLD,(int)(40*MAP_WIDTH/500));
        score.setFont("Cooper Black-40");
        add(score, MAP_WIDTH/50, 4*MAP_HEIGHT/75);
    }

    /**This method changes a red heart for a white heart, when users lose the ball*/
    private void minusLife(){
        if(NTURNS ==3) {
            remove(getElementAt(MAP_WIDTH - 171, BAR_HEIGHT / 2));
        }else if(NTURNS ==2){
            remove(getElementAt(MAP_WIDTH-109,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-129,7*BAR_HEIGHT/50);
        } else if(NTURNS ==1){
            remove(getElementAt(MAP_WIDTH-67,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-87,7*BAR_HEIGHT/50);
        } else if (NTURNS == 0){
            remove(getElementAt(MAP_WIDTH-25,BAR_HEIGHT/2));
            add(new GImage("Heart_empty.png"),MAP_WIDTH-45,7*BAR_HEIGHT/50);
        }
    }
    private void plusLife(){
        if(NTURNS ==3){
            add(new GImage("Heart.png"),MAP_WIDTH-171,7*BAR_HEIGHT/50);
        } else if(NTURNS ==2){
            remove(getElementAt(MAP_WIDTH-109,BAR_HEIGHT/2));
            add(new GImage("Heart.png"),MAP_WIDTH-129,7*BAR_HEIGHT/50);
        } else if (NTURNS == 1){
            remove(getElementAt(MAP_WIDTH-87,BAR_HEIGHT/2));
            add(new GImage("Heart.png"),MAP_WIDTH-87,7*BAR_HEIGHT/50);
        }
    }
}
