package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.horses.HorseHandler;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.RidingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class HorseRacingSubView extends SubView {

    private final Horse horse;
    private final RidingSprite spriteToUse;
    protected static final Sprite greenBlock = new FilledBlockSprite(MyColors.GREEN);

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.horse = horse;
        spriteToUse = new RidingSprite(rider, horse, 1);
        spriteToUse.setDelay(8);
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, greenBlock);
        model.getScreenHandler().register(spriteToUse.getName(), convertToScreen(4, 5), spriteToUse);
    }

    private Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4, Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Giddiyup!";
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE";
    }
}
