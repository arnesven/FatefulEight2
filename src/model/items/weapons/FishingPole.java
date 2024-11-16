package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class FishingPole extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(13, 4);

    public FishingPole() {
        super("Fishing Pole", 8, new int[]{9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FishingPole();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        super.didOneAttackWith(model, combatEvent, gameCharacter, target, damage, critical);
        if (MyRandom.rollD10() > 8) {
            combatEvent.println("The fishing pole is broken!");
            combatEvent.partyMemberSay(gameCharacter, MyRandom.sample(List.of("Darn it!", "That's unfortunate.", "No! My fishing pole!")));
            gameCharacter.unequipWeapon();
        }
    }
}
