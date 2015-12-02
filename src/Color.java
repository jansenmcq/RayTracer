/**
 * Created by Jansen on 10/31/2015.
 */
public class Color {
    public double r, g, b;

    public Color(String r, String g, String b) {
        this.r = Double.parseDouble(r);
        this.g = Double.parseDouble(g);
        this.b = Double.parseDouble(b);
    }

    /**
     * Will automatically clamp any new Color to 1 on each channel
     * @param r
     * @param g
     * @param b
     */
    public Color(double r, double g, double b) {
        this.r = Math.min(1, r);
        this.g = Math.min(1, g);
        this.b = Math.min(1, b);
    }

    public Color times(double d) {
        return new Color(this.r * d, this.g * d, this.b * d);
    }

    public Color times(Color that) {
        return new Color(this.r * that.r, this.g * that.g, this.b * that.b);
    }

    public Color plus(Color that) {
        return new Color(this.r + that.r, this.g + that.g, this.b + that.b);
    }

    public String toString() {
        return "(" + r + ", " + g + ", " + b + ")";
    }

    public String toPPM() {
        return colorTo255(r) + " " + colorTo255(g) + " " + colorTo255(b) + " ";
    }

    private int colorTo255(double value) {
        return (int) (value * 255);
    }
}
