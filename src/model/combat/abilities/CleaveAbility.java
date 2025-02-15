package model.combat.abilities;

import model.Model;
import model.actions.StaminaCombatAbility;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.weapons.AxeWeapon;
import model.states.CombatEvent;
import view.help.CleaveHelpDialog;
import view.help.HelpDialog;
import view.sprites.DamageValueEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CleaveAbility extends StaminaCombatAbility implements SkillAbilityCombatAction {
    public static final int AXE_RANKS_REQUIRED = 5;

    public CleaveAbility() {
        super("Cleave", true);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new CleaveHelpDialog(model.getView());
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().cleaveAbility(model);
        int hpBefore = target.getHP();
        performer.doOneAttack(model, combat, target, false, 2,
                performer.getEquipment().getWeapon().getCriticalTarget());
        int damage = (int)(Math.ceil((hpBefore - target.getHP()) / 2.0));
        List<Enemy> enemies = new ArrayList<>(combat.getEnemies());
        enemies.remove(target);
        Collections.shuffle(enemies);
        for (int i = 0; damage > 0 && i < 2 && !enemies.isEmpty(); ++i) {
            target = enemies.remove(0);
            combat.println(target.getName() + " takes " + damage + " damage from Cleave.");
            combat.addFloatyDamage(target, damage, DamageValueEffect.STANDARD_DAMAGE);
            combat.doDamageToEnemy(target, damage, performer);
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return hasRequiredRanks(performer);
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return target instanceof Enemy &&
                target.canBeAttackedBy(performer) &&
                performer.getEquipment().getWeapon().isOfType(AxeWeapon.class);
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.Axes);
    }

    @Override
    public int getRequiredRanks() {
        return AXE_RANKS_REQUIRED;
    }
}
