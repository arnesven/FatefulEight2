package model.quests.scenes;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.quests.QuestSubScene;
import model.states.DailyEventState;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class TrapSubScene extends QuestSubScene {
    private static final Sprite32x32 SPRITE = new Sprite32x32("trapsubscene", "quest.png", 0x07,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    private final int damage;

    public TrapSubScene(int col, int row, int trapStrength) {
        super(col, row);
        this.damage = trapStrength;
    }

    public TrapSubScene(int col, int row) {
        this(col, row, 4);
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }

    @Override
    public String getDescription() {
        return "Trap of 1D10-" + damage + " damage.";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.print("Each party member takes 1D10-" + damage + " damage. Press enter to continue.");
        state.waitForReturn();
        List<GameCharacter> victims = new ArrayList<>(model.getParty().getPartyMembers());
        for (GameCharacter gc : victims) {
            int result = Math.max(0, MyRandom.rollD10()-damage);
            gc.addToHP(-result);
            if (gc.isDead()) {
                state.println("The trap killed " + gc.getName() + "!");
                if (!DailyEventState.didResurrect(model, state, gc)) {
                    model.getParty().remove(gc, true, false, 0);
                }
            } else {
                state.println(gc.getName() + " takes " + result + " damage.");
            }
        }
        model.setGameOver(model.getParty().isWipedOut());
        return getSuccessEdge();
    }

    @Override
    public String getDetailedDescription() {
        return "Trap";
    }
}
