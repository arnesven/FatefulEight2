package model.states.battle;

import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class GoblinWolfRiderUnit extends GoblinBattleUnit implements MountedBattleUnit {

    private static final Sprite[] spritesSix = makeSpriteSet(8, 0, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.GRAY);
    private static final Sprite[] spritesFour = makeSpriteSet(8, 4, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.GRAY);
    private static final Sprite[] spritesTwo = makeSpriteSet(8, 8, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.GRAY);

    public GoblinWolfRiderUnit(int count) {
        super("Wolf Riders", count, 1, 7, 12, 14, 2);
    }

    @Override
    protected boolean hasFirstStrike() {
        return true;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Wolf Riders are very fast, light cavalry with First Strike. " +
                "When attacking, units with First Strike have their hits applied to the defending unit before the defender makes its counter attack. Wolf Riders units " +
                "will be routed if falling below " + MyStrings.numberWord(getRoutThreshold()) + " combatants.");
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() >= 6) {
            return spritesSix;
        }
        if (getCount() >= 4) {
            return spritesFour;
        }
        return spritesTwo;
    }

    @Override
    public Enemy makeEnemy() {
        return new WolfEnemy('A'); // TODO
    }

    @Override
    protected BattleUnit copyYourself() {
        return new GoblinWolfRiderUnit(getCount());
    }
}
