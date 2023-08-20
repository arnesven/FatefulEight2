package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class UnstablePotion extends ThrowablePotion {

    private static final Sprite SPRITE = new ItemSprite(15, 7, MyColors.WHITE, MyColors.ORANGE);
    private final int damage;

    public UnstablePotion() {
        super("Unstable Potion", 16);
        damage = 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Explodes on impact when thrown at an enemy.";
    }

    @Override
    public Item copy() {
        return new UnstablePotion();
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
        combat.println(target.getName() + " was hit by the " + getName() + ", took " + damage + " damage.");
        combat.addFloatyDamage(target, damage, true);
        combat.doDamageToEnemy(target, damage, performer);
        SoundEffects.playBoom();
    }
}
