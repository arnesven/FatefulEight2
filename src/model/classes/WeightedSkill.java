package model.classes;

import java.io.Serializable;

public class WeightedSkill implements Serializable {

    public static final int[][] RANK_MATRIX = new int[][]{
            new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3}, // Weight 1
            new int[]{1, 1, 1, 2, 2, 3, 3, 3, 4}, // Weight 2
            new int[]{2, 2, 3, 3, 3, 4, 4, 5, 5}, // Weight 3
            new int[]{2, 3, 3, 4, 4, 5, 5, 5, 6}, // Weight 4
            new int[]{2, 3, 4, 5, 5, 6, 6, 6, 6}, // Weight 5
            new int[]{3, 4, 5, 5, 6, 7, 7, 7, 7}  // Weight 6
            // level  1  2  3  4  5  6  7  8  9
    };
    public static final int MAX_LEVEL = 9;
    public static final int MAX_WEIGHT = 6;

    private final Skill skill;
    private final int weight;

    public WeightedSkill(Skill skill, int weight) {
        this.skill = skill;
        this.weight = weight;
    }

    public static int getRankForWeight(int weight, int level) {
        return RANK_MATRIX[weight][level];
    }

    public int getRank(int level) {
        if (weight == 0 || level == 0) {
            return 0;
        }
        if (level > RANK_MATRIX[0].length) {
            level = RANK_MATRIX[0].length;
        }
        return RANK_MATRIX[weight-1][level-1];
    }

    public int getWeight() {
        return weight;
    }

    public double getBalancingScore() {
        return weight;
    }

    public Skill getSkill() {
        return skill;
    }

    public String asString() {
        return weight + "";
    }

//    public static final int[][] RANK_MATRIX = new int[][]{
//                      0, 0, 0, 1, 1, 1, 2, 2, 3}, // Weight 1-
//            new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3}, // Weight 1
//                      0, 1, 1, 1, 2, 2, 3, 3, 3}, // Weight 1+
//                      1, 1, 1, 1, 2, 2, 3, 3, 3}, // Weight 2-
//            new int[]{1, 1, 1, 2, 2, 3, 3, 3, 4}, // Weight 2
//                      1, 1, 2, 2, 3, 3, 3, 4, 4}, // Weight 2+
//                      2, 2, 2, 3, 3, 3, 4, 4, 5}, // Weight 3-
//            new int[]{2, 2, 3, 3, 3, 4, 4, 5, 5}, // Weight 3
//                      2, 3, 3, 3, 4, 4, 5, 5, 5}, // Weight 3+
//            new int[]{2, 3, 3, 4, 4, 5, 5, 5, 6}, // Weight 4
//                      3, 3, 4, 4, 5, 5, 5, 6, 6}, // Weight 4+
//                      2, 2, 3, 4, 5, 5, 6, 6, 6}, // Weight 5-
//            new int[]{2, 3, 4, 5, 5, 6, 6, 6, 6}, // Weight 5
//                      3, 4, 5, 5, 6, 6, 6, 6, 6}, // Weight 5+
//            new int[]{3, 4, 5, 5, 6, 7, 7, 7, 7}, // Weight 6
//                      4, 5, 5, 6, 7, 7, 7, 7, 7}  // Weight 6+
//            // level  1  2  3  4  5  6  7  8  9
//    };


/*
    private static final int[][] LINEAR_RANK_MATRIX = new int[][]{
            new int[]{0, 1, 1, 1, 2, 2, 2, 3, 3}, // Weight 1
            new int[]{1, 1, 2, 2, 2, 3, 3, 3, 4}, // Weight 2
            new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5}, // Weight 3
            new int[]{2, 2, 3, 3, 4, 4, 5, 5, 6}, // Weight 4
            new int[]{2, 3, 3, 4, 4, 5, 5, 6, 6}, // Weight 5
            new int[]{3, 3, 4, 4, 5, 5, 6, 6, 7}  // Weight 6
            // level  1  2  3  4  5  6  7  8  9
    };

    private static final int[][] POLYNOMIAL_RANK_MATRIX = new int[][]{
            new int[]{0, 1, 1, 1, 1, 2, 2, 3, 4}, // Weight 1
            new int[]{1, 1, 1, 1, 2, 2, 3, 4, 5}, // Weight 2
            new int[]{1, 1, 1, 2, 2, 2, 3, 4, 6}, // Weight 3
            new int[]{1, 1, 2, 2, 3, 3, 4, 5, 6}, // Weight 4
            new int[]{1, 1, 2, 3, 3, 4, 5, 7, 8}, // Weight 5
            new int[]{1, 2, 2, 3, 3, 4, 6, 7, 9}  // Weight 6
            // level  1  2  3  4  5  6  7  8  9
    };

   private static final int[][] LOGARITHMIC_RANK_MATRIX = new int[][]{
           new int[]{0, 1, 2, 3, 3, 3, 3, 3, 4}, // Weight 1
           new int[]{1, 2, 3, 3, 3, 3, 4, 4, 5}, // Weight 2
           new int[]{1, 2, 3, 3, 4, 4, 5, 5, 5}, // Weight 3
           new int[]{1, 2, 3, 4, 5, 5, 6, 6, 6}, // Weight 4
           new int[]{2, 3, 4, 5, 5, 6, 6, 6, 7}, // Weight 5
           new int[]{3, 4, 5, 5, 6, 7, 7, 7, 7}  // Weight 6
           // level  1  2  3  4  5  6  7  8  9
   };
*/
}
