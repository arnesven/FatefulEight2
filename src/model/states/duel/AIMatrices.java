package model.states.duel;

import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class AIMatrices {
    private static final int NO_OF_OPPONENT_STRENGTHS = 2;
    private static final int NO_OF_STRENGTHS = 4;
    private static final int NO_OF_SHIFTS = 5;
    private static final int NO_OF_BASIC_ACTIONS = 3;
    private static final int NO_OF_SHIELDS = 4;
    private static final int NO_OF_ATTACKS = 2;
    private static final int NO_OF_BEAM_OPTIONS = 3;
    private static final double[] BEAM_BASELINE = new double[]{0.333, 0.333, 0.333};
    private static final double[] SHIELD_BASELINE = new double[]{0.25, 0.25, 0.25, 0.25};
    private static final double[] ATTACK_BASELINE = new double[]{0.5, 0.5};
    private static final double[] NORMAL_BASELINE = new double[]{0.333, 0.333, 0.333};

    private final double[][][] normalTables;
    private final double[][][] attackTables;
    private final double[][][] shieldTables;
    private final double[][][] beamTables;

    private final double[][][] convertedNormal;
    private final double[][][] convertedAttack;
    private final double[][][] convertedShield;
    private final double[][][] convertedBeams;

    private AIMatrices(double[][][] normal, double[][][] attack, double[][][] shield, double[][][] beams) {
        this.normalTables = normal;
        this.attackTables = attack;
        this.shieldTables = shield;
        this.beamTables = beams;

        this.convertedNormal = convert3D(normalTables);
        this.convertedAttack = convert3D(attackTables);
        this.convertedShield = convert3D(shieldTables);
        this.convertedBeams = convert3D(beamTables);
    }

    private static double[][][] makeRandomMatrix(int layers, int rows, int columns) {
        double[][][] result = new double[layers][][];
        for (int i = 0; i < result.length; ++i) {
            result[i] = makeRandomMatrix(rows, columns);
        }
        return result;
    }

    private static double[][] makeRandomMatrix(int rows, int columns) {
        double[][] result = new double[rows][columns];
        for (int y = 0; y < result[0].length; ++y) {
            for (int x = 0; x < result.length; ++x) {
                result[x][y] = MyRandom.nextDouble();
            }
        }
        return result;
    }

    public static double[][][] convert3D(double[][][] table) {
        for (int row = 0; row < table.length; ++row) {
            table[row] = convert2D(table[row]);
        }
        return table;
    }

    public static double[][] convert2D(double[][] table) {
        for (int row = 0; row < table.length; ++row) {
            table[row] = convert1D(table[row]);
        }
        return table;
    }

    private static double[] convert1D(double[] doubles) {
        double[] result = new double[doubles.length];
        double previous = 0.0;
        for (int i = 0; i < result.length; ++i) { // Prefix sums
            result[i] = doubles[i] + previous;
            previous = result[i];
        }
        for (int i = 0; i < result.length; ++i) { // Normalizing
            result[i] = doubles[i] / previous;
        }
        return result;
    }

    public static AIMatrices makeBaselineMatrix() {
        return new AIMatrices(
                makeBaselineNormalMatrix(),
                makeBaselineAttackMatrix(),
                makeBaselineShieldMatrix(),
                makeBaselineBeamMatrix());
    }

    private static double[][][] make3dMatrix(int layers, int rows, int columns, double[] template) {
        double[][][] result = new double[layers][rows][columns];
        for (int layer = 0; layer < layers; ++layer) {
            for (int row = 0; row < rows; ++row) {
                System.arraycopy(template, 0, result[layer][row], 0, columns);
            }
        }
        return result;
    }


    private static double[][][] makeBaselineBeamMatrix() {
        return make3dMatrix(NO_OF_SHIFTS, NO_OF_STRENGTHS, NO_OF_BEAM_OPTIONS,
                BEAM_BASELINE);
    }

    private static double[][][] makeBaselineShieldMatrix() {
        return make3dMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS, NO_OF_SHIELDS,
                SHIELD_BASELINE);
    }

    private static double[][][] makeBaselineAttackMatrix() {
        return make3dMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS-1, NO_OF_ATTACKS,
                ATTACK_BASELINE);
    }

    private static double[][][] makeBaselineNormalMatrix() {
        return make3dMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS, NO_OF_BASIC_ACTIONS,
                NORMAL_BASELINE);
    }

    public static AIMatrices makeRandomMatrix() {
        return new AIMatrices(makeRandomMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS, NO_OF_BASIC_ACTIONS),
                            makeRandomMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS-1, NO_OF_ATTACKS),
                            makeRandomMatrix(NO_OF_OPPONENT_STRENGTHS, NO_OF_STRENGTHS, NO_OF_SHIELDS),
                            makeRandomMatrix(NO_OF_SHIFTS, NO_OF_STRENGTHS, NO_OF_BEAM_OPTIONS));
    }

    public static List<AIMatrices> makeChildren(AIMatrices m1, AIMatrices m2) {
        List<AIMatrices> result = new ArrayList<>();
        result.add(new AIMatrices(m1.normalTables, m1.attackTables, m1.shieldTables, m1.beamTables)); // TODO: Copy these

        result.add(new AIMatrices(m2.normalTables, m1.attackTables, m1.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m2.attackTables, m1.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m1.attackTables, m2.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m1.attackTables, m1.shieldTables, m2.beamTables));

        result.add(new AIMatrices(m2.normalTables, m2.attackTables, m1.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m2.attackTables, m2.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m1.attackTables, m2.shieldTables, m2.beamTables));

        result.add(new AIMatrices(m2.normalTables, m1.attackTables, m2.shieldTables, m1.beamTables));
        result.add(new AIMatrices(m1.normalTables, m2.attackTables, m1.shieldTables, m2.beamTables));
        result.add(new AIMatrices(m2.normalTables, m1.attackTables, m1.shieldTables, m2.beamTables));

        result.add(new AIMatrices(m1.normalTables, m2.attackTables, m2.shieldTables, m2.beamTables));
        result.add(new AIMatrices(m2.normalTables, m1.attackTables, m2.shieldTables, m2.beamTables));
        result.add(new AIMatrices(m2.normalTables, m2.attackTables, m1.shieldTables, m2.beamTables));
        result.add(new AIMatrices(m2.normalTables, m2.attackTables, m2.shieldTables, m1.beamTables));

        result.add(new AIMatrices(m2.normalTables, m2.attackTables, m2.shieldTables, m2.beamTables));
        return result;
    }

    public void printYourself() {
        System.out.print("double[][][] normalTables = ");
        printMatrix3D(normalTables);
        System.out.println(";");
        System.out.print("double[][][] attackTables = ");
        printMatrix3D(attackTables);
        System.out.println(";");
        System.out.print("double[][][] shieldTables = ");
        printMatrix3D(shieldTables);
        System.out.println(";");
        System.out.print("double[][][] beamTables = ");
        printMatrix3D(beamTables);
        System.out.println(";");
    }

    private void printMatrix3D(double[][][] beamTables) {
        System.out.println("new double[][][]{");
        for (double[][] beamTable : beamTables) {
            printMatrix2D(beamTable);
            System.out.println(",");
        }
        System.out.print("}");
    }

    private void printMatrix2D(double[][] normalTables) {
        System.out.println("new double[][]{");
        for (double[] normalTable : normalTables) {
            System.out.print("new double[]{");
            printArray(normalTable);
            System.out.println("}, ");
        }
        System.out.print("}");
    }

    private void printArray(double[] normalTable) {
        for (int col = 0; col < normalTable.length; ++col) {
            System.out.print(String.format("%1.2f", normalTable[col]) + (col == normalTable.length-1? "": ", "));
        }
    }


    public static AIMatrices level6WizardWithBGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.57, 0.17, 0.25},
                        new double[]{0.11, 0.36, 0.53},
                        new double[]{0.30, 0.22, 0.48},
                        new double[]{0.56, 0.16, 0.28},
                },
                new double[][]{
                        new double[]{0.12, 0.55, 0.33},
                        new double[]{0.91, 0.05, 0.03},
                        new double[]{0.39, 0.28, 0.33},
                        new double[]{0.38, 0.39, 0.23},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.66, 0.34},
                        new double[]{0.59, 0.41},
                        new double[]{0.39, 0.61},
                },
                new double[][]{
                        new double[]{0.31, 0.69},
                        new double[]{0.17, 0.83},
                        new double[]{0.02, 0.98},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.73, 0.23, 0.00, 0.03},
                        new double[]{0.09, 0.31, 0.43, 0.17},
                        new double[]{0.05, 0.15, 0.24, 0.56},
                        new double[]{0.59, 0.26, 0.09, 0.06},
                },
                new double[][]{
                        new double[]{0.16, 0.10, 0.25, 0.49},
                        new double[]{0.13, 0.30, 0.47, 0.10},
                        new double[]{0.10, 0.31, 0.33, 0.27},
                        new double[]{0.31, 0.35, 0.06, 0.28},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.01, 0.66, 0.33},
                        new double[]{0.48, 0.22, 0.30},
                        new double[]{0.11, 0.01, 0.89},
                        new double[]{0.43, 0.19, 0.39},
                },
                new double[][]{
                        new double[]{0.28, 0.38, 0.34},
                        new double[]{0.41, 0.21, 0.38},
                        new double[]{0.09, 0.45, 0.46},
                        new double[]{0.83, 0.09, 0.09},
                },
                new double[][]{
                        new double[]{0.16, 0.49, 0.36},
                        new double[]{0.31, 0.22, 0.47},
                        new double[]{0.45, 0.46, 0.09},
                        new double[]{0.52, 0.19, 0.29},
                },
                new double[][]{
                        new double[]{0.46, 0.03, 0.51},
                        new double[]{0.11, 0.56, 0.33},
                        new double[]{0.35, 0.25, 0.39},
                        new double[]{0.33, 0.35, 0.32},
                },
                new double[][]{
                        new double[]{0.37, 0.45, 0.18},
                        new double[]{0.33, 0.23, 0.43},
                        new double[]{0.41, 0.14, 0.44},
                        new double[]{0.41, 0.36, 0.23},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    private int rollOnTable(double[] table) {
        double roll = MyRandom.nextDouble();
        for (int i = 0; i < table.length; ++i) {
            if (roll < table[i]) {
                return i;
            }
        }
        return table.length - 1;
    }

    public int rollOnAttackTypeTable(int opponentStrength, int ownStrength) {
        opponentStrength = checkAndCap(opponentStrength, convertedAttack.length, "Opponent's strength");
        ownStrength = checkAndCap(ownStrength, convertedAttack[0].length, "Own strength");
        return rollOnTable(convertedAttack[opponentStrength][ownStrength]);
    }

    public int rollOnNormalTable(int opponentPowerStrength, int ownStrength) {
        opponentPowerStrength = checkAndCap(opponentPowerStrength, convertedNormal.length, "Opponent's strength");
        ownStrength = checkAndCap(ownStrength, convertedNormal[0].length, "Own strength");
        return rollOnTable(convertedNormal[opponentPowerStrength][ownStrength]);
    }

    public int rollOnShieldTable(int opponentPowerStrength, int ownStrength) {
        opponentPowerStrength = checkAndCap(opponentPowerStrength, convertedShield.length, "Opponent's strength");
        ownStrength = checkAndCap(ownStrength, convertedShield[0].length, "Own strength");
        return rollOnTable(convertedShield[opponentPowerStrength][ownStrength]);
    }

    public int rollOnBeamTable(int shift, int ownStrength) {
        shift = checkAndCap(shift, convertedBeams.length, "Shift");
        ownStrength = checkAndCap(ownStrength, convertedBeams[0].length, "Own Strength");
        return rollOnTable(convertedBeams[shift][ownStrength]);
    }

    private int checkAndCap(int x, int length, String label) {
        if (x >= length) {
            System.err.println(label + " larger than expected: " + x +
                    ", capping it at " + (length - 1));
            x = length - 1;
        }
        return x;
    }
}


