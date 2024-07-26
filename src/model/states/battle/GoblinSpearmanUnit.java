package model.states.battle;

import model.enemies.Enemy;
import model.enemies.GoblinSpearman;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

public class GoblinSpearmanUnit extends GoblinBattleUnit {
    private static final Sprite[] sixteen = makeSpriteSet(7, 0, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] twelve = makeSpriteSet(7, 4, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] nine = makeSpriteSet(7, 8, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] four = makeSpriteSet(7, 12, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);

    public GoblinSpearmanUnit(int count) {
        super("Goblin Spearmen", count, 0, 4, 4, 32, 5  );
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() >= 16) {
            return sixteen;
        }
        if (getCount() >= 12) {
            return twelve;
        }
        if (getCount() >= 9) {
            return nine;
        }
        return four;
    }

    @Override
    public Enemy makeEnemy() {
        return new GoblinSpearman('A');
    }

    @Override
    protected int getSpecificVSAttackBonusWhenAttacking(BattleState battleState, BattleUnit defender) {
        return bonusVSMounted(battleState, defender);
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Goblin Spearmen are a light infantry unit. " +
                "They are particularly efficient against mounted units and enjoys a +2 " +
                "attack bonus when attacking or being attacked by them. Goblin Spearmen units " +
                "will be routed if falling below " + MyStrings.numberWord(getRoutThreshold()) + " combatants.");
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

    @Override
    protected BattleUnit copyYourself() {
        return new GoblinSpearmanUnit(getCount());
    }
}
