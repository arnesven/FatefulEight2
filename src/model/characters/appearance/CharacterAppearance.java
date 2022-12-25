package model.characters.appearance;

import model.classes.CharacterClass;
import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.io.Serializable;
import java.util.List;

public abstract class CharacterAppearance implements Serializable {

    private final PortraitSprite NECK_1 = new NakedFaceAndClothesSprite(0x90);
    private final PortraitSprite NECK_LEFT = new NakedFaceAndClothesSprite(0xA0);
    private final PortraitSprite NECK_RIGHT = new NakedFaceAndClothesSprite(0xB0);
    private final PortraitSprite CHEST_1 = new NakedFaceAndClothesSprite(0xC2);
    private final PortraitSprite CHEST_2 = new NakedFaceAndClothesSprite(0xC1);

    private final PortraitSprite SHOULDER_LEFT_TOP = new NakedClothesSprite(0x00);
    private final PortraitSprite SHOULDER_LEFT_BOTTOM = new NakedClothesSprite(0x10);
    private final PortraitSprite SHOULDER_TOP = new NakedClothesSprite(0x20);
    private final PortraitSprite SHOULDER_RIGHT_TOP = new NakedClothesSprite(0x01);
    private final PortraitSprite FILLED_BLOCK_CLOTHES = new NakedClothesSprite(0xFF);
    private final PortraitSprite SHOULDER_RIGHT_BOTTOM = new NakedClothesSprite(0x11);
    private final PortraitSprite FRAME_UL_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UL_CORNER);
    private final PortraitSprite FRAME_TOP = new PortraitFrameSprite(PortraitFrameSprite.TOP);
    private final PortraitSprite FRAME_UR_CORNER = new PortraitFrameSprite(PortraitFrameSprite.UR_CORNER);
    private final PortraitSprite FRAME_LEFT = new PortraitFrameSprite(PortraitFrameSprite.LEFT);
    private final PortraitSprite FRAME_RIGHT = new PortraitFrameSprite(PortraitFrameSprite.RIGHT);
    private final Race race;
    private final MyColors hairColor;
    private final boolean femaleGender;

    private PortraitSprite[][] grid;

    public CharacterAppearance(Race race, boolean femaleGender, MyColors hairColor) {
        this.hairColor = hairColor;
        this.race = race;
        this.femaleGender = femaleGender;
    }

    private void refresh() {
        grid = new PortraitSprite[7][7];
        this.grid[0][0] = FRAME_UL_CORNER;
        this.grid[1][0] = FRAME_TOP;
        this.grid[2][0] = FRAME_TOP;
        this.grid[3][0] = FRAME_TOP;
        this.grid[4][0] = FRAME_TOP;
        this.grid[5][0] = FRAME_TOP;
        this.grid[6][0] = FRAME_UR_CORNER;

        this.grid[0][1] = FRAME_LEFT;
        this.grid[1][1] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[2][1] = new FaceSpriteWithHair(getHeadTopLeft(), hairColor);
        this.grid[3][1] = new FaceSpriteWithHair(getHeadTop(), hairColor);
        this.grid[4][1] = new FaceSpriteWithHair(getHeadTopRight(), hairColor);
        this.grid[5][1] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[6][1] = FRAME_RIGHT;

        this.grid[0][2] = FRAME_LEFT;
        this.grid[1][2] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[2][2] = new FaceSpriteWithHair(getForeheadLeft(), hairColor);
        this.grid[3][2] = new FaceSpriteWithHair(getForeheadCenter(), hairColor);
        this.grid[4][2] = new FaceSpriteWithHair(getForeheadRight(), hairColor);
        this.grid[5][2] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[6][2] = FRAME_RIGHT;

        this.grid[0][3] = FRAME_LEFT;
        this.grid[1][3] = classSpecificEars() ? race.getLeftEar(hairColor) : getLeftEar(hairColor);
        this.grid[2][3] = new EyeSprite(symmetricalEyes() ? getEye() : getLeftEye(), hairColor);
        this.grid[3][3] = new FaceSprite(getNose());
        this.grid[4][3] = new EyeSprite(symmetricalEyes() ? getEye() : getRightEye(), hairColor);
        this.grid[5][3] = classSpecificEars() ? race.getRightEar(hairColor) : getRightEar(hairColor);
        this.grid[6][3] = FRAME_RIGHT;

        this.grid[0][4] = FRAME_LEFT;
        this.grid[1][4] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[2][4] = new FaceSpriteWithHair(getLeftCheek(), hairColor);
        this.grid[3][4] = new MouthSprite(getMouth(), hairColor);
        this.grid[4][4] = new FaceSpriteWithHair(getRightCheek(), hairColor);
        this.grid[5][4] = new FilledBlockSprite(MyColors.BLACK);
        this.grid[6][4] = FRAME_RIGHT;

        this.grid[0][5] = SHOULDER_LEFT_TOP;
        this.grid[1][5] = SHOULDER_TOP;
        this.grid[2][5] = NECK_LEFT;
        this.grid[3][5] = NECK_1;
        this.grid[4][5] = NECK_RIGHT;
        this.grid[5][5] = SHOULDER_TOP;
        this.grid[6][5] = SHOULDER_RIGHT_TOP;

        this.grid[0][6] = SHOULDER_LEFT_BOTTOM;
        this.grid[1][6] = FILLED_BLOCK_CLOTHES;
        this.grid[2][6] = FILLED_BLOCK_CLOTHES;
        this.grid[3][6] = femaleGender ? CHEST_1 : CHEST_2;
        this.grid[4][6] = FILLED_BLOCK_CLOTHES;
        this.grid[5][6] = FILLED_BLOCK_CLOTHES;
        this.grid[6][6] = SHOULDER_RIGHT_BOTTOM;

        specialization();
        setRaceSkinColor(race);
    }

    protected void specialization() { }

    protected PortraitSprite getRightEar(MyColors hairColor) {
        return null;
    }

    protected PortraitSprite getLeftEar(MyColors hairColor) {
        return null;
    }

    protected boolean classSpecificEars() {
        return true;
    }

    protected int getRightEye() {
        return getEye();
    }

    protected int getLeftEye() {
        return getEye();
    }

    protected boolean symmetricalEyes() {
        return true;
    }

    protected int getRightCheek() {
        return 0x40;
    }

    protected int getLeftCheek() {
        return 0x30;
    }

    protected int getForeheadRight() {
        return 0x10;
    }

    protected int getForeheadCenter() {
        return 0xFF;
    }

    protected int getForeheadLeft() {
        return 0x00;
    }

    protected int getHeadTopRight() {
        return 0x11;
    }

    protected int getHeadTop() {
        return 0xFE;
    }

    protected int getHeadTopLeft() {
        return 0x01;
    }

    protected abstract int getMouth();

    protected abstract int getEye();

    public abstract int getNose();

    private void setRaceSkinColor(Race race) {
        for (int y = 0; y < grid[0].length; ++y) {
            for (int x = 0; x < grid.length; ++x) {
                if (grid[x][y] != null) {
                    grid[x][y].setSkinColor(race.getColor());
                }
            }
        }
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        if (grid == null) {
            refresh();
        }
        for (int y = 0; y < grid[0].length; ++y) {
            for (int x = 0; x < grid.length; ++x) {
                if (grid[x][y] != null) {
                    screenHandler.put(col + x, row + y, grid[x][y]);
                }
            }
        }
    }

    public void setClass(CharacterClass charClass) {
        if (grid == null) {
            refresh();
        }
        charClass.putClothesOn(this);
        setRaceSkinColor(race);
        charClass.finalizeLook(this);
        if (charClass.showFacialHair()) {
            applyFacialHair(race);
        }
        addHairInBack();
    }

    protected void addHairInBack() {

    }

    protected void applyFacialHair(Race race) {

    }

    public void setRow(int i, PortraitSprite[] portraitSprites) {
        if (grid == null) {
            refresh();
        }
        for (int x = 0; x < grid.length; ++x) {
            grid[x][i] = portraitSprites[x];
        }
    }

    public void setSprite(int x, int y, PortraitSprite sprite) {
        if (grid == null) {
            refresh();
        }
        grid[x][y] = sprite;
    }

    public Sprite getSprite(int x, int y) {
        return grid[x][y];
    }

    public MyColors getHairColor() {
        return hairColor;
    }

    public MyColors getFacialHairColor() {
        return hairColor;
    }

    public abstract boolean hairInForehead();

    public abstract boolean hairOnTop();

    public void reset() {
        refresh();
    }

    public boolean hasBeard() {
        return false;
    }

    public void addSpriteOnTop(int x, int y, Sprite8x8 ontop) {
        List<Sprite> sprs = List.of(getSprite(x, y), ontop);
        PortraitSprite compound = new PortraitSprite("adsfsdaf", "face.png", 0x0F, sprs) {
            @Override
            public void setSkinColor(MyColors color) {
                sprs.get(0).setColor1(color);
            }
        };
        setSprite(x, y, compound);
    }

    public void addSpriteOnBelow(int x, int y, Sprite8x8 below) {
        List<Sprite> sprs = List.of(below, getSprite(x, y));
        PortraitSprite compound = new PortraitSprite("adsfsdaf2", "face.png", 0x0F, sprs) {
            @Override
            public void setSkinColor(MyColors color) {
                sprs.get(0).setColor1(color);
            }
        };
        setSprite(x, y, compound);
    }

    public abstract CharacterAppearance copy();

    protected Race getRace() {
        return race;
    }

    public boolean isFemale() {
        return femaleGender;
    }
}
