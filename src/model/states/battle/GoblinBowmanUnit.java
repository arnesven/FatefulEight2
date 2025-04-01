package model.states.battle;

import model.enemies.Enemy;
import model.enemies.GoblinBowman;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.BattleUnitHelpDialog;
import view.help.HelpDialog;
import view.sprites.Sprite;

import java.util.List;

public class GoblinBowmanUnit extends GoblinBattleUnit {
    private static final Sprite[] sixteen = makeSpriteSet(6, 0, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] twelve = makeSpriteSet(6, 4, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] nine = makeSpriteSet(6, 8, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);
    private static final Sprite[] four = makeSpriteSet(6, 12, MyColors.BLACK, MyColors.TAN, MyColors.ORC_GREEN, MyColors.RED);

    public GoblinBowmanUnit(int count) {
        super("Bowmen", count, -1, 3, 4, 32, 5);
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
    public List<BattleAction> getBattleActions(BattleState battleState) {
        return List.of(new MoveOrAttackBattleAction(this),
                new ShorterShootBattleAction(this, battleState));
    }

    @Override
    public HelpDialog getHelpSection(GameView view) {
        return new BattleUnitHelpDialog(view, this, "Goblin Bowmen are archers equipped with short bows and " +
                "can perform ranged attacks at a range of " + ShorterShootBattleAction.SHORT_RANGE +
                " (" + ShorterShootBattleAction.LONG_RANGE + " if standing on a hill). " +
                "Goblin Bowmen units " +
                "will be routed if falling below " + MyStrings.numberWord(getRoutThreshold()) + " combatants.");
    }

    @Override
    public Enemy makeEnemy() {
        return new GoblinBowman('A');
    }

    @Override
    protected BattleUnit copyYourself() {
        return new GoblinBowmanUnit(getCount());
    }
}
