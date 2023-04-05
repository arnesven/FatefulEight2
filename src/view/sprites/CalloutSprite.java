package view.sprites;

import util.MyPair;
import view.MyColors;

public class CalloutSprite extends TimedAnimationSprite {

    public CalloutSprite(int num) {
        super("callout"+num, "callouts.png", num, 300, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    public static MyPair<Integer, String> getSpriteNumForText(String text) {
        int spriteNum = 2;
        if (text.charAt(text.length()-1) == '!') {
            spriteNum = 0;
        } else if (text.charAt(text.length()-1) == '?') {
            spriteNum = 1;
        } else if (text.charAt(text.length()-1) == '^') {
            spriteNum = 0x10;
            text = text.substring(0, text.length()-1);
        } else if (text.charAt(text.length()-1) == '3') {
            spriteNum = 0x11;
            text = text.substring(0, text.length()-1);
        } else if (text.charAt(text.length()-1) == '#') {
            spriteNum = 0x12;
            text = text.substring(0, text.length()-1);
        }
        return new MyPair<>(spriteNum, text);
    }
}
