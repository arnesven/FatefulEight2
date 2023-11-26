package model.characters.appearance;

import model.classes.CharacterClass;
import model.races.Race;
import model.races.Shoulders;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.io.Serializable;
import java.util.List;

public abstract class CharacterAppearance implements Serializable {

    private final PortraitSprite FILLED_BLOCK_CLOTHES = new NakedClothesSprite(0xFF);

    private static Sprite hairSprite = new Sprite32x32("standardhair", "hair.png",0x0,
            MyColors.BLACK, MyColors.GOLD, MyColors.CYAN);
    private static Sprite noHairSprite = new Sprite32x32("nohair", "hair.png",0x0,
            MyColors.BLACK, MyColors.GOLD, MyColors.CYAN);
    private final Race race;
    private final MyColors hairColor;
    private MyColors mascaraColor;
    private MyColors lipColor;
    private final boolean femaleGender;
    private Shoulders shoulders;
    private final TorsoChest chest;
    private TorsoNeck neck;

    private PortraitSprite[][] grid;

    public CharacterAppearance(Race race, boolean femaleGender, MyColors hairColor) {
        this.hairColor = hairColor;
        this.race = race;
        this.mascaraColor = race.getColor();
        this.lipColor = race.getMouthDefaultColor();
        this.femaleGender = femaleGender;
        shoulders = race.makeShoulders(femaleGender);
        neck = race.makeNeck();
        if (femaleGender) {
            this.chest = new FemaleChest();
        } else {
            this.chest = new MaleChest();
        }
    }

    public static Sprite noHair() {
        return noHairSprite;
    }

    private void refresh() {
        grid = new PortraitSprite[7][7];
        this.grid[0][0] = PortraitSprite.FRAME_UL_CORNER;
        this.grid[1][0] = getOuterFrameSprite(3);
        this.grid[2][0] = getOuterFrameSprite(4);
        this.grid[3][0] = getOuterFrameSprite(5);
        this.grid[4][0] = getOuterFrameSprite(6);
        this.grid[5][0] = getOuterFrameSprite(7);
        this.grid[6][0] = PortraitSprite.FRAME_UR_CORNER;

        this.grid[0][1] = PortraitSprite.FRAME_LEFT;
        this.grid[1][1] = getOuterFrameSprite(2);
        this.grid[2][1] = new FaceSpriteWithHair(getHeadTopLeft(), hairColor);
        this.grid[3][1] = new FaceSpriteWithHair(getHeadTop(), hairColor);
        this.grid[4][1] = new FaceSpriteWithHair(getHeadTopRight(), hairColor);
        this.grid[5][1] = getOuterFrameSprite(8);
        this.grid[6][1] = PortraitSprite.FRAME_RIGHT;

        this.grid[0][2] = PortraitSprite.FRAME_LEFT;
        this.grid[1][2] = getOuterFrameSprite(1);
        this.grid[2][2] = new FaceSpriteWithHair(getForeheadLeft(), hairColor);
        this.grid[3][2] = new FaceSpriteWithHair(getForeheadCenter(), hairColor);
        this.grid[4][2] = new FaceSpriteWithHair(getForeheadRight(), hairColor);
        this.grid[5][2] = getOuterFrameSprite(9);
        this.grid[6][2] = PortraitSprite.FRAME_RIGHT;

        this.grid[0][3] = PortraitSprite.FRAME_LEFT;
        this.grid[1][3] = classSpecificEars() ? race.getLeftEar(hairColor) : getLeftEar(hairColor);
        this.grid[2][3] = new EyeSprite(symmetricalEyes() ? getEye() : getLeftEye(), hairColor, getEyeballColor(), getMascaraColor());
        this.grid[3][3] = new NoseSprite(getNose(), getMascaraColor());
        this.grid[4][3] = new EyeSprite(symmetricalEyes() ? getEye() : getRightEye(), hairColor, getEyeballColor(), getMascaraColor());
        this.grid[5][3] = classSpecificEars() ? race.getRightEar(hairColor) : getRightEar(hairColor);
        this.grid[6][3] = PortraitSprite.FRAME_RIGHT;

        this.grid[0][4] = PortraitSprite.FRAME_LEFT;
        this.grid[1][4] = PortraitSprite.BLACK_BLOCK;
        this.grid[2][4] = new FaceSpriteWithHair(getLeftCheek(), hairColor);
        this.grid[3][4] = new MouthSprite(getMouth(), getLipColor(), hairColor);
        this.grid[4][4] = new FaceSpriteWithHair(getRightCheek(), hairColor);
        this.grid[5][4] = PortraitSprite.BLACK_BLOCK;
        this.grid[6][4] = PortraitSprite.FRAME_RIGHT;

        shoulders.makeNaked(grid);
        neck.makeNaked(grid);

        grid[2][6] = FILLED_BLOCK_CLOTHES;
        grid[3][6] = chest.makeNakedSprite();
        grid[4][6] = FILLED_BLOCK_CLOTHES;

        specialization();
        setRaceSkinColor(race);
    }

