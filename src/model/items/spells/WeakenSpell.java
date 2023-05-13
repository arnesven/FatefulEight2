package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.WeakenCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DownArrowAnimation;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class WeakenSpell extends CombatSpell {
    private static final Sprite SPRITE = new ItemSprite(12, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public WeakenSpell() {
        super("Weaken", 18, MyColors.BLACK, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WeakenSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getName() + " has weakened the enemy.");
        List<Enemy> targets = getTargets(combat, target, 4);
        for (Enemy e : targets) {
            e.addCondition(new WeakenCondition());
            combat.addSpecialEffect(e, new DownArrowAnimation());
        }
    }

    @Override
    public String getDescription() {
        return "Weakens up to 4 enemies, reducing their damage for 4 turns.";
    }

}
