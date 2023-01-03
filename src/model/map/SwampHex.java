package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import sound.BackgroundMusic;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

public class SwampHex extends WorldHex {
    private static SubView subView = new ImageSubView("theswamp", "THE SWAMP", "A nasty bog...", true);;

    public SwampHex(int roads, int rivers) {
        super(MyColors.GREEN, roads, rivers, new SwampLocation());
        super.setMusic(BackgroundMusic.mysticSong);
    }

    @Override
    public String getTerrainName() {
        return "swamp";
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }
}
