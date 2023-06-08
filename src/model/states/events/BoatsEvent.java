package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.*;
import util.MyRandom;
import view.subviews.BoatPlacementSubView;
import view.subviews.CollapsingTransition;
import view.subviews.GrassCombatTheme;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class BoatsEvent extends RiverEvent {
    private static final int MATRIX_COLUMNS = 8;
    private static final int MATRIX_ROWS = 5;
    private SteppingMatrix<GameCharacter> matrix;
    private List<GameCharacter> shore;
    private ArrayList<Boat> boats;
    private boolean riverCrossed = false;
    private BoatPlacementSubView boatsView;
    private boolean allowManualAssign = false;

    public BoatsEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return !riverCrossed;
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() < 3) {
            if (MyRandom.flipCoin()) {
                new ShallowsEvent(model).doEvent(model);
                riverCrossed = true;
            } else {
                new NoRiverCrossingEvent(model).doEvent(model);
                riverCrossed = false;
            }
            return;
        }

        println("There are boats here. They don't look sturdy enough to " +
                "go for a longer ride in, but surely they will hold for just crossing the river.");
        model.getLog().waitForAnimationToFinish();
        println("The boats are rather small however and the party must split into smaller groups.");
        makeBoats(model);
        shore = new ArrayList<>();
        shore.addAll(model.getParty().getPartyMembers());
        Collections.shuffle(shore);
        this.matrix = new SteppingMatrix<>(MATRIX_COLUMNS, MATRIX_ROWS);
        addCharactersToMatrix();
        boatsView = new BoatPlacementSubView(this, matrix, boats);
        boatsView.setCursorEnabled(false);
        CollapsingTransition.transition(model, boatsView);

        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this,
                model.getParty().getLeader(), Skill.Leadership, 7, 10, 0);
        if (!result.isSuccessful()) {
            automaticPlacement(model);
        } else {
            manualAssignment(model);
        }
        if (!shore.isEmpty()) {
            return;
        }
        boats.removeIf(ArrayList::isEmpty);

        super.showRiverSubView(model);
        leaderSay("Okay, see you on the other side. Be safe!");
        println("You cross the river in the boats.");
        riverCrossed = true;
        println("The party disembarks on the other.");
        model.getLog().waitForAnimationToFinish();

        int roll = MyRandom.rollD10();
        if (roll < 8) {
            missingBoatSubEvent(model);
        } else {
            leaderSay("Everybody here? Great! That went well.");
        }
        leaderSay("Let's leave these boats here and continue our journey.");
    }

    private void makeBoats(Model model) {
        do {
            boats = new ArrayList<>();
            int seatsToFill = model.getParty().size();
            int[] indices = new int[]{2, 5, 3, 4};
            int currIndex = 0;
            int maximumBoatSize = model.getParty().size() - 1;
            while (seatsToFill > 0) {
                Boat b = new Boat(indices[currIndex], MyRandom.randInt(2, Math.min(4, maximumBoatSize)));
                seatsToFill -= b.length;
                boats.add(b);
                currIndex++;
            }
        } while (boats.size() < 2);
        Collections.sort(boats, new Comparator<Boat>() {
            @Override
            public int compare(Boat b1, Boat b2) {
                return b1.xPos - b2.xPos;
            }
        });
    }

    private void missingBoatSubEvent(Model model) {
        Boat missing = MyRandom.sample(boats);
        GameCharacter talker = null;
        do {
            talker = model.getParty().getRandomPartyMember();
        } while (missing.contains(talker));

        model.getParty().benchPartyMembers(missing);

        if (missing.size() > 1) {
            model.getParty().partyMemberSay(model, talker, "Wait, where are the others?");
            print("The boat with ");
            if (missing.size() == 2) {
                print(missing.get(0).getFirstName() + " and " + missing.get(1).getFirstName());
            } else if (missing.size() == 3) {
                print(missing.get(0).getFirstName() + ", " + missing.get(1).getFirstName() + " and " + missing.get(2).getFirstName());
            } else {
                print(missing.get(0).getFirstName() + "'s group");
            }
            println(" is missing.");
        } else {
            model.getParty().partyMemberSay(model, talker, "Wait, where is " + missing.get(0).getFirstName() + "?");
        }
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
        for (Boat b : boats) {
            if (b != missing) {
                model.getParty().benchPartyMembers(b);
            }
        }

        boolean singleCharacterMissing = missing.size() == 1;

        model.getParty().partyMemberSay(model, missing.get(0), "Hmm... seems like " + (singleCharacterMissing?"I":"we") + " got separated from the others.");
        GameCharacter oldLeader = null;
        if (!missing.contains(model.getParty().getLeader())) {
            oldLeader = model.getParty().getLeader();
            if (singleCharacterMissing) {
                model.getParty().setLeader(missing.get(0));
                println(missing.get(0).getName() + " has been temporarily set to the party leader.");
            } else {
                print("Who do you want to set as the temporary party leader?");
                GameCharacter newLeader = model.getParty().partyMemberInput(model, this, missing.get(0));
                model.getParty().setLeader(newLeader);
            }
        }

        leaderSay((singleCharacterMissing?"I'm":"We're") + " not alone here...");
        int roll2 = MyRandom.rollD10();
        if (singleCharacterMissing) {
            print(missing.get(0).getName() + " encounters ");
        } else {
            print("The group of characters encounters ");
        }
        if (roll2 < 3) {
            println("a bear!");
            runCombat(List.of(new BearEnemy('A')), new GrassCombatTheme(), true);
        } else if (roll2 < 5) {
            println("wild boar!");
            runCombat(List.of(new WildBoarEnemy('A'), new WildBoarEnemy('A'), new WildBoarEnemy('A')),
                    new GrassCombatTheme(), true);
        } else if (roll2 < 7) {
            println("a pair of hungry wolves!");
            runCombat(List.of(new WolfEnemy('A'), new WolfEnemy('A')),
                    new GrassCombatTheme(), true);
        } else if (roll2 < 9) {
            println("some slithering snakes!");
            runCombat(List.of(new ViperEnemy('A'), new ViperEnemy('A'), new ViperEnemy('A')),
                    new GrassCombatTheme(), true);
        } else {
            println("a scary looking spider!");
            runCombat(List.of(new SpiderEnemy('A')), new GrassCombatTheme(), true);
        }
        showRiverSubView(model);
        println("The separated group has joined with the rest of the party again.");
        model.getParty().unbenchAll();
        if (oldLeader != null) {
            model.getParty().setLeader(oldLeader);
            println(oldLeader.getName() + " is the leader again.");
        }
        leaderSay("I'm happy we're all back together now.");
    }

    @Override
    public boolean haveFledCombat() {
        return false;
    }

    private void automaticPlacement(Model model) {
        boatsView.setCursorEnabled(false);
        leaderSay("Okay, let's take a moment...");
        {
            GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            model.getParty().partyMemberSay(model, gc, List.of("Shotgun!", "Yay, boats!", "Jippie!",
                    "Too slow!", "I want to go in that one!", "I don't want to sit with..."));
        }
        while (!shore.isEmpty()) {
            GameCharacter guy = shore.get(0);
            matrix.setSelectedPoint(guy);
            int times = MyRandom.randInt(99);
            for (int i = 0; i < times; ++i) {
                internalShiftCharacter();
            }
            if (!shore.contains(guy)) {
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        print("The party members have assigned themselves to the boats. Press return to cross the river.");
        waitForReturn();
    }

    private void manualAssignment(Model model) {
        this.allowManualAssign = true;
        boatsView.setCursorEnabled(true);
        leaderSay("Okay, let's take a moment to figure out who goes with whom.");
        print("Please assign the party members to the boats. Use SPACE to shift a character's position. Press enter when you are done.");
        do {
            waitForReturn();
            if (shore.isEmpty()) {
                print("Are you ready to cross the river? (Y/N) ");
            } else {
                print("There are still unassigned party members. Do you want to abandon your attempt to cross the river today? (Y/N) ");
            }
        } while (!yesNoInput());
    }

    public void shiftCharacter() {
        if (allowManualAssign) {
            internalShiftCharacter();
        }
    }

    private void internalShiftCharacter() {
        GameCharacter gc = matrix.getSelectedElement();
        matrix.removeAll();
        if (shore.contains(gc)) {
            for (Boat b : boats) {
                if (!b.isFull()) {
                    shore.remove(gc);
                    b.add(gc);
                    break;
                }
            }
        } else {
            boolean found = false;
            for (Boat b : boats) {
                if (found && !b.isFull()) {
                    b.add(gc);
                    found = false;
                    break;
                } else if (b.contains(gc)) {
                    found = true;
                    b.remove(gc);
                }
            }
            if (found) {
                shore.add(gc);
            }
        }
        addCharactersToMatrix();
        matrix.setSelectedPoint(gc);
    }

    private void addCharactersToMatrix() {
        int xOffset = (MATRIX_COLUMNS - shore.size()) / 2;
        for (int i = 0; i < shore.size(); ++i) {
            matrix.addElement(i + xOffset, 0, shore.get(i));
        }
        for (int i = 0; i < boats.size(); ++i) {
            Boat currentBoat = boats.get(i);
            for (int y = 0; y < currentBoat.size(); ++y) {
                matrix.addElement(currentBoat.xPos, y+1, currentBoat.get(y));
            }
        }

    }

    public static class Boat extends ArrayList<GameCharacter> {
        private final int xPos;
        private final int length;

        public Boat(int xPos, int length) {
            this.xPos = xPos;
            this.length = length;
        }

        public boolean isFull() {
            return size() == length;
        }

        public int getLength() {
            return length;
        }

        public int getXPos() {
            return xPos;
        }
    }
}
