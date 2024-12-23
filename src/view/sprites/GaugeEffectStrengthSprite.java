package view.sprites;

import view.MyColors;

public class GaugeEffectStrengthSprite extends LoopingSprite{
    public GaugeEffectStrengthSprite(int row) {
        super("gaugeeffectstrength", "gauge.png", row * 0x10 + 7, 16, 16);
        setFrames(5);
        setColor1(MyColors.WHITE);
        setColor2(MyColors.LIGHT_BLUE);
        setDelay(8);
    }
}
