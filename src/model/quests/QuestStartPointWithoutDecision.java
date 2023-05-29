package model.quests;

import model.Model;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class QuestStartPointWithoutDecision extends SimpleJunction {
    private final Sprite32x32 SPRITE = new Sprite32x32("startpointnol", "quest.png", 0x15, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    private final String leaderTalk;

    public QuestStartPointWithoutDecision(QuestEdge questEdge, String leaderTalk) {
        super(0, 0, questEdge);
        this.leaderTalk = leaderTalk;
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderTalk.equals("")) {
            state.leaderSay(leaderTalk);
        }
        return super.run(model, state);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }
}
