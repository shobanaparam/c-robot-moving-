import javax.swing.*;



public class score implements Runnable{
SwingArena arena = new SwingArena();
     JLabel labels=new JLabel();
    volatile int score=0;
    public score(){
    score=0;
}
    public score(JLabel label) {
        labels=label;
        score=0;
    }

    @Override
    public void run() {
        while(arena.gamefinish==true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            score=score+10;
            labels.setText("score:"+" " +score);
        }
    }
}
