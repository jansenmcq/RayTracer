import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //String fileName = "diffuse";
//        String fileName = "SceneII";
        String fileName = "Custom";
        String sceneFile = fileName + ".rayTracing";

        SceneModel scene = new SceneModel(sceneFile);

        RayTracer tracer = new RayTracer(scene);

        ArrayList<ArrayList<Color>> sceneImage = tracer.traceScene(1000, 100);

        String imageFile = fileName + ".ppm";

        PpmWriter writer = new PpmWriter();
        writer.write(sceneImage, imageFile);

    }

}