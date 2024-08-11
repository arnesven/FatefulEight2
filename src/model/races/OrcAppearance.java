package model.races;

import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.util.List;

public class OrcAppearance extends AdvancedAppearance {
    private static final List<MyColors> ORC_HAIR_COLORS = List.of(
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.GRAY_RED,
            MyColors.DARK_BROWN, MyColors.DARK_RED, MyColors.LIGHT_GRAY);
    private final Sprite[][] faceOver;

    public OrcAppearance(boolean gender, MyColors hairColor, int mouth, int nose, CharacterEyes characterEyes, HairStyle hairStyle) {
        super(Race.ORC, gender, hairColor, mouth, nose, characterEyes, hairStyle, null);
        faceOver = makeFaceOverSprites(MyColors.BLACK, getRace().getColor(), getLipColor(), MyColors.BEIGE);
    }

    public OrcAppearance() {
        this(false, randomHairColor(), 7, 5, randomEyes(), randomHair());
        setMascaraColor(MyColors.DARK_GREEN);
        setClass(randomClass());
    }

    @Override
    public boolean supportsSpeakingAnimation() {
        return false;
    }

    private static Sprite[][] makeFaceOverSprites(MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        Sprite8x8[][] result = new Sprite8x8[3][3];
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                result[col][row] = new Sprite8x8("orcface" + col + ":" + row, "face.png", 0x1ED + 0x10*row + col,
                        color1, color2, color3, color4);

            }
        }
        return result;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row, int fromRow, int toRow, int fromColumn, int toColumn) {
        super.drawYourself(screenHandler, col, row, fromRow, toRow, fromColumn, toColumn);
        if (showFacialHair()) {
            drawSpecialMouth(screenHandler, col, row, fromRow, toRow, fromColumn, toColumn);
        }
    }

    private void drawSpecialMouth(ScreenHandler screenHandler, int col, int row, int fromRow, int toRow, int fromColumn, int toColumn) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int finalX = x + 2;
                int finalY = y + 3;
                if (fromColumn <= finalX && finalX <= toColumn && fromRow <= finalY && finalY <= toRow) {
                    screenHandler.register(faceOver[x][y].getName(), new Point(col + finalX, row + finalY), faceOver[x][y]);
                }
            }
        }
    }

    private static CharacterClass randomClass() {
        return MyRandom.sample(List.of(Classes.AMZ, Classes.BBN, Classes.None));
    }

    private static MyColors randomHairColor() {
        return MyRandom.sample(ORC_HAIR_COLORS);
    }

    private static HairStyle randomHair() {
        return HairStyle.maleHairStyles[MyRandom.randInt(HairStyle.maleHairStyles.length)];
    }

    private static CharacterEyes randomEyes() {
        return CharacterEyes.allEyes[MyRandom.randInt(CharacterEyes.allEyes.length)];
    }

}
