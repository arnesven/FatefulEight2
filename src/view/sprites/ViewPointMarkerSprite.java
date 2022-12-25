package view.sprites;

import model.Model;
import view.MyColors;

public class ViewPointMarkerSprite extends Sprite32x32 implements Animation {
    private static MyColors[] colors = new MyColors[]{MyColors.CYAN, MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GREEN};
    private int colorIndex = 0;
    private int counter = 0;


    public ViewPointMarkerSprite() {
        super("viewpointmarkersprite", "world_foreground.png", 0x00, MyColors.BLACK, MyColors.CYAN, MyColors.BLUE);
        AnimationManager.register(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        counter++;
        if (counter % 4 == 0) {
            colorIndex = (colorIndex + 1) % colors.length;
            setColor1(colors[colorIndex]);
        }
    }

    @Override
    public void synch() {

    }
}
