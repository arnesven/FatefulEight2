package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SwordOfVigor extends BladedWeapon {

    private static final Sprite SPRITE = new ItemSprite(8, 0, MyColors.BLUE, MyColors.GOLD, MyColors.ORANGE);

    public SwordOfVigor() {
        super("Sword of Vigor", 110, new int[]{5, 8, 12}, false, 1);
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (damage > 0 && target.isDead()) {
            combatEvent.println(gameCharacter.getName() + " absorbed energy from the slain " + target.getName() + " - regained 1 SP!");
            gameCharacter.addToSP(1);
        }
    }

    @Override
    public String getExtraText() {
        return ", gain 1 SP on kill.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item copy() {
        return new SwordOfVigor();
    }
}
