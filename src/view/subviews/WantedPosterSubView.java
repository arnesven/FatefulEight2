package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.NoClothing;
import model.classes.CharacterClass;
import model.races.ColoredRace;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

public class WantedPosterSubView extends SubView {
    private static final int POSTER_WIDTH = 21;
    private static final int POSTER_HEIGHT = 21;
    private final SubView previous;
    private final CharacterAppearance appearance;
    private final String name;
    private final boolean companions;
    private final String town;
    private final int reward;
    private Sprite[][] POSTER_SPRITES = makePosterSprites();

    public WantedPosterSubView(SubView previous, String name, CharacterAppearance app,
                               CharacterClass charClass, boolean withCompanions, int gold, String town) {
        this.previous = previous;
        this.appearance = app.copy();
        this.name = name;
        this.companions = withCompanions;
        this.town = town;
        this.reward = gold;
        appearance.setHairColor(MyColors.GRAY);
        appearance.setRace(new ColoredRace(MyColors.WHITE, appearance.getRace()));
        appearance.setMascaraColor(MyColors.WHITE);
        appearance.setLipColor(MyColors.GRAY);
        appearance.setClass(charClass);
        appearance.setSpecificClothing(new NoClothing());
    }

    public WantedPosterSubView(SubView subView, GameCharacter character, boolean withCompanions, int gold, String town) {
        this(subView, character.getFullName(), character.getAppearance(),
                character.getCharClass(), withCompanions, gold, town);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);

        int xAdjust = (X_MAX - X_OFFSET - POSTER_WIDTH) / 2;
        for (int y = 0; y < POSTER_HEIGHT; ++y) {
            for (int x = 0; x < POSTER_WIDTH; ++x) {
                int spriteX = x == 0 ? 0 : (x == POSTER_WIDTH - 1 ? 4 : (1 + x % 3));
                int spriteY = y == 0 ? 0 : (y == POSTER_HEIGHT - 1 ? 3 : (1 + x % 2));
                model.getScreenHandler().put(X_OFFSET+xAdjust+x, Y_OFFSET+7+y, POSTER_SPRITES[spriteX][spriteY]);
            }
        }

        appearance.drawYourself(model.getScreenHandler(), X_OFFSET + 12, Y_OFFSET + 9, 1, 5,
                1, 6);

        BorderFrame.drawCentered(model.getScreenHandler(), "WANTED!", Y_OFFSET + 9, MyColors.WHITE, MyColors.BLACK);

        int y = Y_OFFSET + 16;
        BorderFrame.drawCentered(model.getScreenHandler(), name, y++, MyColors.WHITE, MyColors.BLACK);
        if (companions) {
            BorderFrame.drawCentered(model.getScreenHandler(), "and companions", y++, MyColors.WHITE, MyColors.BLACK);
        }
        y++;
        BorderFrame.drawCentered(model.getScreenHandler(), "Wanted Dead", y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawCentered(model.getScreenHandler(), "or Alive", y++, MyColors.WHITE, MyColors.BLACK);
        y++;
        BorderFrame.drawCentered(model.getScreenHandler(), reward + " Gold Reward", y++, MyColors.WHITE, MyColors.BLACK);
        y++;
        BorderFrame.drawCentered(model.getScreenHandler(), town, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawCentered(model.getScreenHandler(), "Constabulary", y++, MyColors.WHITE, MyColors.BLACK);

    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    private Sprite[][] makePosterSprites() {
        Sprite[][] result = new Sprite[5][4];
        for (int y = 0; y < result[0].length; ++y) {
            for (int x = 0; x < result.length; ++x) {
                result[x][y] = new Sprite8x8("poster"+x+"-"+y, "poster.png",
                        0x10*y + x, MyColors.BLACK, MyColors.WHITE, MyColors.BROWN, MyColors.CYAN);
            }
        }
        return result;
    }
}
