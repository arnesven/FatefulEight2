package model.states.battle;

import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class KnightsUnit extends BattleUnit implements MountedBattleUnit {
    private final Sprite[] spritesFew;
    private final Sprite[] sprites;

    public KnightsUnit(int count, String origin, MyColors color) {
        super("Knights", count, 3, 10, 10, origin);
        this.sprites = makeSpriteSet(2, 0, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
        this.spritesFew = makeSpriteSet(2, 4, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
    }

    @Override
    protected boolean hasFirstStrike() {
        return true;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Knights are fast, heavily armored fighting behemoths with First Strike. " +
                "When attacking, units with First Strike have their hits applied to the defending unit before the defender makes its counter attack.");
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 4) {
            return spritesFew;
        }
        return sprites;
    }
}
