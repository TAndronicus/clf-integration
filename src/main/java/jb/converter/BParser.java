package jb.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jb.config.Constants.convertedSourcesPath;
import static jb.config.Constants.normalize;
import static jb.util.BUtils.getAllPermutations;

public class BParser {

    public static void main(String[] args) {
        String pathToSources = "src/main/resources/source";
        File rootCatalog = new File(pathToSources);
        List<int[]> permutations = getAllPermutations();
        for (File file : rootCatalog.listFiles()) {
            if (file.isDirectory()) continue;
            try {
                for (int[] permutation : permutations) convertFile(file, permutation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void convertFile(File file, int[] columns) throws IOException {
        createDirIfNone();
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        DatasetInfo datasetInfo = new DatasetInfo(lines).invoke();
        double[] average = datasetInfo.getAverage();
        double[] square = datasetInfo.getSquare();
        //int[] columns = selectFeatures(square);
        try (PrintWriter printWriter = new PrintWriter(new File(generatePath(file, columns)))) {
            int counter = 0;
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                String[] values = line.split(",");
                StringBuilder newline = appendLabel(values[values.length - 3]);
                int loopCounter = 0;
                for (int i = 0; i < values.length - 3; i++) {
                    if (i != columns[0] && i != columns[1]) {
                        continue;
                    }
                    if (normalize) {
                        newline.append(" " + (loopCounter + 1) + ":" + ((Double.valueOf(values[i].trim()) - average[i]) / square[i]));
                    } else {
                        newline.append(" " + (loopCounter + 1) + ":" + Double.valueOf(values[i].trim()));
                    }
                    loopCounter++;
                }
                for (int i = 0; i < 2; i++) {
                    newline.append(" " + (i + 3) + ":" + values[values.length - 2 + i]);
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

    private static StringBuilder appendLabel(String value) {
        StringBuilder newline = new StringBuilder();
        newline.append(value);
        return newline;
    }

    private static String generatePath(File file, int[] columns) {
        return convertedSourcesPath + "//" + file.getName().split("[.]")[0] + "_" + columns[0] + "_" +
                columns[1] + "_converted." + file.getPath().split("[.]")[1];
    }

    private static void createDirIfNone() {
        File root = new File(convertedSourcesPath);
        if (!root.exists()) root.mkdir();
    }

    private static int[] selectFeatures(double[] square) {
        double[] copyOfSquare = Arrays.copyOf(square, square.length);
        Arrays.sort(copyOfSquare);
        int[] columns = new int[2];
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                if (Arrays.binarySearch(copyOfSquare, square[i]) == square.length - 1 - j) {
                    columns[j] = i;
                }
            }
        }
        return columns;
    }

    private static class DatasetInfo {
        private List<String> lines;
        private double[] average;
        private double[] square;

        public DatasetInfo(List<String> lines) {
            this.lines = lines;
        }

        public double[] getAverage() {
            return average;
        }

        public double[] getSquare() {
            return square;
        }

        public DatasetInfo invoke() {
            int valuesCounter = 0;
            average = null;
            square = null;
            for (String line : lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue;
                }
                String[] values = line.split(",");
                if (average == null) {
                    average = new double[values.length - 3];
                    square = new double[values.length - 3];
                }
                for (int i = 0; i < values.length - 3; i++) {
                    average[i] += Double.valueOf(values[i].trim());
                    square[i] += Math.pow(Double.valueOf(values[i].trim()), 2);
                }
                valuesCounter++;
            }
            for (int i = 0; i < average.length; i++) {
                average[i] = average[i] / valuesCounter;
                square[i] = Math.sqrt(square[i] / valuesCounter);
            }
            return this;
        }
    }
}
