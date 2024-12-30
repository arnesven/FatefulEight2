package model.states.duel;

import java.util.List;

public class AIMatricesPresets {

    public static List<AIMatrices> getOldMatrices() {
        return List.of(AIMatrices.makeBaselineMatrix(),
                level6WizardWithBGauge(),
                mutatedLevel6BGauge(),
                mutatedLevel6AGauge(),
                mutatedLevel6VGauge(),
                mutatedLevel6CGauge(),
                mutatedLevel6SGauge(),
                mutatedLevel6KGauge(),
                mutatedLevel6TGauge());
    }

    public static AIMatrices mutatedLevel6TGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.66, 0.17, 0.17},
                        new double[]{0.86, 0.09, 0.05},
                        new double[]{0.11, 0.44, 0.45},
                        new double[]{0.63, 0.33, 0.04},
                },
                new double[][]{
                        new double[]{0.93, 0.03, 0.03},
                        new double[]{0.92, 0.07, 0.01},
                        new double[]{0.30, 0.43, 0.28},
                        new double[]{0.46, 0.10, 0.44},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.26, 0.74},
                        new double[]{0.91, 0.09},
                        new double[]{0.75, 0.25},
                },
                new double[][]{
                        new double[]{0.45, 0.55},
                        new double[]{0.32, 0.68},
                        new double[]{0.47, 0.53},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.15, 0.03, 0.67, 0.16},
                        new double[]{0.10, 0.33, 0.37, 0.20},
                        new double[]{0.10, 0.39, 0.19, 0.31},
                        new double[]{0.07, 0.74, 0.02, 0.18},
                },
                new double[][]{
                        new double[]{0.26, 0.38, 0.26, 0.11},
                        new double[]{0.44, 0.11, 0.09, 0.36},
                        new double[]{0.42, 0.01, 0.50, 0.07},
                        new double[]{0.28, 0.28, 0.14, 0.30},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.52, 0.19},
                        new double[]{0.15, 0.33, 0.52},
                        new double[]{0.67, 0.17, 0.16},
                        new double[]{0.07, 0.58, 0.35},
                },
                new double[][]{
                        new double[]{0.54, 0.32, 0.14},
                        new double[]{0.01, 0.31, 0.68},
                        new double[]{0.42, 0.08, 0.50},
                        new double[]{0.33, 0.41, 0.26},
                },
                new double[][]{
                        new double[]{0.28, 0.25, 0.47},
                        new double[]{0.39, 0.24, 0.37},
                        new double[]{0.43, 0.53, 0.04},
                        new double[]{0.54, 0.11, 0.35},
                },
                new double[][]{
                        new double[]{0.53, 0.31, 0.16},
                        new double[]{0.20, 0.32, 0.49},
                        new double[]{0.10, 0.42, 0.48},
                        new double[]{0.33, 0.44, 0.23},
                },
                new double[][]{
                        new double[]{0.41, 0.53, 0.06},
                        new double[]{0.45, 0.15, 0.40},
                        new double[]{0.13, 0.45, 0.42},
                        new double[]{0.04, 0.11, 0.85},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel6KGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.47, 0.26, 0.27},
                        new double[]{0.48, 0.49, 0.03},
                        new double[]{0.11, 0.44, 0.45},
                        new double[]{0.73, 0.20, 0.07},
                },
                new double[][]{
                        new double[]{0.95, 0.02, 0.03},
                        new double[]{0.92, 0.07, 0.01},
                        new double[]{0.51, 0.01, 0.47},
                        new double[]{0.48, 0.10, 0.42},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.38, 0.62},
                        new double[]{0.20, 0.80},
                        new double[]{0.75, 0.25},
                },
                new double[][]{
                        new double[]{0.21, 0.79},
                        new double[]{0.38, 0.62},
                        new double[]{0.67, 0.33},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.18, 0.34, 0.17, 0.31},
                        new double[]{0.16, 0.36, 0.36, 0.11},
                        new double[]{0.34, 0.11, 0.45, 0.10},
                        new double[]{0.13, 0.56, 0.15, 0.16},
                },
                new double[][]{
                        new double[]{0.02, 0.12, 0.75, 0.11},
                        new double[]{0.29, 0.24, 0.28, 0.19},
                        new double[]{0.03, 0.35, 0.46, 0.16},
                        new double[]{0.31, 0.06, 0.12, 0.51},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.22, 0.53, 0.25},
                        new double[]{0.19, 0.32, 0.50},
                        new double[]{0.39, 0.30, 0.31},
                        new double[]{0.30, 0.67, 0.02},
                },
                new double[][]{
                        new double[]{0.35, 0.40, 0.26},
                        new double[]{0.42, 0.01, 0.56},
                        new double[]{0.61, 0.24, 0.15},
                        new double[]{0.32, 0.34, 0.34},
                },
                new double[][]{
                        new double[]{0.28, 0.30, 0.42},
                        new double[]{0.41, 0.14, 0.45},
                        new double[]{0.22, 0.36, 0.42},
                        new double[]{0.37, 0.58, 0.05},
                },
                new double[][]{
                        new double[]{0.66, 0.17, 0.17},
                        new double[]{0.42, 0.09, 0.49},
                        new double[]{0.35, 0.39, 0.26},
                        new double[]{0.32, 0.22, 0.45},
                },
                new double[][]{
                        new double[]{0.44, 0.34, 0.23},
                        new double[]{0.39, 0.54, 0.08},
                        new double[]{0.09, 0.62, 0.29},
                        new double[]{0.22, 0.53, 0.26},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel6SGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.00, 0.72, 0.28},
                        new double[]{0.22, 0.14, 0.64},
                        new double[]{0.21, 0.55, 0.24},
                        new double[]{0.60, 0.09, 0.31},
                },
                new double[][]{
                        new double[]{0.01, 0.97, 0.02},
                        new double[]{0.24, 0.18, 0.58},
                        new double[]{0.26, 0.04, 0.70},
                        new double[]{0.52, 0.25, 0.23},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.43, 0.57},
                        new double[]{0.26, 0.74},
                        new double[]{0.31, 0.69},
                },
                new double[][]{
                        new double[]{0.30, 0.70},
                        new double[]{0.20, 0.80},
                        new double[]{0.39, 0.61},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.06, 0.09, 0.44, 0.41},
                        new double[]{0.21, 0.47, 0.21, 0.11},
                        new double[]{0.05, 0.54, 0.27, 0.14},
                        new double[]{0.34, 0.07, 0.32, 0.27},
                },
                new double[][]{
                        new double[]{0.13, 0.27, 0.21, 0.39},
                        new double[]{0.18, 0.12, 0.25, 0.45},
                        new double[]{0.31, 0.53, 0.03, 0.13},
                        new double[]{0.38, 0.44, 0.07, 0.10},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.03, 0.47, 0.50},
                        new double[]{0.34, 0.09, 0.57},
                        new double[]{0.19, 0.35, 0.45},
                        new double[]{0.05, 0.40, 0.55},
                },
                new double[][]{
                        new double[]{0.33, 0.41, 0.26},
                        new double[]{0.20, 0.66, 0.14},
                        new double[]{0.37, 0.34, 0.29},
                        new double[]{0.30, 0.16, 0.53},
                },
                new double[][]{
                        new double[]{0.27, 0.10, 0.63},
                        new double[]{0.37, 0.25, 0.38},
                        new double[]{0.14, 0.07, 0.79},
                        new double[]{0.16, 0.73, 0.11},
                },
                new double[][]{
                        new double[]{0.36, 0.21, 0.43},
                        new double[]{0.42, 0.18, 0.39},
                        new double[]{0.42, 0.20, 0.38},
                        new double[]{0.15, 0.33, 0.52},
                },
                new double[][]{
                        new double[]{0.00, 0.46, 0.54},
                        new double[]{0.31, 0.26, 0.43},
                        new double[]{0.39, 0.14, 0.47},
                        new double[]{0.44, 0.21, 0.35},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel6CGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.76, 0.18, 0.05},
                        new double[]{0.56, 0.16, 0.27},
                        new double[]{0.06, 0.29, 0.65},
                        new double[]{0.29, 0.32, 0.39},
                },
                new double[][]{
                        new double[]{0.87, 0.06, 0.07},
                        new double[]{0.59, 0.26, 0.15},
                        new double[]{0.36, 0.38, 0.25},
                        new double[]{0.05, 0.83, 0.13},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.10, 0.90},
                        new double[]{0.51, 0.49},
                        new double[]{0.34, 0.66},
                },
                new double[][]{
                        new double[]{0.66, 0.34},
                        new double[]{0.57, 0.43},
                        new double[]{0.41, 0.59},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.34, 0.36, 0.14, 0.16},
                        new double[]{0.27, 0.02, 0.26, 0.45},
                        new double[]{0.13, 0.75, 0.01, 0.11},
                        new double[]{0.01, 0.15, 0.58, 0.26},
                },
                new double[][]{
                        new double[]{0.00, 0.31, 0.27, 0.42},
                        new double[]{0.14, 0.02, 0.30, 0.54},
                        new double[]{0.21, 0.13, 0.21, 0.45},
                        new double[]{0.26, 0.35, 0.13, 0.25},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.24, 0.26, 0.50},
                        new double[]{0.27, 0.58, 0.15},
                        new double[]{0.37, 0.52, 0.11},
                        new double[]{0.22, 0.43, 0.35},
                },
                new double[][]{
                        new double[]{0.34, 0.37, 0.30},
                        new double[]{0.12, 0.41, 0.47},
                        new double[]{0.40, 0.52, 0.08},
                        new double[]{0.34, 0.51, 0.15},
                },
                new double[][]{
                        new double[]{0.38, 0.47, 0.14},
                        new double[]{0.23, 0.23, 0.55},
                        new double[]{0.56, 0.27, 0.17},
                        new double[]{0.38, 0.57, 0.05},
                },
                new double[][]{
                        new double[]{0.44, 0.25, 0.32},
                        new double[]{0.30, 0.52, 0.18},
                        new double[]{0.41, 0.26, 0.32},
                        new double[]{0.30, 0.66, 0.04},
                },
                new double[][]{
                        new double[]{0.49, 0.03, 0.48},
                        new double[]{0.55, 0.07, 0.39},
                        new double[]{0.26, 0.12, 0.63},
                        new double[]{0.28, 0.47, 0.25},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
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

    public static AIMatrices mutatedLevel6BGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.32, 0.52, 0.17},
                        new double[]{0.10, 0.22, 0.68},
                        new double[]{0.15, 0.46, 0.39},
                        new double[]{0.49, 0.39, 0.12},
                },
                new double[][]{
                        new double[]{0.01, 0.97, 0.02},
                        new double[]{0.51, 0.13, 0.36},
                        new double[]{0.18, 0.24, 0.58},
                        new double[]{0.77, 0.14, 0.08},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.45, 0.55},
                        new double[]{0.17, 0.83},
                        new double[]{0.59, 0.41},
                },
                new double[][]{
                        new double[]{0.68, 0.32},
                        new double[]{0.51, 0.49},
                        new double[]{0.07, 0.93},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.13, 0.33, 0.54, 0.01},
                        new double[]{0.25, 0.16, 0.48, 0.12},
                        new double[]{0.32, 0.08, 0.28, 0.32},
                        new double[]{0.37, 0.37, 0.22, 0.04},
                },
                new double[][]{
                        new double[]{0.29, 0.18, 0.45, 0.08},
                        new double[]{0.13, 0.30, 0.47, 0.11},
                        new double[]{0.39, 0.27, 0.10, 0.24},
                        new double[]{0.10, 0.30, 0.05, 0.55},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.27, 0.44},
                        new double[]{0.01, 0.63, 0.36},
                        new double[]{0.35, 0.34, 0.31},
                        new double[]{0.60, 0.10, 0.30},
                },
                new double[][]{
                        new double[]{0.19, 0.36, 0.45},
                        new double[]{0.93, 0.06, 0.01},
                        new double[]{0.49, 0.24, 0.27},
                        new double[]{0.31, 0.62, 0.07},
                },
                new double[][]{
                        new double[]{0.57, 0.31, 0.12},
                        new double[]{0.72, 0.12, 0.16},
                        new double[]{0.49, 0.17, 0.33},
                        new double[]{0.29, 0.36, 0.35},
                },
                new double[][]{
                        new double[]{0.40, 0.45, 0.15},
                        new double[]{0.48, 0.21, 0.31},
                        new double[]{0.46, 0.25, 0.29},
                        new double[]{0.23, 0.23, 0.54},
                },
                new double[][]{
                        new double[]{0.22, 0.30, 0.48},
                        new double[]{0.19, 0.33, 0.48},
                        new double[]{0.17, 0.55, 0.28},
                        new double[]{0.21, 0.52, 0.27},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel6AGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.00, 0.72, 0.28},
                        new double[]{0.28, 0.21, 0.51},
                        new double[]{0.45, 0.21, 0.35},
                        new double[]{0.60, 0.09, 0.31},
                },
                new double[][]{
                        new double[]{0.01, 0.97, 0.02},
                        new double[]{0.02, 0.34, 0.64},
                        new double[]{0.19, 0.04, 0.76},
                        new double[]{0.79, 0.11, 0.10},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.63, 0.37},
                        new double[]{0.03, 0.97},
                        new double[]{0.31, 0.69},
                },
                new double[][]{
                        new double[]{0.68, 0.32},
                        new double[]{0.65, 0.35},
                        new double[]{0.46, 0.54},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.05, 0.24, 0.19, 0.52},
                        new double[]{0.20, 0.12, 0.53, 0.15},
                        new double[]{0.16, 0.16, 0.57, 0.10},
                        new double[]{0.33, 0.35, 0.12, 0.20},
                },
                new double[][]{
                        new double[]{0.01, 0.26, 0.06, 0.67},
                        new double[]{0.13, 0.17, 0.36, 0.34},
                        new double[]{0.16, 0.21, 0.11, 0.52},
                        new double[]{0.30, 0.05, 0.17, 0.48},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.50, 0.02, 0.49},
                        new double[]{0.10, 0.60, 0.30},
                        new double[]{0.31, 0.43, 0.27},
                        new double[]{0.49, 0.35, 0.16},
                },
                new double[][]{
                        new double[]{0.38, 0.43, 0.20},
                        new double[]{0.05, 0.59, 0.36},
                        new double[]{0.32, 0.66, 0.02},
                        new double[]{0.65, 0.28, 0.07},
                },
                new double[][]{
                        new double[]{0.32, 0.67, 0.01},
                        new double[]{0.17, 0.51, 0.32},
                        new double[]{0.16, 0.56, 0.28},
                        new double[]{0.52, 0.46, 0.02},
                },
                new double[][]{
                        new double[]{0.03, 0.58, 0.39},
                        new double[]{0.58, 0.14, 0.28},
                        new double[]{0.18, 0.36, 0.46},
                        new double[]{0.09, 0.41, 0.49},
                },
                new double[][]{
                        new double[]{0.18, 0.44, 0.38},
                        new double[]{0.38, 0.36, 0.25},
                        new double[]{0.30, 0.34, 0.36},
                        new double[]{0.24, 0.54, 0.23},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel6VGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.00, 0.44, 0.56},
                        new double[]{0.57, 0.13, 0.31},
                        new double[]{0.30, 0.14, 0.56},
                        new double[]{0.63, 0.09, 0.28},
                },
                new double[][]{
                        new double[]{0.01, 0.97, 0.02},
                        new double[]{0.02, 0.34, 0.64},
                        new double[]{0.15, 0.23, 0.62},
                        new double[]{0.75, 0.11, 0.14},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.17, 0.83},
                        new double[]{0.66, 0.34},
                        new double[]{0.62, 0.38},
                },
                new double[][]{
                        new double[]{0.77, 0.23},
                        new double[]{0.57, 0.43},
                        new double[]{0.22, 0.78},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.46, 0.10, 0.36, 0.08},
                        new double[]{0.34, 0.16, 0.38, 0.11},
                        new double[]{0.02, 0.21, 0.16, 0.60},
                        new double[]{0.09, 0.01, 0.42, 0.48},
                },
                new double[][]{
                        new double[]{0.13, 0.09, 0.32, 0.46},
                        new double[]{0.02, 0.57, 0.15, 0.26},
                        new double[]{0.14, 0.01, 0.35, 0.51},
                        new double[]{0.25, 0.08, 0.41, 0.27},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.03, 0.47, 0.50},
                        new double[]{0.34, 0.09, 0.57},
                        new double[]{0.19, 0.35, 0.45},
                        new double[]{0.05, 0.40, 0.55},
                },
                new double[][]{
                        new double[]{0.33, 0.41, 0.26},
                        new double[]{0.25, 0.63, 0.13},
                        new double[]{0.37, 0.34, 0.29},
                        new double[]{0.26, 0.14, 0.60},
                },
                new double[][]{
                        new double[]{0.40, 0.15, 0.45},
                        new double[]{0.12, 0.46, 0.42},
                        new double[]{0.11, 0.27, 0.62},
                        new double[]{0.08, 0.37, 0.54},
                },
                new double[][]{
                        new double[]{0.36, 0.21, 0.43},
                        new double[]{0.42, 0.18, 0.39},
                        new double[]{0.42, 0.20, 0.38},
                        new double[]{0.15, 0.33, 0.52},
                },
                new double[][]{
                        new double[]{0.00, 0.49, 0.51},
                        new double[]{0.31, 0.26, 0.43},
                        new double[]{0.39, 0.14, 0.47},
                        new double[]{0.13, 0.32, 0.55},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1AGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.91, 0.03, 0.06},
                        new double[]{0.70, 0.20, 0.10},
                        new double[]{0.11, 0.28, 0.62},
                        new double[]{0.41, 0.27, 0.33},
                },
                new double[][]{
                        new double[]{0.86, 0.06, 0.08},
                        new double[]{0.50, 0.28, 0.22},
                        new double[]{0.36, 0.15, 0.49},
                        new double[]{0.31, 0.59, 0.09},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.10, 0.90},
                        new double[]{0.75, 0.25},
                        new double[]{0.15, 0.85},
                },
                new double[][]{
                        new double[]{0.49, 0.51},
                        new double[]{0.62, 0.38},
                        new double[]{0.36, 0.64},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.29, 0.45, 0.12, 0.14},
                        new double[]{0.45, 0.18, 0.14, 0.22},
                        new double[]{0.13, 0.75, 0.01, 0.11},
                        new double[]{0.22, 0.24, 0.18, 0.36},
                },
                new double[][]{
                        new double[]{0.00, 0.31, 0.27, 0.42},
                        new double[]{0.14, 0.02, 0.30, 0.54},
                        new double[]{0.22, 0.13, 0.52, 0.12},
                        new double[]{0.26, 0.35, 0.13, 0.25},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.24, 0.26, 0.50},
                        new double[]{0.27, 0.58, 0.15},
                        new double[]{0.37, 0.52, 0.11},
                        new double[]{0.22, 0.43, 0.35},
                },
                new double[][]{
                        new double[]{0.34, 0.37, 0.30},
                        new double[]{0.12, 0.41, 0.47},
                        new double[]{0.25, 0.28, 0.47},
                        new double[]{0.52, 0.37, 0.11},
                },
                new double[][]{
                        new double[]{0.38, 0.47, 0.14},
                        new double[]{0.23, 0.23, 0.54},
                        new double[]{0.56, 0.27, 0.17},
                        new double[]{0.24, 0.36, 0.41},
                },
                new double[][]{
                        new double[]{0.35, 0.29, 0.37},
                        new double[]{0.30, 0.52, 0.18},
                        new double[]{0.33, 0.45, 0.22},
                        new double[]{0.30, 0.66, 0.04},
                },
                new double[][]{
                        new double[]{0.49, 0.03, 0.48},
                        new double[]{0.54, 0.07, 0.39},
                        new double[]{0.26, 0.12, 0.62},
                        new double[]{0.18, 0.53, 0.28},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1BGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.91, 0.06, 0.02},
                        new double[]{0.86, 0.09, 0.05},
                        new double[]{0.24, 0.37, 0.38},
                        new double[]{0.46, 0.48, 0.06},
                },
                new double[][]{
                        new double[]{0.94, 0.03, 0.03},
                        new double[]{0.84, 0.14, 0.02},
                        new double[]{0.10, 0.29, 0.61},
                        new double[]{0.28, 0.45, 0.27},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.57, 0.43},
                        new double[]{0.44, 0.56},
                        new double[]{0.09, 0.91},
                },
                new double[][]{
                        new double[]{0.55, 0.45},
                        new double[]{0.52, 0.48},
                        new double[]{0.47, 0.53},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.14, 0.27, 0.13, 0.45},
                        new double[]{0.25, 0.12, 0.12, 0.52},
                        new double[]{0.34, 0.11, 0.45, 0.10},
                        new double[]{0.55, 0.29, 0.08, 0.08},
                },
                new double[][]{
                        new double[]{0.02, 0.12, 0.75, 0.11},
                        new double[]{0.36, 0.06, 0.35, 0.23},
                        new double[]{0.05, 0.16, 0.69, 0.10},
                        new double[]{0.19, 0.42, 0.07, 0.31},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.51, 0.19},
                        new double[]{0.15, 0.33, 0.52},
                        new double[]{0.55, 0.14, 0.31},
                        new double[]{0.09, 0.57, 0.34},
                },
                new double[][]{
                        new double[]{0.54, 0.32, 0.14},
                        new double[]{0.01, 0.31, 0.68},
                        new double[]{0.36, 0.21, 0.43},
                        new double[]{0.33, 0.41, 0.26},
                },
                new double[][]{
                        new double[]{0.28, 0.25, 0.47},
                        new double[]{0.37, 0.23, 0.40},
                        new double[]{0.43, 0.53, 0.04},
                        new double[]{0.41, 0.08, 0.51},
                },
                new double[][]{
                        new double[]{0.53, 0.31, 0.16},
                        new double[]{0.19, 0.32, 0.49},
                        new double[]{0.14, 0.60, 0.25},
                        new double[]{0.33, 0.44, 0.23},
                },
                new double[][]{
                        new double[]{0.41, 0.53, 0.06},
                        new double[]{0.45, 0.15, 0.40},
                        new double[]{0.39, 0.48, 0.13},
                        new double[]{0.14, 0.37, 0.49},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1CGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.78, 0.20, 0.02},
                        new double[]{0.86, 0.09, 0.05},
                        new double[]{0.18, 0.07, 0.75},
                        new double[]{0.52, 0.06, 0.43},
                },
                new double[][]{
                        new double[]{0.94, 0.03, 0.03},
                        new double[]{0.91, 0.08, 0.01},
                        new double[]{0.35, 0.58, 0.07},
                        new double[]{0.58, 0.13, 0.30},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.26, 0.74},
                        new double[]{0.51, 0.49},
                        new double[]{0.79, 0.21},
                },
                new double[][]{
                        new double[]{0.53, 0.47},
                        new double[]{0.46, 0.54},
                        new double[]{0.72, 0.28},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.46, 0.02, 0.45, 0.07},
                        new double[]{0.07, 0.53, 0.26, 0.14},
                        new double[]{0.11, 0.44, 0.22, 0.23},
                        new double[]{0.07, 0.73, 0.02, 0.18},
                },
                new double[][]{
                        new double[]{0.12, 0.18, 0.32, 0.39},
                        new double[]{0.49, 0.07, 0.06, 0.38},
                        new double[]{0.52, 0.03, 0.40, 0.06},
                        new double[]{0.28, 0.28, 0.14, 0.30},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.29, 0.23, 0.48},
                        new double[]{0.54, 0.06, 0.40},
                        new double[]{0.08, 0.46, 0.46},
                        new double[]{0.20, 0.39, 0.41},
                },
                new double[][]{
                        new double[]{0.43, 0.38, 0.19},
                        new double[]{0.00, 0.85, 0.15},
                        new double[]{0.68, 0.32, 0.00},
                        new double[]{0.24, 0.59, 0.17},
                },
                new double[][]{
                        new double[]{0.95, 0.01, 0.05},
                        new double[]{0.29, 0.32, 0.39},
                        new double[]{0.01, 0.75, 0.24},
                        new double[]{0.25, 0.48, 0.27},
                },
                new double[][]{
                        new double[]{0.30, 0.04, 0.66},
                        new double[]{0.02, 0.25, 0.73},
                        new double[]{0.27, 0.56, 0.17},
                        new double[]{0.56, 0.21, 0.23},
                },
                new double[][]{
                        new double[]{0.27, 0.20, 0.52},
                        new double[]{0.54, 0.16, 0.29},
                        new double[]{0.33, 0.25, 0.42},
                        new double[]{0.16, 0.60, 0.24},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1KGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.77, 0.11, 0.11},
                        new double[]{0.87, 0.08, 0.05},
                        new double[]{0.01, 0.49, 0.50},
                        new double[]{0.63, 0.33, 0.04},
                },
                new double[][]{
                        new double[]{0.94, 0.03, 0.03},
                        new double[]{0.81, 0.07, 0.12},
                        new double[]{0.06, 0.47, 0.47},
                        new double[]{0.46, 0.10, 0.44},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.32, 0.68},
                        new double[]{0.57, 0.43},
                        new double[]{0.28, 0.72},
                },
                new double[][]{
                        new double[]{0.95, 0.05},
                        new double[]{0.69, 0.31},
                        new double[]{0.42, 0.58},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.15, 0.02, 0.70, 0.12},
                        new double[]{0.10, 0.33, 0.37, 0.20},
                        new double[]{0.49, 0.32, 0.16, 0.03},
                        new double[]{0.49, 0.40, 0.01, 0.10},
                },
                new double[][]{
                        new double[]{0.08, 0.12, 0.31, 0.48},
                        new double[]{0.44, 0.11, 0.09, 0.36},
                        new double[]{0.60, 0.01, 0.28, 0.10},
                        new double[]{0.35, 0.11, 0.06, 0.48},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.51, 0.19},
                        new double[]{0.24, 0.53, 0.23},
                        new double[]{0.67, 0.17, 0.16},
                        new double[]{0.07, 0.58, 0.35},
                },
                new double[][]{
                        new double[]{0.54, 0.32, 0.14},
                        new double[]{0.01, 0.31, 0.68},
                        new double[]{0.54, 0.06, 0.39},
                        new double[]{0.17, 0.40, 0.44},
                },
                new double[][]{
                        new double[]{0.28, 0.25, 0.47},
                        new double[]{0.39, 0.24, 0.37},
                        new double[]{0.43, 0.53, 0.04},
                        new double[]{0.53, 0.04, 0.43},
                },
                new double[][]{
                        new double[]{0.53, 0.31, 0.16},
                        new double[]{0.21, 0.27, 0.52},
                        new double[]{0.10, 0.42, 0.48},
                        new double[]{0.33, 0.44, 0.23},
                },
                new double[][]{
                        new double[]{0.24, 0.31, 0.45},
                        new double[]{0.45, 0.15, 0.40},
                        new double[]{0.13, 0.45, 0.42},
                        new double[]{0.02, 0.50, 0.48},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1SGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.00, 1.00, 0.00},
                        new double[]{0.74, 0.20, 0.06},
                        new double[]{0.45, 0.21, 0.35},
                        new double[]{0.60, 0.09, 0.31},
                },
                new double[][]{
                        new double[]{0.01, 0.97, 0.02},
                        new double[]{0.01, 0.48, 0.51},
                        new double[]{0.18, 0.29, 0.54},
                        new double[]{0.70, 0.10, 0.21},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.07, 0.93},
                        new double[]{0.13, 0.87},
                        new double[]{0.13, 0.87},
                },
                new double[][]{
                        new double[]{0.02, 0.98},
                        new double[]{0.56, 0.44},
                        new double[]{0.46, 0.54},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.05, 0.24, 0.19, 0.52},
                        new double[]{0.15, 0.35, 0.39, 0.11},
                        new double[]{0.14, 0.14, 0.50, 0.22},
                        new double[]{0.18, 0.07, 0.38, 0.36},
                },
                new double[][]{
                        new double[]{0.01, 0.26, 0.06, 0.67},
                        new double[]{0.18, 0.10, 0.21, 0.51},
                        new double[]{0.16, 0.21, 0.11, 0.52},
                        new double[]{0.24, 0.05, 0.19, 0.52},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.10, 0.18, 0.73},
                        new double[]{0.35, 0.29, 0.36},
                        new double[]{0.67, 0.05, 0.28},
                        new double[]{0.47, 0.24, 0.29},
                },
                new double[][]{
                        new double[]{0.05, 0.78, 0.17},
                        new double[]{0.33, 0.21, 0.46},
                        new double[]{0.44, 0.12, 0.43},
                        new double[]{0.40, 0.34, 0.26},
                },
                new double[][]{
                        new double[]{0.32, 0.33, 0.35},
                        new double[]{0.38, 0.59, 0.03},
                        new double[]{0.01, 0.91, 0.08},
                        new double[]{0.04, 0.62, 0.34},
                },
                new double[][]{
                        new double[]{0.31, 0.32, 0.37},
                        new double[]{0.35, 0.11, 0.54},
                        new double[]{0.39, 0.16, 0.45},
                        new double[]{0.82, 0.15, 0.03},
                },
                new double[][]{
                        new double[]{0.44, 0.33, 0.23},
                        new double[]{0.59, 0.31, 0.10},
                        new double[]{0.42, 0.01, 0.57},
                        new double[]{0.57, 0.29, 0.13},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1TGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.78, 0.20, 0.02},
                        new double[]{0.86, 0.09, 0.05},
                        new double[]{0.68, 0.19, 0.13},
                        new double[]{0.23, 0.22, 0.55},
                },
                new double[][]{
                        new double[]{0.94, 0.03, 0.03},
                        new double[]{0.85, 0.13, 0.02},
                        new double[]{0.41, 0.58, 0.01},
                        new double[]{0.46, 0.10, 0.44},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.31, 0.69},
                        new double[]{0.91, 0.09},
                        new double[]{0.72, 0.28},
                },
                new double[][]{
                        new double[]{0.70, 0.30},
                        new double[]{0.39, 0.61},
                        new double[]{0.86, 0.14},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.09, 0.02, 0.40, 0.49},
                        new double[]{0.10, 0.33, 0.37, 0.20},
                        new double[]{0.24, 0.56, 0.07, 0.12},
                        new double[]{0.05, 0.51, 0.32, 0.12},
                },
                new double[][]{
                        new double[]{0.26, 0.38, 0.26, 0.11},
                        new double[]{0.49, 0.10, 0.08, 0.33},
                        new double[]{0.15, 0.42, 0.41, 0.03},
                        new double[]{0.28, 0.28, 0.14, 0.30},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.51, 0.19},
                        new double[]{0.15, 0.33, 0.52},
                        new double[]{0.67, 0.17, 0.16},
                        new double[]{0.17, 0.52, 0.31},
                },
                new double[][]{
                        new double[]{0.54, 0.32, 0.14},
                        new double[]{0.01, 0.25, 0.74},
                        new double[]{0.59, 0.06, 0.35},
                        new double[]{0.33, 0.41, 0.26},
                },
                new double[][]{
                        new double[]{0.28, 0.25, 0.47},
                        new double[]{0.21, 0.46, 0.33},
                        new double[]{0.33, 0.64, 0.03},
                        new double[]{0.54, 0.11, 0.35},
                },
                new double[][]{
                        new double[]{0.58, 0.35, 0.07},
                        new double[]{0.20, 0.32, 0.49},
                        new double[]{0.10, 0.42, 0.48},
                        new double[]{0.33, 0.44, 0.23},
                },
                new double[][]{
                        new double[]{0.41, 0.53, 0.06},
                        new double[]{0.45, 0.15, 0.40},
                        new double[]{0.13, 0.45, 0.42},
                        new double[]{0.02, 0.51, 0.47},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }

    public static AIMatrices mutatedLevel1VGauge() {
        double[][][] normalTables = new double[][][]{
                new double[][]{
                        new double[]{0.69, 0.15, 0.15},
                        new double[]{0.93, 0.01, 0.05},
                        new double[]{0.11, 0.44, 0.45},
                        new double[]{0.66, 0.18, 0.16},
                },
                new double[][]{
                        new double[]{0.94, 0.03, 0.03},
                        new double[]{0.76, 0.06, 0.18},
                        new double[]{0.22, 0.48, 0.31},
                        new double[]{0.36, 0.29, 0.35},
                },
        };
        double[][][] attackTables = new double[][][]{
                new double[][]{
                        new double[]{0.33, 0.67},
                        new double[]{0.60, 0.40},
                        new double[]{0.38, 0.62},
                },
                new double[][]{
                        new double[]{0.40, 0.60},
                        new double[]{0.38, 0.62},
                        new double[]{0.47, 0.53},
                },
        };
        double[][][] shieldTables = new double[][][]{
                new double[][]{
                        new double[]{0.09, 0.41, 0.40, 0.10},
                        new double[]{0.06, 0.21, 0.23, 0.50},
                        new double[]{0.10, 0.39, 0.19, 0.31},
                        new double[]{0.07, 0.73, 0.02, 0.18},
                },
                new double[][]{
                        new double[]{0.51, 0.25, 0.17, 0.07},
                        new double[]{0.35, 0.21, 0.05, 0.39},
                        new double[]{0.15, 0.01, 0.40, 0.44},
                        new double[]{0.32, 0.26, 0.13, 0.28},
                },
        };
        double[][][] beamTables = new double[][][]{
                new double[][]{
                        new double[]{0.30, 0.51, 0.19},
                        new double[]{0.15, 0.33, 0.52},
                        new double[]{0.67, 0.17, 0.16},
                        new double[]{0.06, 0.53, 0.41},
                },
                new double[][]{
                        new double[]{0.54, 0.32, 0.14},
                        new double[]{0.01, 0.31, 0.68},
                        new double[]{0.42, 0.08, 0.50},
                        new double[]{0.33, 0.41, 0.26},
                },
                new double[][]{
                        new double[]{0.28, 0.25, 0.47},
                        new double[]{0.39, 0.24, 0.37},
                        new double[]{0.43, 0.53, 0.04},
                        new double[]{0.53, 0.12, 0.35},
                },
                new double[][]{
                        new double[]{0.53, 0.31, 0.16},
                        new double[]{0.20, 0.32, 0.49},
                        new double[]{0.55, 0.21, 0.24},
                        new double[]{0.54, 0.29, 0.18},
                },
                new double[][]{
                        new double[]{0.41, 0.53, 0.06},
                        new double[]{0.61, 0.11, 0.28},
                        new double[]{0.21, 0.11, 0.68},
                        new double[]{0.04, 0.11, 0.85},
                },
        };
        return new AIMatrices(normalTables, attackTables, shieldTables, beamTables);
    }
}
