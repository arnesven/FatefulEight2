package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public abstract class WeaponImbuement {
    private static final Sprite BURNING_SPRITE = CharSprite.make(0xDD, MyColors.LIGHT_GRAY, MyColors.RED, MyColors.BEIGE);

    public int[] makeDamageTable(int[] damageTable) {
        return damageTable;
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.put(col, row, BURNING_SPRITE);
    }

    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {

    }

    public abstract String getText();

    public int getAttackBonus() {
        return 0;
    }
}
