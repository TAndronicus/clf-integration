package jb.files;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Problem;
import jb.config.Opts;
import jb.data.Dataset;
import jb.data.clfobj.ClfObject;
import jb.data.clfobj.ClfObjectDoubleSorted;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BReader implements FileHelper {
    @Override
    public Dataset readFile(Opts opts) throws IOException, InvalidInputDataException {
        String path = opts.getFilePath();
        Problem rootProblem0 = Problem.readFromFile(new File(path), opts.getBias());
        BClfObject[] objects0 = getClfObjects(rootProblem0);

        BReader.ExtremeValues extremeValues = (new BReader.ExtremeValues(objects0)).invoke();
        double xMin = extremeValues.getXMin();
        double xMax = extremeValues.getXMax();

        List<List<ClfObject>> clfObjectsTables = prepareLists();
        for (BClfObject bClfObject : objects0) {
            clfObjectsTables.get(bClfObject.wt == 0 ? bClfObject.zb - 1 : 10).add(new ClfObjectDoubleSorted(bClfObject.getX(), bClfObject.getY()));
        }
        List<jb.data.Problem> problems = new ArrayList<>();
        for (List<ClfObject> objectList : clfObjectsTables) {
            Collections.sort(objectList);
            double[][] x = new double[objectList.size()][2];
            int[] y = new int[objectList.size()];
            for (int i = 0; i < objectList.size(); i++) {
                x[i] = objectList.get(i).getX();
                y[i] = objectList.get(i).getY();
            }
            problems.add(new jb.data.Problem(x, y));
        }

        return new Dataset(problems, xMin, xMax);
    }

    private List<List<ClfObject>> prepareLists() {
        List<List<ClfObject>> lists = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            lists.add(new ArrayList<>());
        }
        return lists;
    }

    private BClfObject[] getClfObjects(Problem problem) {
        BClfObject[] result = new BClfObject[problem.l];
        for (int i = 0; i < problem.l; i++) {
            double[] x = new double[problem.n - 2];
            int counter = 0;
            for (int j = 0; j < problem.n; j++) {
                if (j == problem.n - 3 || j == problem.n - 2) continue;
                x[counter] = problem.x[i][j].getValue();
                counter++;
            }
            result[i] = new BClfObject(x, (int) problem.y[i], (int) problem.x[i][problem.n - 3].getValue(), (int) problem.x[i][problem.n - 2].getValue());
        }
        return result;
    }

    @Data
    @RequiredArgsConstructor
    private class ExtremeValues {
        @NonNull
        private ClfObjectDoubleSorted[] clfObjectsDoubleSorted;
        private double xMin;
        private double xMax;

        public BReader.ExtremeValues invoke() {
            Arrays.sort(clfObjectsDoubleSorted);
            int classChangeIndex = 0;
            while (clfObjectsDoubleSorted[classChangeIndex].getY() == 0) {
                classChangeIndex++;
            }
            xMin = Math.min(clfObjectsDoubleSorted[0].getX()[0], clfObjectsDoubleSorted[classChangeIndex].getX()[0]);
            xMax = Math.max(clfObjectsDoubleSorted[clfObjectsDoubleSorted.length - 1].getX()[0], clfObjectsDoubleSorted[classChangeIndex - 1].getX()[0]);
            return this;
        }
    }

    private class BClfObject extends ClfObjectDoubleSorted {

        /**
         * Index of 10x validation
         */
        private int zb;
        /**
         * <code>0</code> for training, <code>1</code> for testing
         */
        private int wt;

        public BClfObject(double[] x, int y, int zb, int wt) {
            this.x = x;
            this.y = y;
            this.zb = zb;
            this.wt = wt;
        }
    }

}
