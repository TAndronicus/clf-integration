package jb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Dataset {

    private List<Problem> problems;
    private double minX;
    private double maxX;

}
