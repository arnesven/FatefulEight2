package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArcheryContestBoardSubView extends ContestSubView {
    private final Map<GameCharacter, Sprite> fletchings;
    protected static final Sprite whiteBlock = new FilledBlockSprite(MyColors.WHITE);

    public ArcheryContestBoardSubView(List<GameCharacter> contestants, Map<GameCharacter, Sprite> fletchings) {
        super(contestants);
        this.fletchings = fletchings;
    }

    protected void specificDrawContestant(Model model, SteppingMatrix<GameCharacter> matrix, Point pos) {
        Sprite fletch = fletchings.get(matrix.getSelectedElement());
        model.getScreenHandler().fillSpace(pos.x + 13, pos.x + 20, pos.y + 6, pos.y + 10, whiteBlock);
        BorderFrame.drawString(model.getScreenHandler(), "ARROW", pos.x + 14, pos.y + 6, MyColors.BLACK, MyColors.WHITE);
        model.getScreenHandler().register(fletch.getName(), new Point(pos.x + 15, pos.y + 7), fletch);
    }

    @Override
    protected String getUnderText(Model model) {
        return "The contestants of the archery contest.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "ARCHERY CONTEST";
    }
}
