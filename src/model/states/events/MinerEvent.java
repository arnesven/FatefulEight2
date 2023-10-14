package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Pickaxe;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MinerEvent extends DarkDeedsEvent {
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
}
