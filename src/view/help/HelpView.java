package view.help;

import model.Model;
import model.tutorial.TutorialAlignment;
import model.tutorial.TutorialLoans;
import model.tutorial.TutorialTraining;
import view.GameView;
import view.TwoPaneSelectableListMenu;

import java.util.ArrayList;
import java.util.List;

public class HelpView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 57;
    private final List<HelpDialog> chapters;

    public HelpView(GameView view) {
        super(view, WIDTH, 42, 36);
        chapters = new ArrayList<>(List.of(
            new TutorialStartDialog(null),
            new TutorialAlchemy(null),
            new TutorialAllies(null),
            new TutorialAlignment(null),
            new TutorialAttitudes(null),
            new TutorialClassesDialog(null),
            new CombatHelpChapter(null),
            new ConditionsHelpChapter(null),
            new TutorialCrafting(null),
            new TutorialDailyActions(null),
            new TutorialDungeons(null),
            new TutorialEveningDialog(null),
            new TutorialEquipmentDialog(null),
            new TutorialHorses(null),
            new TutorialHorseRacing(null),
            new TutorialLeaderDialog(null),
            new TutorialLoans(null),
            new TutorialObols(null),
            new TutorialQuests(null),
            new TutorialRecruitDialog(null),
            new TutorialCardGameRunny(null),
            new TutorialScoringDialog(null),
            new TutorialScrolls(null),
            new TutorialShoppingDialog(null),
            new TutorialSkillChecksDialog(null),
            new TutorialSpells(null),
            new TutorialTraining(null),
            new TutorialTravelDialog(null)));
    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        for (DrawableObject dObject : chapters.get(index).buildDecorations(model, x, y)) {
            dObject.drawYourself(model, dObject.position.x, dObject.position.y);
        }
    }

    @Override
    protected String getHeading() {
        return "Help Sections";
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
