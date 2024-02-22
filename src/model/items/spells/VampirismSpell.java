package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.enemies.AutomatonEnemy;
import model.enemies.Enemy;
import model.enemies.UndeadEnemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class VampirismSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(1, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public VampirismSpell() {
        super("Vampirism", 18, MyColors.BLACK, 8, 3, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new VampirismSpell();
    }

    @Override
    protected boolean masteriesEnabled() {
        return true;
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof UndeadEnemy || target instanceof AutomatonEnemy) {
            combat.println(getName() + " had no effect on " + target.getName() + ".");
            return;
        }
        int damage = 4 + getMasteryLevel(performer) * 3;
        combat.addFloatyDamage(target, damage, DamageValueEffect.MAGICAL_DAMAGE);
        SkillCheckResult result = performer.testSkill(Skill.MagicBlack);
        combat.println(performer.getName() + " tests " + Skill.MagicBlack.getName() + " to determine how much " +
                "health points can be absorbed, " + result.asString() + " (of 12).");
        double ratio = Math.min(1.0, result.getModifiedRoll() / 12.0);
        int totalRecovered = (int)Math.round(ratio * damage);
        if (totalRecovered > 0) {
            combat.addFloatyDamage(performer, totalRecovered, DamageValueEffect.HEALING);
            performer.addToHP(totalRecovered);
        }
        combat.addSpecialEffect(performer, new VampirismEffect());
        combat.println(target.getName() + " took " + damage + " from " + getName() + ", " +
                performer.getName() + " absorbed " + totalRecovered + " of the damage as health points!");
    }

    @Override
    public String getDescription() {
        return "Absorbs up to 4 health points from the target.";
    }
}
