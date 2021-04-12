import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class App
{

        public static BlockingQueue<bulletrobo> queue = new ArrayBlockingQueue(50);
        public static double score = 0;

    public static void main(String[] args)
    {


       // Note: SwingUtilities.invokeLater() is equivalent to JavaFX's Platform.runLater().
           SwingUtilities.invokeLater(() ->
           {
               JFrame window = new JFrame("Example App (Swing)");
               SwingArena arena = new SwingArena();

               Runnable myTask = () ->
               {
                   ExecutorService pool = Executors.newFixedThreadPool(10);
                   pool.execute(arena);

               };

               Thread myThread = new Thread(myTask, "my-thread");

               myThread.start();

               

               arena.addListener((x, y) ->
               {    
                   
                bulletrobo br= new bulletrobo(x ,y,System.currentTimeMillis());
                   
                  queue.add(br);


               });
               JToolBar toolbar = new JToolBar();
//             JButton btn1 = new JButton("My Button 1");
//             JButton btn2 = new JButton("My Button 2");

//               
               //score
               JLabel label = new JLabel("score :0");





//             toolbar.add(btn1);,
//             toolbar.add(btn2);
               toolbar.add(label);
//               score score= new score( label);
//               Thread s = new Thread(score);
//               s.start();




//             btn1.addActionListener((event) -
//             {
//                 System.out.println("Button 1 pressed");
//             });

               JTextArea logger = new JTextArea();
               JScrollPane loggerArea = new JScrollPane(logger);
               loggerArea.setBorder(BorderFactory.createEtchedBorder());
               logger.append("Hello\n");
               logger.append("World\n");


               SwingArena ar= new SwingArena(logger);
               JSplitPane splitPane = new JSplitPane(
                       JSplitPane.HORIZONTAL_SPLIT, arena, logger);

               Container contentPane = window.getContentPane();
               contentPane.setLayout(new BorderLayout());
               contentPane.add(toolbar, BorderLayout.NORTH);
               contentPane.add(splitPane, BorderLayout.CENTER);

               window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               window.setPreferredSize(new Dimension(800, 800));
               window.pack();
               window.setVisible(true);
               splitPane.setDividerLocation(0.75);

               
               Runnable myTask3 = () ->
               {
                   bulletrobo bulr =null;
                   while (arena.gamefinish == true) {
                      
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException ex) {
                           Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                       }
                       
                        score = score + 10;
                        label.setText("score :" + score);
                       
                   }

               };

               Thread myThread3 = new Thread(myTask3, "my-thread");
               myThread3.start();
               
               
               Runnable myTask2 = () ->
               {
                   bulletrobo bulr =null;
                   while (arena.gamefinish == true) {
                      
                       try {
                           Thread.sleep(1000);
                           bulr = queue.take();
                       } catch (InterruptedException ex) {
                           Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                       }
                       
                       for (int i = 0; i < arena.list.size(); i++) {
                           if(arena.list.get(i).x == bulr.getX() && arena.list.get(i).y == bulr.getY()) {
                           
                               arena.stoprobo(i);
                              long t = System.currentTimeMillis() - bulr.getTime();
                              score = score + 10 + 100*(t/arena.list.get(i).getDel());
                               label.setText("score :" + score);

                               logger.append("Robot " + i + " Destroyed\n");
                           
                           }
                       }
                       
                   }

               };

               Thread myThread2 = new Thread(myTask2, "my-thread");
               myThread2.start();
               
               
               
               

           });
       }
    }


