package view.combat;

import model.Model;
import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class MonastaryWallCombatTheme extends MountainCombatTheme {
    private static final Sprite WALL_UPPER = new Sprite32x32("wallcombatupper", "combat.png", 0x120,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.TAN);
    private static final Sprite WALL_LOWER = new Sprite32x32("WALLcombatlower", "combat.png", 0x121,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.GRAY);
    private final List<GameCharacter> bystanders;

    public MonastaryWallCombatTheme(List<GameCharacter> gameCharacters) {
        this.bystanders = gameCharacters;
    }

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        Random random = new Random(1234);
        super.drawBackground(model, xOffset, yOffset);
        Sprite[] grassSprites = GrassCombatTheme.grassSprites;
        for (int i = 0; i < 8; ++i) {
            for (int y = 6; y < 9; y++) {
                model.getScreenHandler().put(xOffset + i * 4, yOffset + y * 4,
                        grassSprites[random.nextInt(grassSprites.length)]);
            }
        }

        int yShift = 5;

        for (int x = 0; x < 8; ++x) {
            model.getScreenHandler().put(xOffset + x * 4, yOffset + 4, groundSprites[random.nextInt(groundSprites.length)]);
            model.getScreenHandler().put(xOffset + 4 * x, yOffset + 4 * yShift, WALL_UPPER);
            model.getScreenHandler().put(xOffset + 4 * x, yOffset + 4 * (yShift + 1), WALL_LOWER);

            model.getScreenHandler().put(xOffset + 4 * x, yOffset, WALL_LOWER);
        }

        int x = (8 - bystanders.size()) / 2;
        for (GameCharacter gc : bystanders) {
            gc.drawAvatar(model.getScreenHandler(), xOffset + x * 4, yOffset + 4);
            x += 1;
        }
    }
}
