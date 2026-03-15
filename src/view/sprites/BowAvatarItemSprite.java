package view.sprites;

import view.MyColors;

public class BowAvatarItemSprite extends AvatarItemSprite {
    public BowAvatarItemSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(num, color1, color2, color3, color4);
        setFlipHorizontal(true);
    }
}
