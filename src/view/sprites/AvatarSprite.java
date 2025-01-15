package view.sprites;

import model.characters.appearance.CharacterAppearance;
import model.races.Dwarf;
import model.races.Halfling;
import model.races.Race;
import view.MyColors;

import java.util.List;

public class AvatarSprite extends LoopingSprite {

    private final Race race;
    private final MyColors color2;
    private final MyColors color3;
    private final MyColors color4;
    private final Sprite hairSprite;
    private final Sprite hairFromBack;
    private final int num;
    private Sprite32x32 deadSprite;
    private int currentFrame = 0;
    private int count = 0;
    private int delay = 16;

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color3, MyColors color4, Sprite hairSprite, Sprite hairFromBack) {
        super("avatar"+num+race.getName(), "avatars.png", adjustForRace(num, race), 32, 32, List.of(hairSprite));
        this.race = race;
        this.num = num;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.hairSprite = hairSprite;
        this.hairFromBack = hairFromBack;
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
        deadSprite = new Sprite32x32("deadavatar", "avatars.png", num+3, MyColors.BLACK, color2, race.getColor());
        deadSprite.setColor4(color4);
    }

    private static int adjustForRace(int num, Race race) {
        if (race instanceof Halfling) {
            return num + 7;
        }
        if (race instanceof Dwarf) {
            return num + 4;
        }
        return num;
    }

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color3, Sprite hairSprite, Sprite hairFromBack) {
        this(race, num, color2, color3, color2, hairSprite, hairFromBack);
    }

    public Sprite getDead() {
        return deadSprite;
    }

    @Override
    protected int getCurrentFrameIndex(int currentFrame) {
        switch (currentFrame) {
            case 2:
                return 0;
            case 3:
                return 2;
            default:
                return currentFrame;
        }
    }

    public Sprite getAvatarBack() {
        return new AvatarSprite(race, num+0x10, color2, color3, color4, hairFromBack, CharacterAppearance.noHair());
    }
}
