package view.subviews;

import model.Model;
import model.items.IngredientsDummyItem;
import model.items.Item;
import model.items.potions.Potion;
import util.MyRandom;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.Sprite;

public class AlchemySubView extends SubView {

    private static AlchemySubViewSprite SPRITE = new AlchemySubViewSprite();
    private static final int Y_PIC_END = Y_OFFSET + SPRITE.getHeight() / 8;
    private static Sprite EYES = new CatEyesSprite();
    private static final Sprite EMPTY = Item.EMPTY_ITEM_SPRITE;
    private boolean eyesOn = false;
    private int delay;
    private int blinkLength;
    private boolean isInDistill = true;
    private Potion selectedPotion;
    private IngredientsDummyItem ingredientsDummy;

    public AlchemySubView() {
        randomizeBlink();
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        model.getScreenHandler().put(X_OFFSET, Y_OFFSET, SPRITE);
        drawBlink(model);

        int yStart = Y_PIC_END + 4;
        for (int i = 0; i < 2; ++i) {
            model.getScreenHandler().put(X_OFFSET + 15 + i, yStart+2, ArrowSprites.RIGHT_BLACK);
        }
        if (selectedPotion != null) {
            drawIngredients(model, yStart);
            drawPotion(model, yStart);
        } else {
            model.getScreenHandler().put( X_OFFSET + 6, yStart, EMPTY);
            model.getScreenHandler().put( X_OFFSET + 22, yStart, EMPTY);
        }
    }

    private synchronized void drawIngredients(Model model, int yStart) {
        int xOffset = isInDistill ? X_OFFSET + 16 : X_OFFSET;
        ingredientsDummy.drawYourself(model.getScreenHandler(), xOffset + 6, yStart);
        int textX = isInDistill ? X_MAX - ingredientsDummy.getName().length() : X_OFFSET;
        BorderFrame.drawString(model.getScreenHandler(), ingredientsDummy.getName(),
                textX, yStart + 4, MyColors.WHITE);

    }

    private void drawPotion(Model model, int yStart) {
        int xOffset = isInDistill ? X_OFFSET : X_OFFSET + 16;
        selectedPotion.drawYourself(model.getScreenHandler(), xOffset+6, yStart);
        String[] parts = MyStrings.partition(selectedPotion.getName(), 12);
        int row = yStart + 4;
        for (String s : parts) {
            int textX = isInDistill ? X_OFFSET : X_MAX - s.length();
            BorderFrame.drawString(model.getScreenHandler(), s,
                    textX, row++, MyColors.WHITE);
        }
    }


    @Override
    protected String getUnderText(Model model) {
        return "Preparing to perform alchemy.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "ALCHEMY";
    }

    private void randomizeBlink() {
        delay = MyRandom.randInt(10, 30);
        blinkLength = MyRandom.randInt(delay);
    }

    private void drawBlink(Model model) {
        if (System.currentTimeMillis() / 1000 % delay <= blinkLength) {
            if (!eyesOn) {
                eyesOn = true;
            }
            model.getScreenHandler().put(X_MAX - 6, Y_PIC_END, EYES);
        } else if (eyesOn) {
            eyesOn = false;
            randomizeBlink();
        }
    }

    public synchronized void setContents(Potion selectedPotion, int ingredientCost) {
        this.selectedPotion = selectedPotion;
        this.ingredientsDummy = new IngredientsDummyItem(ingredientCost);
    }

    public void setDistill(boolean distill) {
        isInDistill = distill;
    }

    public synchronized void unsetContents() {
        this.selectedPotion = null;
        this.ingredientsDummy = null;
    }

    private static class CatEyesSprite extends Sprite {
        public CatEyesSprite() {
            super("alchemycateyes", "mouth.png", 3, 3, 16, 8);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.YELLOW);
        }
    }

    private static class AlchemySubViewSprite extends Sprite {
        public AlchemySubViewSprite() {
            super("alchemysubview", "alchemy.png", 0, 0, 256, 164);
            MyColors.transformImage(this);
        }
    }
}
