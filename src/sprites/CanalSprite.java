package sprites;

import model.TimeOfDay;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.subviews.TownishSubView;

public class CanalSprite extends LoopingSprite {
    public CanalSprite(TimeOfDay timeOfDay, boolean rotate) {
        super("palacecanal", "world_foreground.png", 0x4C, 32);
        setFrames(4);
        if (rotate) {
            setRotation(180);
        }
        if (timeOfDay == TimeOfDay.EVENING) {
            setColor1(TownishSubView.GROUND_COLOR_NIGHT);
            setColor2(MyColors.BLUE);
            setColor3(MyColors.DARK_BROWN);
            setColor4(MyColors.DARK_BLUE);
        } else {
            setColor1(TownishSubView.GROUND_COLOR);
            setColor2(MyColors.CYAN);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.LIGHT_BLUE);
        }
    }
}
