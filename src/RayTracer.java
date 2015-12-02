import java.util.ArrayList;

/**
 * Created by Jansen on 11/2/2015.
 */
public class RayTracer {
    private SceneModel scene;
    private final double INTERSECT_OFFSET = Math.pow(2, -16);
    private final Color BLACK = new Color(0, 0, 0);

    public RayTracer(SceneModel scene) {
        this.scene = scene;
    }

    /**
     * Allowing this function to receive input from calling function to dictate how intensely to model the scene
     * @param pixelDensity number of pixels in each row and column in the screen.
     */
    public ArrayList<ArrayList<Color>> traceScene(int pixelDensity, int recurseDepth) {
        ArrayList<ArrayList<Color>> imageMatrix = new ArrayList<ArrayList<Color>>();
        for (int i = 0; i < pixelDensity; i++) {
            imageMatrix.add(new ArrayList<Color>());
            for (int j = 0; j < pixelDensity; j++) {
                //calculate ray for pixel
//                Vertex pixelPoint = scene.getPixelInWorldCoords(50, 80, 100);
                Vertex pixelPoint = scene.getPixelInWorldCoords(i, j, pixelDensity);
                Vertex origin = scene.getCameraLocation();
                Ray traceRay = new Ray(origin, pixelPoint.minus(origin));
                //System.out.println(i + ":" + j);
                Color tehColor = this.calculateColor(traceRay, recurseDepth, 1);//We'll conceive of recursion as a 1-indexed array
//                System.out.println("Color: " + tehColor);
                imageMatrix.get(i).add(tehColor);
                //imageMatrix.get(i).add(this.getColorForRay(traceRay));
//                return imageMatrix;
            }
        }

        return imageMatrix;
    }

    private Color calculateColor(Ray ray, int maxDepth, int depth) {
        if (depth == maxDepth) {
            return scene.getBackgroundColor();
        }
        IntersectInfo intersect = this.getRayIntersect(ray);
        if (intersect.shapeIndex == -1) {//if we found no intersections on any of the shapes.
            return scene.getBackgroundColor();
        }

        //direction of Shadow ray
        Vertex intersectPoint = ray.getPointAtT(intersect.intersectTValue);



        Shape intersectedShape = scene.getShape(intersect.shapeIndex);
        Vector shapeNormal = intersectedShape.getNormal(intersectPoint);

        switch (intersectedShape.getMaterialType()) {
            case Diffuse:
                Ray shadowRay = new Ray(intersectPoint, scene.getLightDirection());
                //recreate the ray using a point a little offset from the surface
                Vertex shadowOrigin = shadowRay.getPointAtT(INTERSECT_OFFSET);
                shadowRay = new Ray(shadowOrigin, scene.getLightDirection());
                IntersectInfo shadowIntersect = this.getShadowRayIntersect(shadowRay);
                if (shadowIntersect.shapeIndex >= 0) {
                    return BLACK;
                }
                //calculate ambient side of the equation
                return this.calculateThisShapeColor(ray, intersectedShape, shapeNormal);
            case Transparent:
                //recurse into shape and reflect
                
                //reflect
                if (shapeNormal.dot(ray.getDirection()) < 0) {
                    shapeNormal = shapeNormal.negative();
                }
                Vector reflectedDirection = this.reflectedVector(shapeNormal, ray.getDirection());
                /**This is the ray that reflects off of the intersect*/
                Ray reflectedRay = new Ray(intersectPoint, reflectedDirection);
                Vertex reflectedOrigin = reflectedRay.getPointAtT(INTERSECT_OFFSET);
                reflectedRay = new Ray(reflectedOrigin, reflectedDirection);

                Color reflectedColor = this.calculateColor(reflectedRay, maxDepth, depth+1);
                Color reflectionIntensity = new Color(.25, .25, .25);
                reflectedColor = reflectedColor.times(reflectionIntensity);
                
                //refract (this will require two steps down, two recursions)

                //Must find the new direction into the shape (1st recursion)
                double refractionRatio = 1 / intersectedShape.getIndexOfRefraction();
                double incidentDot = ray.getDirection().dot(shapeNormal);
                double cValue = Math.sqrt(1 - Math.pow(refractionRatio, 2) * (1 - Math.pow(incidentDot, 2)));
                Vector leftTerm = ray.getDirection().times(refractionRatio);
                Vector rightTerm = shapeNormal.times(refractionRatio * incidentDot * cValue);

                Vector enterRefractionDirection = leftTerm.plus(rightTerm).normalize();
                Ray enterRefractionRay = new Ray(intersectPoint, enterRefractionDirection);
                Vertex enterRefractedRayOrigin = enterRefractionRay.getPointAtT(INTERSECT_OFFSET);
                enterRefractionRay = new Ray(enterRefractedRayOrigin, enterRefractionDirection);

                // now to find the point of intersection inside the sphere (2nd recursion)
                double exitRefractionTValue = intersectedShape.getTIntersectOfRay(enterRefractionRay);
                Vertex exitRefractionPoint = enterRefractionRay.getPointAtT(exitRefractionTValue);
                Vector insideNormal = intersectedShape.getNormal(exitRefractionPoint).negative();
                double exitRefractionRatio = intersectedShape.getIndexOfRefraction();
                double exitIncidentDot = enterRefractionRay.getDirection().dot(insideNormal);
                double exitCValue = Math.sqrt(1 - Math.pow(exitRefractionRatio, 2) * (1 - Math.pow(exitIncidentDot, 2)));
                Vector exitRefractionLeftTerm = enterRefractionRay.getDirection().times(exitRefractionRatio);
                Vector exitRefractionRightTerm = insideNormal.times(exitRefractionRatio * exitIncidentDot * exitCValue);

                Vector exitRefractionDirection = exitRefractionLeftTerm.plus(exitRefractionRightTerm).normalize();
                Ray exitRefractionRay = new Ray(exitRefractionPoint, exitRefractionDirection);
                Vertex exitRefractedRayOrigin = exitRefractionRay.getPointAtT(INTERSECT_OFFSET);
                exitRefractionRay = new Ray(exitRefractedRayOrigin, exitRefractionDirection);

                //Now get the color that ray hits
                Color refractedColor = this.calculateColor(exitRefractionRay, maxDepth, depth + 1);
//                return refractedColor;
                Color resultantColor = reflectedColor.plus(refractedColor);
                return resultantColor;
            case Reflective:
                //recurse and reflect
                if (shapeNormal.dot(ray.getDirection()) < 0) {
                    shapeNormal = shapeNormal.negative();
                }
                Vector newDirection = this.reflectedVector(shapeNormal, ray.getDirection());
                /**This is the ray that reflects off of the intersect*/
                Ray newRay = new Ray(intersectPoint, newDirection);
                Vertex newOrigin = newRay.getPointAtT(INTERSECT_OFFSET);
                newRay = new Ray(newOrigin, newDirection);

                Color deeperColor = this.calculateColor(newRay, maxDepth, depth+1);
                Color thisShapeColor = intersectedShape.getColor().times(deeperColor);
                Color diffuse = calculateThisShapeColor(ray, intersectedShape, shapeNormal);
                return diffuse.plus(thisShapeColor);
            default:
                return null;
        }
    }

