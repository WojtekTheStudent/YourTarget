import javax.swing.*;
import java.awt.*;


public class Screen extends JPanel implements Runnable {

    private Countdown timer;
    private Board board;

    private Thread thread;
    private boolean end = false;

    //constructor
    public Screen(Countdown cd, Board game_board) {

        timer = cd;
        board = game_board;
        this.setFocusable(true);
        this.add(timer);

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        setOpaque(false);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //draw background
        Image background = new ImageIcon("dedust24.png").getImage();
        if (!g2.drawImage(background, 0, 0, this)) {
            System.out.println("Error with background");
        }

        //to do
        //-random target generation
        //-movmets
        //...
        int tmp1 = 1024 * 3 / 4;
        int tmp2 = 668;
        for (int i = 1; i < 4; i++) //targets
        {
            for (int j = 1; j < 4; j++) {
                g2.setColor(Color.BLACK);
                int x = (tmp1 / 4 * i) - 50;
                int y = (tmp2 / 4 * j) - 50;
                g2.fillOval(x, y, 100, 100);
                g2.setColor(Color.WHITE);
                g2.fillOval(x + 2, y + 2, 96, 96);
            }
        }


        g2.setColor(Color.RED);
        g2.fillRect(tmp1, 0, 1024 - tmp1, 768);
        g2.fillRect(0, tmp2, 1024, 100);

        int outline = 5;
        g2.setColor(Color.BLACK);
        g2.fillRect(tmp1 + outline, 0 + outline, 1024 - tmp1 - (outline * 5) + 1, 768 - (outline * 14));
        g2.fillRect(0 + outline, tmp2 + outline, 1024 - (outline * 5), 100 - (outline * 14));

        Image menu = new ImageIcon("MENU.png").getImage();
        if (!g2.drawImage(menu, tmp1 + outline, 5, this)) {
            System.out.println("Error with menu image");
        }
        Image points = new ImageIcon("POINTS.png").getImage();
        if (!g2.drawImage(points, tmp1 + outline, 210, this)) {
            System.out.println("Error with points image");
        }
        Image time = new ImageIcon("TIME.png").getImage();
        if (!g2.drawImage(time, tmp1 + outline, 420, this)) {
            System.out.println("Error with time image");
        }
    }

    @Override
    public void run() {
        //run the game loop
        while (true) {
            //allows for smooth motion of the game
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
            timer.increment();
            repaint();
            if (timer.isTimeUp()) {
                end = true;
                break;
            }
        }
    }

    //method to reset the screen back to what it was before
    public void reset() {
        timer.reset();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }

        if (end) {
            end = false;

            //start up a new thread
            thread = new Thread(this);
            thread.start();
        }
    }
}
