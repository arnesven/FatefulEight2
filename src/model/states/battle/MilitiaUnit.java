package model.states.battle;

import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class MilitiaUnit extends BattleUnit {
    private final Sprite[] spritesEleven;
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;
    private final MyColors color;

    public MilitiaUnit(int count, String origin, MyColors color) {
        super("Militia", count, 0, 5, 6, origin, 32, 10, 20);
        color = fixColor(color);
        this.spritesEleven = makeSpriteSet(4, 0, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesSeven = makeSpriteSet(4, 4, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesFour = makeSpriteSet(4, 8, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.color = color;
    }

    @Override
    protected int getRoutThreshold() {
        return 4;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this,
                "Militia units are general infantry units. They are normally lightly equipped and can " +
                        "move quickly across the battlefield. They are normally less disciplined than other units and " +
                        "will be routed if falling below " + MyStrings.numberWord(getRoutThreshold()) + " combatants.");
    }

    @Override
    public MyColors getColor() {
        return color;
    }

    @Override
    protected BattleUnit copyYourself() {
        return new MilitiaUnit(getCount(), getOrigin(), color);
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

    @Override
    public Enemy makeEnemy() {
        return new MilitiaEnemy();
    }


    private static class MilitiaEnemy extends BanditEnemy {
        public MilitiaEnemy() {
            super('A', "Militia", 4);
        }
    }

}
