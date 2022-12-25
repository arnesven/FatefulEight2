package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.FaceSprite;
import view.sprites.FilledBlockSprite;

public class BigAndLongBeard extends BigBeard {
    private LongBeard longBeard;
    public BigAndLongBeard(MyColors lineColor) {
        super(lineColor);
        longBeard = new LongBeard(lineColor);
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        super.apply(appearance, race);
        longBeard.apply(appearance, race);
   }
}
