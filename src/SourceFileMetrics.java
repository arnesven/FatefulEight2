import util.MyLists;
import util.MyPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class SourceFileMetrics {

    public int fileCount = 0;
    private int folders = 0;
    private int linesOfCode = 0;
    private int blankLines = 0;
    private Map<Character, Integer> charCount = new HashMap<>();
    private int characters = 0;
    private int longestFile = 0;
    private String longestFileName = "";
    private int shortestFile = Integer.MAX_VALUE;
    private String shortestFileName = "";


    public static void main(String[] args) {
        SourceFileMetrics metrics = new SourceFileMetrics();
        File folder = new File(".");
        collectMetrics(folder.getAbsolutePath(), metrics);



        System.out.println(".java files: " + metrics.fileCount);
        System.out.println("directories: " + metrics.folders);
        System.out.println("lines of code: " + metrics.linesOfCode);
        System.out.println("blank lines: " + metrics.blankLines);
        System.out.println("longest file: " + metrics.longestFile + " (" + metrics.longestFileName.replace(folder.getPath(), "") + ")");
        System.out.println("Shortest file: " + metrics.shortestFile + " (" + metrics.shortestFileName.replace(folder.getPath(), "") + ")");
        System.out.println("characters: " + metrics.characters);

        List<MyPair<Integer, Character>> charCountList = MyLists.transform(
                new ArrayList<>(metrics.charCount.entrySet()), es -> new MyPair<>(es.getValue(), es.getKey()));
        charCountList.sort(Comparator.comparingInt(o -> o.first));
        System.out.println("Character counts:");
        for (int i = charCountList.size() - 1; i >= 0; --i) {
            MyPair<Integer, Character> pair = charCountList.get(i);
            System.out.format("%6d %s\n", pair.first, "" + pair.second);
        }
    }

    private static void collectMetrics(String absolutePath, SourceFileMetrics metrics) {
        File folder = new File(absolutePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                checkSourceCodeFile(file, metrics);
                if (file.isDirectory()) {
                    metrics.folders++;
                    collectMetrics(file.getAbsolutePath(), metrics);
                }
            }
        } else {
            System.out.println("Directory does not exist or is not accessible.");
        }
    }

    private static void checkSourceCodeFile(File file, SourceFileMetrics metrics) {
        if (file.getPath().endsWith(".java")) {
            metrics.fileCount++;
            System.out.println(metrics.fileCount + " " + file.getPath());
            parseFile(file, metrics);
        }
    }

    private static void parseFile(File file, SourceFileMetrics metrics) {
        try {
            Scanner scanner = new Scanner(new FileReader(file));
            int fileLength = 0;
            while (scanner.hasNext()) {
                fileLength++;
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    metrics.blankLines++;
                }
                for (int i = 0; i < line.length(); ++i) {
                    Character c = line.charAt(i);
                    if (!metrics.charCount.containsKey(line.charAt(i))) {
                        metrics.charCount.put(c, 0);
                    }
                    metrics.charCount.put(c, metrics.charCount.get(c) + 1);
                }
                metrics.characters += line.length();
            }

            metrics.linesOfCode += fileLength;
            if (fileLength > metrics.longestFile) {
                metrics.longestFile = fileLength;
                metrics.longestFileName = file.getName();
            }
            if (fileLength < metrics.shortestFile) {
                metrics.shortestFile = fileLength;
                metrics.shortestFileName = file.getName();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
