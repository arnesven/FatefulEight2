package view.help;

import model.Model;
import view.BorderFrame;
import view.GameView;
import view.MyColors;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class HelpView extends SelectableListMenu {

    private static final int WIDTH = 57;
    private final HelpDialog[] chapters;
    private int index;

    public HelpView(GameView view) {
        super(view, WIDTH, 42);
        chapters = new HelpDialog[]{
            new TutorialStartDialog(null),
            new TutorialClassesDialog(null),
            new TutorialCombatActionsDialog(null),
            new TutorialCombatDamageDialog(null),
            new TutorialCombatFormationDialog(null),
            new TutorialDailyActions(null),
            new TutorialEveningDialog(null),
            new TutorialEquipmentDialog(null),
            new TutorialLeaderDialog(null),
            new TutorialRecruitDialog(null),
            new TutorialShoppingDialog(null),
            new TutorialSkillChecksDialog(null),
            new TutorialTravelDialog(null),
        };
        index = 0;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new TextDecoration("Help Sections", xStart + 1, yStart + 1, MyColors.WHITE, MyColors.BLUE, false),
                new DrawableObject(xStart + 1, yStart + 1) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        BorderFrame.drawFrame(model.getScreenHandler(), x + 20, y - 1, 36, getHeight(),
                                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, false);
                        for (DrawableObject dObject : chapters[index].buildDecorations(model, x + 20, y - 1)) {
                            dObject.drawYourself(model, dObject.position.x, dObject.position.y);
                        }
                    }
                },
                new DrawableObject(xStart + 1, yStart + index + 2) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        print(model.getScreenHandler(), x, y, (char)0x10+"");
                    }
                });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        for (int i = 0; i < chapters.length; ++i) {
            HelpDialog chapter = chapters[i];
            int finalI = i;
            content.add(new SelectableListContent(xStart+2, yStart+finalI+2, chapter.getTitle()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    index = finalI;
                    HelpView.this.madeChanges();
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });
        }
        return content;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
