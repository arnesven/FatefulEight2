package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;

public class SleepingPotion extends ThrowablePotion {

    private static final Sprite SPRITE = new ItemSprite(15, 7, MyColors.WHITE, MyColors.LIGHT_RED);

    public SleepingPotion() {
        super("Sleeping Potion", 16);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", a draft that makes you sleepy.";
    }

    @Override
    public Item copy() {
        return new SleepingPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return false;
    }

    @Override
    public void throwYourself(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(target.getName() + " fell asleep!");
        target.addCondition(new TimedParalysisCondition());
        combat.addSpecialEffect(target, new SmokeBallAnimation());
    }

}
