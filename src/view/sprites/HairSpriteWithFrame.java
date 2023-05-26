package view.sprites;

import view.MyColors;

public class HairSpriteWithFrame extends FaceSpriteWithHair {
    public HairSpriteWithFrame(int num, MyColors haircol) {
        super(num, haircol);
        setColor3(MyColors.LIGHT_GRAY);
    }
}
