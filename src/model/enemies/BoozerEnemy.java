package model.enemies;

import model.races.Race;
import view.sprites.Sprite;

public class BoozerEnemy extends MuggerEnemy {
    private final MuggerSprite sprite;

    public BoozerEnemy(char b, Race r) {
        super(b);
        setName("Boozer");
        this.sprite = new MuggerSprite(r);
    }


    @Override
    protected Sprite getSprite() {
        return sprite;
    }
}
