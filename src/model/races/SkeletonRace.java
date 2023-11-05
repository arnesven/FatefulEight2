package model.races;

import model.characters.appearance.CharacterEyes;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.PortraitSprite;

public class SkeletonRace extends Race {
    public static final CharacterEyes EYES = new CharacterEyes(0x167, 0x169);
    private static final PortraitSprite BLOCK_SPRITE = new FaceSprite(0x0F);
    private final Race innerRace;

    public SkeletonRace(Race innerRace) {
        super("Skeleton", MyColors.WHITE, 0, 0, null, "");
        this.innerRace = innerRace;
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return BLOCK_SPRITE;
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return BLOCK_SPRITE;
    }

    @Override
    public boolean isSkeleton() {
        return true;
    }

    @Override
    public Shoulders getShoulders() {
        return innerRace.getShoulders();
    }
}
