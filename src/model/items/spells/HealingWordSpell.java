package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class HealingWordSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(1, 8, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);

    public HealingWordSpell() {
        super("Healing Word", 16, MyColors.WHITE, 8, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new HealingWordSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter && target.getHP() < target.getMaxHP() && !target.isDead();
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int hpBefore = target.getHP();
        target.addToHP(6 + getMasteryLevel(performer) * 2);
        int totalRecovered = target.getHP() - hpBefore;
        combat.println(target.getName() + " recovers " + totalRecovered + " HP!");
        combat.addSpecialEffect(target, new ShinyRingEffect());
        combat.addFloatyDamage(target, totalRecovered, DamageValueEffect.HEALING);
        if (target != performer) {
            model.getParty().partyMemberSay(model, (GameCharacter) target,
                    List.of("Thank you so much!3", "Much obliged!3",
                            "I really needed that!3", "Aah, I feel great!3"));
        }
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "A soothing incantation which restores 6 HP of a character.";
    }

}
