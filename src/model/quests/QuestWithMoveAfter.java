package model.quests;

import model.Model;
import model.states.GameState;
import model.states.QuestState;
import model.states.events.MoveAwayFromCurrentPositionEvent;
import view.BorderFrame;
import view.MyColors;

public abstract class QuestWithMoveAfter extends Quest {

    private final int moveSteps;

    public QuestWithMoveAfter(int moveSteps, String name, String giver, QuestDifficulty difficulty, int rep, int gold, int exp, String intro, String outro) {
        super(name, giver, difficulty, rep, gold, exp, intro, outro);
        this.moveSteps = moveSteps;
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        y += 1;
        BorderFrame.drawString(model.getScreenHandler(), "After", x, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), "Move " + moveSteps, x, y++, MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        super.endOfQuest(model, state, questWasSuccess);
        MoveAwayFromCurrentPositionEvent event = new MoveAwayFromCurrentPositionEvent(model, moveSteps);
        event.doTheEvent(model);
        return model.getCurrentHex().getEveningState(model, false, false);
    }
}
