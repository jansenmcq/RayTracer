/**
 * Created by Jansen on 11/2/2015.
 */
public class Ray {
    private Vertex origin;
    private Vector direction;
    public Ray(Vertex origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vertex getOrigin() {
        return this.origin;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public Vertex getPointAtT(double t) {
        return origin.plus(direction.times(t));
    }

    public double getTForPoint(Vertex v) {
        Vector distance = v.minus(this.origin);
        return distance.magnitude();
    }

    public String toString() {
        return "Origin: " + origin + "; Direction: " + direction;
    }
}
