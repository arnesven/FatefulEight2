package model.items.puzzletube;

import model.Model;
import util.Arithmetics;
import util.MyLists;
import util.MyRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordPuzzle implements Serializable {


    private static class LetterRing implements Serializable {

        List<Character> letters = new ArrayList<>();
        int rotation = 0;
    }

    public static final int LOCKPICK_DIFFICULTY = 7;
    private static final int LETTERS_PER_RING = 10;
    private static final List<Character> alpha = List.of(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

    private final String solutionWord;
    private int maxLocks = -1;
    private final List<LetterRing> letterRings;
    private final List<Integer> locksIndices;
    private int activeLocks;
    private String snapshot;
    private boolean parchmentRemoved = false;
    private long timeSpent;
    private int solvedOnDay;

    public WordPuzzle(String solutionWord) {
        this.solutionWord = solutionWord;
        setMaxLocks();
        this.letterRings = makeLetters();
        this.locksIndices = makeLocks();
        this.timeSpent = 0;
        randomize();
        printInfo();
        startLockCheck();
    }

    private void setMaxLocks() {
        maxLocks = solutionWord.length() / 3;
        if (solutionWord.length() == 8 || solutionWord.length() == 11) {
            maxLocks += MyRandom.randInt(2);
        }
    }

    private int getNumberOfLocks() {
        return maxLocks;
    }

    public boolean addNewLock() {
        if (maxLocks == solutionWord.length()-1) {
            return false;
        }
        maxLocks += 1;

        final int[] start = {1};
        List<Integer> intList = MyLists.generate(letterRings.size()-1, () -> start[0]++);
        for (Integer lock : locksIndices) {
            intList.remove(lock);
        }
        locksIndices.add(MyRandom.sample(intList));
        printInfo();
        return true;
    }

    public void startLockCheck() {
        this.activeLocks = getNumberOfActiveLocks();
        this.snapshot = getActiveLocksSnapshot();
    }

    public String getActiveLocksSnapshot() {
        return MyLists.commaAndJoin(getActiveLocks(), b -> b + "");
    }

    public boolean didLocksActivate() {
        int active = getNumberOfActiveLocks();
        if (active < activeLocks) {
            return false;
        }
        if (active == activeLocks) {
            String newSnap = getActiveLocksSnapshot();
            return !newSnap.equals(snapshot);
        }
        return true;
    }

    private List<LetterRing> makeLetters() {
        List<LetterRing> result = new ArrayList<>();
        int ringCount = solutionWord.length();
        for (int i = 0; i < ringCount; ++i) {
            LetterRing ring = new LetterRing();
            ring.letters.add(solutionWord.charAt(i));
            List<Character> letters = new ArrayList<>(alpha);
            Character toRemove = solutionWord.charAt(i);
            letters.remove(toRemove);
            Collections.shuffle(letters);
            ring.letters.addAll(letters.subList(0, LETTERS_PER_RING-1));
            result.add(ring);
        }
        return result;
    }

    private void randomize() {
        int rings = letterRings.size();
        int offset = MyRandom.randInt(rings);
        for (int i = 0; i < 100; ++i) {
            int index = (int)Math.floor((MyRandom.nextDouble() + MyRandom.nextDouble()) / 2.0 * rings);
            index = (index + offset) % rings;
            rotateUp(index);
        }
    }

    private List<Integer> makeLocks() {
        final int[] start = {1};
        List<Integer> intList = MyLists.generate(letterRings.size()-1, () -> start[0]++);
        while (intList.size() > getNumberOfLocks()) {
            intList.remove(MyRandom.randInt(intList.size()));
        }
        return intList;
    }

    public List<Boolean> getActiveLocks() {
        return MyLists.transform(locksIndices,
                index -> letterRings.get(index-1).rotation == letterRings.get(index).rotation);
    }


    public boolean isSolved() {
        int firstRotation = letterRings.get(0).rotation;
        return MyLists.all(letterRings, ring -> ring.rotation == firstRotation);
    }

    public int getNumberOfActiveLocks() {
        return MyLists.intAccumulate(getActiveLocks(), b -> b ? 1 : 0);
    }

    private void printInfo() {
        System.out.println("Puzzle tube rotations: [" +
                MyLists.commaAndJoin(MyLists.transform(letterRings, ring -> ring.rotation), i -> i+"") + "]");
        System.out.println("         lock indices: [" +
                MyLists.commaAndJoin(locksIndices, i -> i+"") + "]");
    }

    public String getWord(int level) {
        StringBuilder bldr = new StringBuilder();
        for (LetterRing ring : letterRings) {
            bldr.append(ring.letters.get(level));
        }
        return bldr.toString();
    }

    public void rotateUp(int ringIndex) {
        LetterRing ring = letterRings.get(ringIndex);
        ring.letters.add(ring.letters.remove(0));
        ring.rotation = Arithmetics.incrementWithWrap(ring.rotation, LETTERS_PER_RING);
    }

    public void rotateDown(int ringIndex) {
        LetterRing ring = letterRings.get(ringIndex);
        ring.letters.add(0, ring.letters.remove(ring.letters.size()-1));
        ring.rotation = Arithmetics.decrementWithWrap(ring.rotation, LETTERS_PER_RING);
    }

    public void removeParchment(Model model) {
        this.parchmentRemoved = true;
        this.solvedOnDay = model.getDay();
    }


    public int getSolvedOnDay() {
        return solvedOnDay;
    }

    public boolean isParchmentRemoved() {
        return parchmentRemoved;
    }

    public String getSolutionWord() {
        return solutionWord;
    }

    public void addToTimeSpent(long time) {
        timeSpent += time;
    }

    public long getTimeSpentMillis() {
        return timeSpent;
    }
}
