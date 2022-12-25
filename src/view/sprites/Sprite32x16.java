package view.sprites;

public class Sprite32x16 extends Sprite {
    public Sprite32x16(String name, String mapPath, int num) {
        super(name, mapPath, num % 16, num / 16, 32, 16);
    }
}
