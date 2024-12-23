package sprites;

import view.MyColors;
import view.sprites.LoopingSprite;

public class SparklingLockedBeamsSprite extends LoopingSprite {
    public SparklingLockedBeamsSprite(MyColors color1, MyColors color2) {
        super("sparklinglockedbeams", "gauge.png", 0x30, 32, 32);
        setColor1(color1);
        setColor2(color2);
        setColor3(MyColors.WHITE);
        setFrames(4);
        setDelay(6);
    }
}
