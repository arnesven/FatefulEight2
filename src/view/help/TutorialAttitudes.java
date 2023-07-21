package view.help;

import model.Model;
import view.GameView;
import view.PartyAttitudesDialog;
import view.sprites.Sprite;
import view.widget.TopText;

import java.awt.*;
import java.util.List;

public class TutorialAttitudes extends HelpDialog {
    private static final String TEXT =
            "Party members have feelings. These feelings are represented " +
            "by a character's attitude toward another character. Here are some examples of things that can affect a " +
            "character's attitude toward another character:\n\n" +
                "*The result of Solo Skill Checks\n" +
                "*The outcome of Quests\n" +
                "*When a character starves\n" +
                "*When a character uses a potion\n or spell on another character\n" +
                "*Various events\n" +
            "\n" +
            "It is important to keep the party positively disposed toward the party leader. If a character starts to dislike the party " +
            "leader too much, she may consider leaving the party.\n\n" +
            "You can get a visual presentation of the party members' attitudes by talking to your party members, " +
            "when you are at a tavern or an inn.\n\n" +
            "  Like            Dislike\n\n" +
            "  Strong Like     Strong Dislike\n\n" +
            "  Love            Hate\n\n" +
            "            Neutral\n";

    public TutorialAttitudes(GameView view) {
        super(view, "Attitudes", TEXT);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+1, yStart+31) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                drawSprite(model, x, y, PartyAttitudesDialog.HAPPY_SPRITE);
                drawSprite(model, x, y+2, PartyAttitudesDialog.VERY_HAPPY_SPRITE);
                drawSprite(model, x, y+4, PartyAttitudesDialog.LOVE_SPRITE);
                drawSprite(model, x+10, y+6, PartyAttitudesDialog.NEUTRAL_SPRITE);

                x += 16;

                drawSprite(model, x, y, PartyAttitudesDialog.SAD_SPRITE);
                drawSprite(model, x, y+2, PartyAttitudesDialog.VERY_SAD_SPRITE);
                drawSprite(model, x, y+4, PartyAttitudesDialog.HATE_SPRITE);
            }
        });
        return textContent;
    }

    private void drawSprite(Model model, int x, int y, Sprite s) {
        Point p = new Point(x, y);
        model.getScreenHandler().register(s.getName(), p, s);
    }
}
