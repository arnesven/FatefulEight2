package model.items.special;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.spells.ImmediateSpell;
import model.quests.MindMachineQuest;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.awt.*;

public class MagicMirror extends StoryItem {

    private static final Sprite SPRITE = new ItemSprite(4, 17,
            MyColors.PURPLE, MyColors.WHITE, MyColors.CYAN);

    private final ImmediateSpell innerSpell;

    public MagicMirror() {
        super("Magic Mirror", 1);
        this.innerSpell = new MagicMirrorSpell();
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 500;
    }

    @Override
    public String getShoppingDetails() {
        return "Has a mysterious vibe...";
    }

    @Override
    public Item copy() {
        return new MagicMirror();
    }

    @Override
    public boolean canBeUsedFromMenu() {
        return true;
    }

    @Override
    public String useFromMenu(Model model, GameCharacter gc) {
        String start = gc.getName() + " peers into the Magic Mirror. ";
        if (model.getMainStory().isStarted() && model.isInOriginalWorld()) {
            String ignored = innerSpell.castFromMenu(model, gc);
            return start + "A spell is being cast...";
        }
        return start + "But nothing is happening.";
    }

    private static class MagicMirrorSpell extends ImmediateSpell {
        public MagicMirrorSpell() {
            super("Magic Mirror", 0, COLORLESS, 0, 0);
        }

        @Override
        protected boolean preCast(Model model, GameState state, GameCharacter caster) {
            return true;
        }

        @Override
        protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
            Point destination =  model.getMainStory().getPastEntryPosition();
            MindMachineQuest.teleportToOtherWorld(model, state, destination);
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        protected Sprite getSprite() {
            return null;
        }

        @Override
        public Item copy() {
            return null;
        }
    }
}
