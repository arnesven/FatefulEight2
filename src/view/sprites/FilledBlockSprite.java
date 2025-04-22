package view.sprites;

import view.MyColors;

public class FilledBlockSprite extends PortraitSprite {

    public FilledBlockSprite(MyColors color) {
        super("filled", "charset.png", (char)(0xFF));
        super.setColor1(color);
        super.setColor2(color);
        super.setColor3(color);
    }

    @Override
    public void setSkinColor(MyColors color) {

    }

    @Override
    public void setColor1(MyColors color) {
        throw new IllegalStateException("Should not set color on filled block sprites!");
    }

    @Override
    public void setColor2(MyColors color) {
        throw new IllegalStateException("Should not set color on filled block sprites!");
    }

    @Override
    public void setColor3(MyColors color) {
        throw new IllegalStateException("Should not set color on filled block sprites!");
    }

    @Override
    public void setColor4(MyColors color) {
        throw new IllegalStateException("Should not set color on filled block sprites!");
    }
}
