import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame implements ActionListener {
    //size of the board
    private final int WIDTH = 1024;
    private final int HEIGHT = 768;

    //objects that actions are performed on
    private JMenuItem fileRestart;

    //objects that are added to the content pane (through the Screen)
    private Board board;

    private Screen screen;

    public Board() {
        //set the title of the frame
        super("Shoot the target");

        board = this;

        //get screen information
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = kit.getScreenSize();
        int screenWidth = (int) screenDimension.getWidth();
        int screenHeight = (int) screenDimension.getHeight();

        //set location at center of screen
        int gameX = (screenWidth / 2) - (WIDTH / 2);
        int gameY = (screenHeight / 2) - (HEIGHT / 2);

        //set frame information
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(gameX, gameY);
        this.setVisible(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        //create the countdown
        Countdown cd = new Countdown();

        screen = new Screen(cd, this);
        this.getContentPane().add(screen);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileRestart) {

            //resets the screen so the game can be played again
            screen.reset();
        }
    }
}