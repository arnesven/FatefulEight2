package model.classes;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.StaminaRecoveryItem;
import model.items.weapons.UnarmedCombatWeapon;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.events.CheckForVampireEvent;
import util.MyLists;
import util.MyRandom;
import view.InventoryView;

public class SkillChecks {
    public static SkillCheckResult doSkillCheckWithReRoll(Model model, GameState event,
                                                          GameCharacter performer, Skill skill, int difficulty, int exp, int bonus) {
        SkillCheckResult result;
        do {
            result = performer.testSkill(model, skill, difficulty, bonus);
            event.println(performer.getFirstName() + " performs " + skill.getName() + " " + result.asString());
            if (result.isSuccessful()) {
                if (exp > 0) {
                    model.getParty().giveXP(model, performer, exp);
                }
                break;
            }
            if (performer.getSP() == 0) {
                if (!model.isInCombat() && MyLists.any(model.getParty().getInventory().getAllItems(),
                        (Item p) -> p instanceof StaminaRecoveryItem) &&
                        !CheckForVampireEvent.isVampire(performer)) {
                    event.print(performer.getFirstName() + " is out of Stamina, but you " +
                            "have items in your inventory which you can raise stamina. " +
                            "Do you want to use one? (Y/N) ");
                    if (event.yesNoInput()) {
                        model.transitionToDialog(new InventoryView(model.getView()));
                    } else {
                        break;
                    }
                    event.print(performer.getFirstName());
                    model.getLog().waitForAnimationToFinish();
                    if (performer.getSP() == 0) {
                        event.println(" is still out of Stamina.");
                        break;
                    } else {
                        event.println(" now has Stamina.");
                    }
                } else { // No potions or in combat
                    break;
                }
            }
            // Has SP to spend
            if (declinedReRoll(event, performer)) {
                break;
            }
            GameStatistics.incrementRerollsUsed();
        } while (true);
        return result;
    }

    private static boolean declinedReRoll(GameState event, GameCharacter performer) {
        event.print("Use 1 Stamina Point to re-roll? (Y/N) ");
        if (event.yesNoInput()) {
            performer.addToSP(-1);
            return false;
        }
        return true;
    }

    public static int adjustDifficulty(Model model, int difficulty) {
        return difficulty + model.getSettings().getGameDifficulty() - 1;
    }

    public static int doDamageToDoor(Model model, GameState state) {
        state.println("Who should try to break down the door.");
        GameCharacter performer = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());
        if (performer.getEquipment().getWeapon() instanceof UnarmedCombatWeapon) {
            state.println("This character has nothing to break down the door with.");
            return 0;
        }
        performer.addToSP(-1);
        SkillCheckResult result = performer.testSkill(model, performer.getEquipment().getWeapon().getSkillToUse(performer));
        int damage = performer.getEquipment().getWeapon().getDamage(result.getModifiedRoll(), performer);
        state.println(performer.getName() + " did " + damage + " damage to the door.");
        if (MyRandom.rollD10() == 1) {
            state.println("The " + performer.getEquipment().getWeapon().getName() + " broke!");
            performer.getEquipment().setWeapon(new UnarmedCombatWeapon());
        }
        return damage;
    }
}
