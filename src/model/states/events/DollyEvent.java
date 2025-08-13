package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.items.Inventory;
import model.items.InventoryDummyItem;
import model.items.Item;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.CraftItemState;
import util.MyLists;
import util.MyRandom;
import view.sprites.Sprite;
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

        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Search for dolly", "Buy dolly", "Craft dolly", "Walk away"));
        if (choice == 0) {
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 8);
            if (result) {
                println("After spending some time searching, you finally find the dolly under a bush, not far from " +
                        "where you first encountered the mother and child. You hand the dolly over to the child, who grabs " +
                        "it and runs off in glee. The mother turns to you with a smile.");
                getReward(model, childGender);
            } else {
                println("You spend hours looking for the toy. Finally you give up, frustrated and tired.");
                println("Each party member exhausts 1 SP.");
                MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-1));
            }
            model.getParty().randomPartyMemberSay(model, List.of("I'm all sweaty!", "Kids..."));
        } else if (choice == 1) {
            leaderSay("I think I saw a toy store earlier. I'm sure they sell dolls.");
            println("You head over to the toy store and immediately see a cute dolly. Your heart warms briefly, until you see the price tag.");
            leaderSay("10 gold? That's outrageous.");
            randomSayIfPersonality(PersonalityTrait.stingy, List.of(), "Can't we just give "+ himOrHer(childGender) + " a stick to play with instead?");
            if (model.getParty().getGold() < 10) {
                leaderSay(iOrWeCap() + " can't even afford that. Let's just get on with our day and hope they find their doll.");
            } else {
                print("Do you spend 10 gold on the dolly? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("You know, I remember when I was a kid. Some things seemed so important...");
                    model.getParty().spendGold(10);
                    println("You buy the doll and return to the mother and child. You hand the dolly over to the child, who grabs " +
                            "it and runs off in glee. The mother turns to you with a smile.");
                    getReward(model, childGender);
                } else {
                    leaderSay(iOrWeCap() + " can't really afford this. Let's just get on with our day and hope they find their doll.");
                }
            }
        } else if (choice == 2) {
            leaderSay("Maybe we can make a dolly from the materials we've gathered.");
            boolean result;
            if (model.getParty().getInventory().getMaterials() == 0) {
                println("Since you do not have any materials you attempt to make a dolly from things you find " +
                        "in a nearby park, like twigs and leaves.");
                result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 10);
            } else {
                int materialCost = Math.min(3, model.getParty().getInventory().getMaterials());
                Item dummy = new DummyDollyItem();
                result = CraftItemState.makeItemFromMaterials(model, this, dummy, materialCost, "craft", false);
            }
            print("You present the crafted dolly to the child. ");
            if (result) {
                println(heOrSheCap(childGender) + " seems very happy with the new dolly and runs off in glee. The mother turns to you with a smile.");
                getReward(model, childGender);
            } else {
                println(heOrSheCap(childGender) + " just stares blankly at the shoddily made toy, then goes back to whining about " +
                        hisOrHer(childGender) + " missing dolly.");
                println("The mother shrugs apologetically toward you as she tries to calm her child.");
                leaderSay("Disheartened, you leave the mother and child.");
            }
        } else {
            leaderSay("We really don't have time for this foolishness.");
            println("The mother of the child seems utterly dismayed as you walk away.");
        }

    }

    private void getReward(Model model, boolean childGender) {
        printQuote("Mother", "Thank you so much. We would have had a rough night if you hadn't saved the day. " +
                "I was saving this heirloom for " + himOrHer(childGender) + ", but seeing as " +
                heOrShe(childGender) + " can never keep track of things, I might as well give it to you as thanks.");
        Item heirloom = model.getItemDeck().getRandomJewelry();
        println("You received " + heirloom.getName() + "!");
        model.getParty().getInventory().addItem(heirloom);
    }

    private static class DummyDollyItem extends InventoryDummyItem {
        public DummyDollyItem() {
            super("Dolly", 14);
        }

        @Override
        protected Sprite getSprite() {
            return null; // Should not be used.
        }

        @Override
        public int getWeight() {
            return 0;
        }

        @Override
        public void addYourself(Inventory inventory) { }

        @Override
        public String getShoppingDetails() {
            return null;  // Should not be used.
        }

        @Override
        public Item copy() {
            return null;  // Should not be used.
        }
    }
}
