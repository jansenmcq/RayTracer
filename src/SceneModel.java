
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.*;
import java.util.*;


public class SceneModel {
    private Vertex cameraLookAt, cameraLookFrom;
    private Vector cameraLookUp;
    private double windowSize, xMin, yMin, xMax, yMax;
    private double fieldOfView;
    private Vector directionToLight;
    private Color lightColor, ambientLight, backgroundColor;
    private ArrayList<Shape> shapes;

    public SceneModel(String fileName) {

        shapes = new ArrayList<Shape>();

        //open and read file
        List<String[]> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            list = stream
                    .map(s -> s.split(" +"))//note this is regex for 'any number of spaces'
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        list.forEach(line -> processLine(line));

    }

    private void processLine(String[] line) {
        switch (line[0]) {
            case "CameraLookAt":
                cameraLookAt = new Vertex(line[1], line[2], line[3]);
                break;
            case "CameraLookFrom":
                cameraLookFrom = new Vertex(line[1], line[2], line[3]);
                break;
            case "CameraLookUp":
                cameraLookUp = new Vector(line[1], line[2], line[3]);
                break;
            case "FieldOfView":
                fieldOfView = Double.parseDouble(line[1]);

                double viewPlaneDistance = cameraLookAt.minus(cameraLookFrom).magnitude();
                double tangent = Math.tan(Math.toRadians(fieldOfView * 2));
                this.windowSize = tangent * viewPlaneDistance;

                this.xMin = -windowSize/2;
                this.xMax = windowSize/2;
                this.yMin = -windowSize/2;
                this.yMax = windowSize/2;
                break;
            case "DirectionToLight":
                directionToLight = new Vector(line[1], line[2], line[3]);
                lightColor = new Color(line[5], line[6], line[7]);
                break;
            case "AmbientLight":
                ambientLight = new Color(line[1], line[2], line[3]);
                break;
            case "BackgroundColor":
                backgroundColor = new Color(line[1], line[2], line[3]);
                break;
            case "Sphere":
                shapes.add(new Sphere(line));
                break;
            case "Triangle":
                shapes.add(new Triangle(line));
                break;
        }
    }

    public Vertex getCameraLocation() {
        return this.cameraLookFrom;
    }

    public Vertex getPixelInWorldCoords(int i, int j, int pixelDensity) {
        /*//sWorld = LookAt + usu + vsv + wsw
        double u = i * ( (this.viewU.magnitude() * 2) / pixelDensity) - this.viewU.magnitude();
        double v = j * ( (this.viewV.magnitude() * 2) / pixelDensity) - this.viewV.magnitude();
        double w = 0;
        double x =
        double y;
        double z;
        Vector offset = new Vector(x, y, z);
        return this.cameraLookAt.plus(offset);
*/
        double pixelSize = this.windowSize / (double) pixelDensity;
        double x = this.xMin + (pixelSize * j);//whhyyyyyy
        double y = this.yMax - (pixelSize * i);//whaaaaaaaaaat
        double z = 0;

        return new Vertex(x, y, z);
    }

    public Shape getShape(int index) {
        return this.shapes.get(index);
    }

    public Color getLightColor() {
        return this.lightColor;
    }

    public Color getAmbientLight() {
        return this.ambientLight;
    }

    public int shapesLength() {
        return this.shapes.size();
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public Vector getLightDirection() {
        return this.directionToLight;
    }

    public String toString() {
        String ret = "CameraLookAt: " + cameraLookAt.toString() + "\nCameraLookFrom: " + cameraLookFrom.toString() + "\nCameraLookUp: " + cameraLookUp.toString() +
                "\nFieldOfView: " + fieldOfView + "\nDirectionToLight: " + directionToLight.toString() + "\nLight Color " + lightColor.toString() +
                "\nAmbientLight: " + ambientLight.toString() + "\nBackgroundColor: " + backgroundColor.toString() +
                "\nWindowSize: " + this.windowSize;
        for (Shape shape : shapes) {
            ret += "\n" + shape.toString();
        }
        return ret;
    }
}