package jb.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NormalizingArff2LibSvmConverter {

    private static String pathToResources = "src/main/resources";

    public static void main(String[] args) {
        File rootCatalog = new File(pathToResources);
        for (File file : rootCatalog.listFiles()) {
            try {
                convertFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void convertFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        try (PrintWriter printWriter = new PrintWriter(new File(file.getPath().split("[.]")[0] + "_converted." + file.getPath().split("[.]")[1]))) {
            int valuesCounter = 0;
            Double[] average = null;
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                String[] values = line.split(",");
                if (average == null) {
                    average = new Double[values.length - 1];
                }
                for (int i = 0; i < values.length - 1; i++) {
                    average[i] += Double.valueOf(values[i]);
                }
                valuesCounter++;
            }
            for (int i = 0; i < average.length; i++) {
                average[i] = average[i] / valuesCounter;
            }
            int counter = 0;
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                StringBuilder newline = new StringBuilder();
                String[] values = line.split(",");
                newline.append(values[values.length - 1]);
                for (int i = 0; i < values.length - 1; i++) {
                    newline.append(" " + (i + 1) + ":" + (Double.valueOf(values[i]) / average[i]));
                }
                printWriter.println(newline.toString());
                counter++;
                if (counter > 100) {
                    printWriter.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
