package view.sprites;

import java.util.ArrayList;
import java.util.List;

public class Sprite8x8 extends Sprite {
    public Sprite8x8(String name, String mapPath, int number, List<Sprite> layers) {
        super(name, mapPath, number % 16, number / 16, 8, 8, layers);
    }
    public Sprite8x8(String name, String mapPath, int number) {
        this(name, mapPath, number, new ArrayList<>());
    }
}
