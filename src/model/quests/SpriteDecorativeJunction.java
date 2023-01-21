package model.quests;

import model.Model;
import view.sprites.Sprite;

import java.awt.*;

class SpriteDecorativeJunction extends DecorativeJunction {
    private final Sprite avatar;
    private final String description;

    public SpriteDecorativeJunction(int col, int row, Sprite avatar, String description) {
        super(col, row);
        this.avatar = avatar;
        this.description = description;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(avatar.getName(), new Point(xPos, yPos), avatar);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
