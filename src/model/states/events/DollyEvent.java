package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.items.Item;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.GameView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class DollyEvent extends DailyEventState {
    public DollyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean childGender = MyRandom.randInt(2) == 0;
        CharacterAppearance kid = PortraitSubView.makeChildAppearance(Race.randomRace(), childGender);
        showExplicitPortrait(model, kid, "Crying Child");
        println("You spend some time in a park and cannot help but overhearing a mother trying to console her child. " +
                "The child seems to be in some kind of distress. Apparently " + heOrShe(childGender) +
                " has lost " + hisOrHer(childGender) + " dolly.");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.benevolent, new ArrayList<>(), "Aw, poor child. Can we help?");
        didSay = didSay || randomSayIfPersonality(PersonalityTrait.unkind, new ArrayList<>(), MyRandom.sample(List.of("Typical kids...",
                "This really isn't any of our business.")));
        didSay = didSay || randomSayIfPersonality(PersonalityTrait.playful, new ArrayList<>(), "Hey kid, wanna see a magic trick?");

        print("Do you offer to look for the dolly? (Y/N) "); // TODO: More options: Craft dolly, Buy dolly
        if (yesNoInput()) {
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 10);
            if (result) {
                println("After spending some time searching, you finally find the dolly under a bush, not far from " +
                        "where you first encountered the mother and child. You hand the dolly over to the child, who grabs " +
                        "it and runs off in glee. The mother turns to you with a smile.");
                printQuote("Mother", "Thank you so much. We would have had a rough night if you hadn't saved the day. " +
                        "I was saving this heirloom for " + himOrHer(childGender) + ", but seeing as " +
                        heOrShe(childGender) + " can never keep track of things, I might as well give it to you as thanks.");
                Item heirloom = model.getItemDeck().getRandomJewelry();
                println("You received " + heirloom.getName() + "!");
                model.getParty().getInventory().addItem(heirloom);
            } else {
                println("You spend hours looking for the toy. Finally you give up, frustrated and tired.");
                println("Each party member exhausts 1 SP.");
                MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-1));

            }
            model.getParty().randomPartyMemberSay(model, List.of("I'm all sweaty!", "Kids..."));
        }

    }
}
