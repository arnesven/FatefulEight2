package test;

import model.classes.CharacterClass;
import model.classes.Classes;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.util.*;

public class ClassGraph extends ArrayList<CharacterClass> {

         /*
         BKN--------SOR--------PRI
        /   \        |       /    \
       /      D ----WIT----WIZ     \
      /     /                 \     \
   BBN----PAL                 MAG---- N
    |      |                   |      |
   MIN---- C                  ART----BRD
      \     \                 /     /
       \     AMZ----ASN---- T      /
        \   /        |        \   /
          F --------MAR--------SPY
Cost: 169

    */

    private static final List<CharacterClass> BEST_SOLUTION_SO_FAR = List.of(
            Classes.BBN, Classes.BKN, Classes.SOR, Classes.PRI, Classes.NOB,
            Classes.BRD, Classes.SPY, Classes.MAR, Classes.FOR, Classes.MIN,
            Classes.PAL, Classes.DRU, Classes.WIT, Classes.WIZ, Classes.MAG,
            Classes.ART, Classes.THF, Classes.ASN, Classes.AMZ, Classes.CAP);

    private final HashMap<CharacterClass, Integer> indices;
    private final ClassDiffCostTable costTable;

    public ClassGraph(ClassDiffCostTable costTable,
                      List<CharacterClass> contents) {
        addAll(contents);
        this.costTable = costTable;
        this.indices = new HashMap<>();
        for (CharacterClass cls : this) {
            indices.put(cls, indexOf(cls));
        }
    }

    public ClassGraph(ClassDiffCostTable costTable) {
        this(costTable, BEST_SOLUTION_SO_FAR);
        // Sanity check:
        for (CharacterClass cls : this) {
            assert(MyLists.filter(this, cl -> cl == cls).size() == 1);
        }
    }

    private static List<CharacterClass> makeRandomGraph() {
        List<CharacterClass> list = new ArrayList<>(BEST_SOLUTION_SO_FAR);
        Collections.shuffle(list);
        return list;
    }


    private ClassGraph copy() {
        return new ClassGraph(costTable, this);
    }

    private int analyzeGraph() {
        int totalCost = 0;
        for (CharacterClass key : this) {
            for (CharacterClass other : getNeighbors(key)) {
                int cost = costTable.get(key).get(other);
                totalCost += cost;
            }
        }
        return totalCost / 2;
    }

    private int wrap(int i) {
        if (i == -1) {
            return 9;
        }
        return i % 10;
    }

    private List<CharacterClass> getNeighbors(CharacterClass key) {
        int idx = indices.get(key);
        return List.of(getLeftNeighbor(idx), getRightNeighbor(idx), getAcrossNeighbor(idx));
    }

    private CharacterClass getAcrossNeighbor(int idx) {
        return idx < 10 ? get(idx + 10) : get(idx - 10);
    }

    private CharacterClass getRightNeighbor(int idx) {
        return idx < 10 ? get(wrap(idx + 1)) : get(wrap(idx - 10 + 1) + 10);
    }

    private CharacterClass getLeftNeighbor(int idx) {
        return idx < 10 ? get(wrap(idx - 1)) : get(wrap(idx - 10 - 1) + 10);
    }

    private void printNoCosts() {
        System.out.printf("         %3s--------%3s--------%3s\n", shortName(1), shortName(2), shortName(3));
        System.out.printf("        /   \\        |       /    \\\n");
        System.out.printf("       /     %3s----%3s----%3s     \\\n", shortName(11), shortName(12), shortName(13));
        System.out.printf("      /     /                 \\     \\\n");
        System.out.printf("   %3s----%3s                 %3s----%3s\n", shortName(0), shortName(10), shortName(14), shortName(4));
        System.out.printf("    |      |                   |      |\n");
        System.out.printf("   %3s----%3s                 %3s----%3s\n", shortName(9), shortName(19), shortName(15), shortName(5));
        System.out.printf("      \\     \\                 /     /\n");
        System.out.printf("       \\     %3s----%3s----%3s     /\n", shortName(18), shortName(17), shortName(16));
        System.out.printf("        \\   /        |        \\   /\n");
        System.out.printf("         %3s--------%3s--------%3s\n", shortName(8), shortName(7), shortName(6));
    }

    private void print() {
        System.out.printf("               %2d         %2d\n", cost(1, 2), cost(2, 3));
        System.out.printf("         %3s--------%3s--------%3s\n", shortName(1), shortName(2), shortName(3));
        System.out.printf("        /   \\%2d      |%2d     /%2d  \\\n", cost(1, 11), cost(2, 12), cost(3, 13));
        System.out.printf("     %2d/     %3s----%3s----%3s     \\%2d\n", cost(0, 1), shortName(11), shortName(12), shortName(13), cost(3, 4));
        System.out.printf("      /%2d %2d/    %2d     %2d    \\%2d %2d\\\n", cost(0, 10), cost(10, 11), cost(11, 12), cost(12, 13), cost(13, 14), cost(4, 14));
        System.out.printf("   %3s----%3s                 %3s----%3s\n", shortName(0), shortName(10), shortName(14), shortName(4));
        System.out.printf("  %2d|      |%2d               %2d|      |%2d\n", cost(9, 0), cost(19, 10), cost(14, 15), cost(4, 5));
        System.out.printf("   %3s----%3s                 %3s----%3s\n", shortName(9), shortName(19), shortName(15), shortName(5));
        System.out.printf("      \\%2d %2d\\    %2d     %2d    /%2d %2d/\n", cost(9, 19), cost(18, 19), cost(17, 18), cost(16, 17), cost(15, 16), cost(5, 15));
        System.out.printf("     %2d\\     %3s----%3s----%3s     /%2d\n", cost(8, 9), shortName(18), shortName(17), shortName(16), cost(5, 6));
        System.out.printf("        \\   /%2d      |%2d      \\%2d /\n", cost(8, 18), cost(7, 17), cost(6, 16));
        System.out.printf("         %3s--------%3s--------%3s\n", shortName(8), shortName(7), shortName(6));
        System.out.printf("               %2d         %2d\n", cost(8, 7), cost(7, 6));
    }


