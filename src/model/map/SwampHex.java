package model.map;

import model.Model;
import model.map.locations.SwampMountainLocation;
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

    public SwampHex(int roads, int rivers, HexLocation location, int state) {
        super(MyColors.GREEN, roads, rivers, location, state);
        super.setMusic(BackgroundMusic.mysticSong);
    }

    public SwampHex(int roads, int rivers, int state) {
        this(roads, rivers, new SwampLocation(), state);
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
    public String getTerrainDescription() {
        return "Swamps are wetlands and marshlands, which are often home to all matter of strange creatures " +
                "and other dangers. They are often difficult to travel through and explorers often get lost in " +
                "the unwelcoming environment. Some swamps are wet enough that a raft may be constructed to traverse the swamp " +
                "upon.";
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        int roll = MyRandom.rollD10();
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 2) {
            return new DogEvent(model);
        } else if (3 <= roll && roll <= 4 && getLocation() instanceof SwampMountainLocation) {
            return MountainHex.generateMountainEvent(model);
        } else if (roll >= 5) {
            return MyRandom.sample(List.of(
                    new SpidersEvent(model),
                    new WitchHutEvent(model),
                    new VipersEvent(model),
                    new CrocodilesEvent(model),
                    new LostEvent(model),
                    new LostEvent(model),
                    new MosquitoesEvent(model),
                    new WoundedAdventurerEvent(model),
                    new MushroomsEvent(model),
                    new HermitEvent(model),
                    new AmazonEvent(model),
                    new ChestEvent(model),
                    new AbandonedShackEvent(model),
                    new FrogmenScoutsEvent(model),
                    new SwampRaftEvent(model),
                    new SwampRaftEvent(model),
                    new SwampRaftEvent(model),
                    new CryptEvent(model),
                    new LostExplorerEvent(model)
                    // TODO: Kiss a toad event
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState getNightTimeAmbushEvent(Model model) {
        if (MyRandom.rollD10() == 1) {
            return new CobraNightAttackEvent(model);
        }
        return null;
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new SwampRaftEvent(model), new WoundedAdventurerEvent(model), new MushroomsEvent(model)));
    }
}
