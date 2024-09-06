package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.weapons.BowWeapon;
import model.items.weapons.Weapon;
import model.states.CombatEvent;
import view.help.HelpDialog;
import view.help.MultiShotHelpChapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiShotCombatAction extends StaminaCombatAbility {
    public static final String MULTI_SHOT = "Multi-Shot";
    public static final int BOW_RANKS_REQUIRED = 5;

    public MultiShotCombatAction() {
        super(MULTI_SHOT, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new MultiShotHelpChapter(model.getView());
    }

    @Override
    protected void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.print(performer.getFirstName() + " performs a Multi-Shot");
        int bonus = performer.getAttackBonusesFromConditions();
        Weapon weapon = performer.getEquipment().getWeapon();
        SkillCheckResult result = performer.testSkill(model, weapon.getSkillToUse(performer),
                SkillCheckResult.NO_DIFFICULTY, bonus);
        performer.applyAttack(model, combat, target, false, 0,
                weapon.getCriticalTarget(), weapon.getEffectSprite(), result);
        List<Enemy> enemies = new ArrayList<>(combat.getEnemies());
        enemies.remove(target);
        Collections.shuffle(enemies);
        for (int i = 0; i < 4 && !enemies.isEmpty(); ++i) {
            target = enemies.remove(0);
            performer.applyAttack(model, combat, target, false, 0,
                    weapon.getCriticalTarget(), weapon.getEffectSprite(), result);
        }
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(Skill.Bows) >= BOW_RANKS_REQUIRED;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return target instanceof Enemy && performer.getEquipment().getWeapon().isOfType(BowWeapon.class);
    }

}
