package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.WolfHead;
import model.items.clothing.FurArmor;
import model.items.weapons.BastardSword;
import model.items.weapons.GrandMaul;
import model.items.weapons.GreatAxe;
import model.items.weapons.Weapon;
import model.races.Race;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class BarbarianEvent extends CombatExpertGeneralInteractionEvent {
    private Race race;
    private AdvancedAppearance portrait;

    public BarbarianEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(5, 10));
        this.race = Race.ALL;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.BBN, race);
        println("Clad in bear fur, a few patches of leather armor and wielding an enormous axe, " +
                "this barbarian seems to belong to a proud tribe. ");
        showExplicitPortrait(model, portrait, "Barbarian");
        randomSayIfPersonality(PersonalityTrait.snobby, List.of(model.getParty().getLeader()),
                "Does anybody know how to deal with this savage?");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("The party and the barbarian share " +
                "stories at the campfire this evening but must be careful " +
                "not to seem too superior, lest to offend the barbarian.");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Entertain, 7);
        if (result) {
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.BBN);
            print("The barbarian is pleased with your stories and songs and offers to train you in the ways of being a barbarian, ");
            change.areYouInterested(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, portrait, "Barbarian");
            return true;
        }
        String gender = heOrShe(portrait.getGender());
        println("The barbarian is quickly bored by your exaggerations, superlatives " +
                " and expletives. With an ugly frown, " + gender + " just wanders off.");
        leaderSay("I guess " + gender + " got offended...");
        return false;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a tribesman, a hunter, a warrior, all in one.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        Weapon weapon = MyRandom.sample(List.of(new BastardSword(), new GreatAxe(), new GrandMaul()));
        GameCharacter barb = new GameCharacter("Barbarian", "", race, Classes.BBN, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(weapon, new FurArmor(), new WolfHead()));
        barb.setLevel(MyRandom.randInt(3, 6));
        return barb;
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
