package view.subviews;

import model.Model;
import model.TimeOfDay;
import model.headquarters.Headquarters;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.sprites.Sprite32x32;
import view.widget.TopText;

import java.awt.*;
import java.util.Random;

public class HeadquartersSubView extends SubView {

    private static final Sprite SKY_SPRITE = new Sprite32x32("hqsky", "world_foreground.png", 0x72, MyColors.LIGHT_BLUE,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);
    private static final Sprite SKY_SPRITE_16 = new Sprite16x16("hqsky16", "world_foreground.png", 0xE4, MyColors.LIGHT_BLUE,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);

    @Override
    protected void drawArea(Model model) {
        drawBackground(model);
        drawResources(model);
    }

    private void drawResources(Model model) {
        Headquarters hq = model.getParty().getHeadquarters();
        Point p = VampireFeedingSubView.convertToScreen(new Point(0, 0));
        p.y -= 2;
        int fieldWidth = 6;
        int extra = 1;
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth+ "d", hq.getGold()), p.x+extra+1, p.y, MyColors.LIGHT_YELLOW);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.GOLD_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getFood()), p.x+extra+1, p.y, MyColors.PEACH);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.FOOD_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getIngredients()), p.x+extra+1, p.y, MyColors.LIGHT_GREEN);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.INGREDIENTS_ICON_SPRITE);

        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%" + fieldWidth + "d", hq.getMaterials()), p.x+extra+1, p.y, MyColors.GRAY);
        extra += fieldWidth + 1;
        model.getScreenHandler().put(p.x+extra, p.y, TopText.MATERIALS_ICON_SPRITE);

        p.y += 1;
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("Chars:%2d   Horses:%2d   Items:%3d",
                        hq.getCharacters().size(),
                        hq.getHorses().size(),
                        hq.getItems().size()), p.x, p.y, MyColors.WHITE);
    }

    private void drawBackground(Model model) {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            VampireFeedingSubView.drawSkyNight(model, new Random(1234));
            VampireFeedingSubView.drawGroundNight(model);
        } else {
            for (int x = 0; x < 8; ++x) {
                Point p = VampireFeedingSubView.convertToScreen(new Point(x, 0));
                model.getScreenHandler().put(p.x, p.y, SKY_SPRITE);
                p.y += 4;
                model.getScreenHandler().put(p.x, p.y, SKY_SPRITE);
            }
            VampireFeedingSubView.drawGroundDay(model);
        }
        Point p = VampireFeedingSubView.convertToScreen(new Point(6, 1));
        p.x -= 2;
        p.y += 2;
        model.getParty().getHeadquarters().drawYourself(model, p);
    }

    @Override
    protected String getUnderText(Model model) {
        return "The headquarters of your party";
    }

    @Override
    protected String getTitleText(Model model) {
        return "HEADQUARTERS";
    }
}
