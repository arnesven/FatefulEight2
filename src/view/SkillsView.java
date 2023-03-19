package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SkillsView extends SelectableListMenu {
    public SkillsView(GameView previous) {
        super(previous, 45, 38);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                int charNum = 0;
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    for (int i = gc.getFirstName().length()-1; i >= 0; --i) {
                        BorderFrame.drawString(model.getScreenHandler(), ""+gc.getFirstName().charAt(i),
                                x + 3*charNum + 15, y+i-gc.getFirstName().length()+12,
                                model.getParty().getColorForPartyMember(gc), MyColors.BLUE);
                    }
                    int row = 13;
                    print(model.getScreenHandler(), x, y + row-1, "____________________________________________");
                    charNum++;
                }
                String best = "BEST";
                for (int i = best.length()-1; i >= 0; --i) {
                    print(model.getScreenHandler(),x + 3*charNum + 17, y+i-best.length()+12,
                            ""+best.charAt(i));
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = 14;
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
            result.add(new ListContent(xStart + 1, yStart + row, bldr.toString()) {
                @Override
                public void drawDecorations(Model model, int x, int y) {
                    if (finalBest != 0) {
                        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", finalBest), x + finalBestXpos, y,
                                MyColors.YELLOW, MyColors.BLUE);
                    }
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
