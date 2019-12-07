
import java.awt.Color;
import acm.program.*;
import acm.graphics.*;


 class Menu extends GraphicsProgram{
    private GLabel start;
    private int x=150;
    private int y=400;

   public Menu(){
    setup();
    }

    private void setup(){
        start = new GLabel("Start");
        start.setFont("Copperplate Gothic Bold-40");
        filledRectS(x,y+3,160,35,Color.GRAY);
        add(start,x+13,y);
        start.sendToFront();
    }

    private void filledRectS(int x, int y, int w,int h, Color color){
        GRect startR = new GRect (x, y-h, w, h);
        startR.setColor(color);
        startR.sendToFront();
        add(startR);
    }

}
