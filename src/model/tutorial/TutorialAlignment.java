package model.tutorial;

import model.Model;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.CharSprite;
import view.widget.TopText;

import java.util.List;

public class TutorialAlignment extends HelpDialog {
    private static final String TEXT =
            "Alignment is a measure which describes how aligned your party is " +
            "with law and order. It fully depends on the the party member's respective classes.\n\n" +
            "CLASS       ALIGNMENT MODIFIER\n" +
            "Assassin            -2\n" +
            "Black Knight        -2\n" +
            "Thief               -1\n" +
            "Sorcerer            -1\n" +
            "Witch               -1\n" +
            "Barbarian           -1\n" +
            "Spy                 -1\n" +
            "Priest              +1\n" +
            "Paladin             +1\n" +
            "Noble               +1\n\n" +
            "Characters who's class is not listed in the table above do not contribute " +
            "to the party's alignment. You can see your current alignment in the top " +
            "bar at any time. The alignment icon looks like this:\n\n\n";

    public TutorialAlignment(GameView view) {
        super(view, "Alignment", TEXT);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+16, yStart+28) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().put(x+1, y, TopText.ALIGNMENT_ICON_SPRITE);
            }
        });
        return textContent;
    }
}
