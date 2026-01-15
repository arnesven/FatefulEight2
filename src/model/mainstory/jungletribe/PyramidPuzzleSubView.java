package model.mainstory.jungletribe;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.sprites.Sprite32x32;
import view.subviews.SubView;

import java.awt.*;
import java.util.Random;

public abstract class PyramidPuzzleSubView extends SubView {

    protected static final Sprite[] BG_SPRITES = new Sprite[]{new Sprite32x32("rubiqwall", "quest.png",0xCD,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.BEIGE),
            new Sprite32x32("rubiqfullSquare", "quest.png",0xBE,
                    MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.RED)};

    private static final Sprite16x16[] CRACK_SPRITES = new Sprite16x16[]{
            new Sprite16x16("cracksprite1", "quest.png", 0x1E0),
            new Sprite16x16("cracksprite2", "quest.png", 0x1E1),
            new Sprite16x16("cracksprite3", "quest.png", 0x1F0),
            new Sprite16x16("cracksprite4", "quest.png", 0x1F1)
    };

    private static final Sprite32x32[] VINES = new Sprite32x32[]{
            new Sprite32x32("vines1", "quest.png", 0xF1,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
            new Sprite32x32("vines2", "quest.png", 0xF2,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
            new Sprite32x32("vines3", "quest.png", 0xF3,
                    MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.BLUE, MyColors.BEIGE),
    };


    private int cracks = 0;
    private boolean shake = false;
    private int shakeCount = 0;

    protected abstract void specificDrawArea(Model model);

    @Override
    protected void drawArea(Model model) {
        shakeCount++;
        drawBackground(model);
        specificDrawArea(model);
    }

    public void setCracks(int cracks) {
        this.cracks = cracks;
    }

    public void setShakeEnabled(boolean b) {
        this.shake = b;
        if (shake) {
            this.shakeCount = 0;
        }
    }

    protected int xOffFromShake() {
        return shake ? (shakeCount/2 % 3 - 1) : 0;
    }

    protected Point convertToScreen(int x, int y) {
        int shakeExtra = xOffFromShake();
        return new Point(X_OFFSET + x * 4 + 2 + shakeExtra, Y_OFFSET + y * 4);
    }

    private void drawBackground(Model model) {
        Random random = new Random(1234);
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 7; ++x) {
                Point p = convertToScreen(x, y);
                model.getScreenHandler().put(p.x, p.y, BG_SPRITES[0]);
                Point p2 = new Point(p);
                p2.x += random.nextInt(2)*2;
                p2.y += random.nextInt(2)*2;
                Sprite toUse = CRACK_SPRITES[random.nextInt(CRACK_SPRITES.length)];
                if (random.nextInt(10) < cracks*2) {
                    model.getScreenHandler().register(toUse.getName(), p2, toUse);
                }
            }
            Sprite vineSprite;
            if (y == 0) {
                vineSprite = VINES[0];
            } else {
                vineSprite = VINES[y % 2 + 1];
            }
            model.getScreenHandler().register(vineSprite.getName(), convertToScreen(0, y), vineSprite);
        }
    }

}
