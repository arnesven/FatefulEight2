package test;

import model.classes.CharacterClass;
import model.classes.Classes;
import util.MyLists;
import util.MyRandom;

import java.util.*;

public class ClassGraph extends ArrayList<CharacterClass> {

        /*
    ORIGINAL:
              1          2          3
             SOR--------WIT--------BBN
            /   \11    12|     13/    \
           /     MAR----FOR----AMZ     \
        0 /   10/                 \14   \4
       BKN----CAP                 WIZ----DRU
       9|    19|                   |15    |5
       MIN----PAL                 MAG----PRI
          \     \                 /     /
           \     NOB----BRD----SPY     /
            \   /18    17|      16\   /
             ART--------THF--------ASN
              8          7          6
    */


    private static final List<CharacterClass> BEST_SOLUTION_SO_FAR = List.of(
            Classes.FOR, Classes.MIN, Classes.BBN, Classes.DRU, Classes.PRI,
            Classes.NOB, Classes.BRD, Classes.THF, Classes.ASN, Classes.MAR,
            Classes.CAP, Classes.PAL, Classes.BKN, Classes.SOR, Classes.WIZ,
            Classes.ART, Classes.MAG, Classes.SPY, Classes.WIT, Classes.AMZ);

    /*
    Best so far:
         MIN--------BBN-------- D
        /   \        |       /    \
       /     PAL----BKN----SOR     \
      /     /                 \     \
    F ---- C                  WIZ----PRI
    |      |                   |      |
   MAR----AMZ                 ART---- N
      \     \                 /     /
       \     WIT----SPY----MAG     /
        \   /        |        \   /
         ASN-------- T --------BRD
    Total cost: 742

    */

    private static final List<CharacterClass> CONTENDER = List.of(
            Classes.PRI, Classes.DRU, Classes.BBN, Classes.MIN, Classes.FOR,
            Classes.AMZ, Classes.WIT, Classes.SPY, Classes.NOB, Classes.BRD,
            Classes.WIZ, Classes.SOR, Classes.BKN, Classes.PAL, Classes.CAP,
            Classes.MAR, Classes.ASN, Classes.THF, Classes.ART, Classes.MAG);

    /*
          D --------BBN--------MIN
        /   \        |       /    \
       /     SOR----BKN----PAL     \
      /     /                 \     \
   PRI----WIZ                  C ---- F
    |      |                   |      |
   BRD----MAG                 MAR----AMZ
      \     \                 /     /
       \     ART---- T ----ASN     /
        \   /        |        \   /
          N --------SPY--------WIT
    Cost: 743

          C --------AMZ--------ASN
        /   \        |       /    \
       /      F ----MAR----SPY     \
      /     /                 \     \
   PAL----MIN                 ART---- T
    |      |                   |      |
   BKN----BBN                  N ----BRD
      \     \                 /     /
       \      D ----WIT----PRI     /
        \   /        |        \   /
         SOR--------WIZ--------MAG
    Cost: 743

         BBN-------- D --------WIT
        /   \        |       /    \
       /     BKN----SOR----SPY     \
      /     /                 \     \
   MIN----PAL                 ART----WIZ
    |      |                   |      |
    F ---- C                  MAG----PRI
      \     \                 /     /
       \     MAR---- T ----BRD     /
        \   /        |        \   /
         AMZ--------ASN-------- N
    Cost: 753

         ASN--------AMZ-------- C
        /   \        |       /    \
       /     SPY----MAR---- F      \
      /     /                 \     \
    T ----BRD                 MIN----PAL
    |      |                   |      |
    N ----PRI                 BBN----BKN
      \     \                 /     /
       \     MAG----WIZ---- D      /
        \   /        |        \   /
         ART--------WIT--------SOR
    Cost: 757

         BKN--------PAL-------- C
        /   \        |       /    \
       /      D ----BBN----MIN     \
      /     /                 \     \
   SOR----WIT                  F ----MAR
    |      |                   |      |
   PRI----WIZ                 ASN----AMZ
      \     \                 /     /
       \     MAG----BRD---- T      /
        \   /        |        \   /
          N --------ART--------SPY
    Cost: 761
     */

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
        //ClassGraph classGraph = new ClassGraph(costTable);
        ClassGraph classGraph = new ClassGraph(costTable, makeRandomGraph());
        System.out.println();
        classGraph.print();
        System.out.println();

        int total = classGraph.analyzeGraph();
        System.out.println("Total cost: " + total);
        classGraph.printNoCosts();
        System.out.println("Press return to start.");
        new Scanner(System.in).nextLine();

        boolean betterFound = false;
        for (int i = 0; i < 1000000; i++) {
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
    }
}
