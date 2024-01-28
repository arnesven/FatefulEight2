package model.enemies;

import model.characters.preset.*;
import model.classes.Classes;
import model.races.Race;
import util.MyRandom;
import view.sprites.Sprite;

public class BrotherhoodCronyEnemy extends BanditEnemy {
    private static Sprite[] avatars = new Sprite[]{
            Classes.THF.getAvatar(Race.SOUTHERN_HUMAN, new SebastianSmith()),
            Classes.THF.getAvatar(Race.NORTHERN_HUMAN, new RolfFryt()),
            Classes.THF.getAvatar(Race.HALF_ORC, new BazUrGhan()),
            Classes.THF.getAvatar(Race.DARK_ELF, new MialeeSeverin()),
            Classes.THF.getAvatar(Race.HALFLING, new BungoDarkwood()),
            Classes.THF.getAvatar(Race.DWARF, new TorhildAmbershard()),
            Classes.THF.getAvatar(Race.WOOD_ELF, new RiboxAnari())
    };
    private final Sprite sprite;

    public BrotherhoodCronyEnemy(char enemyGroup) {
        super(enemyGroup);
        setName("Brotherhood thug");
        this.sprite = avatars[MyRandom.randInt(avatars.length)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }
}
