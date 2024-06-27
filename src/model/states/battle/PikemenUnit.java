package model.states.battle;

import model.enemies.MountedEnemy;
import view.MyColors;
import view.sprites.Sprite;

public class PikemenUnit extends BattleUnit {
    private final Sprite[] spritesFew;
    private final Sprite[] sprites;
    private final Sprite[] spritesMany;

    public PikemenUnit(int count, String origin, MyColors color) {
        super("Pikemen", count, 1, 5, 6, origin);
        if (color == MyColors.WHITE) {
            color = MyColors.GRAY;
        } else if (color == MyColors.YELLOW) {
            color = MyColors.GOLD;
        }
        this.spritesMany = makeSpriteSet(1, 0, MyColors.BLACK, MyColors.BEIGE, MyColors.PEACH, color);
        this.sprites = makeSpriteSet(1, 4, MyColors.BLACK, MyColors.BEIGE, MyColors.PEACH, color);
        this.spritesFew = makeSpriteSet(1, 8, MyColors.BLACK, MyColors.BEIGE, MyColors.PEACH, color);
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 3) {
            return spritesFew;
        }
        if (getCount() < 7) {
            return sprites;
        }
        return spritesMany;
    }

    @Override
    protected int getSpecificVSAttackBonusWhenAttacking(BattleState battleState, BattleUnit defender) {
        return bonusVSMounted(battleState, defender);
    }

    @Override
    protected int getSpecificVSAttackBonusWhenDefending(BattleState battleState, BattleUnit attacker) {
        return bonusVSMounted(battleState, attacker);
    }

    private int bonusVSMounted(BattleState battleState, BattleUnit defender) {
        if (defender instanceof MountedBattleUnit) {
            battleState.println(getName() + " get +2 attack bonus against mounted units.");
            return 2;
        }
        return 0;
    }
}
