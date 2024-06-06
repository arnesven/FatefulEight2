package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.items.potions.Potion;
import model.items.potions.UnstablePotion;
import model.items.spells.ErodeSpell;
import model.items.spells.Spell;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrackedWall extends DungeonDoor {

    private final boolean isHorizontal;
    private ExplosionAnimation explo;
    private final String direction;

    public CrackedWall(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.direction = direction;
        this.isHorizontal = isHorizontal;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getCrackedWall(isHorizontal);
    }

    @Override
    public String getDescription() {
        return "A cracked wall";
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        super.drawYourself(model, xPos, yPos, theme);
        if (explo != null && !explo.isDone()) {
            model.getScreenHandler().register(explo.getName(), new Point(xPos, yPos), explo);
        }
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        Potion pot = null;
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (p instanceof UnstablePotion) {
                pot = p;
            }
        }
        ErodeSpell erodeSpell = null;
        for (Spell sp : model.getParty().getInventory().getSpells()) {
            if (sp instanceof ErodeSpell) {
                erodeSpell = (ErodeSpell) sp;
            }
        }

        List<String> options = new ArrayList<>();
        if (pot != null) {
            options.add("Use " + pot.getName());
        }
        if (erodeSpell != null) {
            options.add("Cast " + erodeSpell.getName());
        }

        if (options.isEmpty()) {
            state.println("This wall is cracked and could probably be breached if you had some kind of explosives.");
            return;
        }
        state.println("What do you want to use on the cracked wall?");

        options.add("Cancel");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 28, 28 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        if (selected[0].contains("Use")) {
            model.getParty().getInventory().remove(pot);
            explodeAndSound();
            state.println("The " + pot.getName() + " explodes on contact with the wall. " +
                    "The wall crumbles and opens a passage into the next room.");
            state.unlockDoor(model, direction);
        } else if (selected[0].contains("Cast")) {
            state.println("Select who will cast " + erodeSpell.getName());
            GameCharacter caster = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());
            boolean success = erodeSpell.castYourself(model, state, caster);
            if (success) {
                explodeAndSound();
                state.println("The wall crumbles to dust before your eyes.");
                state.unlockDoor(model, direction);
            }
        }

    }

    private void explodeAndSound() {
        explo = new ExplosionAnimation();
        SoundEffects.playBoom();
        while (!explo.isDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ExplosionAnimation extends RunOnceAnimationSprite {
        public ExplosionAnimation() {
            super("explosion", "dungeon.png", 0, 8, 32, 32, 6, MyColors.ORANGE);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.YELLOW);
        }
    }
}
