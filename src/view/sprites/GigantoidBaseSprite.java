package view.sprites;

import model.races.Race;
import view.MyColors;

import java.util.List;

public class GigantoidBaseSprite extends FullPortraitSprite {

    public GigantoidBaseSprite(Race race, MyColors eyeColor, MyColors irisColor, MyColors pupilColor, Sprite overlay) {
        super(0, 2, List.of(new SilhouetteFrameSprite(), overlay));
        setColor1(pupilColor);
        setColor2(eyeColor);
        setColor3(race.getColor());
        setColor4(irisColor);
    }
}
