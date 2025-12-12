package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.RedSpellSprite;
import view.sprites.Sprite;

public class FireWallSpell extends CombatSpell {

    public static final String SPELL_NAME = "Fire Wall";
    private static final Sprite SPRITE = new RedSpellSprite(6, true);

    public FireWallSpell() {
        super(SPELL_NAME, 28, MyColors.RED, 9, 3, false);
    }

    public static String getMagicExpertTips() {
        return "The damage of Fire Wall is based on your level of mastery with the spell.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public Item copy() {
        return new FireWallSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getName() + " summons a wall of flames!");
        combat.setFlameWall(performer, 1 + getMasteryLevel(performer));
    }

    @Override
    public String getDescription() {
        return "Conjures a barrier of fire for one combat turn, " +
                "damaging combatants that come close.";
    }
}
