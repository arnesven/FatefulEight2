package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.items.designs.CraftingDesign;
import model.races.AllRaces;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class WorkshopEvent extends DailyEventState {
    public WorkshopEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit workshop",
                "There's a workshop in town where a skilled craftsman makes lot's of interesting stuff.");
    }

    @Override
    protected void doEvent(Model model) {
        println("You enter what you think is a shop.");
        leaderSay("Hey... this isn't a shop, it's a workshop! There are tools and materials everywhere.");
        if (MyRandom.rollD10() < 5) {
            print("The tinkerer doesn't seem to be here right now. Do you 'borrow' some of " +
                    hisOrHer(MyRandom.flipCoin()) + " stuff? (Y/N) ");
            if (!yesNoInput()) {
                return;
            }
            int materials = MyRandom.randInt(5, 10);
            println("You found " + materials + " materials.");
            model.getParty().getInventory().addToMaterials(materials);
            randomSayIfPersonality(PersonalityTrait.lawful, List.of(model.getParty().getLeader()),
                    "Petty theft, that's what we're about now?");
            if (materials < 7) {
                CraftingDesign design = model.getItemDeck().getRandomDesign();
                println("You found " + design.getName() + "!");
                design.addYourself(model.getParty().getInventory());
            }
        } else {
            showRandomPortrait(model, Classes.ART, "Tinkerer");
            portraitSay("Oh hello there. Can I help you?");
            leaderSay("Uhm, actually I thought this was a store.");
            portraitSay("Oh, no, I don't sell anything. I make special orders though.");
            GameCharacter gc = model.getParty().getRandomPartyMember();
            if (gc.getRace().id() == AllRaces.HIGH_ELF.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me an elven spear similar to the ones forged in ancient times?");
            } else if (gc.getRace().id() == AllRaces.WOOD_ELF.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me pristine oak wood bow?");
            } else if (gc.getRace().id() == AllRaces.DARK_ELF.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me an elven blade with emeralds along the hilt?");
            } else if (gc.getRace().id() == AllRaces.DWARF.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me an axe as heavy and strong as the mountain itself?");
            } else if (gc.getRace().id() == AllRaces.HALFLING.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me cape which lets me blend into the surroundings?");
            } else if (gc.getRace().id() == AllRaces.NORTHERN_HUMAN.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me ring which turns me invisible?");
            } else if (gc.getRace().id() == AllRaces.SOUTHERN_HUMAN.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me an impenetrable suit of armor?");
            } else if (gc.getRace().id() == AllRaces.HALF_ORC.id()) {
                model.getParty().partyMemberSay(model, gc, "Can you make me a mace that will shatter anything I strike?");
            } else {
                model.getParty().partyMemberSay(model, gc, "Can you make me a pair of winged shoes?");
            }
            portraitSay("Certainly! Just let me estimate a cost...");
            println("The tinkerer seems to be counting on his fingers.");
            int cost = 0;
            int[] levels = new int[]{1200, 3200, 5600, 11500, 56000};
            for (int i = 0; i < levels.length; ++i) {
                if (levels[i] > model.getParty().getGold()) {
                    cost = levels[i];
                    break;
                }
            }
            portraitSay("The order will cost " + cost + " gold.");
            randomSayIfPersonality(PersonalityTrait.stingy, List.of(model.getParty().getLeader()), "That's ludicrous!");
            leaderSay("You can't be serious.");
            portraitSay("Was your request serious?");
            leaderSay("Don't you make 'normal stuff' too?");
            portraitSay("If you want 'normal stuff', why don't you craft it yourself at the crafting table out there? " +
                    "In fact, I'll give you some materials to get you started. Good Luck! Now get out of my workshop.");
            println("The party has received 5 materials.");
            model.getParty().getInventory().addToMaterials(5);
        }
    }
}
