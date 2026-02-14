package model.races;

import model.characters.appearance.CharacterEyes;
import model.characters.appearance.SkeletonNeck;
import model.characters.appearance.TorsoNeck;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.PortraitSprite;

public class SkeletonRace extends Race {
    public static final CharacterEyes EYES = new CharacterEyes(0x167, 0x169, "", 0, 0);
    private static final PortraitSprite BLOCK_SPRITE = new FaceSprite(0x0F);

    public SkeletonRace() {
        super("Skeleton", MyColors.WHITE, 0, 0, 20, null, "");
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
    public TorsoNeck makeNeck(boolean gender) {
        return new SkeletonNeck();
    }

    @Override
    public int getInitialAttitudeFor(Race race) {
        return 0; // unused
    }
}
