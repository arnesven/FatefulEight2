package view.help;

import model.states.battle.BattleUnit;
import model.states.battle.DenseWoodsBattleTerrain;
import model.states.battle.WoodsBattleTerrain;
import view.GameView;

import java.util.List;

public class TutorialBattles extends ExpandableHelpDialog {
    private static final String TEXT =
            "Sometimes your party will find itself near two clashing armies. In some cases " +
            "it may be possible for you to direct one of the armies' troops on the battlefield.\n\n" +
            "During battles one army will activate all of its units before the other army gets a turn. Every unit has " +
            "a number of Movement Points (MP) with which it can move. Turning a unit normally costs 1 MP. " +
            "Moving a unit costs 2-3 MP, depending on terrain. Attacking with a unit consumes all remaining MP for that unit. " +
            "When a unit has exhausted all of its MP it can no longer take any action.\n\n" +
            "There are two types of attacks, melee and ranged attacks, See the corresponding help sections.\n\n" +
            "There are five types of units: Swordsmen, Pikemen, Archers, Militia and Knights. " +
            "You can read more about different units and their attributes in the help section.\n\n" +
            "The battle is over if either army retreats, or if all remaining units of one army are eliminated.";

    public TutorialBattles(GameView view) {
        super(view, "Battles", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new BattleMeleeAttacksChapter(view),
                new BattleRangedAttackChapter(view),
                new BattleTerrainChapter(view),
                new BattleUnitsChapter(view));
    }

    private static class BattleMeleeAttacksChapter extends SubChapterHelpDialog {
        public BattleMeleeAttacksChapter(GameView view) {
            super(view, "Melee Attacks", "When a unit enters close combat with another unit it " +
                    "rolls a number of dice equal to its current count. Each roll which equals or exceeds " +
                    "the defending unit's Defense is counted as a hit. The rolls are modified with the attacking units " +
                    "combat skill, as well as bonuses from Flanking or Rear attack.\n\n" +
                    "Attacking into another units flank gives a +" + BattleUnit.FLANK_ATTACK_BONUS +
                    " bonus whereas attacking in the rear provides a +" + BattleUnit.REAR_ATTACK_BONUS + " bonus.\n\n" +
                    "The defender's defense value is increased by 1 if it is positioned on a hill.\n\n" +
                    "The defending unit then counter attacks in the same way. Then, each unit inflicts casualties on the " +
                    "other unit equal to the number of hits scored. If both units still remain after this, the unit " +
                    "with more hits is the winner, with ties going to the defender. The loser must retreat. If the attacker lost the fight " +
                    "it must retreat to the space it started from. If the defender lost the fight it must retreat to " +
                    "an adjacent empty space (not the space the attacker came from). If there is no legal space to retreat the " +
                    "retreating unit is eliminated.");
        }
    }

    private static class BattleRangedAttackChapter extends SubChapterHelpDialog {
        public BattleRangedAttackChapter(GameView view) {
            super(view, "Ranged Attacks", "When a unit makes a ranged attack against another unit it rolls " +
                    "a number of dice equal to its current count. The rolls are modified with the attacking units " +
                    "combat skill.\n\nThe defender's defense value is increased if it is positioned in terrain " +
                    "which provides cover. Woods provide a cover bonus of +" + WoodsBattleTerrain.COVER_BONUS +
                    ", Dense Woods provide a cover bonus of +" + DenseWoodsBattleTerrain.COVER_BONUS + ".");
        }
    }
}
