import java.awt.*;
import java.util.Random;

public class Target {
    private double x;
    private double y;
    private double r;
    private Color color;
    private Vector v; //movment vector

    public Target() {
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.color = Color.WHITE;
        this.v = new Vector();
    }

    public Target(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = Color.RED;
        this.v = new Vector();
    }

    public Target(int x, int y, int r, Vector v) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = Color.RED;
        this.v = v;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getRadius() {
        return this.r;
    }

    public Color getColor() {
        return this.color;
    }

    public Vector getMovmentVec() {
        return this.v;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRadius(double r) {
        this.r = r;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMovmentVec(Vector v) {
        this.v = v;
    }

    public void setMovmentVec(int x, int y) {
        this.v.x = x;
        this.v.y = y;
    }

    public void setRandMovmentVec() {
        Random rand = new Random();
        int lvl = 2;
        v.x = rand.nextDouble() * lvl;
        v.y = rand.nextDouble() * lvl;
    }

    public void paintTarget(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillOval((int) this.x, (int) this.y, (int) this.r * 2, (int) this.r * 2);
        g.setColor(this.color);
        g.fillOval((int) this.x + 2, (int) this.y + 2, ((int) this.r - 2) * 2, ((int) this.r - 2) * 2);
    }

    public boolean isHit(int x, int y) {
        double centerX = this.x + this.r;
        double centerY = this.y + this.r;
        double difX = centerX - x;
        double difY = centerY - y;
        double distance = Math.sqrt((difX * difX) + (difY * difY));
        if (distance > r) return false;
        return true;
    }

    public void move() {
        this.x += v.x;
        this.y += v.y;
    }

    public void bounceX() {
        this.v.x *= -1;
    }

    public void bounceY() {
        this.v.y *= -1;
    }
}