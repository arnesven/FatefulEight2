package view.sprites;

import view.MyColors;

public class CardGameButtonSprite extends Sprite32x16 {
    public CardGameButtonSprite(int num, MyColors color2) {
        super("cardgamebutton" + num, "cardgame.png", num);
        setColor1(MyColors.BLACK);
        setColor2(color2);
    }
}
