package jb.files.serialization;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import jb.config.Opts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static jb.config.Constants.modelsPath;

public class ModelWriter {

    public void saveModels(List<Model> clfs, Opts opts) {

        for (int i = 0; i < clfs.size(); i++) {
            try {
                Linear.saveModel(new File(modelsPath + "/" + opts.getFilename().substring(0, 3) + "_" + i + "_on_" + clfs.size()), clfs.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
