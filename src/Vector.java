public class Vector {
    public double x;
    public double y;

    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt((x * x) + (y * y));
    }
}