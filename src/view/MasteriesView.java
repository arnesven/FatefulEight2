package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.spells.MasterySpell;
import model.items.spells.Spell;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import javax.swing.border.Border;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MasteriesView extends SelectableListMenu {
    private final MasterySpell spell;

    public MasteriesView(Model model, MasterySpell spell) {
        super(model.getView(), 23, 16);
        this.spell = spell;
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                BorderFrame.drawCentered(model.getScreenHandler(), "Mastery for", y, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawCentered(model.getScreenHandler(), spell.getName(), y+1, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "          Level Next", x+1, y+3, MyColors.WHITE, MyColors.BLUE);
                int row = 4;
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    String name = gc.getFirstName();
                    BorderFrame.drawString(model.getScreenHandler(), name, x+1, y+row, MyColors.WHITE, MyColors.BLUE);
                    String str = String.format("%d   %3d%%", gc.getMasteries().getMasteryLevel(spell.getName()),
                            gc.getMasteries().getCastCountPercentage(spell));
                    BorderFrame.drawString(model.getScreenHandler(), str,
                            x+13, y+row, MyColors.WHITE, MyColors.BLUE);
                    row++;
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(makeOkButton(model, xStart+getWidth()/2 - 1, yStart+getHeight()-2, this));
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
}
