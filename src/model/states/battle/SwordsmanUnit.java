package model.states.battle;

import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class SwordsmanUnit extends BattleUnit {
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;
    private final Sprite[] spritesTwo;
    private final MyColors color;

    public SwordsmanUnit(int count, String origin, MyColors color) {
        super("Swordsmen", count, 3, 8, 4, origin, 16, 2, 6);
        this.spritesSeven = makeSpriteSet(0, 0, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.WHITE, color);
        this.spritesFour = makeSpriteSet(0, 4, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.WHITE, color);
        this.spritesTwo = makeSpriteSet(0, 8, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.WHITE, color);
        this.color = color;
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 3) {
            return spritesTwo;
        }
        if (getCount() < 7) {
            return spritesFour;
        }
        return spritesSeven;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this,
                "Swordsmen units are heavily armed and armored fighters.");
    }

    @Override
    public MyColors getColor() {
        return color;
    }

    @Override
    protected BattleUnit copyYourself() {
        return new SwordsmanUnit(getCount(), getOrigin(), color);
    }
}
