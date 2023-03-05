package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import sound.BackgroundMusic;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.util.List;

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
        int roll = MyRandom.rollD10();
        if (roll >= 5) {
            return MyRandom.sample(List.of(
                    new SpidersEvent(model),
                    new WitchHutEvent(model),
                    new VipersEvent(model),
                    // new CrocodilesEvent(model),
                    new LostEvent(model),
                    new LostEvent(model),
                    new LostEvent(model),
                    // new MosquitoesEvent(model),
                    new WoundedAdventurerEvent(model),
                    // new MushroomsEvent(model),
                    new HermitEvent(model),
                    // new SwampRaftEvent(model),
                    // new AmazonEvent(model),
                    new ChestEvent(model)
                    // new OctopusEvent(model)
            ));
        }
        return new NoEventState(model);
    }
}
