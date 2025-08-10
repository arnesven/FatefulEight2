package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.potions.Potion;
import model.items.potions.UnstablePotion;
import model.items.spells.ErodeSpell;
import model.items.spells.Spell;
import model.items.weapons.GrandMaul;
import model.items.weapons.Pickaxe;
import model.items.weapons.RustyPickaxe;
import model.items.weapons.Weapon;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import model.states.GameState;
import sound.SoundEffects;
import util.MyLists;
import view.sprites.ExplosionAnimation;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrackedWall extends DungeonDoor {

    private static final int BREAK_DOWN_DIFFICULTY = 8;
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
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        super.drawYourself(drawer, xPos, yPos, theme);
        if (explo != null && !explo.isDone()) {
            drawer.register(explo.getName(), new Point(xPos, yPos), explo);
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
        for (Spell sp : model.getParty().getSpells()) {
            if (sp instanceof ErodeSpell) {
                erodeSpell = (ErodeSpell) sp;
            }
        }

        List<String> options = new ArrayList<>();
        if (pot != null) {
            options.add("Throw " + pot.getName());
        }
        if (erodeSpell != null) {
            options.add("Cast " + erodeSpell.getName());
        }
        if (MyLists.any(model.getParty().getPartyMembers(), this::hasBreakDownWeapon)) {
            options.add("Use Weapon");
        }

        if (options.isEmpty()) {
            state.println("This wall is cracked and could probably be breached if " +
                    "you had some mining gear or explosives.");
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
        if (selected[0].contains("Throw")) {
            model.getParty().getInventory().remove(pot);
            explodeAndSound(state);
            state.println("The " + pot.getName() + " explodes on contact with the wall. " +
                    "The wall crumbles and opens a passage into the next room.");
            state.unlockDoor(model, direction);
        } else if (selected[0].contains("Cast")) {
            state.println("Select who will cast " + erodeSpell.getName());
            GameCharacter caster = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());
            boolean success = erodeSpell.castYourself(model, state, caster);
            if (success) {
                explodeAndSound(state);
                state.println("The wall crumbles to dust before your eyes.");
                state.unlockDoor(model, direction);
            }
        } else if (selected[0].contains("Use")) {
            state.println("Who should attempt to break down the wall with his or her equipped weapon?");
            GameCharacter user = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
            if (!hasBreakDownWeapon(user)) {
                state.println(user.getName() + " doesn't have a suitable weapon for this.");
                return;
            }
            if (user.getSP() == 0) {
                state.println(user.getFirstName() + " is too exhausted to break down the cracked wall.");
            } else {
                state.println(user.getFirstName() + " lost 1 SP.");
                user.addToSP(-1);
                SkillCheckResult result = user.testSkill(model, Skill.Labor, BREAK_DOWN_DIFFICULTY);
                if (result.isSuccessful()) {
                    state.println(user.getName() + " broke down the wall with " +
                            user.getEquipment().getWeapon().getName() + ". (Labor " + result.asString() + ")");
                    explodeAndSound(state);
                    state.unlockDoor(model, direction);
                } else {
                    state.println(user.getName() + " failed to brake down the wall. (Labor " + result.asString() + ")");
                }
            }
        }
    }

    private boolean hasBreakDownWeapon(GameCharacter user) {
        return MyLists.any(List.of(GrandMaul.class, Pickaxe.class, RustyPickaxe.class),
                cls -> user.getEquipment().getWeapon().isOfType(cls));
    }

    private void explodeAndSound(GameState state) {
        if (state instanceof ExploreRuinsState) {
            ExploreRuinsState ruinsState = (ExploreRuinsState) state;
            if (ruinsState.getCurrentRoom().shouldObjectBeDrawnFromCurrent(this)) {
                explo = new ExplosionAnimation();
                SoundEffects.playBoom();
                state.waitUntil(explo, RunOnceAnimationSprite::isDone);
            } else {
                if (getLinkedDoor() instanceof CrackedWall) {
                    ((CrackedWall) getLinkedDoor()).explodeAndSound(state);
                }
            }
        }
    }

}
