package jb.files.serialization;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static jb.config.Constants.modelsPath;

public class ModelReader {

    public List<Model> read(Opts opts) {
        File modelsDir = new File(modelsPath);
        List<Model> clfs = new ArrayList<>();
        for (File file : modelsDir.listFiles()) {
            if (isRightData(opts, file) && hasRightAmountOfBaseClfs(opts, file) && !inPermutation(Integer.valueOf(file.getName().split("_")[1]), opts.getPermutation())) {
                try {
                    clfs.add(Linear.loadModel(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return clfs;
    }

    private boolean inPermutation(Integer numberOfSubset, int[] permutation) {
        return numberOfSubset.equals(permutation[0]) || numberOfSubset.equals(permutation[1]);
    }

    private boolean hasRightAmountOfBaseClfs(Opts opts, File file) {
        return Integer.valueOf(file.getName().split("_")[file.getName().split("_").length - 1]).equals(opts.getNumberOfBaseClassifiers() + 2);
    }

    private boolean isRightData(Opts opts, File file) {
        return file.getName().startsWith(opts.getFilename().substring(0, 3));
    }

}
