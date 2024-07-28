package model.states.battle;

import model.enemies.Enemy;
import model.enemies.OrcBoarRiderEnemy;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class OrcBoarRiderUnit extends OrcishBattleUnit implements MountedBattleUnit {

    private static final Sprite[] spritesFour = makeSpriteSet(9, 0, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.BROWN);
    private static final Sprite[] spritesTwo = makeSpriteSet(9, 4, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.BROWN);
    private static final Sprite[] spritesOne = makeSpriteSet(9, 8, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.BROWN);

    public OrcBoarRiderUnit(int count) {
        super("Boar Riders", count, 2, 8, 8, 16);
    }

    @Override
    protected boolean hasFirstStrike() {
        return true;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Boar Riders are cavalry with First Strike. " +
                "When attacking, units with First Strike have their hits applied to the defending unit before the defender makes its counter attack.");
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() >= 4) {
            return spritesFour;
        }
        if (getCount() >= 2) {
            return spritesTwo;
        }
        return spritesOne;
    }

    @Override
    public Enemy makeEnemy() {
        return new OrcBoarRiderEnemy('A');
    }

    @Override
    protected BattleUnit copyYourself() {
        return new OrcBoarRiderUnit(getCount());
    }
}
