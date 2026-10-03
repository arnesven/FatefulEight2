package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.map.ResourcePrevalence;
import model.races.Race;
import util.MyRandom;
import view.sprites.DieRollAnimation;

public class LumberMillEvent extends SimpleGeneralInteractionEvent {

    private static final int SCORE_QUOTIENT = 6;
    private final ChangeClassEvent changeClassEvent;
    private boolean freeLodge = false;

    public LumberMillEvent(Model model) {
        super(model, Classes.FOR, Race.randomRace(), "Lumberjack",
                "A large platform stands in the clearing ahead. Many huge logs lay all around " +
                        "and there is sawdust everywhere. As the party takes a short break the door to" +
                        " a nearby hut opens and a stocky fellow walks out and greets them.");
        changeClassEvent = new ChangeClassEvent(model, Classes.FOR);
    }

    @Override
    public String getDistantDescription() {
        return "a lumber mill";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to lumber mill", "There's " + getDistantDescription() + " nearby");
    }

    @Override
    protected boolean isFreeLodging() {
        return freeLodge;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        showEventCard("The lumberjack invites the party into his home for the night. A good earthy stew" +
                " awaits and good beer and bread. Stories are shared and the lumberjack tells of " +
                "the many strange things that lay hidden in these parts of the forest.");
        print("The Lumberjack offers to train you in the ways of being a Forester, ");
        changeClassEvent.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, getPortrait(), "Lumberjack");
        print("You may also chop some lumber here in an attempt to gain materials. Do you want to? (Y/N) ");
        if (yesNoInput()) {
            chopWood(model);
        }
        this.freeLodge = true;
        return true;
    }

    private void chopWood(Model model) {
        DieRollAnimation.setAnimationBlocks(false);
        Skill skillToUse = Skill.Labor;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult skill2Result = gc.testSkill(model, skillToUse);
            int score = skill2Result.getModifiedRoll();
            int resourcesFound = ResourcePrevalence.GOOD * (score / SCORE_QUOTIENT);
            if (resourcesFound == 0) {
                println(gc.getFirstName() + " didn't meaningfully contribute (" +
                        skillToUse.getName() + " " + skill2Result.asString() + ")");
            } else {
                println(gc.getFirstName() + " gained " + resourcesFound +
                        " resources " + skillToUse.getName()+ " " + skill2Result.asString() + ")");
                model.getParty().getInventory().addToMaterials(resourcesFound);
            }
            if (gc.getSP() > 0 && MyRandom.rollD6() < 3) {
                println(gc.getFirstName() + " lost 1 Stamina while chopping wood.");
                gc.addToSP(-1);
            }
        }
        DieRollAnimation.setAnimationBlocks(true);
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a lumber jack. I cut down trees and turn them into planks.";
    }
}
