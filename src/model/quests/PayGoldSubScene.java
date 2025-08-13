package model.quests;

import model.Model;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class PayGoldSubScene extends QuestSubScene {
    private static final Sprite32x32 SPRITE = new Sprite32x32("paygoldsubscene", "quest.png", 0x14,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private final int gold;
    private final String leaderSpeak;

    public PayGoldSubScene(int col, int row, int gold, String leaderSpeak) {
        super(col, row);
        this.gold = gold;
        this.leaderSpeak = leaderSpeak;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }

    @Override
    public String getDescription() {
        return "Pay " + gold + " gold per party member";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderSpeak.equals("")) {
            state.leaderSay(leaderSpeak);
        }
        int amount = Math.min(model.getParty().getGold(), model.getParty().size() * gold);
        state.print("Paying " + amount + " gold. Press enter to continue.");
        state.waitForReturn();
        model.getParty().spendGold(amount);
        return getSuccessEdge();
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    public String getDetailedDescription() {
        if (gold < 5) {
            return "Small Expenses";
        }
        if (gold > 9) {
            return "Large Expenses";
        }
        return "Expenses";
    }

    @Override
    protected boolean isEligibleForSelection(Model model, QuestState state) {
        int amount = model.getParty().size() * gold;
        if (amount > model.getParty().getGold()) {
            state.println("You do not have enough money!");
            return false;
        }
        return true;
    }
}
