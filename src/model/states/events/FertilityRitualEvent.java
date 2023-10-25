package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.List;

public class FertilityRitualEvent extends RitualEvent {
    private static final Sprite MAYPOLE_SPRITE = new Sprite32x32("maypole", "ritual.png", 0x01,
            MyColors.DARK_GREEN, MyColors.ORC_GREEN, MyColors.LIGHT_RED, MyColors.YELLOW);

    public FertilityRitualEvent(Model model) {
        super(model, MyColors.GREEN);
    }

    @Override
    protected CombatTheme getTheme() {
        return new GrassCombatTheme();
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        GameCharacter druid = makeRandomCharacter(3);
        druid.setClass(Classes.DRU);
        druid.addToHP(999);
        ritualists.set(0, druid);
        println("You come to a meadow where peasants have erected a maypole adorned with many beautiful flowers.");
        println("There are " + MyStrings.numberWord(ritualists.size()) + " mages here who are about to do a fertility ritual. " +
                "They are looking for some extra mages to join them in performing it.");
        if (ritualists.size() + model.getParty().size() < 5) {
            println("Unfortunately you do not have enough party members to join the ritual.");
            model.getLog().waitForAnimationToFinish();
            return false;
        }
        showExplicitPortrait(model, ritualists.get(0).getAppearance(), ritualists.get(0).getName());
        portraitSay("We try this ritual once a year. For those years when the ritual has been successful " +
                "the crops around here grow well, " +
                "the orchards are full of fruit, and many babies are born.");
        portraitSay("But when the ritual fails, there is drought, or terrible rains, and there is not much " +
                "merrymaking in the huts.");
        portraitSay("Some of you seem slightly magically inclined. Won't you lend us a hand?");
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        if (success) {
            portraitSay("Thank you for helping us. Please allow me to heal your wounds...");
            super.healParty(model);
            portraitSay("Now the fertility of the land will be much improved and we " +
                    "shall have no trouble replenishing our stores. Please allow me to grant you " +
                    "this gift.");
            println("The party receives " + power*15 + " ingredients.");
            model.getParty().getInventory().addToIngredients(power*15);
            println("The party receives " + power*20 + " rations.");
            model.getParty().addToFood(power*20);
        } else {
            portraitSay("How unfortunate. Now we will have to endure another year of hardship. Well, so long friends.");
        }
        println("You part ways with the fertility mages.");
    }

    @Override
    public Sprite getCenterSprite() {
        return MAYPOLE_SPRITE;
    }

}
