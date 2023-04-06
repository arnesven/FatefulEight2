package model.states;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Skill;
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
    private static final int TRAINING_COLUMNS = 4;
    private static final List<Skill> allMartialSkills = List.of(Skill.Axes, Skill.Blades, Skill.BluntWeapons, Skill.Bows, Skill.Polearms);
    private static final List<Skill> allArcaneSkills = List.of(Skill.MagicBlue, Skill.MagicWhite, Skill.MagicBlack, Skill.MagicRed, Skill.MagicGreen);
    private final List<Skill> lessons = new ArrayList<>();
    private SteppingMatrix<GameCharacter> matrix;

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
            print("You cannot afford the tuition fea, which is 5 gold.");
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
        waitForReturn();

        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void addPartyRandomlyToMatrix(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            Point p;
            do {
                p = new Point(MyRandom.randInt(TRAINING_COLUMNS), MyRandom.randInt(TRAINING_ROWS));
            } while (matrix.getElementAt(p.x, p.y) != null);
            matrix.addElement(p.x, p.y, gc);
        }
    }

    public List<Skill> getLessons() {
        return lessons;
    }
}
