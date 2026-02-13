package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.EnemyPoisonCondition;
import model.combat.conditions.PoisonCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CommonPoison extends PoisonPotion {

    private static final Sprite SPRITE = new ItemSprite(7, 16, MyColors.WHITE, MyColors.DARK_GREEN);

    public CommonPoison() {
        super("Poison", 16);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Toxic when consumed, applies Poisoned condition when thrown.";
    }

    @Override
    public Item copy() {
        return new CommonPoison();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        if (!gc.hasCondition(PoisonCondition.class)) {
            if (!didResistWeakPotion(gc)) {
                gc.addCondition(new PoisonCondition());
                return gc.getName() + " has become poisoned!";
            }
            return gc.getName() + " resisted the poison.";
        }
        return getName() + " had no effect on " + gc.getName() + ".";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return true;
    }

    @Override
    public void throwYourself(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof GameCharacter) {
            useYourself(model, (GameCharacter) target);
        } else {
            target.addCondition(new EnemyPoisonCondition(performer));
        }
    }
}