    private MyColors getLipColor() {
        return lipColor;
    }

    private MyColors getMascaraColor() {
        return mascaraColor;
    }

    protected PortraitSprite getOuterFrameSprite(int i) {
        switch (i) {
            case 1:
            case 2:
            case 8:
            case 9:
                return PortraitSprite.BLACK_BLOCK;
        }
        return PortraitSprite.FRAME_TOP;
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

    public void drawYourself(ScreenHandler screenHandler, int col, int row, int fromRow, int toRow) {
        if (grid == null) {
            refresh();
        }
        for (int y = fromRow; y <= toRow; ++y) {
            for (int x = 0; x < grid.length; ++x) {
                if (grid[x][y] != null) {
                    screenHandler.put(col + x, row + y, grid[x][y]);
                }
            }
        }
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        drawYourself(screenHandler, col, row, 0, grid[0].length-1);
    }

    public void setClass(CharacterClass charClass) {
        refresh();
        charClass.putClothesOn(this);
        setRaceSkinColor(race);
        charClass.manipulateAvatar(this, race);
        charClass.finalizeLook(this);
        if (charClass.showFacialHair()) {
            applyFacialHair(race, charClass.coversEars());
        }
        if (charClass.showDetail()) {
            applyDetail(race, charClass.coversEars());
        }
        if (charClass.showHairInBack()) {
            addHairInBack();
        }
    }

    public void setSpecificClothing(PortraitClothing clothing) {
        refresh();
        clothing.putClothesOn(this);
        setRaceSkinColor(race);
        clothing.finalizeLook(this);
        if (clothing.showFacialHair()) {
            applyFacialHair(race, clothing.coversEars());
        }
        applyDetail(race, clothing.coversEars());
        addHairInBack();
    }

    protected void addHairInBack() {

    }

    protected void applyFacialHair(Race race, boolean coversEars) {

    }

    protected void applyDetail(Race race, boolean coversEars) {

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

    public Race getRace() {
        return race;
    }

    public boolean isFemale() {
        return femaleGender;
    }

    public Sprite getNormalHair() {
        return hairSprite;
    }

    public Sprite getBackHairOnly() {
        return getNormalHair();
    }

    public Sprite getFacialOnly() {
        return getNormalHair();
    }

    public void removeOuterHair() {
        for (int i = 1; i < 6; ++i) {
            this.grid[i][0] = PortraitSprite.FRAME_TOP;
        }
        this.grid[1][1] = PortraitSprite.BLACK_BLOCK;
        this.grid[1][2] = PortraitSprite.BLACK_BLOCK;
        this.grid[5][1] = PortraitSprite.BLACK_BLOCK;
        this.grid[5][2] = PortraitSprite.BLACK_BLOCK;
    }

    protected MyColors getEyeballColor() {
        return MyColors.WHITE;
    }

    public boolean getGender() {
        return femaleGender;
    }

    public Shoulders getShoulders() {
        return shoulders;
    }

    public TorsoChest getChest() {
        return chest;
    }

    public void setMascaraColor(MyColors myColors) {
        this.mascaraColor = myColors;
    }

    public void setLipColor(MyColors myColors) {
        this.lipColor = myColors;
    }

    public void setShoulders(Shoulders newShoulders) {
        this.shoulders = newShoulders;
    }

    public TorsoNeck getNeck() {
        return neck;
    }

    public void setNeck(TorsoNeck neck) {
        this.neck = neck;
    }
}
