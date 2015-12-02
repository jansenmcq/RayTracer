/**
 * Created by Jansen on 10/31/2015.
 */
public class Vector extends Vertex {
    public Vector(String x, String y, String z) {
        super(x, y, z);
    }

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector(Vertex v) {
        super(v.x, v.y, v.z);
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector cross(Vector that) {
        double newX = this.y * that.z - this.z * that.y;
        double newY = this.z * that.x - this.x * that.z;
        double newZ = this.x * that.y - this.y * that.x;
        return new Vector(newX, newY, newZ);
    }

    public double dot(Vector that) {
        return this.x * that.x + this.y * that.y + this.z * that.z;
    }

    public Vector projectOn(Vector that) {
        double dotProduct = this.dot(that);
        double magnitude = that.magnitude();
        return that.times(dotProduct/magnitude);
    }


    public Vector normalize() {
        double magnitude = this.magnitude();
        double newX = this.x / magnitude;
        double newY = this.y / magnitude;
        double newZ = this.z / magnitude;
        return new Vector(newX, newY, newZ);
    }

    public Vector negative() {
        return this.times(-1);
    }

    public Vector minus(Vector from) {
        return new Vector(this.x - from.x, this.y - from.y, this.z - from.z);
    }

    public Vector plus(Vector that) {
        return new Vector(this.x + that.x, this.y + that.y, this.z + that.z);
    }

    public Vector times(double value) {
        return new Vector(this.x * value, this.y * value, this.z * value);
    }

}
