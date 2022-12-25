package model.map;

import model.Model;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class RuinsLocation extends HexLocation {
    // TODO: Add imagesubview
    public RuinsLocation(String ruinsName) {
        super("Ruins of " + ruinsName);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("ruinsupper", 0x01, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("ruinslower", 0x11, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
    }

    @Override
    public void travelFrom(Model model) {
        model.playMainSong();
    }
}
