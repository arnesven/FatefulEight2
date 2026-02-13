package model.classes;

import util.Arithmetics;
import util.MyLists;

import java.util.*;

public class ClassGraph {
    // Copied from ClassGraphSynthesis
    private static final List<CharacterClass> list = List.of(
            Classes.PRI, Classes.WIZ, Classes.WIT, Classes.DRU, Classes.CAP,
            Classes.NOB, Classes.MAG, Classes.SOR, Classes.BKN, Classes.PAL,
            Classes.ART, Classes.SPY, Classes.MAR, Classes.AMZ, Classes.FOR,
            Classes.BRD, Classes.THF, Classes.ASN, Classes.BBN, Classes.MIN);
    private static final Map<Integer, Set<CharacterClass>> graph = buildGraph();

    private static Map<Integer, Set<CharacterClass>> buildGraph() {
        Map<Integer, Set<CharacterClass>> graph = new HashMap<>();
        int noOfClasses = list.size();
        int ringSize = noOfClasses / 2;
        for (int i = 0; i < ringSize; ++i) {
            int forward = Arithmetics.incrementWithWrap(i, ringSize);
            int backward = Arithmetics.decrementWithWrap(i, ringSize);
            int across = i + ringSize;
            int wrap = (i + 5) % ringSize;
            graph.put(list.get(i).id(), Set.of(list.get(forward), list.get(backward), list.get(across), list.get(wrap)));
        }

        for (int i = ringSize; i < noOfClasses; ++i) {
            int forward = Arithmetics.incrementWithWrap(i - ringSize, ringSize) + ringSize;
            int backward = Arithmetics.decrementWithWrap(i - ringSize, ringSize) + ringSize;
            int across = i - ringSize;
            int wrap = ((i - ringSize) + 5) % ringSize + ringSize;
            graph.put(list.get(i).id(), Set.of(list.get(forward), list.get(backward), list.get(across), list.get(wrap)));
        }
        return graph;
    }

    public static Set<CharacterClass> get(int characterClassId) {
        Set<CharacterClass> result = graph.get(characterClassId);
        if (result == null) {
            return Set.of();
        }
        return graph.get(characterClassId);
    }

    private static String shortName(int index) {
        String shortName = list.get(index).getShortName();
        if (shortName.length() == 1) {
            return " " + shortName + " ";
        }
        return shortName;
    }

    public static String printNoCosts() {
        StringBuilder bldr = new StringBuilder();
        bldr.append(String.format("      %3s-----%3s-----%3s\n", shortName(1), shortName(2), shortName(3)));
        bldr.append(String.format("     /   \\     :     /   \\\n"));
        bldr.append(String.format("    /     %3s-%3s-%3s     \\\n", shortName(11), shortName(12), shortName(13)));
        bldr.append(String.format("   /     /           \\     \\\n"));
        bldr.append(String.format("%3s----%3s           %3s----%3s\n", shortName(0), shortName(10), shortName(14), shortName(4)));
        bldr.append(String.format(" :      :             :      :\n"));
        bldr.append(String.format("%3s----%3s           %3s----%3s\n", shortName(9), shortName(19), shortName(15), shortName(5)));
        bldr.append(String.format("   \\     \\           /     /\n"));
        bldr.append(String.format("    \\     %3s-%3s-%3s     /\n", shortName(18), shortName(17), shortName(16)));
        bldr.append(String.format("     \\   /     :     \\   /\n"));
        bldr.append(String.format("      %3s-----%3s-----%3s\n", shortName(8), shortName(7), shortName(6)));
        return bldr.toString();
    }

    public static CharacterClass getWrapNeighbor(int id) {
        int ringSize = list.size() / 2;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).id() == id) {
               int wrap = (i + 5) % ringSize;
               if (i >= ringSize) {
                   wrap = ((i - ringSize) + 5) % ringSize + ringSize;
               }
               return list.get(wrap);
            }
        }
        throw new IllegalArgumentException("No class found for id = " + id);
    }
}
