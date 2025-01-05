package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.duel.*;
import model.states.duel.gauges.*;
import util.MyRandom;
import view.MyColors;

import java.util.*;

public class AIMagicDuelEvent extends MagicDuelEvent {
    public static long clockTime = 0;
    private List<MagicDuelist> opponents;

    public AIMagicDuelEvent(Model m) {
        super(m, true, makeNPCMageOfClassAndLevel(Classes.WIZ, 1), null);
    }

    @Override
    protected void doEvent(Model model) {
        simulateAIs(model);
    }

    public PowerGauge makeGaugeToTest() {
        return new VTypePowerGauge(false);
    }

    private void simulateAIs(Model model) {
        GameCharacter npcMage1 = makeNPCMageOfClassAndLevel(Classes.WIZ, 1);
        MyColors color1 = findBestMagicColor(npcMage1);
        GameCharacter npcMage2 = makeNPCMageOfClassAndLevel(Classes.WIZ, 1);
        MyColors color2 = findBestMagicColor(npcMage2);

        this.opponents = List.of(
                new MagicDuelist(npcMage2, color2, new ATypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new BTypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new CTypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new VTypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new STypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new KTypePowerGauge(false), false),
                new MagicDuelist(npcMage2, color2, new TTypePowerGauge(false), false)
        );

        final int NO_OF_MATCHES = 20;
        final int TOTAL_MATRICES = 208;
        final int SUBSET_MATRICES = TOTAL_MATRICES / 16;

        List<AIMatrices> allMatrices = new ArrayList<>();
        Map<AIMatrices, Integer> winCounts = new HashMap<>();

        List<AIMatrices> oldMatrices = AIMatricesPresets.getOldMatrices();
        for (AIMatrices old : oldMatrices) {
            allMatrices.add(old);
            winCounts.put(old, 0);
        }

        for (int i = 0; i < TOTAL_MATRICES - oldMatrices.size(); ++i) {
            AIMatrices matrix = AIMatrices.makeRandomMatrix();
            allMatrices.add(matrix);
            winCounts.put(matrix, 0);
        }

        runMatches(model, allMatrices, winCounts, npcMage1, color1, npcMage2, color2, NO_OF_MATCHES);
        {
            int i = 0;
            for (AIMatrices m : allMatrices) {
                System.out.println("Matrix " + (++i) + ": " + winCounts.get(m));
            }
        }

        List<AIMatrices> round2Matrices = new ArrayList<>();
        for (int generation = 1; generation < 11; generation++) {
            int i = 0;
            System.out.print("Making babies....");
            for (AIMatrices m : allMatrices) {
                if (i < SUBSET_MATRICES) {
                    round2Matrices.addAll(AIMatrices.makeChildren(m, allMatrices.get(i + 1)));
                }
                i++;
            }
            System.out.println(" done!");

            Map<AIMatrices, Integer> winCountsRound2 = new HashMap<>();
            for (AIMatrices m : round2Matrices) {
                winCountsRound2.put(m, 0);
            }

            runMatches(model, round2Matrices, winCountsRound2, npcMage1, color1, npcMage2, color2, NO_OF_MATCHES);
            int i4 = 0;
            for (AIMatrices m : round2Matrices) {
                System.out.println("Matrix " + (++i4) + ": " + winCountsRound2.get(m));
            }
            System.out.println("Best from generation " + generation);
            round2Matrices.get(0).printYourself();

            allMatrices.clear();
            allMatrices.addAll(round2Matrices);
            round2Matrices.clear();
        }
    }

    private void runMatches(Model model, List<AIMatrices> allMatrices,
                            Map<AIMatrices, Integer> winCounts,
                            GameCharacter npcMage1, MyColors color1,
                            GameCharacter npcMage2, MyColors color2,
                            int noOfMatches) {
        this.duelists = new ArrayList<>();
        int count = 0;
        MagicDuelist duelist1 = new MagicDuelist(npcMage1, color1, makeGaugeToTest(), false);
        duelists.add(duelist1);
        this.controller1 = new MatrixDuelistController(duelist1, null);
        this.controller2 = new MatrixDuelistController(null, null);

        for (int index = 0; index < allMatrices.size(); index++) {
            AIMatrices matrix1 = allMatrices.get(index);
            ((MatrixDuelistController)controller1).setMatrix(matrix1);

            int winsForThis = 0;
            for (int match = 0; match < noOfMatches; ++match) {
                int vsIndex;
                do {
                    vsIndex = MyRandom.randInt(allMatrices.size());
                } while (vsIndex == index);
                AIMatrices matrix2 = allMatrices.get(vsIndex);

                MagicDuelist duelist2 = MyRandom.sample(opponents);
                duelists.add(duelist2);
                ((MatrixDuelistController)controller2).setDuelist(duelist2);
                ((MatrixDuelistController)controller2).setMatrix(matrix2);

                //clockStart();
                int roundsPlayed = runDuel(model);
                //clockEnd();

                if (duelists.get(1).isKnockedOut()) {
                    winsForThis++;
                }
                duelists.remove(duelist2);
                //else {
//                    winCounts.put(matrix2, winCounts.get(matrix2) + 1);
//                }
//                System.out.println(" Match " + match + " over, outcome: " +
//                        duelist1.getHitsTaken() + " vs " + duelist2.getHitsTaken() + " (" +
//                        roundsPlayed + " rounds)");
                duelist2.reset();
                duelist1.reset();
            }

            winCounts.put(matrix1, winCounts.get(matrix1) + winsForThis);
            System.out.println("Progress: " + (++count) + "/" + winCounts.keySet().size() + " (" + winsForThis + " wins)");
        }
        allMatrices.sort(Comparator.comparingInt(winCounts::get));
        Collections.reverse(allMatrices);

        int total = 0;
        for (AIMatrices key : winCounts.keySet()) {
            total += winCounts.get(key);
        }
        System.out.println("Matches over Total wins: " + total);
    }

    private void clockEnd() {
        long timeTaken = System.currentTimeMillis() - clockTime;
        System.out.println( "time=" + timeTaken + "ms)");
    }

    private void clockStart() {
        clockTime = System.currentTimeMillis();
    }
}
