package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.ShiningAegisCondition;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DownArrowAnimation;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class ShiningAegisSpell extends CombatSpell {
    private static final Sprite SPRITE = new ItemSprite(3, 8, MyColors.BROWN, MyColors.WHITE, MyColors.GOLD);
    public ShiningAegisSpell() {
        super("Shining Aegis", 12, MyColors.WHITE, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShiningAegisSpell();
    }

    @Override
    public String getDescription() {
        return "Conjures a shield of light which gives the target 3 AP for 5 rounds.";
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter && !target.hasCondition(ShiningAegisCondition.class);
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        target.addCondition(new ShiningAegisCondition());
        combat.addSpecialEffect(target, new UpArrowAnimation());
    }
}
