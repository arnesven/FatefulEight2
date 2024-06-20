package view.sprites;

import view.MyColors;

public class FilledBlockSprite extends PortraitSprite {

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
