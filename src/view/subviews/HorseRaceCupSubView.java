package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.horses.Horse;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class HorseRaceCupSubView extends ContestSubView {
    private final Map<GameCharacter, Horse> horseMap;

    public HorseRaceCupSubView(List<GameCharacter> riders, Map<GameCharacter, Horse> horses) {
        super(riders);
        this.horseMap = horses;
    }

    @Override
    protected void specificDrawContestant(Model model, SteppingMatrix<GameCharacter> matrix, Point pos) {
        model.getScreenHandler().clearSpace(pos.x + 7, pos.x + 20, pos.y + 6, pos.y + 10);
        Sprite background = Horse.getBackgroundSprite();
        Horse horse = horseMap.get(matrix.getSelectedElement());
        model.getScreenHandler().register(background.getName(),  new Point(pos.x + 18, pos.y+2), background);
        model.getScreenHandler().register(horse.getSprite().getName(),  new Point(pos.x + 18, pos.y+2), horse.getSprite());
        BorderFrame.drawString(model.getScreenHandler(), horse.getName(), pos.x + 18, pos.y, MyColors.LIGHT_GRAY, MyColors.BLACK);
    }

    @Override
    protected String getUnderText(Model model) {
        return "The riders of the Grand Horse Race Cup.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE CUP";
    }
}
