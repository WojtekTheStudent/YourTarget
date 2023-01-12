import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.FileWriter;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Random;

public class Screen extends JPanel implements Runnable, MouseListener {

    private Countdown timer;
    private Board board;
    private Target[][] targets;
    private int hitCount;
    private Vector selectedTarget;
    private int t1 = 1;
    private int t2 = 0;
    private double reactionTime = -1;
    private long st;
    private int[] t;
    private int interspace = 100;
    private int first = 140;

    private Thread thread;
    private boolean end = false;
    private boolean start = true;
    private boolean nextLvl = false;
    private boolean benchmarkEnd = false;

    //movment borders
    private final int top = 0;
    private final int bot = 667;
    private final int left = 0;
    private final int right = 768;

    //variables for placing targets
    private int startX;
    private int startY;
    private int offsetX;
    private int offsetY;

    //game settings
    private int gameSpeed = 10;
    private int dificulty = 3;
    private boolean movement = false;
    private int targetSize = 20;
    private int lvl = 0;

    public Screen(Countdown cd, Board game_board) {

        timer = cd;
        board = game_board;
        this.setFocusable(true);
        this.add(timer);

        thread = new Thread(this);
        thread.start();

        board.addMouseListener(this);
        addMouseListener(this);

        startX = (1024 * 3 / 4) / (dificulty + 1);
        startX -= targetSize;
        startY = 668 / (dificulty + 1);
        startY -= targetSize;
        offsetX = startX + targetSize;
        offsetY = startY + targetSize;

        targets = new Target[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                targets[i][j] = new Target(startX + ((i) * offsetX), startY + ((j) * offsetY), targetSize);
                if (movement) targets[i][j].setRandMovmentVec();
            }

        selectedTarget = null;
        hitCount = 0;

        t = new int[4];
        for (int i = 0; i < 4; i++) t[i] = 0;
    }

    public void paintComponent(Graphics g) {
        setOpaque(false);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (lvl) {

            case (1): {
                Image background = new ImageIcon("dedust24.png").getImage();
                if (!g2.drawImage(background, 0, 0, this)) System.out.println("Error with background 1");
                break;
            }
            case (2): {
                Image background = new ImageIcon("WESTERN.png").getImage();
                if (!g2.drawImage(background, 0, 0, this)) System.out.println("Error with background 2");
                break;
            }
            case (3): {
                Image background = new ImageIcon("MIRAGE.png").getImage();
                if (!g2.drawImage(background, 0, 0, this)) System.out.println("Error with background 3");
                break;
            }
            case (4): {
                Image background = new ImageIcon("DUST2_1.6.png").getImage();
                if (!g2.drawImage(background, 0, 0, this)) System.out.println("Error with background 4");
                break;
            }
            case (5): {
                Image background = new ImageIcon("REFLEX.png").getImage();
                if (!g2.drawImage(background, 0, 0, this)) System.out.println("Error with background 5");
                break;
            }
        }

        for (int i = 0; i < dificulty; i++)
            for (int j = 0; j < dificulty; j++)
                targets[i][j].paintTarget(g2);

        int tmp1 = 1024 * 3 / 4;
        int tmp2 = 668;
        g2.setColor(Color.RED);
        g2.fillRect(tmp1, 0, 1024 - tmp1, 768);
        g2.fillRect(0, tmp2, 1024, 100);

        int outline = 5;
        g2.setColor(Color.BLACK);
        g2.fillRect(tmp1 + outline, 0 + outline, 1024 - tmp1 - (outline * 5) + 1, 768 - (outline * 14));
        g2.fillRect(0 + outline, tmp2 + outline, 1024 - (outline * 5), 100 - (outline * 9));

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

        g2.setColor(Color.WHITE);
        AttributedString as;
        as = new AttributedString("Level: " + lvl);
        as.addAttribute(TextAttribute.SIZE, 40);
        g2.drawString(as.getIterator(), outline, tmp2 + outline + 40);

        if (lvl != 5) {
            g2.setColor(Color.WHITE);
            as = new AttributedString(timer.getStringTime());
            as.addAttribute(TextAttribute.SIZE, 80);
            g2.drawString(as.getIterator(), 835, 600);

            as = new AttributedString(Integer.toString(hitCount));
            as.addAttribute(TextAttribute.SIZE, 80);
            g2.drawString(as.getIterator(), 860, 380);
        }

        if (start) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 1024, 768);
            g2.setColor(Color.WHITE);
            for (int i = 0; i < 5; i++) {
                int tmp = first + (interspace * i);
                if (i < 4) {
                    as = new AttributedString("Level " + (i + 1));
                    as.addAttribute(TextAttribute.SIZE, 40);
                    g2.drawString(as.getIterator(), 10, tmp);
                }
                g2.fillRect(10, tmp + 10 - 45, 130, 2);
                g2.fillRect(10, tmp + 10, 130, 2);
                g2.fillRect(10, tmp + 10 - 45, 2, 45);
                g2.fillRect(10 + 130 - 2, tmp + 10 - 45, 2, 45);
            }
            as = new AttributedString("Reflex");
            as.addAttribute(TextAttribute.SIZE, 40);
            g2.drawString(as.getIterator(), 17, first + interspace * 4);

