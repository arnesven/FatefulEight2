package model.enemies;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcBoarRiderEnemy extends OrcWarrior {

    private static final Sprite SPRITE = new MountedOrcWarriorSprite();

    public OrcBoarRiderEnemy(char a) {
        super(a);
        setName("Orc Boar Rider");
    }

    @Override
    public int getSpeed() {
        return 7;
    }

    @Override
    public int getMaxHP() {
        return 9;
    }

    @Override
    protected int getHeight() {
        return 2;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    private static class MountedOrcWarriorSprite extends LoopingSprite {
        public MountedOrcWarriorSprite() {
            super("mountedorcwarrior", "enemies.png", 0x8C, 32, 64);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.BROWN);
            setFrames(4);
        }
    }
}
