package model.states.battle;

import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class MilitiaUnit extends BattleUnit {
    private final Sprite[] spritesEleven;
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;

    public MilitiaUnit(int count, String origin, MyColors color) {
        super("Militia", count, 0, 5, 6, origin);
        color = fixColor(color);
        this.spritesEleven = makeSpriteSet(4, 0, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesSeven = makeSpriteSet(4, 4, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesFour = makeSpriteSet(4, 8, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
    }

    @Override
    protected boolean hasLowMorale() {
        return true;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this,
                "Militia units are general infantry units. They are normally lightly equipped and can " +
                        "move quickly across the battlefield. They are normally less disciplined than other units and " +
                        "will be routed if falling below four combatants.");
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 7) {
            return spritesFour;
        }
        if (getCount() < 11) {
            return spritesSeven;
        }
        return spritesEleven;
    }

}
