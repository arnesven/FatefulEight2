package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.GiantGrowthCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class GiantGrowthSpell extends CombatSpell {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.BEIGE, MyColors.GREEN);


    public GiantGrowthSpell() {
        super("Giant Growth", 20, MyColors.GREEN, 8, 2); // TODO: Diff 8
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
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        target.addCondition(new GiantGrowthCondition());
        target.addToHP(2);
        combat.addSpecialEffect(target, new UpArrowAnimation());
    }

    @Override
    public String getDescription() {
        return "Targets HP is increased by 2 and gets +2 to attack rolls this combat.";
    }
}