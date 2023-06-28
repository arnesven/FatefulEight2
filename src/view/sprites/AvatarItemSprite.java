package view.sprites;

import view.MyColors;

public class AvatarItemSprite extends LoopingSprite {
    private final int num;
    private final MyColors[] colors;

    public AvatarItemSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4, int shiftUp) {
        super("swordanimation", "weapons.png", num, 32, 32);
        this.num = num;
        this.colors = new MyColors[]{color1, color2, color3, color4};
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
        shiftUpPx(shiftUp);
        setFrames(4);
    }

    public AvatarItemSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        this(num, color1, color2, color3, color4, 0);
    }

    public AvatarItemSprite copy() {
        return new AvatarItemSprite(num, colors[0], colors[1], colors[2], colors[3]);
    }
}
