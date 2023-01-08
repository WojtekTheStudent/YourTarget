import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.text.AttributedString;


public class Screen extends JPanel implements Runnable, MouseListener {

    private Countdown timer;
    private Board board;
    private Target[][] targets;
    private int hitCount;

    //movment borders
    private final int top = 0;
    private final int bot = 667;
    private final int left = 0;
    private final int right = 768;

    //variables for placing targets
    private final int startX = 142;
    private final int startY = 117;
    private final int offsetX = 192;
    private final int offsetY = 167;

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

        board.addMouseListener(this);
        addMouseListener(this);

        targets = new Target[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                targets[i][j] = new Target(startX + ((i) * offsetX), startY + ((j) * offsetY), 20);
                //targets[i][j].setRandMovmentVec();
            }

        hitCount = 0;
    }

    public void paintComponent(Graphics g) {
        setOpaque(false);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image background = new ImageIcon("dedust24.png").getImage();
        if (!g2.drawImage(background, 0, 0, this)) {
            System.out.println("Error with background");
        }

        for (int i = 0; i < targets.length; i++)
            for (int j = 0; j < targets[0].length; j++)
                targets[i][j].paintTarget(g2);

        int tmp1 = 1024 * 3 / 4;
        int tmp2 = 668;
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

        g2.setColor(Color.CYAN);
        AttributedString as1 = new AttributedString(timer.getStringTime());
        as1.addAttribute(TextAttribute.SIZE, 80);
        g2.drawString(as1.getIterator(), 820, 600);
    }

    @Override
    public void run() {
        while (!end) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }

            for (int i = 0; i < targets.length; i++)
                for (int j = 0; j < targets[0].length; j++) {
                    targets[i][j].move();
                    double curX = targets[i][j].getX();
                    double curY = targets[i][j].getY();
                    if (curY <= top || curY + (targets[i][j].getRadius() * 2) >= bot) targets[i][j].bounceY();
                    if (curX <= left || curX + (targets[i][j].getRadius() * 2) >= right) targets[i][j].bounceX();
                }

            timer.increment();
            repaint();
            //if(timer.isTimeUp()) end = true;
        }
    }

    //method to reset the screen back to what it was before
    public void reset() {
        //timer.reset();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                targets[i][j].setX(startX + ((i) * offsetX));
                targets[i][j].setY(startY + ((j) * offsetY));
                targets[i][j].setRandMovmentVec();
            }

        if (end) {
            end = false;
            thread = new Thread(this);
            thread.start();
        }
    }


    void eventOutput(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription + " detected on " + e.getComponent().getClass().getName() + ".");
    }

    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < targets.length; i++)
            for (int j = 0; j < targets[0].length; j++) {
                if (targets[i][j].isHit(e.getX(), e.getY())) {
                    targets[i][j].setColor(Color.GREEN);
                    hitCount++;
                    break;
                }
            }
        eventOutput("Mouse pressed, hitCount: " + hitCount, e);
        System.out.println("x: " + e.getX() + " y: " + e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        eventOutput("Mouse released (# of clicks: "
                + e.getClickCount() + ")", e);
    }

    public void mouseEntered(MouseEvent e) {
        eventOutput("Mouse entered", e);
    }

    public void mouseExited(MouseEvent e) {
        eventOutput("Mouse exited", e);
    }

    public void mouseClicked(MouseEvent e) {
        eventOutput("Mouse clicked (# of clicks: "
                + e.getClickCount() + ")", e);


    }
}