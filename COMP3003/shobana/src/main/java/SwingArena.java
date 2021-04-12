import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.URL ;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


//  int [] integerarray={0,0,0,8,8,0,8,8};
/**
 * A Swing GUI element that displays a grid on which you can draw images, text and lines.
 */
public class SwingArena extends JPanel implements  Runnable {
    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String IMAGE_FILE = "1554047213.png";
    private static final String IMAGE_FILE2="rg1024-isometric-tower.png";
    public boolean gamefinish=true;

    // public Object  score;
   BlockingQueue queue = new ArrayBlockingQueue(100);
    JTextArea logger= new JTextArea();

    private ImageIcon robot1;
    private ImageIcon robot2;
    ArrayList<Implementation> list = new ArrayList<>();
    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth = 9;
    private int gridHeight = 9;
    private double robotX = 1.0;
    private double robotY = 3.0;
int d;
    private double Rx1=4.0;
    private double ry2= 4.0;
    Random r = new Random();
    //public Implementation imp=new Implementation();

    //
    //
    // private String  name[];


    private double gridSquareSize; // Auto-calculated

    private LinkedList<ArenaListener> listeners = null;

    /**
     * Creates a new arena object, loading the robot image.
     */
    public SwingArena() {
        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory.
        URL url = getClass().getClassLoader().getResource(IMAGE_FILE);
        if (url == null) {
            throw new AssertionError("Cannot find image file " + IMAGE_FILE);
        }
        robot1 = new ImageIcon(url);
         robot2= new ImageIcon(getClass().getClassLoader().getResource(IMAGE_FILE2));

    }

    public SwingArena(JTextArea logger) {
        this.logger=logger;
    }


    /**
     * Moves a robot image to a new grid position. This is highly rudimentary, as you will need
     * many different robots in practice. This method currently just serves as a demonstration.
     */
    public void setRobotPosition(double x, double y) {

        if (x==4  || y==4){
            robotX = x;
        robotY = y;



            gamefinish = false;
         for (int j = 0; j<list.size(); j++)
         {
             list.get(j).stoprobo=false;
         }

        }

        repaint();
    }
    
    
    public void stoprobo(int i){
        list.get(i).stoprobo();
    }


    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    int gridX = (int) ((double) event.getX() / gridSquareSize);
                    int gridY = (int) ((double) event.getY() / gridSquareSize);

                    if (gridX < gridWidth && gridY < gridHeight) {
                        for (ArenaListener listener : listeners) {
                            listener.squareClicked(gridX, gridY);
                        }
                    }
                }
            });
        }
        listeners.add(newListener);
    }


    /**
     * This method is called in order to redraw the screen, either because the user is manipulating
     * the window, OR because you've called 'repaint()'.
     * <p>
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
                (double) getWidth() / (double) gridWidth,
                (double) getHeight() / (double) gridHeight);

        int arenaPixelWidth = (int) ((double) gridWidth * gridSquareSize);
        int arenaPixelHeight = (int) ((double) gridHeight * gridSquareSize);


        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setColor(Color.GRAY);
        gfx.drawRect(0, 0, arenaPixelWidth - 1, arenaPixelHeight - 1); // Outer edge

        for (int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            int x = (int) ((double) gridX * gridSquareSize);
            gfx.drawLine(x, 0, x, arenaPixelHeight);
        }

        for (int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            int y = (int) ((double) gridY * gridSquareSize);
            gfx.drawLine(0, y, arenaPixelWidth, y);
        }

       drawImage(gfx, robot2, Rx1, ry2);

        drawLabel( gfx, Rx1, ry2,1500);
        int t = 0;
        for (int i = 0; i < list.size(); i++) {

            robotX = list.get(i).getX();
            robotY = list.get(i).getY();
            System.out.println(robotX + " " + robotY);


            list.get(i).robot(g, robotX, robotY,t);
                t++;



        }
    }


    /**
     * Draw an image in a specific grid location. *Only* call this from within paintComponent().
     * <p>
     * Note that the grid location can be fractional, so that (for instance), you can draw an image
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     * <p>
     * You shouldn't need to modify this method.
     */
    protected void drawImage(Graphics2D gfx, ImageIcon icon, double gridX, double gridY) {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;

        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = (double) robot1.getIconWidth();
        double fullSizePixelHeight = (double) robot1.getIconHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if (fullSizePixelWidth > fullSizePixelHeight) {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        } else {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(icon.getImage(),
                (int) (x - displayedPixelWidth / 2.0),  // Top-left pixel coordinates.
                (int) (y - displayedPixelHeight / 2.0),
                (int) displayedPixelWidth,              // Size of displayed image.
                (int) displayedPixelHeight,
                null);
    }


    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within
     * paintComponent().
     * <p>
     * You shouldn't need to modify this method.
     */
    protected void drawLabel(Graphics2D gfx, double gridX, double gridY,int t ) {
        gfx.setColor(Color.BLUE);

        FontMetrics fm = gfx.getFontMetrics();
       // b=b+1;


//       Random rand = new Random();
//        int t = rand.nextInt(80);
           gfx.drawString("robo" + t,
                   (int) ((gridX + 0.5) * gridSquareSize - (double) fm.stringWidth("robo" + t) / 2.0),
                   (int) ((gridY + 1.0) * gridSquareSize) + fm.getHeight());

       }


    /**
     * Draws a (slightly clipped) line between two grid coordinates.
     * <p>
     * You shouldn't need to modify this method.
     */
    private void drawLine(Graphics2D gfx, double gridX1, double gridY1,
                          double gridX2, double gridY2) {
        gfx.setColor(Color.RED);

        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;

        gfx.drawLine((int) ((clippedGridX1 + 0.5) * gridSquareSize),
                (int) ((clippedGridY1 + 0.5) * gridSquareSize),
                (int) ((gridX2 + 0.5) * gridSquareSize),
                (int) ((gridY2 + 0.5) * gridSquareSize));
    }

    @Override
    public void run() {

  while(gamefinish==true){

            Random rand = new Random();
            int sw = rand.nextInt(4);
          //  System.out.println(sw);

            if (sw == 0) {
                robotX = 0;
                robotY = 0;
            logger.append("robo created");

            } else if (sw == 1) {
                robotX = 0;
                robotY = 8;
                logger.append("robo created");

            } else if (sw == 2) {
                robotX = 8;
                robotY = 0;
                logger.append("robo created");


            } else if(sw==3){
                robotX = 8;
                robotY = 8;
                logger.append("robo created");

            }
            else{
                robotX=4;
                robotY=4;

            }
             
               d = 500 + r.nextInt(1500);
            Implementation ImplementionA = new Implementation(robotX, robotY, this,d);

            list.add(ImplementionA);


            Thread robotA = new Thread(ImplementionA);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            g();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    repaint();
                }
            });


            robotA.start();



        }
    }


    public void g(){
        Implementation ImplementionA = new Implementation(robotX, robotY, this,d);

        list.add(ImplementionA);



        Thread robotA = new Thread(ImplementionA);



    }

}
