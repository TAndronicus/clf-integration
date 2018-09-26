package jb.util;

import java.util.ArrayList;
import java.util.List;

public class BUtils {

    public static List<int[]> getAllPermutations() {
        List<int[]> permutations = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = i + 1; j < 8; j++) {
                permutations.add(new int[]{i, j});
            }
        }
        return permutations;
    }
}
