package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.EmeraldRing;
import model.items.clothing.StuddedJerkin;
import model.items.weapons.CompositeBow;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class ArcherEvent extends GeneralInteractionEvent {
    private final boolean withIntro;
    private final CharacterAppearance appearance;

    public ArcherEvent(Model model, boolean withIntro, CharacterAppearance appearance) {
        super(model, "Talk to", MyRandom.randInt(10, 20));
        this.withIntro = withIntro;
        this.appearance = appearance;
    }

    public ArcherEvent(Model model) {
        this(model, true, PortraitSubView.makeRandomPortrait(Classes.MAR));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit archer", "I know an archer who offers training in marksmanship");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        if (withIntro) {
            println("Out on the grounds, a skilled archer puts arrow after " +
                    "arrow right in the bullseye.");
        }
        showExplicitPortrait(model, appearance, "Archer");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        if (withIntro) {
            println("The archer gladly gives all who will listen a free lesson in marksmanship.");
            println("Each party member gains 10 experience.");
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                model.getParty().giveXP(model, gc, 10));
        }
        print("The archer offers to train you in the ways of being a Marksman, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.MAR);
        change.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, appearance, "Archer");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm an archer. I love sending arrows straight into the bullseye!";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Archer", "", appearance.getRace(), Classes.MAR,
                appearance, Classes.NO_OTHER_CLASSES, new Equipment(new CompositeBow(), new StuddedJerkin(), new EmeraldRing()));
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
