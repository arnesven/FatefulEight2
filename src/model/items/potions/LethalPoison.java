package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.PoisonCondition;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LethalPoison extends ThrowablePotion {

    private static final Sprite SPRITE = new ItemSprite(13, 6, MyColors.WHITE, MyColors.DARK_PURPLE);
    private final int damage;

    public LethalPoison() {
        super("Lethal Poison", 48);
        this.damage = 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "A very potent and deadly poison";
    }

    @Override
    public Item copy() {
        return new LethalPoison();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(-gc.getHP() + 1);
        gc.addCondition(new PoisonCondition());
        return gc.getName() + " drank the deadly poison!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return true;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public void throwYourself(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(target.getName() + " was hit by the " + getName() + ", took " + damage + " damage.");
        combat.addFloatyDamage(target, damage, DamageValueEffect.MAGICAL_DAMAGE);
        combat.doDamageToEnemy(target, damage, performer);
    }
}
