package view.sprites;

import java.util.List;

public class Sprite16x16 extends Sprite {
    public Sprite16x16(String name, String mapPath, int num) {
        super(name, mapPath, num % 16, num / 16, 16, 16);
    }
}
