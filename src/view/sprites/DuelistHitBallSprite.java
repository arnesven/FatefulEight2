package view.sprites;

import view.MyColors;

public class DuelistHitBallSprite extends Sprite16x16 {
    public DuelistHitBallSprite(MyColors color2, MyColors color3, MyColors color4) {
        super("duelisthitball", "gauge.png", 0x14);
        setColor1(MyColors.DARK_GRAY);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
