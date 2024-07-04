package model.states.battle;

import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

import java.util.List;

public class ArchersUnit extends BattleUnit {
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;
    private final Sprite[] spritesTwo;
    private final MyColors color;

    public ArchersUnit(int count, String origin, MyColors color) {
        super("Archers", count, 0, 5, 5, origin, 28, 4, 8);
        color = fixColor(color);
        this.spritesSeven = makeSpriteSet(3, 0, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesFour = makeSpriteSet(3, 4, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesTwo = makeSpriteSet(3, 8, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.color = color;
    }

    @Override
    public List<BattleAction> getBattleActions(BattleState battleState) {
        return List.of(new MoveOrAttackBattleAction(this),
                new ShootBattleAction(this, battleState));
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this,
                "Archers are trained marksmen, wielding bows and crossbows. " +
                         "Archers can perform ranged attacks at a range of " + ShootBattleAction.SHORT_RANGE +
                        " (" + ShootBattleAction.LONG_RANGE + " if standing on a hill).");
    }

    @Override
    public MyColors getColor() {
        return color;
    }

    @Override
    protected BattleUnit copyYourself() {
        return new ArchersUnit(getCount(), getOrigin(), color);
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
}
