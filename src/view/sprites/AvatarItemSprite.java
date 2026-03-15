package view.sprites;

import view.MyColors;

public class AvatarItemSprite extends LoopingSprite {
    private final int num;
    private final MyColors[] colors;
    private boolean isFlipped = false;

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

    @Override
    protected void setFlipHorizontal(boolean b) {
        super.setFlipHorizontal(b);
        isFlipped = b;
    }

    public AvatarItemSprite copy() {
        return new AvatarItemSprite(num, colors[0], colors[1], colors[2], colors[3]);
    }

    @Override
    protected int getCurrentFrameIndex(int currentFrame) {
        if (isFlipped) {
            return switch (currentFrame) {
              case 0, 2 -> 3;
              case 1 -> 1;
              default -> 2;
            };
        }
        return switch (currentFrame) {
            case 2 -> 0;
            case 3 -> 2;
            default -> currentFrame;
        };
    }
}
