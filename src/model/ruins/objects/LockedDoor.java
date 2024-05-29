package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.classes.SkillCheckResult;
import model.items.weapons.UnarmedCombatWeapon;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import util.MyLists;
import util.MyRandom;
import view.sprites.Sprite32x32;

import java.awt.*;

public class LockedDoor extends DungeonDoor {

    private final String direction;
    private final boolean isHorizontal;
    private boolean securityTried = false;
    private int hp = 10;
    private boolean firstBreakDown = true;

    public LockedDoor(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.direction = direction;
        this.isHorizontal = isHorizontal;
    }

    @Override
    public Sprite32x32 getSprite(DungeonTheme theme) {
        return theme.getLockedDoor(isHorizontal);
    }

    @Override
    public String getDescription() {
        return "A locked door going " + direction;
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (securityTried) {
            state.leaderSay("We're not getting anywhere with that lock.");
            if (eligibleDamagesExist(model)) {
                breakDownDoor(model, state);
            } else {
                state.println("You do not have the right kind of weapons to attempt to break down the door.");
            }
        } else {
            securityTried = true;
            boolean result = model.getParty().doSoloLockpickCheck(model, state, MyRandom.randInt(5, 7));
            if (result) {
                state.println("The door is unlocked!");
                model.getLog().waitForAnimationToFinish();
                state.unlockDoor(model, direction);
                SoundEffects.playUnlock();
            } else {
                state.println("The door remains locked");
            }
        }
    }

    private boolean eligibleDamagesExist(Model model) {
        return MyLists.any(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                        !(gc.getEquipment().getWeapon() instanceof UnarmedCombatWeapon ||
                                gc.getEquipment().getWeapon().isRangedAttack()));
    }

    private void breakDownDoor(Model model, ExploreRuinsState state) {
        if (firstBreakDown) {
            firstBreakDown = false;
            state.print("You may try to break down the door. It will consume a character's stamina and there is " +
                    "a chance your weapon will break. Do wish to try to break down the door? (Y/N) ");
        } else {
            state.print("Do wish to try to break down the door? (Y/N) ");
        }
        if (state.yesNoInput()) {
            state.println("Who should try to break down the door.");
            GameCharacter performer = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());
            performer.addToSP(-1);
            SkillCheckResult result = performer.testSkill(model, performer.getEquipment().getWeapon().getSkillToUse(performer));
            int damage = performer.getEquipment().getWeapon().getDamage(result.getModifiedRoll(), performer);
            state.println(performer.getName() + " did " + damage + " damage to the door.");
            if (MyRandom.rollD10() == 1) {
                state.println("The " + performer.getEquipment().getWeapon().getName() + " broke!");
                performer.getEquipment().setWeapon(new UnarmedCombatWeapon());
            }
            hp -= damage;
            if (hp <= 0) {
                state.println("The door shatters to pieces!");
                state.unlockDoor(model, direction);
            } else if (hp >= 8) {
                state.println("The door has a few marks.");
            } else if (hp >= 5) {
                state.println("The door is slightly damaged.");
            } else {
                state.println("THe door is starting to break apart.");
            }
        }
    }
}
