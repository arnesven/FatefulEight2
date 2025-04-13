package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.GiantGrowthCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.GreenSpellSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class GiantGrowthSpell extends CombatSpell {
    public static final String SPELL_NAME = "Giant Growth";
    private static final Sprite SPRITE = new GreenSpellSprite(4, true);


    public GiantGrowthSpell() {
        super(SPELL_NAME, 20, MyColors.GREEN, 8, 2);
    }

    public static String getMagicExpertTips() {
        return "A combatant targeted with Giant Growth will deal more damage in combat. How much damage? It depends on " +
                "your level of mastery with the spell";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GiantGrowthSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter && !target.hasCondition(GiantGrowthCondition.class);
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        target.addCondition(new GiantGrowthCondition(2 + getMasteryLevel(performer)));
        target.addToHP(2 + getMasteryLevel(performer));
        combat.addSpecialEffect(target, new UpArrowAnimation());
    }

    @Override
    public String getDescription() {
        return "Targets HP is increased by 2 and gets +2 to attack rolls this combat.";
    }
}
