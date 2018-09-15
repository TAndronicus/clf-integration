package jb.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Normalizing2FeatureArff2LibSvmConverter {

    public static void main(String[] args) {
        String pathToResources = "src/main/resources";
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
        int valuesCounter = 0;
        double[] average = null;
        double[] square = null;
        int[] columns;
        for (String line : lines) {
            if (line.startsWith("@") || line.startsWith("%")) {
                continue;
            }
            String[] values = line.split(",");
            if (average == null) {
                average = new double[values.length - 1];
                square = new double[values.length - 1];
            }
            int loopCounter = 0;
            for (int i = 0; i < values.length - 1; i++) {
                average[loopCounter] += Double.valueOf(values[i].trim());
                square[loopCounter] += Math.pow(Double.valueOf(values[i].trim()), 2);
                loopCounter++;
            }
            valuesCounter++;
        }
        for (int i = 0; i < average.length; i++) {
            average[i] = average[i] / valuesCounter;
            square[i] = Math.sqrt(square[i] / valuesCounter);
        }
        columns = selectFeatures(square);
        try (PrintWriter printWriter = new PrintWriter(new File(file.getPath().split("[.]")[0] + "_" + columns[0] + "_" +
                columns[1] + "_converted." + file.getPath().split("[.]")[1]))) {
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
                    if (i != columns[0] && i != columns[1]) {
                        continue;
                    }
                    newline.append(" " + (loopCounter + 1) + ":" + ((Double.valueOf(values[loopCounter].trim()) - average[loopCounter]) / square[loopCounter]));
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

    private static int[] selectFeatures(double[] square) {
        double[] copyOfSquare = Arrays.copyOf(square, square.length);
        Arrays.sort(copyOfSquare);
        int[] columns = new int[2];
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                if (Arrays.binarySearch(copyOfSquare, square[i]) == j) {
                    columns[j] = i;
                }
            }
        }
        return columns;
    }

}
