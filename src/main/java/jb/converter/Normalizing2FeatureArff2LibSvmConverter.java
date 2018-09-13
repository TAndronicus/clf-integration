package jb.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Normalizing2FeatureArff2LibSvmConverter {

    private static String pathToResources = "src/main/resources";
    private static int col1 = 3;
    private static int col2 = 4;

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
            double[] average = new double[2];
            double[] square = new double[2];
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                String[] values = line.split(",");
                int loopCounter = 0;
                for (int i = 0; i < values.length - 1; i++) {
                    if (i != col1 && i != col2) {
                        continue;
                    }
                    average[loopCounter] += Double.valueOf(values[i]);
                    square[loopCounter] += Math.pow(Double.valueOf(values[i]), 2);
                    loopCounter++;
                }
                valuesCounter++;
            }
            for (int i = 0; i < average.length; i++) {
                average[i] = average[i] / valuesCounter;
                square[i] = Math.sqrt(square[i] / valuesCounter);
            }
            int counter = 0;
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                StringBuilder newline = new StringBuilder();
                String[] values = line.split(",");
                newline.append(values[values.length - 1]);
                int loopCounter = 0;
                for (int i = 0; i < values.length - 1; i++) {
                    if (i != col1 && i != col2) {
                        continue;
                    }
                    newline.append(" " + (loopCounter + 1) + ":" + ((Double.valueOf(values[loopCounter]) - average[loopCounter]) / square[loopCounter]));
                    loopCounter++;
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
