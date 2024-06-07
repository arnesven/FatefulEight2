package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.items.spells.ChainLightningSpell;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class LightningJavelins extends PolearmWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 4, MyColors.BROWN, MyColors.CYAN, MyColors.BEIGE);
    private final ChainLightningSpell spell;

    public LightningJavelins() {
        super("Lightning Javelins", 128, new int[]{10, 10, 10});
        this.spell = new ChainLightningSpell();
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.flipCoin()) {
            spell.applyCombatEffect(model, combatEvent, gameCharacter, target);
        }
    }

    @Override
    public String getExtraText() {
        return ", 50% chance to cast chain lightning on hit.";
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
        return new LightningJavelins();
    }
}
