package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.help.SpecificSkillHelpDialog;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SkillsView extends SelectableListMenu {
    public static final int ROW_OFFSET = 14;

    public SkillsView(GameView previous) {
        super(previous, 45, 39);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart + 1, yStart + 1) {
                   @Override
                   public void drawYourself(Model model, int x, int y) {
                       int charNum = drawCharacterNames(model, x, y);
                       print(model.getScreenHandler(), x + 14 + 3*charNum, y + ROW_OFFSET - 2, "______");
                       String best = "BEST";
                       for (int i = best.length() - 1; i >= 0; --i) {
                           print(model.getScreenHandler(), x + 3 * charNum + 17, y + i - best.length() + 12,
                                   "" + best.charAt(i));
                       }
                   }
               },
                new DrawableObject(xStart+1, yStart+1) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        BorderFrame.drawString(model.getScreenHandler(), "Skills", x+1, y, MyColors.WHITE, MyColors.BLUE);
                        BorderFrame.drawString(model.getScreenHandler(), "Overview", x+1, y+1, MyColors.WHITE, MyColors.BLUE);
                    }
                });
    }

    public static int drawCharacterNames(Model model, int x, int y) {
        StringBuilder builder = new StringBuilder();
        builder.repeat("_", 3);
        String littleLine = builder.toString();
        int charNum = 0;
        int row = ROW_OFFSET-1;
        print(model.getScreenHandler(), x, y + row - 1, "______________");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            for (int i = gc.getFirstName().length() - 1; i >= 0; --i) {
                BorderFrame.drawString(model.getScreenHandler(), "" + gc.getFirstName().charAt(i),
                        x + 3 * charNum + 15, y + i - gc.getFirstName().length() + 12,
                        model.getParty().getColorForPartyMember(gc), MyColors.BLUE);
            }
            print(model.getScreenHandler(), x + 14 + 3*charNum, y + row - 1, littleLine);
            charNum++;
        }
        return charNum;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = ROW_OFFSET;
        for (Skill skill : Skill.values()) {
            if (skill.areEqual(Skill.UnarmedCombat) || skill.areEqual(Skill.MagicAny)) {
                continue;
            }
            StringBuilder bldr = new StringBuilder();
            bldr.append(String.format("%-13s", skill.getName()));
            int best = 0;
            int bestXpos = 0;
            int charNum = 0;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                int rank = gc.getRankForSkill(skill);
                if (rank != 0) {
                    bldr.append(String.format("%3d", rank));
                } else {
                    bldr.append("   ");
                }
                if (rank > best) {
                    best = rank;
                    bestXpos = 3*charNum + 14;
                }
                charNum++;
            }
            bldr.append(String.format(" |%3d", best));
            int finalBestXpos = bestXpos;
            int finalBest = best;
            result.add(new SelectableListContent(xStart + 1, yStart + row, bldr.toString()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SpecificSkillHelpDialog(model.getView(), skill), model);
                }

                @Override
                public void drawDecorations(Model model, int x, int y) {
                    MyColors fgColor = MyColors.YELLOW;
                    MyColors bgColor = MyColors.BLUE;
                    MyColors bestFg = MyColors.YELLOW;
                    if (y - ROW_OFFSET - yStart == getSelectedRow()) {
                        fgColor = MyColors.BLACK;
                        bgColor = MyColors.LIGHT_YELLOW;
                        bestFg = MyColors.BLUE;
                    }
                    BorderFrame.drawString(model.getScreenHandler(), skill.getName(), x, y, fgColor, bgColor);
                    if (finalBest != 0) {
                        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", finalBest), x + finalBestXpos, y,
                                bestFg, bgColor);
                    }
                }

                @Override
                public MyColors getForegroundColor(Model model) {
                    return MyColors.WHITE;
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });

            row++;
        }
        return result;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
