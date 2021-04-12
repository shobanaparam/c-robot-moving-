import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Implementation implements  Runnable {
public boolean stoprobo=true;
    SwingArena s ;
    public double x;
    public double y ;
    public int z;
    private ImageIcon robot1;
    private int b;
        private int del;

SwingArena arena=new SwingArena();


    private static final String IMAGE_FILE = "1554047213.png";
    public Implementation(double Rox, double Roy, SwingArena sw, int d) {
        x=Rox;
        y=Roy;
        s=sw;
        this.del = d;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }



    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public synchronized  void robot(Graphics g, double x, double y,int z){

        URL url = getClass().getClassLoader().getResource(IMAGE_FILE);
        if (url == null) {
            throw new AssertionError("Cannot find image file " + IMAGE_FILE);
        }
        robot1 = new ImageIcon(url);

        Graphics2D gfx = (Graphics2D) g;


        s.drawImage(gfx, robot1, x, y);
        s.drawLabel( gfx, x, y,z);



    }


    @Override
    public void run() {
        while (stoprobo==true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            try {
                Thread.sleep(del);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

            switch (random()) {

                case "left":

                    System.out.println(getX());
                 moveEast();

                    break;
                case "up":
                    System.out.println(getX());

                    moveNorth();
                    break;
                case "down":

                    System.out.println(getX());
                    moveSouth();
                    break;
                case "right":

                    System.out.println(getX());
                    moveWest();
                    break;
            }
        }
    }

    public String random ()
    {
        java.util.List<String> list2 = new ArrayList<>();

        list2.add("left");
        list2.add("right");
        list2.add("up");
        list2.add("down");

        Implementation ro = new Implementation(x,y,this.s,del);


        return ro.getRandomElement(list2);
    }


    String getRandomElement(List<String> list2)
    {
        Random rand = new Random();
        return list2.get(rand.nextInt(list2.size()));
    }






    public boolean moveNorth(){

        y -= 1;
        if(y >= 0 && y <= 7 ) {
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        else
        {
            y +=2;
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        return true;
    }
    public boolean moveSouth(){

        y += 1;
        if(y >= 0 && y <= 7 ) {
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        else
        {
            y -=2;
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        return true;
    }
    public boolean moveWest(){

        x -= 1;
        if(x >= 0 && x <= 7 ) {
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        else
        {
            x +=2;
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }
        return true;

    }
    public boolean moveEast(){

        x += 1;
        if(x >= 0 && x <= 7 ) {
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);        }
        else
        {
            x -=2;
            System.out.println("x :" + x + " y :" + y);
            arena.setRobotPosition(x,y);
        }

        return true;
    }
    
    
     public void stoprobo(){
        stoprobo = false;
        arena.setRobotPosition(-20, -20);
    }

}
