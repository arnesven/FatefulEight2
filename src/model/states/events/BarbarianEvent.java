package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

public class BarbarianEvent extends DailyEventState {
    private Race race;

    public BarbarianEvent(Model model) {
        super(model);
        this.race = Race.ALL;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.BBN, race,"Barbarian");
        println("Clad in bear fur, a few patches of leather armor and wielding an enormous axe, " +
                "this barbarian seems to belong to a proud tribe. The party and the barbarian share " +
                "stories at the campfire this evening but must be careful " +
                "not to seem too superior, lest to offend the barbarian.");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Entertain, 7);
        if (result) {
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.BBN);
            print("The barbarian is pleased with your stories and songs and offers to train you in the ways of being a barbarian, ");
            change.areYouInterested(model);
        } else {
            String gender = MyRandom.randomGender();
            println("The barbarian is quickly bored by your exaggerations, superlatives " +
                    " and expletives. With an ugly frown, " + gender + " just wanders off.");
        }
    }
}
