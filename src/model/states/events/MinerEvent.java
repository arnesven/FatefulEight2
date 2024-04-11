package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Pickaxe;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinerEvent extends GeneralInteractionEvent {
    private final Race race;
    private boolean withIntro;
    private AdvancedAppearance app;

    public MinerEvent(Model model, boolean withIntro, Race race) {
        super(model, "Talk to", MyRandom.randInt(10, 40));
        this.withIntro = withIntro;
        this.race = race;
    }

    public MinerEvent(Model model) {
        this(model, true, Race.randomRace());
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.app = PortraitSubView.makeRandomPortrait(Classes.MIN, race);
        showExplicitPortrait(model, app, "Miner");
        if (withIntro) {
            print("The party encounters a miner. ");
        }
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("The miner has all the gear needed to descend deep into " +
                "the earth, to dig for precious gems and metal ore. The " +
                "miner gladly demonstrates the gear and offers to teach you about the life of a miner, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.MIN);
        changeClassEvent.areYouInterested(model);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a miner by trade. I work in the mine.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Miner", "", app.getRace(), Classes.MIN, app,
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Pickaxe(), new LeatherArmor(), new SkullCap()));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(6); i > 0; --i) {
            enemies.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.MIN),
                    Classes.MIN, new Pickaxe()));
        }
        return enemies;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("mine", new MyPair<>("Can you tell me where the nearest mine is?",
                "You think I'll give up my secrets that easily? Get real. However, mines are pretty " +
                        "common actually. Look for them in hills and mountain areas."));
    }
}