    private void printAsList() {
        System.out.println("private static final List<CharacterClass> list = List.of(");
        System.out.print(MyStrings.makeString(this, cls ->
                "Classes." + cls.getShortName() + ", "));
        System.out.println(");");
    }

    private int cost(int cl1, int cl2) {
        return costTable.get(get(cl1)).get(get(cl2));
    }

    private String shortName(int i) {
        CharacterClass cls = get(i);
        if (cls.getShortName().length() == 1) {
            return " " + cls.getShortName().charAt(0) + " ";
        }
        return cls.getShortName();
    }

    private void swap(int a, int b) {
        System.out.printf("Swapping elements %d and %d, (%3s and %3s)\n", a, b, shortName(a), shortName(b));
        CharacterClass clsA = get(a);
        CharacterClass clsB = get(b);
        set(a, clsB);
        set(b, clsA);
        indices.put(clsA, b);
        indices.put(clsB, a);
    }

    public static void main(String[] args) {
        ClassDiffCostTable costTable = new ClassDiffCostTable();
        costTable.print();
        costTable.printOptimumCosts();

        System.out.println("Starting from:");
        ClassGraph classGraph = new ClassGraph(costTable);
        //ClassGraph classGraph = new ClassGraph(costTable, makeRandomGraph());
        System.out.println();
        classGraph.print();
        System.out.println();

        int total = classGraph.analyzeGraph();
        System.out.println("Total cost: " + total);
        classGraph.printNoCosts();
        System.out.println("Press return to start.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        boolean betterFound = false;
        for (int i = 0; i < 2000000; i++) { // 1000000
            ClassGraph copy = classGraph.copy();
            int steps = MyRandom.randInt(3) + MyRandom.randInt(3) + 1;
            if (steps == 5) {
                steps += MyRandom.randInt(4);
            }
            if (steps == 8) {
                steps += MyRandom.randInt(4);
            }
            for (int j = 0; j < steps; ++j) {
                int idx1 = MyRandom.randInt(20);
                int idx2;
                do {
                    idx2 = MyRandom.randInt(20);
                } while (idx2 == idx1);
                copy.swap(idx1, idx2);
            }

            int newCost = copy.analyzeGraph();
            System.out.printf("Copy cost: %d\n", newCost);
            if (newCost < total) {
                System.out.println("Better solution found!");
                copy.print();
                classGraph = copy;
                total = newCost;
                betterFound = true;
            }
        }
        if (betterFound) {
            System.out.println("Found better solution:");
            classGraph.print();
            System.out.println();
            System.out.println();
            classGraph.printNoCosts();
            System.out.println("Cost: " + total);
        } else {
            System.out.println("No better solution found.");
        }

        do {
            System.out.println("1 = Rotate Clockwise");
            System.out.println("2 = Rotate Counter-Clockwise");
            System.out.println("3 = Invert");
            System.out.println("4 = Swap inner/outer");
            System.out.println("5 = Print with costs");
            System.out.println("9 = Quit");
            int op = scanner.nextInt();
            if (op == 9) {
                break;
            }
            if (op == 1) {
                classGraph.rotateClockwise();
            }
            if (op == 2) {
                classGraph.rotateCounterClockwise();
            }
            if (op == 3) {
                classGraph.invert();
            }
            if (op == 4) {
                classGraph.swapInnerOuter();
            }
            if (op == 5) {
                classGraph.print();
            }
            classGraph.printNoCosts();

        } while (true);
        System.out.println("Cost: " + total);
        classGraph.printAsList();
    }

    private void rotateClockwise() {
        CharacterClass elem9 = get(9);
        CharacterClass elem19 = get(19);

        remove(elem9);
        remove(elem19);

        add(0, elem9);
        add(10, elem19);
    }

    private void rotateCounterClockwise() {
        CharacterClass elem0 = get(0);
        CharacterClass elem10 = get(10);

        remove(elem0);
        remove(elem10);

        add(9, elem0);
        add(19, elem10);
    }

    private void invert() {
        CharacterClass[] arr = this.toArray(CharacterClass[]::new);
        clear();
        for (int i = 4; i >= 0; --i) {
            add(arr[i]);
        }
        for (int i = 9; i >= 5; --i) {
            add(arr[i]);
        }
        for (int i = 14; i >= 10; --i) {
            add(arr[i]);
        }
        for (int i = 19; i>= 15; --i) {
            add(arr[i]);
        }
    }

    private void swapInnerOuter() {
        for (int i = 0; i < 10; ++i) {
            CharacterClass first = removeFirst();
            add(first);
        }
    }
}
