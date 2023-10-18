package view.help;

import model.Model;
import view.GameView;
import view.party.DrawableObject;
import view.widget.TopText;

import java.util.List;

public class TutorialNotoriety extends HelpDialog {
    private static final String TEXT =
            "Committing crimes will earn you points of Notoriety. Your current " +
            "notoriety can be seen in the top bar next to the following icon:\n\n\n\n" +
            "A higher notoriety will attract the attention of local authorities when you " +
            "are in towns and castles, and add to your bounty if caught. Your notoriety " +
            "is reset if you repent for your crimes, either by spending time in " +
            "jail or by paying a fine.";

    public TutorialNotoriety(GameView view) {
        super(view, "Notoriety", TEXT);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+16, yStart+9) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().put(x+1, y, TopText.NOTORIETY_SPRITE);
            }
        });
        return textContent;
    }
}
