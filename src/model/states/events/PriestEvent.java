package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.LargeShield;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.Scepter;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class PriestEvent extends DarkDeedsEvent {
    private static final int BLESS_COST = 2;
    private final boolean withIntro;
    private final CharacterAppearance portrait;

    public PriestEvent(Model model, boolean withIro, CharacterAppearance app) {
        super(model, "Talk to", MyRandom.randInt(2, 50));
        this.withIntro = withIro;
        this.portrait = app;
    }

    public PriestEvent(Model model) {
        this(model, true, PortraitSubView.makeRandomPortrait(Classes.PRI));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        if (withIntro) {
            println("The party meets a priest.");
        }
        showExplicitPortrait(model, portrait, "Priest");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("The priest offers to bless the members of the party - for a small 'donation'. ");
        while (true) {
            if (model.getParty().getGold() < BLESS_COST) {
                println("Unfortunately you cannot afford any more 'donations' right now.");
                break;
            }
            print("Would you like to pay " + BLESS_COST + " gold to bless a party member? (Y/N) ");
            if (yesNoInput()) {
                GameCharacter gc = model.getParty().partyMemberInput(model, this, null);
                println("The priest blesses " + gc.getName() + ".");
                gc.addToHP(1000);
                gc.addToSP(1000);
                model.getParty().addToGold(-BLESS_COST);
                model.getParty().partyMemberSay(model, gc, List.of("I feel refreshed!",
                        "I feel like a new person!", "What a cleansing feeling!",
                        "I feel reborn!", "Ahhh, that did wonders for me!"));
            } else {
                break;
            }
        }
        print("The priest also offers to guide you in the ways of priesthood, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.PRI);
        changeClassEvent.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Priest");
        return true;
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Priest", "", portrait.getRace(), Classes.PRI, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new Scepter(), new PilgrimsCloak(), new LargeShield()));
        gc.setLevel(MyRandom.randInt(1, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(4); i > 0; --i) {
            enemies.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.PRI), Classes.PRI, new Scepter()));
        }
        enemies.addAll(makeBodyGuards(MyRandom.randInt(2), 'C'));
        return enemies;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
