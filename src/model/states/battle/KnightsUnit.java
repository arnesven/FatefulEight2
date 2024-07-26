package model.states.battle;

import model.enemies.Enemy;
import model.enemies.MountedEnemy;
import model.enemies.SoldierEnemy;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class KnightsUnit extends BattleUnit implements MountedBattleUnit {
    private final Sprite[] spritesFew;
    private final Sprite[] sprites;
    private final MyColors color;

    public KnightsUnit(int count, String origin, MyColors color) {
        super("Knights", count, 3, 10, 10, origin, 12, 1, 4);
        this.sprites = makeSpriteSet(2, 0, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
        this.spritesFew = makeSpriteSet(2, 4, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
        this.color = color;
    }

    @Override
    protected boolean hasFirstStrike() {
        return true;
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Knights are fast, heavily armored, mounted fighting behemoths with First Strike. " +
                "When attacking, units with First Strike have their hits applied to the defending unit before the defender makes its counter attack.");
    }

    @Override
    public MyColors getColor() {
        return color;
    }

    @Override
    protected BattleUnit copyYourself() {
        return new KnightsUnit(getCount(), getOrigin(), color);
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 4) {
            return spritesFew;
        }
        return sprites;
    }

    @Override
    public Enemy makeEnemy() {
        return new KnightEnemy();
    }

    private static class KnightEnemy extends MountedEnemy {
        public KnightEnemy() {
            super(new SoldierEnemy('A'));
        }

        @Override
        public String getName() {
            return "Knight";
        }

        @Override
        public int getDamage() {
            return 5;
        }

        @Override
        public int getDamageReduction() {
            return 1;
        }
    }

}
