package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.DamageValueEffect;

import java.util.List;

public class ImprovisedWand extends OldWand {

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        super.didOneAttackWith(model, combatEvent, gameCharacter, target, damage, critical);
        if (MyRandom.rollD6() == 1) {
            combatEvent.println("The improvised wand is heating up rapidly!");
            combatEvent.partyMemberSay(gameCharacter, MyRandom.sample(List.of("Help!", "What's with this thing?", "Aaaah!", "Youch! It's hot!")));
            model.getLog().waitForAnimationToFinish();
            SkillCheckResult result = gameCharacter.testSkill(model, Skill.MagicAny, 10);
            if (result.isSuccessful()) {
                combatEvent.println("But " + gameCharacter.getName() + " managed to contain the effect (" + result.asString() + ").");
            } else {
                combatEvent.println("It exploded!");
                gameCharacter.takeCombatDamage(combatEvent, 4, gameCharacter);
                combatEvent.addFloatyDamage(gameCharacter, 4, DamageValueEffect.MAGICAL_DAMAGE);
                if (gameCharacter.getEquipment().getWeapon() instanceof WeaponPair) {
                    WeaponPair pair = (WeaponPair) gameCharacter.getEquipment().getWeapon();
                    pair.splitToInventory(model);
                } else {
                    gameCharacter.unequipWeapon();
                }
                model.getParty().getInventory().remove(this);
            }
        }
    }
}