    private Color calculateThisShapeColor(Ray ray, Shape intersectedShape, Vector shapeNormal) {
        double diffuseComponent = Math.max(0, shapeNormal.dot(scene.getLightDirection()));
        Color diffuseTerm = scene.getLightColor().times(diffuseComponent);
        Color ambientTerm = diffuseTerm.plus(scene.getAmbientLight());
        Color ambientLighting = ambientTerm.times(intersectedShape.getColor());

        //calculate specular side of the equation
        Vector reflectedLight = this.reflectedVector(shapeNormal, scene.getLightDirection().negative());
        Vector dirToCamera = ray.getDirection().negative();
        double lightCameraAngle = reflectedLight.dot(dirToCamera);
        double specularComponent = Math.max(0, lightCameraAngle);
        specularComponent = Math.pow(specularComponent, intersectedShape.getPhongConstant());
        Color specularTerm = scene.getLightColor().times(intersectedShape.getSpecularHighlight()).times(specularComponent);

        return ambientLighting.plus(specularTerm);
    }

    private IntersectInfo getRayIntersect(Ray ray) {
        double closestVertexTValue = Double.MAX_VALUE;
        int intersectedShapeIndex = -1;
        for (int i = 0; i < scene.shapesLength(); i++) {
            double intersectTValue = scene.getShape(i).getTIntersectOfRay(ray);
            if (intersectTValue > 0 && intersectTValue < closestVertexTValue) {
                closestVertexTValue = intersectTValue;
                intersectedShapeIndex = i;
            }
        }
        return new IntersectInfo(closestVertexTValue, intersectedShapeIndex);
    }

    private IntersectInfo getShadowRayIntersect(Ray ray) {
        double closestVertexTValue = Double.MAX_VALUE;
        int intersectedShapeIndex = -1;
        for (int i = 0; i < scene.shapesLength(); i++) {
            if (scene.getShape(i).getMaterialType() == MaterialType.Transparent) {
                continue;
            }
            double intersectTValue = scene.getShape(i).getTIntersectOfRay(ray);
            if (intersectTValue > 0 && intersectTValue < closestVertexTValue) {
                closestVertexTValue = intersectTValue;
                intersectedShapeIndex = i;
            }
        }
        return new IntersectInfo(closestVertexTValue, intersectedShapeIndex);
    }

    /**
     *
     * @param normal The normal at the point of intersection
     * @param incident The vector representing the direction the ray is traveling at (either light or the casted ray)
     * @return The resultant angle reflected against the normal
     */
    private Vector reflectedVector(Vector normal, Vector incident){
        double angleDot = normal.dot(incident);
        Vector subtrahend = normal.times(2 * angleDot);
        return incident.minus(subtrahend);
    }

    private class IntersectInfo {
        public double intersectTValue;
        public int shapeIndex;
        public IntersectInfo(double t, int index) {
            this.intersectTValue = t;
            this.shapeIndex = index;
        }
    }



}
