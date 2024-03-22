package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.WerewolfFormCondition;
import model.items.Item;
import model.states.CombatEvent;
import util.MyStrings;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class WerewolfFormSpell extends CombatSpell {

    private static final Sprite SPRITE = new CombatSpellSprite(13, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);
    public static final int TURNS = 5;

    public WerewolfFormSpell() {
        super("Werewolf Form", 40, MyColors.GREEN, 8, 4, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WerewolfFormSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    protected boolean masteriesEnabled() {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (!performer.hasCondition(WerewolfFormCondition.class)) {
            performer.addCondition(new WerewolfFormCondition(performer, getMasteryLevel(performer) + 1));
            performer.getEquipment().getWeapon().setBurning(true);
        } else {
            combat.println(getName() + " has no effect on " + performer.getName() + ".");
        }
    }

    @Override
    public String getDescription() {
        return "Transforms the caster into a werewolf for " + MyStrings.numberWord(TURNS) + " turns.";
    }
}
