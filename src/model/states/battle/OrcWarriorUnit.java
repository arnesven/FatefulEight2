package model.states.battle;

import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class OrcWarriorUnit extends OrcishBattleUnit {

    private static final Sprite[] spritesSeven = makeSpriteSet(5, 0, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] spritesFour = makeSpriteSet(5, 4, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] spritesTwo = makeSpriteSet(5, 8, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);

    public OrcWarriorUnit(int count) {
        super("Warriors", count, 2, 7, 5, 24);
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
    public Enemy makeEnemy() {
        return new OrcWarrior('A');
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this,
                "Orc Warrior units are heavily armed fighters.");
    }

    @Override
    protected BattleUnit copyYourself() {
        return new OrcWarriorUnit(getCount());
    }
}
