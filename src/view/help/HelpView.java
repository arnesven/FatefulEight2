package view.help;

import model.Model;
import view.GameView;
import view.TwoPaneSelectableListMenu;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public class HelpView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 57;
    public static final int HELP_VIEW_HEIGHT = 42;
    private final List<HelpDialog> chapters;

    public HelpView(GameView view) {
        super(view, WIDTH, HELP_VIEW_HEIGHT, 36);
        chapters = new ArrayList<>(List.of(
            new TutorialStartDialog(null),
            new TutorialAlchemy(null),
            new TutorialAllies(null),
            new TutorialAlignment(null),
            new AttributesHelpChapter(null),
            new TutorialAttitudes(null),
            new TutorialBattles(null),
            new TutorialBounties(null),
            new TutorialBurglary(null),
            new TutorialCarryingCapacity(null),
            new TutorialClassesDialog(null),
            new CombatHelpChapter(null),
            new ConditionsHelpChapter(null),
            new TutorialCrafting(null),
            new TutorialDailyActions(null),
            new TutorialDeliveries(null),
            new DogsTutorial(null),
            new DragonsHelpChapter(null),
            new TutorialDungeons(null),
            new TutorialEveningDialog(null),
            new TutorialEquipmentDialog(null),
            new TutorialFishing(null),
            new TutorialGameLog(null),
            new TutorialGuides(null),
            new TutorialHeadquarters(null),
            new TutorialHorses(null),
            new TutorialHorseRacing(null),
            new HotKeysHelpChapter(null),
            new TutorialLeaderDialog(null),
            new TutorialLoans(null),
            new TutorialNotoriety(null),
            new TutorialObols(null),
            new TutorialOtherParties(null),
            new PickPocketing(null),
            new QuestHelpChapter(null),
            new TutorialRaces(null),
            new TutorialRecruitDialog(null),
            new TutorialRituals(null),
            new TutorialCardGameRunny(null),
            new TutorialScoringDialog(null),
            new TutorialScrolls(null),
            new TutorialShoppingDialog(null),
            new TutorialSkillChecksDialog(null),
            new TutorialSpells(null),
            new TerrainHelpChapter(null),
            new TutorialTraining(null),
            new TutorialTravelDialog(null),
            new TutorialTravellers(null),
            new TutorialVampires(null),
            new TutorialWars(null)));
    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        for (DrawableObject dObject : chapters.get(index).buildDecorations(model, x, y)) {
            dObject.drawYourself(model, dObject.position.x, dObject.position.y);
        }
    }

    @Override
    protected String getHeading() {
        return "Help Sections       ";
    }

    @Override
    protected String getEntryName(int index) {
        if (chapters.get(index).isExpandable()) {
            if (!chapters.get(index).isExpanded()) {
                return chapters.get(index).getTitle() + ((char) 0x7E);
            }
        }
        return chapters.get(index).getTitle();
    }

    @Override
    protected int getNumberOfEntries() {
        return chapters.size();
    }

    @Override
    protected void indexWasSelected(int index) {
        if (chapters.get(index).isExpandable()) {
            chapters.get(index).setExpanded(!chapters.get(index).isExpanded());
            if (chapters.get(index).isExpanded()) {
                chapters.addAll(index+1, chapters.get(index).getSubSections());
            } else {
                chapters.removeAll(chapters.get(index).getSubSections());
            }
        }
    }
}
