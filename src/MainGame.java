import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.*;

public class MainGame extends GraphicsProgram {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 750;

    RandomGenerator rgen = RandomGenerator.getInstance();

    public void run(){
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(Color.getHSBColor(45f,29f,35f));
        addBricks();
    }

    /* This method creates the bricks for this game*/
    public void addBricks(){
        for(double i = HEIGHT/10; i < HEIGHT/10+0.2*HEIGHT; i += 0.02*HEIGHT){
            Color color = rgen.nextColor();
            for(double j = WIDTH*0.01; j < WIDTH; j += 0.099*WIDTH){
                GRect brick = new GRect(j,i,0.089*WIDTH,0.015*HEIGHT);
                brick.setFilled(true);
                brick.setFillColor(color);
                add(brick);
            }
        }
    }

    /* This method creates a racket, which users will use to beat back the ball*/
    public void addRacket(){

    }

}
