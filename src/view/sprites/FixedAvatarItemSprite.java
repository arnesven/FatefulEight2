package view.sprites;

import view.MyColors;

public class FixedAvatarItemSprite extends AvatarItemSprite {
    public FixedAvatarItemSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(num, color1, color2, color3, color4);
        setFrames(1);
    }

    @Override
    public AvatarItemSprite copy() {
        AvatarItemSprite its = super.copy();
        its.setFrames(1);
        return its;
    }
}
