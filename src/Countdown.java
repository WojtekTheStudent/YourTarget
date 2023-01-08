import javax.swing.*;
import java.awt.*;

public class Countdown extends JPanel {
    //variables
    private int time;
    //final score
    private final int TIME_MAX = 10000;
    //determine the winners
    private boolean end = false;
    private JLabel tm;

    //constructor
    public Countdown() {
        time = 0;
        tm = new JLabel();
        tm.setText(100 - (time / 100) + " ");
        tm.setFont(new Font("Monospaced", Font.BOLD, 0));
        tm.setForeground(Color.WHITE);
        Dimension size = tm.getPreferredSize();
        //tm.setBounds(540, 200, size.width, size.height);
        this.setOpaque(false);
        this.add(tm);
    }

    public void increment() {
        if (time < 100) time = 99;
        time++;
        if (time == TIME_MAX) {
            end = true;
            tm.setText("Time is up!");
        } else {
            tm.setText(100 - (time / 100) + " ");
        }
    }

    public boolean isTimeUp() {
        return end;
    }

    public void reset() {
        time = 0;
        tm.setText(time + " ");
        end = false;
    }

    public String getStringTime() {
        return tm.getText();
    }
}
