/**
 * Created by Jansen on 10/31/2015.
 */
public class Triangle extends Shape {
    private Vertex p1, p2, p3;
    private Vector planeNormal;
    private double distanceToOrigin;
    public Triangle(String[] init) {
        p1 = new Vertex(init[1], init[2], init[3]);
        p2 = new Vertex(init[4], init[5], init[6]);
        p3 = new Vertex(init[7], init[8], init[9]);
        if (init[11].equals("Diffuse")) {
            this.material = MaterialType.Diffuse;
        } else if (init[11].equals("Reflective")) {
            this.material = MaterialType.Reflective;
        } else if (init[11].equals("Transparent")) {
            this.material = MaterialType.Transparent;
        }
        this.materialColor = new Color(init[12], init[13], init[14]);
        if (this.material == MaterialType.Diffuse) {
            this.specularHighlight = new Color(init[16], init[17], init[18]);
            this.phongConstant = Integer.parseInt(init[20]);
            this.indexOfRefraction = 1;
        } else if (this.material == MaterialType.Reflective) {
            this.specularHighlight = this.materialColor;
            this.phongConstant = 1;
            this.indexOfRefraction = 1;
        } else {
            this.specularHighlight = new Color(0, 0, 0);
            this.phongConstant = 0;
            this.indexOfRefraction = Double.parseDouble(init[16]);
        }


        Vector vec1 = p1.minus(p2);
        Vector vec2 = p2.minus(p3);
        this.planeNormal = vec1.cross(vec2).normalize();

        this.distanceToOrigin = -this.planeNormal.dot(p1.asVector());
    }

    public Triangle(Vertex p1, Vertex p2, Vertex p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.planeNormal = null;
        this.distanceToOrigin = -1;
    }

    @Override
    public double getTIntersectOfRay(Ray ray) {
//        System.out.println(this);
        double tValueDenominator = this.planeNormal.dot(ray.getDirection());
//        System.out.println("Denom: " + tValueDenominator);
        if (tValueDenominator == 0) {
            return -1;
        }

        double tValueNumerator = -(this.planeNormal.dot(ray.getOrigin().asVector()) + this.distanceToOrigin);
//        System.out.println("Num: " + tValueNumerator);
        double tValue = tValueNumerator / tValueDenominator;
//        System.out.println("T: " + tValue);
            //System.out.println(tValue);
        //**/tValue = Math.abs(tValue);

        //tValue is intersect of plane. Need to check for intersect of triangle speciically
        Vertex intersectPoint = ray.getPointAtT(tValue);
//        System.out.println("Point: " + intersectPoint);

        //we know it's a triangle, so we can just do dot products along the sides

        Vector edge1 = p2.minus(p1);
        Vector edge1ToIntersect = intersectPoint.minus(p1);
        Vector edge1Normal = edge1.cross(edge1ToIntersect);

        Vector edge2 = p3.minus(p2);
        Vector edge2ToIntersect = intersectPoint.minus(p2);
        Vector edge2Normal = edge2.cross(edge2ToIntersect);

        Vector edge3 = p1.minus(p3);
        Vector edge3ToIntersect = intersectPoint.minus(p3);
        Vector edge3Normal = edge3.cross(edge3ToIntersect);

//        System.out.println(edge1Dot + " " + edge2Dot + " " + edge3Dot);

        double dot12 = edge1Normal.dot(edge2Normal);
        double dot23 = edge2Normal.dot(edge3Normal);
        double dot31 = edge3Normal.dot(edge1Normal);

//        System.out.println("cross dots: " + dot12 + " " + dot23 + " " + dot31);

        if (dot12 > 0 && dot23 > 0 && dot31 > 0) {
            return tValue;
        } else if (dot12 < 0 && dot23 < 0 && dot31 < 0) {
            return tValue;
        } else {
            return -1;
        }



        //drop coordinate

        /*
        Vector collapseTo2d;
        double normX = Math.abs(this.planeNormal.x);
        double normY = Math.abs(this.planeNormal.y);
        double normZ = Math.abs(this.planeNormal.z);

        if (normX > normY) {
            if (normX > normZ) {//drop x
                collapseTo2d = new Vector(0, 1, 1);
            }
            else { //drop z
                collapseTo2d = new Vector(1, 1, 0);
            }
        } else {
            if (normY > normZ) {//drop y
                collapseTo2d = new Vector(1, 0, 1);
            } else {//drop z
                collapseTo2d = new Vector(1, 1, 0);
            }
        }
        Vertex flatP1 = this.p1.times(collapseTo2d);
        Vertex flatP2 = this.p2.times(collapseTo2d);
        Vertex flatP3 = this.p3.times(collapseTo2d);
        Vertex flatIntersectPoint = intersectPoint.times(collapseTo2d);

        System.out.println(flatP1 + " " + flatP2 + " " + flatP3 + " " + flatIntersectPoint);
        //translate points so that intersection point is in origin (subtract value of intersection point from triangle points)
        Vertex transP1 = flatP1.minus(flatIntersectPoint);
        Vertex transP2 = flatP2.minus(flatIntersectPoint);
        Vertex transP3 = flatP3.minus(flatIntersectPoint);


        System.out.println(transP1 + " " + transP2 + " " + transP3);




        return -1;*/
    }

    public Vector getNormal(Vertex intersect) {
        return this.planeNormal;
    }

    public String toString() {
        return "Triangle p1: " + p1.toString() + " p2: " + p2.toString() + " p3: " + p3.toString() +
                " Material: " + material.toString() + " " + materialColor.toString() +
                " Spec Highlight: " + specularHighlight.toString() +
                " Phong Constant: " + phongConstant +
                " PlaneNormal: " + planeNormal +
                " Distance: " + distanceToOrigin;
    }


}
