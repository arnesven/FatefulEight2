package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;

import java.util.List;

public class ArchersUnit extends BattleUnit {
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;
    private final Sprite[] spritesTwo;

    public ArchersUnit(int count, String origin, MyColors color) {
        super("Archers", count, 0, 5, 5, origin);
        color = fixColor(color);
        this.spritesSeven = makeSpriteSet(3, 0, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesFour = makeSpriteSet(3, 4, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesTwo = makeSpriteSet(3, 8, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
    }

    @Override
    public List<BattleAction> getBattleActions(BattleState battleState) {
        return List.of(new MoveOrAttackBattleAction(this),
                new ShootBattleAction(this, battleState));
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
