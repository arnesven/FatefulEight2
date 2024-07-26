package view.help;

import model.states.battle.*;
import view.GameView;
import view.MyColors;
import view.help.ExpandableHelpDialog;
import view.help.HelpDialog;

import java.util.List;

public class BattleUnitsChapter extends ExpandableHelpDialog {

    private static final String UNIT_ORIGIN = "Origin";
    private static final MyColors UNIT_COLOR = MyColors.PURPLE;

    private static final String TEXT = "Battle Units have the following attributes:\n\n" +
            "Count: The number of combatants in the unit. When the count reaches zero, the unit has been eliminated.\n\n" +
            "Movement Points (MP): An amount of points which can be spent during combat rounds to perform actions. " +
            "At the start of each round, all of a unit's MP are restored.\n\n" +
            "Combat Skill: An abstracted value which a unit adds to its rolls during attacks.\n\n" +
            "Defense: The difficulty for attack rolls made against the unit.\n\n";


    public BattleUnitsChapter(GameView view) {
        super(view, "Units", TEXT, true);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new ArchersUnit(99, UNIT_ORIGIN, UNIT_COLOR).getHelpSection(view),
                new GoblinBowmanUnit(99).getHelpSection(view),
                new GoblinSpearmanUnit(99).getHelpSection(view),
                new GoblinWolfRiderUnit(99).getHelpSection(view),
                new KnightsUnit(99, UNIT_ORIGIN, UNIT_COLOR).getHelpSection(view),
                new MilitiaUnit(99, UNIT_ORIGIN, UNIT_COLOR).getHelpSection(view),
                new OrcBoarRiderUnit(99).getHelpSection(view),
                new OrcWarriorUnit(99).getHelpSection(view),
                new PikemenUnit(99, UNIT_ORIGIN, UNIT_COLOR).getHelpSection(view),
                new SwordsmanUnit(99, UNIT_ORIGIN, UNIT_COLOR).getHelpSection(view)
        );
    }
}
