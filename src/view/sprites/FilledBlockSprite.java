package view.sprites;

import view.MyColors;

public class FilledBlockSprite extends PortraitSprite {
    public static final FilledBlockSprite BLACK = new FilledBlockSprite(MyColors.BLACK);
    public static final FilledBlockSprite BLUE = new FilledBlockSprite(MyColors.BLUE);

    public FilledBlockSprite(MyColors color) {
        super("filled", "charset.png", (char)(0xFF));
        setColor1(color);
        setColor2(color);
        setColor3(color);
    }

    @Override
    public void setSkinColor(MyColors color) {

    }
}
