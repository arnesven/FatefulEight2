package model.classes;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.StaminaRecoveryItem;
import model.items.potions.Potion;
import model.states.GameState;
import util.MyLists;
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
                if (MyLists.any(model.getParty().getInventory().getAllItems(),
                        (Item p) -> p instanceof StaminaRecoveryItem)) {
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
                } else { // No potions
                    break;
                }
            }
            // Has SP to spend
            if (declinedReRoll(event, performer)) {
                break;
            }
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
}
