import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jansen on 10/31/2015.
 */
public class Sphere extends Shape {
    private Vertex center;
    private double radius;
    public Sphere(String[] init) {
        this.center = new Vertex(init[2], init[3], init[4]);
        this.radius = Double.parseDouble(init[6]);

        if (init[8].equals("Diffuse")) {
            this.material = MaterialType.Diffuse;
        } else if (init[8].equals("Reflective")) {
            this.material = MaterialType.Reflective;
        } else if (init[8].equals("Transparent")) {
            this.material = MaterialType.Transparent;
        }
        this.materialColor = new Color(init[9], init[10], init[11]);
        if (this.material == MaterialType.Diffuse) {
            this.specularHighlight = new Color(init[13], init[14], init[15]);
            this.phongConstant = Integer.parseInt(init[17]);
            this.indexOfRefraction = 1;
        } else if (this.material == MaterialType.Reflective){
            this.specularHighlight = this.materialColor;
            this.phongConstant = 1;
            this.indexOfRefraction = 1;
        } else {//if (this.material == MaterialType.Transparent) {
            this.specularHighlight = new Color(0, 0, 0);
            this.phongConstant = 0;
            this.indexOfRefraction = Double.parseDouble(init[13]);
        }

    }

    @Override
    public double getTIntersectOfRay(Ray trace) {
        Vector rayOriginToSphereCenter = this.center.minus(trace.getOrigin());

        if (rayOriginToSphereCenter.dot(trace.getDirection()) < 0) {
            if (rayOriginToSphereCenter.magnitude() > radius) {
                return -1;
            } else if (rayOriginToSphereCenter.magnitude() == radius) {
                return 0;
            } else {
                Vector projectOntoRay = rayOriginToSphereCenter.projectOn(trace.getDirection());

                Vertex projectedPoint = trace.getOrigin().plus(projectOntoRay);
                Vector sphereCenterToRay = projectedPoint.minus(this.center);

                double projectedPointToSphereDistance = Math.sqrt(Math.pow(radius, 2) - Math.pow(sphereCenterToRay.magnitude(), 2));
                double rayOriginToSphereEdgeDistance = projectedPointToSphereDistance - projectOntoRay.magnitude();
                return rayOriginToSphereEdgeDistance;
            }

        } else {
            //Find the ray from the center to the closest approach to the ray
            Vector projectOntoRay = rayOriginToSphereCenter.projectOn(trace.getDirection());
            Vertex projectedPoint = trace.getOrigin().plus(projectOntoRay);
            Vector sphereCenterToRay = projectedPoint.minus(this.center);

            if (sphereCenterToRay.magnitude() > this.radius) {
                return -1;
            }
            if (sphereCenterToRay.magnitude() == this.radius) {
                return projectOntoRay.magnitude();
            }//else
            //find distance from projectedPoint to the sphere
            //a^2 + b^2 = c^2
            //b = sqrt(c^2 - a^2)
            //c = radius
            //a = sphereCenterToRay.magnitude
            double projectedPointToSphereDistance = Math.sqrt(Math.pow(radius, 2) - Math.pow(sphereCenterToRay.magnitude(), 2));
            double distanceToFirstIntersection;
            if (rayOriginToSphereCenter.magnitude() > radius) {
                distanceToFirstIntersection = projectedPoint.minus(trace.getOrigin()).magnitude() - projectedPointToSphereDistance;
            } else {
                distanceToFirstIntersection = projectedPoint.minus(trace.getOrigin()).magnitude() + projectedPointToSphereDistance;
            }

            return distanceToFirstIntersection;

        }
    }

    public Vector getNormal(Vertex intersect) {
        Vector difference = this.center.minus(intersect).negative();
        return difference.normalize();
    }

    public String toString() {
        String ret = "Sphere Center: " + center.toString() +  " Radius: " + radius +
                " Material: " + material.name() + " " + materialColor.toString();
        if (this.material == MaterialType.Diffuse) {
            ret +=" Spec Highlight: " + specularHighlight.toString() +
                    " Phong Constant: " + phongConstant;
        }
        return ret;
    }


}
