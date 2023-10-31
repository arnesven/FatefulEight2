package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.weapons.Longsword;
import model.races.Race;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class VeteranEvent extends DarkDeedsEvent {
    private final boolean withIntro;
    private CharacterAppearance app;

    public VeteranEvent(Model model, boolean withIntro) {
        super(model, "Talk to", MyRandom.randInt(2, 10));
        this.withIntro = withIntro;
    }

    public VeteranEvent(Model model) {
        this(model, true);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.app = PortraitSubView.makeOldPortrait(Classes.None, Race.randomRace(), MyRandom.flipCoin());
        showExplicitPortrait(model, app, "Veteran");
        if (withIntro) {
            println("The party passes an old tattered hut. Inside sits a venerable figure, " +
                    "who claims to be a veteran of the wars of old. ");
        }
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("The veteran asks for some food. ");
        if (model.getParty().getFood() < 3) {
            println(" but you are embarrassed to admit you have none you can spare.");
            printQuote("Veteran", "Times are rough, but somehow I'll survive.");
            return true;
        }

        print("Will you hand over 3 rations? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addToFood(-3);
            printQuote("Veteran", "Much obliged kind sir.");
            printQuote("Veteran", "I've served in several units. First I belonged to " +
                    "a unit of regulars, and later I was trained as a marksman.");
            print("The veteran is offering to train you in the ways of being a marksman ");
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.MAR);
            change.areYouInterested(model);
            printQuote("Veteran", "In the last war, I. was the most experienced soldier of my unit, " +
                    "so I was promoted to captain.");
            print("The veteran is offering to train you in the ways of being a captain ");
            change = new ChangeClassEvent(model, Classes.CAP);
            change.areYouInterested(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, app, "Veteran");
        } else {
            printQuote("Veteran", "Times are rough, but somehow I'll survive.");
        }
        return true;
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Veteran", "", app.getRace(), Classes.None, app,
                Classes.NO_OTHER_CLASSES, new Equipment(new Longsword()));
        gc.setLevel(MyRandom.randInt(3, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
