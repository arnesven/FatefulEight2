package model.ruins.objects;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.SingleItemCombatLoot;
import model.items.Item;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import view.sprites.AnimationManager;
import view.sprites.SmokePuffAnimation;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.Random;

public class HiddenChestObject extends DungeonChest {
    private boolean isHidden;
    private final Item innerItem;
    private SmokePuffAnimation animation;

    public HiddenChestObject(Item innerItem) {
        super(new Random(4321));
        this.isHidden = true;
        this.innerItem = innerItem;
        setLocked(false);
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        this.animation = new SmokePuffAnimation();
        SoundEffects.playBamf();
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        if (!isHidden) {
            super.drawYourself(drawer, xPos, yPos, theme);
        } else if (animation != null) {
            if (animation.isDone()) {
                isHidden = false;
                AnimationManager.unregister(animation);
            } else {
                drawer.register(animation.getName(), new Point(xPos, yPos), animation);
            }
        }
    }

    @Override
    protected CombatLoot getLoot(Model model) {
        return new SingleItemCombatLoot(innerItem);
    }

}
