package model.states;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import util.MyRandom;
import view.TrainingView;
import view.subviews.StripedTransition;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrainingState extends GameState {

    private static final int TUITION_FEA = 5;
    private static final int TRAINING_ROWS = 7;
    private static final int TRAINING_COLUMNS = 8;
    private static final List<Skill> allMartialSkills = List.of(Skill.Axes, Skill.Blades, Skill.BluntWeapons, Skill.Bows, Skill.Polearms);
    private static final List<Skill> allArcaneSkills = List.of(Skill.MagicBlue, Skill.MagicWhite, Skill.MagicBlack, Skill.MagicRed, Skill.MagicGreen);
    private final List<Skill> lessons = new ArrayList<>();
    private SteppingMatrix<GameCharacter> matrix;
    public static final String[] LEVELS = new String[]{"Expert", "Advanced", "Novice"};
    public static final int[] DIFFICULTY = new int[]{10, 8, 6};
    public static final int[] EXPERIENCE = new int[]{50, 40, 30};
    private boolean inResolution = false;

    public TrainingState(Model model) {
        super(model);
        for (int i = 0; i < 3; ++i) {
            lessons.add(MyRandom.sample(allArcaneSkills));
        }
        for (int i = 0; i < 3; ++i) {
            lessons.add(MyRandom.sample(allMartialSkills));
        }
    }

    @Override
    public GameState run(Model model) {
        boolean cantAfford = model.getParty().getGold() < TUITION_FEA;
        if (cantAfford) {
            println("You cannot afford the tuition fea, which is 5 gold.");
        } else {
            print("The tuition fea for training at the temple is " + TUITION_FEA + " gold. Do you pay? (Y/N) ");
        }
        if (cantAfford || !yesNoInput()) {
            return model.getCurrentHex().getDailyActionState(model);
        }
        model.getParty().addToGold(-TUITION_FEA);

        this.matrix = new SteppingMatrix<>(TRAINING_COLUMNS, TRAINING_ROWS);
        addPartyRandomlyToMatrix(model);
        SubView trainingView = new TrainingView(this, matrix);
        StripedTransition.transition(model, trainingView);
        print("Please assign the party members to training sessions. Use SPACE to shift a character's position. Press enter when you are done.");
        model.getTutorial().training(model);
        waitForReturn();
        inResolution = true;
        for (GameCharacter gc : matrix.getElementList()) {
            Point p = matrix.getPositionFor(gc);
            matrix.setSelectedPoint(gc);
            if (p.y == matrix.getRows()-1) {
                doTempleChores(model, gc);
            } else {
                doSkillTraining(model, gc,p.y, p.y > 2 ? p.y - 3 : p.y);
            }
        }
        model.getLog().waitForAnimationToFinish();
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void doSkillTraining(Model model, GameCharacter performer, int index, int level) {
        String maybeN = level < 2 ? "n" : "";
        println(performer.getName() + " attends a" + maybeN + " " + LEVELS[level].toLowerCase() +
                " level training session in " + getLessonName(index) + ".");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, performer,
                lessons.get(index), DIFFICULTY[level], EXPERIENCE[level], 0);
        if (result.isSuccessful()) {
            model.getParty().partyMemberSay(model, performer,
                    List.of("Yes!", "Eureka!", "Wonderful!", "I'm learning."));
            println(performer.getName() + " gains " + EXPERIENCE[level] + " experience.");
        } else {
            model.getParty().partyMemberSay(model, performer,
                    List.of("Aaw!#", "Oh no!#", "Shoot#!", "Phooey!#", "Darn it!#"));
        }
    }

    private void doTempleChores(Model model, GameCharacter gc) {
        String job = MyRandom.sample(List.of("cleaning floors", "cooking meals", "brewing ale", "peeling potatoes", "doing laundry"));
        println(gc.getName() + " helps the priests by " + job + ", gets paid 1 gold.");
        model.getParty().addToGold(1);
        model.getLog().waitForAnimationToFinish();
    }

    private void addPartyRandomlyToMatrix(Model model) {
        int i = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            matrix.addElement(i++, TRAINING_ROWS-1, gc);
        }
    }

    public void shiftCharacter(Model model) {
        Point p = matrix.getSelectedPoint();
        GameCharacter gc = matrix.getSelectedElement();
        matrix.remove(gc);
        do {
            p.y = (p.y + 1) % matrix.getRows();
        } while (p.y != matrix.getRows()-1 && emptySlotInRow(p.y) == -1);
        int x = emptySlotInRow(p.y);
        matrix.addElement(x, p.y, gc);
        matrix.setSelectedPoint(matrix.getElementAt(x, p.y));
    }

    private int emptySlotInRow(int y) {
        if (y < 3) {
            for (int x = matrix.getColumns()-2; x >= 4; --x) {
                if (matrix.getElementAt(x, y) == null) {
                    return x;
                }
            }
        } else if (y == matrix.getRows()-1) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) == null) {
                    return x;
                }
            }
        } else {
            for (int x = 0; x < 3; ++x) {
                if (matrix.getElementAt(x, y) == null) {
                    return x;
                }
            }
        }
        return -1;
    }

    public String getLessonName(int i) {
        if (lessons.get(i).getName().contains("(")) {
            String[] parts = lessons.get(i).getName().split("\\(");
            return parts[1].replace(")", "") + " " + parts[0];
        }
        return lessons.get(i).getName();
    }

    public boolean isInResolution() {
        return inResolution;
    }
}
