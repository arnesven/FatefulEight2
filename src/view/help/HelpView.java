package view.help;

import model.Model;
import model.tutorial.TutorialAlignment;
import model.tutorial.TutorialLoans;
import model.tutorial.TutorialTraining;
import view.GameView;
import view.TwoPaneSelectableListMenu;

public class HelpView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 57;
    private final HelpDialog[] chapters;

    public HelpView(GameView view) {
        super(view, WIDTH, 42, 36);
        chapters = new HelpDialog[]{
            new TutorialStartDialog(null),
            new TutorialAlchemy(null),
            new TutorialAllies(null),
            new TutorialAlignment(null),
            new TutorialAttitudes(null),
            new TutorialClassesDialog(null),
            new TutorialCombatActionsDialog(null),
            new TutorialCombatDamageDialog(null),
            new TutorialCombatFormationDialog(null),
            new TutorialCrafting(null),
            new TutorialCraftingDesigns(null),
            new TutorialDailyActions(null),
            new TutorialDungeons(null),
            new TutorialEveningDialog(null),
            new TutorialEquipmentDialog(null),
            new TutorialLeaderDialog(null),
            new TutorialLoans(null),
            new TutorialQuests(null),
            new TutorialRecruitDialog(null),
            new TutorialScoringDialog(null),
            new TutorialScrolls(null),
            new TutorialShoppingDialog(null),
            new TutorialSkillChecksDialog(null),
            new TutorialSpells(null),
            new TutorialTraining(null),
            new TutorialTravelDialog(null),
        };
    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        for (DrawableObject dObject : chapters[index].buildDecorations(model, x, y)) {
            dObject.drawYourself(model, dObject.position.x, dObject.position.y);
        }
    }

    @Override
    protected String getHeading() {
        return "Help Sections";
    }

    @Override
    protected String getEntryName(int index) {
        return chapters[index].getTitle();
    }

    @Override
    protected int getNumberOfEntries() {
        return chapters.length;
    }
}
