package model.quests.scenes;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.quests.QuestNode;
import model.quests.QuestSubScene;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class TrapSubScene extends QuestSubScene {
    private static final Sprite32x32 SPRITE = new Sprite32x32("trapsubscene", "quest.png", 0x07,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);


    public TrapSubScene(int col, int row) {
        super(col, row);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Trap of 1D10-4 damage.";
    }

    @Override
    public QuestNode run(Model model, QuestState state) {
        state.print("Each party member takes 1D10-4 damage. Press enter to continue.");
        state.waitForReturn();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            int result = Math.max(0, MyRandom.rollD10()-4);
            gc.addToHP(-result);
            state.println(gc.getName() + " takes " + result + " damage.");
        }
        model.setGameOver(model.getParty().isWipedOut());
        return getSuccessConnection();
    }
}
