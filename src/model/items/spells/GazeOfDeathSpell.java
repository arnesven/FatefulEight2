package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class GazeOfDeathSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(3, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public GazeOfDeathSpell() {
        super("Gaze of Death", 26, MyColors.BLACK, 8, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GazeOfDeathSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int difficulty = Math.max(target.getMaxHP(), 11);
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, combat, performer, Skill.SpellCasting, difficulty, 0, 0);
        if (result.isSuccessful()) {
            combat.println("Gaze of Death kills " + target.getName() + "!");
            combat.doDamageToEnemy(target, target.getHP(), performer);
        } else {
            combat.println("Gaze of Death did not affect " + target.getName() + ".");
        }
    }

    @Override
    public String getDescription() {
        return "Has a chance to immediately kill an enemy.";
    }
}
