package view.help;

import model.Model;
import model.journal.MainStoryTask;
import util.MyStrings;
import view.BorderFrame;
import view.GameView;
import view.TwoPaneSelectableListMenu;
import view.party.DrawableObject;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class HelpView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 57;
    public static final int HELP_VIEW_HEIGHT = 42;
    private List<HelpDialog> chapters;
    private boolean searchEnabled = false;
    private StringBuffer searchBuffer = new StringBuffer("þþþþþþþþþþþþþþþþþþþ");
    private int buffIndex = 0;

    public HelpView(GameView view) {
        super(view, WIDTH, HELP_VIEW_HEIGHT, 36);
        resetHelpChapters();
    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        for (DrawableObject dObject : chapters.get(index).buildDecorations(model, x, y)) {
            dObject.drawYourself(model, dObject.position.x, dObject.position.y);
        }
    }

    @Override
    protected String getHeading() {
        if (searchEnabled) {
            return " " + searchBuffer.toString();
        }
        return "Help Sections       ";
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model, int index) {
        if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_F) {
            searchEnabled = !searchEnabled;
            filterChapters();
            madeChanges();
        }
        if (searchEnabled) {
            if (isAlphaOrSpace(keyEvent)) {
                if (buffIndex < searchBuffer.length()) {
                    searchBuffer.setCharAt(buffIndex++, keyEvent.getKeyChar());
                    filterChapters();
                    madeChanges();
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (buffIndex > 0) {
                    searchBuffer.setCharAt(--buffIndex, 'þ');
                    filterChapters();
                    madeChanges();
                }
            }
        }
    }

    private void filterChapters() {
        String searchKey = searchBuffer.toString().replaceAll("þ", "");
        resetHelpChapters();
        if (searchEnabled && !searchKey.equals("")) {
            List<HelpDialog> searchResults = new ArrayList<>();
            addSearchResults(searchResults, chapters, searchKey);
            if (searchResults.isEmpty()) {
                searchResults.add(new NoSearchResultsFound());
            }
            chapters = searchResults;
            resetIndex();
            setSelectedRow(0);
        }
    }

    private void addSearchResults(List<HelpDialog> searchResults, List<HelpDialog> chapters, String searchKey) {
        String cap = MyStrings.capitalize(searchKey);
        for (HelpDialog chap : chapters) {
            if (chap.getText().contains(searchKey) || chap.getText().contains(cap)) {
                searchResults.add(chap);
            }
            addSearchResults(searchResults, chap.getSubSections(), searchKey);
        }
    }

    private boolean isAlphaOrSpace(KeyEvent keyEvent) {
        return ('a' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'z') ||
                ('A' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'Z') ||
                keyEvent.getKeyCode() == ' ';
    }

    @Override
    protected String getEntryName(int index) {
        if (chapters.get(index).isExpandable() && !searchEnabled) {
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
        if (chapters.get(index).isExpandable() && !searchEnabled) {
            chapters.get(index).setExpanded(!chapters.get(index).isExpanded());
            if (chapters.get(index).isExpanded()) {
                chapters.addAll(index+1, chapters.get(index).getSubSections());
            } else {
                chapters.removeAll(chapters.get(index).getSubSections());
            }
        }
    }

    private void resetHelpChapters() {
        chapters = new ArrayList<>(List.of(
                new TutorialStartDialog(null, true),
                new TutorialAlchemy(null),
                new TutorialAllies(null),
                new TutorialAlignment(null),
                new AttributesHelpChapter(null),
                new TutorialAttitudes(null),
                new TutorialBattles(null),
                new TutorialBounties(null),
                new TutorialBurglary(null),
                new CardGameHelpChapter(null),
                new TutorialCarryingCapacity(null),
                new TutorialClassesDialog(null),
                new CombatHelpChapter(null),
                new ConditionsHelpChapter(null),
                new TutorialCrafting(null),
                new TutorialDailyActions(null),
                new TutorialDeliveries(null),
                new TutorialDismiss(null),
                new DogsTutorial(null),
                new DragonsHelpChapter(null),
                new TutorialDungeons(null),
                new TutorialEveningDialog(null),
                new TutorialEquipmentDialog(null),
                new TutorialFindResources(null),
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
                new TutorialWars(null),
                new TutorialWeaponPairing(null)));
    }

    private static class NoSearchResultsFound extends HelpDialog {
        public NoSearchResultsFound() {
            super(null, "*No Search Results*", "");
        }
    }
}