            Image background = new ImageIcon("target2.png").getImage();
            if (!g2.drawImage(background, 170, 0, this)) System.out.println("Error with target image");
        }

        if (nextLvl) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 1024, 768);
            g2.setColor(Color.WHITE);
            as = new AttributedString("Menu");
            as.addAttribute(TextAttribute.SIZE, 40);
            g2.drawString(as.getIterator(), 10, first);
            as = new AttributedString("Restart");
            as.addAttribute(TextAttribute.SIZE, 40);
            g2.drawString(as.getIterator(), 10, first + interspace);
            int x = 3;
            if (lvl != 4) {
                as = new AttributedString("Next");
                as.addAttribute(TextAttribute.SIZE, 40);
                g2.drawString(as.getIterator(), 10, first + interspace * 2);
            } else x--;
            for (int i = 0; i < x; i++) {
                int tmp = first + (interspace * i);
                g2.fillRect(10, tmp + 10 - 45, 130, 2);
                g2.fillRect(10, tmp + 10, 130, 2);
                g2.fillRect(10, tmp + 10 - 45, 2, 45);
                g2.fillRect(10 + 130 - 2, tmp + 10 - 45, 2, 45);
            }
            Image background = new ImageIcon("target2.png").getImage();
            if (!g2.drawImage(background, 170, 0, this)) System.out.println("Error with target image");
        }

        if (benchmarkEnd) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 1024, 768);
            g2.setColor(Color.WHITE);
            as = new AttributedString("Restart");
            as.addAttribute(TextAttribute.SIZE, 40);
            g2.drawString(as.getIterator(), 10, first);
            int tmp = first;
            g2.fillRect(10, tmp + 10 - 45, 130, 2);
            g2.fillRect(10, tmp + 10, 130, 2);
            g2.fillRect(10, tmp + 10 - 45, 2, 45);
            g2.fillRect(10 + 130 - 2, tmp + 10 - 45, 2, 45);
            Image background = new ImageIcon("target2.png").getImage();
            if (!g2.drawImage(background, 170, 0, this)) System.out.println("Error with target image");
            as = new AttributedString(reactionTime + " ms!");
            as.addAttribute(TextAttribute.SIZE, 30);
            g2.drawString(as.getIterator(), 10, first + interspace);
        }
    }

    @Override
    public void run() {
        while (!end) {
            try {
                Thread.sleep(gameSpeed);
            } catch (InterruptedException e) {
            }

            if (start) {
                timer.increment();
                continue;
            }

            if (lvl == 5 && !benchmarkEnd) {
                repaint();
                Random rand = new Random();
                try {
                    Thread.sleep(2000 + rand.nextInt(1000));
                } catch (InterruptedException e) {
                }
                targets[0][0].setColor(Color.GREEN);
                repaint();
                st = System.nanoTime();
                end = true;
                continue;
            }

            for (int i = 0; i < dificulty; i++)
                for (int j = 0; j < dificulty; j++) {
                    targets[i][j].move();
                    double curX = targets[i][j].getX();
                    double curY = targets[i][j].getY();
                    if (curY <= top || curY + (targets[i][j].getRadius() * 2) >= bot) targets[i][j].bounceY();
                    if (curX <= left || curX + (targets[i][j].getRadius() * 2) >= right) targets[i][j].bounceX();
                }

            int delay1 = 40; //in between
            int delay2 = 60; //green on
            if (t1 % delay1 == 0) {
                if (t2 == 0) selectRandomTarget();
                t2++;
                if (t2 == delay2) {
                    t2 = 0;
                    t1++;
                }
            } else t1++;

            timer.increment();
            repaint();

            if (timer.isTimeUp()) {
                end = true;
                nextLvl = true;
                if (lvl < 5) {
                    t[lvl - 1] = hitCount;
                    String s = "";
                    for (int i = 0; i < 4; i++) {
                        s += "Poziom ";
                        s += i + 1;
                        s += ".   Liczba punktów: ";
                        s += t[i];
                        s += "\n";
                    }
                    writeToFile("LevelScores.txt", s);
                }
            }
        }
    }

    public void reset() {
        timer.reset();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        startX = (1024 * 3 / 4) / (dificulty + 1);
        startX -= targetSize;
        startY = 668 / (dificulty + 1);
        startY -= targetSize;
        offsetX = startX + targetSize;
        offsetY = startY + targetSize;

        targets = null;
        targets = new Target[dificulty][dificulty];
        for (int i = 0; i < dificulty; i++)
            for (int j = 0; j < dificulty; j++) {
                targets[i][j] = new Target(startX + ((i) * offsetX), startY + ((j) * offsetY), targetSize);
                if (movement) targets[i][j].setRandMovmentVec();
            }

        selectedTarget = null;
        hitCount = 0;

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

        if (start) {
            for (int i = 0; i < 5; i++) {
                if (e.getX() >= 10 && e.getX() <= 130) {
                    int tmp = first + (interspace * i);
                    if (e.getY() >= tmp + 10 - 45 && e.getY() <= tmp + 10) lvl = i + 1;
                }
            }
            if (lvl != 0) {
                setLvl();
                reset();
                start = false;
                System.out.println("lvl " + lvl);
            }
            return;
        }

        if (nextLvl) {
            if (e.getX() >= 10 && e.getX() <= 130) {
                int tmp = first;
                if (e.getY() >= tmp + 10 - 45 && e.getY() <= tmp + 10) //menu
                {
                    nextLvl = false;
                    start = true;
                    repaint();
                    return;
                }
                tmp += interspace;
                if (e.getY() >= tmp + 10 - 45 && e.getY() <= tmp + 10) //restart
                {
                    reset();
                    nextLvl = false;
                    return;
                }
                tmp += interspace;
                if (e.getY() >= tmp + 10 - 45 && e.getY() <= tmp + 10 && lvl != 4) //next
                {
                    t[lvl - 1] = hitCount;
                    lvl++;
                    setLvl();
                    reset();
                    nextLvl = false;
                    return;
                }
            }
            return;
        }

        if (benchmarkEnd) {
            if (e.getX() >= 10 && e.getX() <= 130 && e.getY() >= first + 10 - 45 && e.getY() <= first + 10) {
                reset();
                benchmarkEnd = false;
            }
        }

        if (e.getX() > (1024 * 3 / 4) + 15 && e.getX() < (1024 - 15) && e.getY() > 20 && e.getY() < 95) //menu button
        {
            start = true;
            if (lvl < 5) {
                t[lvl - 1] = hitCount;
                String s = "";
                for (int i = 0; i < 4; i++) {
                    s += "Poziom ";
                    s += i + 1;
                    s += ".   Liczba punktów: ";
                    s += t[i];
                    s += "\n";
                }
                writeToFile("LevelScores.txt", s);
                lvl = 0;
            }
            repaint();
        }

        if (lvl == 5) {
            if (targets[0][0].isHit(e.getX(), e.getY())) {
                targets[0][0].setColor(Color.RED);
                reactionTime = (double) ((System.nanoTime() - st) / 1000000);
                System.out.println("reaction: " + reactionTime);
                String s = "";
                s += "Reaction time: ";
                s += reactionTime;
                s += "ms.";
                writeToFile("ReactionBenchmark.txt", s);
                benchmarkEnd = true;
                repaint();
            }
            return;
        }

        for (int i = 0; i < targets.length; i++)
            for (int j = 0; j < targets[0].length; j++) {
                if (targets[i][j].isHit(e.getX(), e.getY()) && i == (int) selectedTarget.x && j == (int) selectedTarget.y) {
                    playSound("hitsound.wav");
                    targets[i][j].setColor(Color.RED);
                    t2 = 0;
                    t1++;
                    hitCount++;
                    return;
                }
            }
        playSound("miss.wav");
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

    public void selectRandomTarget() {
        Random rand = new Random();
        int x = rand.nextInt(dificulty);
        int y = rand.nextInt(dificulty);

        for (int i = 0; i < dificulty; i++)
            for (int j = 0; j < dificulty; j++)
                targets[i][j].setColor(Color.RED);

        targets[x][y].setColor(Color.GREEN);
        selectedTarget = new Vector(x, y);
    }

    public void setLvl() {
        switch (lvl) {
            case (1): {
                dificulty = 3;
                targetSize = 20;
                movement = false;
                break;
            }
            case (2): {
                dificulty = 4;
                targetSize = 20;
                movement = false;
                break;
            }
            case (3): {
                dificulty = 3;
                targetSize = 20;
                movement = true;
                break;
            }
            case (4): {
                dificulty = 4;
                targetSize = 20;
                movement = true;
                break;
            }
            case (5): {
                dificulty = 1;
                targetSize = 100;
                movement = false;
            }
        }
    }

    public void writeToFile(String fileName, String s) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static synchronized void playSound(final String fileName) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream(fileName));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}