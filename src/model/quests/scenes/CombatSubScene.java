package model.quests.scenes;

import model.Model;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.quests.QuestSubScene;
import model.states.CombatEvent;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public abstract class CombatSubScene extends QuestSubScene {

    private static final Sprite32x32 SPRITE = new Sprite32x32("combatsubscene", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private final List<Enemy> enemies;

    public CombatSubScene(int col, int row, List<Enemy> enemies) {
        super(col, row);
        this.enemies = enemies;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
        Sprite enemyAvatar = enemies.get(0).getAvatar();
        model.getScreenHandler().register(enemyAvatar.getName(), new Point(xPos, yPos), enemyAvatar);
    }

    @Override
    public final String getDescription() {
        return "Combat " + getCombatDetails();
    }

    protected abstract String getCombatDetails();

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.print("The party encounters " + getCombatDetails() + "! Press enter to continue.");
        state.waitForReturn();
        CombatEvent combat = new CombatEvent(model, enemies);
        combat.run(model);
        state.transitionToQuestView(model);
        return getSuccessEdge();
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }
}
