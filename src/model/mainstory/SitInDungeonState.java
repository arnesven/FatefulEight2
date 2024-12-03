package model.mainstory;

import model.Model;
import model.characters.appearance.SilhouetteAppearance;
import model.map.CastleLocation;
import model.quests.Quest;
import model.quests.RescueMissionQuest;
import model.states.EveningState;
import model.states.GameState;
import model.states.QuestState;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public class SitInDungeonState extends GameState {
    private static final SubView JAIL_SUBVIEW =
            new ImageSubView("jail", "DUNGEON", "You are in the castle dungeons.");;
    private final CastleLocation castle;
    private final Quest quest;
    private final int daysToWait;

    public SitInDungeonState(Model model, CastleLocation castle, Quest quest, int daysToWait) {
        super(model);
        this.castle = castle;
        this.quest = quest;
        this.daysToWait = daysToWait;
    }

    @Override
    public GameState run(Model model) {
        CollapsingTransition.transition(model, JAIL_SUBVIEW);
        for (int i = 0; ; ++i) {
            println("You are in the dungeon cell of " + castle.getPlaceName() + ".");
            model.getLog().waitForAnimationToFinish();
            multipleOptionArrowMenu(model, 24, 4, List.of("Do nothing"));
            println("You sit in the cell."); // TODO: Maybe add some more flavor to this.

            if (i >= daysToWait) {
                model.getLog().waitForAnimationToFinish();
                println("An opportunity to escape from the dungeon may have arisen.");
                Quest q2 = EveningState.offerQuests(model, this,
                        List.of(quest));
                if (q2 != null) {
                    stepToNextDay(model);
                    return new QuestState(model, q2);
                }
            }
            stepToNextDay(model);
        }
    }
}
