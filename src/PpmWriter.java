import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A class to take care of writing
 * Created by Jansen on 11/6/2015.
 */
public class PpmWriter {


    public PpmWriter() {

    }

    public void write(ArrayList<ArrayList<Color>> image, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            //HEADER
            //magic number
            writer.println("P3");
            //height, space, width
            writer.println(image.size() + " " + image.get(0).size());
            //max color value (must convert colors from doubles out of 1 to ints out of 255
            writer.println("255");
            for (int i = 0; i < image.size(); i++) {
                String line = "";
                for (int j = 0; j < image.get(i).size(); j++) {
                    line += image.get(i).get(j).toPPM();
                }
                writer.println(line);
            }
        } catch(Exception e) {
            System.err.println("EERRRRRRRRR'R");
            System.err.println(e);
        }
    }
}
