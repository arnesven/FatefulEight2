package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public interface PowerGaugeSegment {

    MyColors FILL_COLOR = MyColors.LIGHT_BLUE;
    MyColors BACKGROUND_COLOR = MyColors.DARK_BROWN;
    MyColors LINE_COLOR = MyColors.LIGHT_GRAY;

    Sprite[] getNormalSpriteSet();

    Sprite[] getAnimatedSpriteSet();

    int getWidth();

    default int getXShift() { return 0; }

    static Sprite[] makeSprites(int colOff, int row, MyColors bgColor, MyColors fillColor) {
        Sprite[] result = new Sprite[5];
        for (int col = 0; col < result.length; ++col) {
            int column = col < result.length - 1 ? col : 0;
            result[col] = makeGaugeSprite(colOff + column, row, bgColor, fillColor);
            if (col == result.length - 1) {
                result[col].setColor3(bgColor);
                result[col].setColor1(fillColor);
            }
        }

        return result;
    }

    static Sprite makeGaugeSprite(int column, int row, MyColors bgColor, MyColors fillColor, int rotation) {
        Sprite sp = new Sprite("gauge", "gauge.png", column, row, 16, 8);
        sp.setColor1(bgColor);
        sp.setColor2(LINE_COLOR);
        sp.setColor3(fillColor);
        sp.setRotation(rotation);
        return sp;
    }

    static Sprite makeGaugeSprite(int column, int row, MyColors bgColor, MyColors fillColor) {
        return makeGaugeSprite(column, row, bgColor, fillColor, 0);
    }
}
