package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.BreastPlate;
import model.items.clothing.ChainMail;
import model.items.clothing.Clothing;
import model.items.clothing.LeatherArmor;
import model.items.weapons.*;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class CaptainEvent extends CombatExpertGeneralInteractionEvent {
    private AdvancedAppearance portrait;

    public CaptainEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(20, 40));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Talk to master-at-arms",
                "I know a master-at-arms who happily teaches anybody who will listen");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        showEventCard("The party meets the master-at-arms of the castle.");
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.CAP);
        showExplicitPortrait(model, portrait, "Master-at-arms");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println(heOrSheCap(portrait.getGender()) + " seems to be in a good mood and happily shows " +
                "you a few tricks with sword, spear and axe.");
        println("Each party member gains 10 experience.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().giveXP(model, gc, 10);
        }
        print("The master-at-arms also offers to instruct you in the ways of being a Captain, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.CAP);
        change.areYouInterested(model);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a captain here. I train the militia and keep the townspeople safe.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        Weapon weapon = MyRandom.sample(List.of(new Longsword(), new Spear(), new Estoc(), new Glaive(), new LongBow()));
        Clothing armor = MyRandom.sample(List.of(new ChainMail(), new BreastPlate(), new LeatherArmor()));
        GameCharacter gc = new GameCharacter("Mater-at-arms", "", portrait.getRace(), Classes.CAP, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(weapon, armor, null));
        gc.setLevel(MyRandom.randInt(4, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
