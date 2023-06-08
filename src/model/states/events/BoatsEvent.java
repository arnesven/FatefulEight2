package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import util.MyRandom;
import view.TrainingView;
import view.subviews.BoatPlacementSubView;
import view.subviews.CollapsingTransition;
import view.subviews.StripedTransition;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BoatsEvent extends RiverEvent {
    private static final int MATRIX_COLUMNS = 8;
    private static final int MATRIX_ROWS = 5;
    private SteppingMatrix<GameCharacter> matrix;
    private int[] boatPositions;
    private int[] boatLengths;
    private List<GameCharacter> shore;
    private ArrayList<Boat> boats;
    private boolean riverCrossed = false;
    private BoatPlacementSubView boatsView;

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
            new ShallowsEvent(model).doEvent(model);
            riverCrossed = true;
            return;
        }

        println("There are boats here. They don't look sturdy enough to " +
                "go for a longer ride in, but surely they will hold for just crossing the river.");
        println("The boats are rather small however and the party must split into smaller groups.");
        boats = new ArrayList<>();
        boats.addAll(List.of(new Boat(1, 2), new Boat(3, 4), new Boat(5, 3)));
        shore = new ArrayList<>();
        shore.addAll(model.getParty().getPartyMembers());
        this.matrix = new SteppingMatrix<>(MATRIX_COLUMNS, MATRIX_ROWS);
        addCharactersToMatrix();
        boatsView = new BoatPlacementSubView(this, matrix, boats);
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

        super.showRiverSubView(model);
        leaderSay("Okay, see you on the other side. Be safe!");
        println("You cross the river in the boats.");
        riverCrossed = true;
        leaderSay("That went well!");
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
                shiftCharacter(model);
            }
            if (!shore.contains(guy)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        print("The party members have assigned themselves to the boats. Press return to cross the river.");
        waitForReturn();
    }

    private void manualAssignment(Model model) {
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

    public void shiftCharacter(Model model) {
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
