package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.AltarEnemy;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.BindDaemonAnimation;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class BindDaemonSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(8, 8, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);

    // TODO: Add to some quests
    public BindDaemonSpell() {
        super("Bind Daemon", 14, MyColors.RED, 13, 3, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BindDaemonSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (!(target instanceof AltarEnemy)) {
            combat.println("The spell had no effect on " + target.getName() + "!");
            return;
        }
        combat.println(target.getName() + " has been bound by the effects of the spell. " +
                performer.getName() + " compels " + target.getName() + " to return to the demonic realm.");
        combat.addSpecialEffect(target, new BindDaemonAnimation());
        combat.retreatEnemy(target);
    }

    @Override
    public String getDescription() {
        return "Binds a daemon and compels it to return to its demonic realm.";
    }
}
