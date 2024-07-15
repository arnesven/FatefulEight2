package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.quests.QuestSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.GameState;
import model.states.QuestState;
import view.sprites.Sprite;

import java.awt.*;

abstract class FeedingSubScene extends QuestSubScene {
    private final GameCharacter vampire;

    public FeedingSubScene(int col, int row, GameCharacter vampire) {
        super(col, row);
        this.vampire = vampire;
    }

    protected abstract QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire);

    @Override
    public String getDetailedDescription() {
        return "Unused";
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        Sprite spr = SoloSkillCheckSubScene.SPRITE;
        model.getScreenHandler().register(spr.getName(), new Point(xPos, yPos), spr);
    }

    @Override
    public String getDescription() {
        return "Unused";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        // Not used
        return null;
    }

    @Override
    public QuestEdge doAction(Model model, GameState state) {
        return specificDoAction(model, state, vampire);
    }

    protected String heOrShe(boolean gender) {
        return GameState.heOrShe(gender);
    }

    protected String hisOrHer(boolean gender) {
        return GameState.hisOrHer(gender);
    }

    protected String himOrHer(boolean gender) {
        return GameState.himOrHer(gender);
    }

}
