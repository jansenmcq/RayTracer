
public class Vertex {
    public double x, y, z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(String x, String y, String z) {
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        this.z = Double.parseDouble(z);
    }

    public Vertex() {
        this.x = this.y = this.z = 0;
    }


    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vector minus(Vertex from) {
        return new Vector(this.x - from.x, this.y - from.y, this.z - from.z);
    }

    public Vertex minus(Vector from) {
        return new Vector(this.x - from.x, this.y - from.y, this.z - from.z);
    }

    public Vertex plus(Vector vec) {
        return new Vertex(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public Vertex times(Vector vec) {
        return new Vertex(this.x * vec.x, this.y * vec.y, this.z * vec.y);
    }

    public Vector asVector() {
        return new Vector(x, y, z);
    }
}