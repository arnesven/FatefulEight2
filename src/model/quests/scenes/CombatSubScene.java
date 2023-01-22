package model.quests.scenes;

import model.Model;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.quests.QuestSubScene;
import model.states.CombatEvent;
import model.states.QuestState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DungeonTheme;

import java.awt.*;
import java.util.List;

public abstract class CombatSubScene extends QuestSubScene {

    private static final Sprite32x32 SPRITE = new Sprite32x32("combatsubscene", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private final List<Enemy> enemies;
    private final boolean fleeingEnabled;
    private boolean defeated = false;

    public CombatSubScene(int col, int row, List<Enemy> enemies, boolean fleeingEnabled) {
        super(col, row);
        this.enemies = enemies;
        this.fleeingEnabled = fleeingEnabled;
    }

    public CombatSubScene(int col, int row, List<Enemy> enemies) {
        this(col, row, enemies, false);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
        if (!hasBeenDefeated()) {
            Sprite enemyAvatar = enemies.get(0).getAvatar();
            int xOff = 0;
            if (enemyAvatar.getWidth() > SPRITE.getWidth()) {
                xOff = (SPRITE.getWidth() - enemyAvatar.getWidth()) / 8 / 2;
            }
            model.getScreenHandler().register(enemyAvatar.getName(), new Point(xPos+xOff, yPos), enemyAvatar);
        }
    }

    private boolean hasBeenDefeated() {
        return defeated;
    }
    public void setDefeated(boolean d) {
        defeated = d;
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
        CombatEvent combat = new CombatEvent(model, enemies, state.getCombatTheme(), fleeingEnabled);
        combat.run(model);
        state.transitionToQuestView(model);
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
        if (combat.fled()) {
            return getFailEdge();
        }
        defeated = true;
        return getSuccessEdge();
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }
}
