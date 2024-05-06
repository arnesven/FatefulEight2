package view.sprites;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;

import java.util.List;

public class AvatarSprite extends LoopingSprite {

    private final Race race;
    private final MyColors color2;
    private final MyColors color4;
    private final Sprite hairSprite;
    private final Sprite hairFromBack;
    private final int num;
    private Sprite32x32 deadSprite;
    private int currentFrame = 0;
    private int count = 0;
    private int delay = 16;

    public AvatarSprite(Race race, int num, MyColors color2, MyColors color4, Sprite hairSprite, Sprite hairFromBack) {
        super("avatar"+num+race.getName(), "avatars.png", num+(race.isShort()?4:0), 32, 32, List.of(hairSprite));
        this.race = race;
        this.num = num;
        this.color2 = color2;
        this.color4 = color4;
        this.hairSprite = hairSprite;
        this.hairFromBack = hairFromBack;
        setFrames(4);
        setColor1(MyColors.BLACK);
        setColor2(color2);
        setColor3(race.getColor());
        setColor4(color4);
        deadSprite = new Sprite32x32("deadavatar", "avatars.png", num+3, MyColors.BLACK, color2, race.getColor());
        deadSprite.setColor4(color4);
    }

    public AvatarSprite(Race race, int num, MyColors color2, Sprite hairSprite, Sprite hairFromBack) {
        this(race, num, color2, color2, hairSprite, hairFromBack);
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
        return new AvatarSprite(race, num+0x10, color2, color4, hairFromBack, CharacterAppearance.noHair());
    }
}
