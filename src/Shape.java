/**
 * Created by Jansen on 10/31/2015.
 */
import java.util.ArrayList;

public abstract class Shape {

    protected MaterialType material;
    protected Color materialColor;
    protected Color specularHighlight;
    protected int phongConstant;
    protected double indexOfRefraction;

    public Shape() {

    }

    public abstract double getTIntersectOfRay(Ray trace);

    public abstract Vector getNormal(Vertex intersect);

    public int getPhongConstant() {
        return this.phongConstant;
    }

    public Color getSpecularHighlight() {
        return this.specularHighlight;
    }

    public MaterialType getMaterialType() {
        return material;
    }

    public double getIndexOfRefraction() {
        return indexOfRefraction;
    }

    public String toString() {
        return "Unimplemented";
    }

    public Color getColor() {
        return this.materialColor;
    }


}
