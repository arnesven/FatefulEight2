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
            Classes.THF.getAvatar(Race.WOOD_ELF, new RiboxAnari()),
            Classes.THF.getAvatar(Race.HIGH_ELF, new AlewynSolethal())
    };
    private final Sprite sprite;

    public BrotherhoodCronyEnemy(char enemyGroup) {
        super(enemyGroup);
        setName("Brotherhood thug");
        this.sprite = avatars[MyRandom.randInt(avatars.length)];
    }

    public BrotherhoodCronyEnemy(char enemyGroup, Race race) {
        super(enemyGroup);
        setName("Brotherhood thug");
        Race[] races = new Race[]{Race.SOUTHERN_HUMAN,
                                  Race.NORTHERN_HUMAN,
                                  Race.HALF_ORC,
                                  Race.DARK_ELF,
                                  Race.HALFLING,
                                  Race.DWARF,
                                  Race.WOOD_ELF,
                                  Race.HIGH_ELF};
        int i = 0;
        for (; i < races.length; ++i) {
            if (race.id() == races[i].id()) {
                break;
            }
        }
        if (i < races.length) {
            sprite = avatars[i];
        } else {
            sprite = avatars[0];
        }
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }
}
