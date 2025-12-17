package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.EquipableItem;
import model.items.HigherTierClothing;
import model.items.Item;
import model.items.accessories.HigherTierAccessory;
import model.items.clothing.Clothing;
import model.items.weapons.HigherTierWeapon;
import model.items.weapons.Weapon;
import model.map.UrbanLocation;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class AuctionEvent extends PersonalityTraitEvent {

    private Item item;
    private AdvancedAppearance manager;

    public AuctionEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
        this.item = null;
        if (mainCharacter.getEquipment().getWeapon() instanceof HigherTierWeapon) {
            item = mainCharacter.getEquipment().getWeapon();
        } else if (mainCharacter.getEquipment().getClothing() instanceof HigherTierClothing) {
            item = mainCharacter.getEquipment().getClothing();
        } else if (mainCharacter.getEquipment().getAccessory() instanceof HigherTierAccessory) {
            item = mainCharacter.getEquipment().getAccessory();
        }

    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation && item != null;
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(), gc -> gc != main));
        showEventCard(main.getFirstName() + " falls behind the rest of the party but " + hisOrHer(main.getGender()) +
                " pack is so terribly uncomfortable that " + heOrShe(main.getGender()) +
                " must repack it before walking any further.");
        partyMemberSay(main, "I guess I'll just have to catch up with them later. " +
                "Now what has been poking me in the back?");
        println("While handling the backpack, " + main.getFirstName() + " puts down the rest of " +
                hisOrHer(main.getGender()) + " gear on the ground. " + heOrSheCap(main.getGender()) +
                " hasn't looked away for more than a few minutes, but when " + heOrShe(main.getGender()) + " turns around...");
        partyMemberSay(main, "My " + item.getName().toLowerCase() + "! Gone!?");
        model.getLog().waitForAnimationToFinish();
        Item inner = null;
        if (item instanceof Weapon) {
            main.unequipWeapon();
            inner = ((HigherTierWeapon)item).getInnerItem();
        } else if (item instanceof Clothing) {
            main.unequipArmor();
            inner = ((HigherTierClothing)item).getInnerItem();
        } else {
            main.unequipAccessory();
            inner = ((HigherTierAccessory)item).getInnerItem();
        }

        partyMemberSay(main, "Just my luck... probably some punk just swiped it.");
        println(main.getFirstName() + " quickly gathers up the rest of " + hisOrHer(main.getGender()) + " stuff.");
        partyMemberSay(main, "But whoever did it can't have gotten far...");
        println(main.getFirstName() + " spends over an hour frantically searching for the " + inner.getName() + ". " +
                "Exhausted, and very annoyed, " + heOrShe(main.getGender()) + " sits down in a small square to catch " +
                hisOrHer(main.getGender()) + " breath.");
        println("While " + main.getFirstName() + " is sitting there, cursing over " +
                hisOrHer(main.getGender()) + " bad luck. A small crowd is gathering in the square in front of a makeshift " +
                "stage. " + main.getFirstName() + " asks a bystander what is going on.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.None, "Commoner");
        partyMemberSay(main, "Hey, what's going on?");
        portraitSay("They're having an auction here today. Supposed to be real nice stuff.");
        partyMemberSay(main, "Huh...");
        println(main.getFirstName() + " is about the start searching again when an announcer shouts loudly.");
        showRandomPortrait(model, Classes.OFFICIAL, "Auctioneer");
        portraitSay("Welcome to today's auction. We have many fine items to sell today! Well, let's get right to it. " +
                "Item number one is a " + item.getName().toLowerCase() + ".");
        partyMemberSay(main, "What the devil!");
        portraitSay("Yes! Very beautiful, very rare! Bidding starts at 50 gold. Do I have 50 gold?");
        print("Do you bid on your own property? (Y/N) ");
        if (giveUp(model, main)) {
            return;
        }
        partyMemberSay(main, "Hey that's mine!");
        portraitSay("50 gold from the " + manOrWoman(main.getGender()) + " over there. Do I have 100 gold?");
        println("A man in a blue coat on the other side of the square raises his hand.");
        portraitSay("100 gold from the man in blue. Thank you, do I have 150? 150 gold for this lovely " + inner.getName().toLowerCase() + "?");
        print("Do you keep fighting for your stuff (Y) or do you give up (N)? ");
        if (giveUp(model, main)) {
            return;
        }
        println(main.getFirstName() + " raises " + hisOrHer(main.getGender()) + " fist in the air.");
        partyMemberSay(main, "Now wait just a minute! I'm not letting anybody have...");
        portraitSay("150 gold from the persistent " + (main.getGender()?"lady":"gentleman") + "! Do I hear 200?");
        println("There is silence, as the crowd tries to guess what will happen next.");
        portraitSay("Nobody? Will this rare item go for just 150 gold? Will nobody bid 200? What about the man in blue over there?");
        println("The previous bidder just shakes his head.");
        portraitSay("Fair enough. 150 gold then, going once...");
        portraitSay("Going twice...");
        portraitSay("Aaaand... 200! The lady at the back bids 200 for this very fine item! Ladies and gentlemen, " +
                "200 has been bid, do I hear 250?");
        print("Do you keep fighting for your stuff (Y) or do you give up (N)? ");
        if (giveUp(model, main)) {
            return;
        }
        partyMemberSay(main, "Mine! It's mine I tell you!");
        println(main.getFirstName() + " is now shaking both " + hisOrHer(main.getGender()) + " fists in the air.");
        portraitSay("The loud " + (main.getGender()?"lady":"gentleman") + " bids 250 I see. Well, good for you. " +
                "But it will be yours when nobody else dares outbid you! Do I hear 300?");
        println("The crowd is now very silent. The woman at the back makes a dismissing wave of her hand.");
        portraitSay("Going once... Going twice... And sold to the " + (main.getGender()?"lady":"gentleman") +
                " jumping up and down. My colleague here will take your money.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        CharacterAppearance other = PortraitSubView.makeRandomPortrait(Classes.None);
        this.manager = PortraitSubView.makeRandomPortrait(Classes.SPY);
        println("The announcer hands the " + inner.getName() + " to his assistant who takes it off the stage. The auction then " +
                "continues with other items. " + main.getFirstName() + " approaches the " + manOrWoman(other.getGender()) +
                " holding the " + inner.getName() + ".");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, other, "Assistant");
        partyMemberSay(main, "Now look. This thing actually belongs to me. I was robbed of it earlier today!");
        portraitSay("Nonsense! Our merchandise is supplied by honest agents!");
        partyMemberSay(main, "But, but... it's mine!");
        portraitSay("I'm afraid you must pay up, 250 gold was the bid.");
        for (int i = 0; i < 2; ++i) {
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            showExplicitPortrait(model, other, "Assistant");
            List<String> options = new ArrayList<>(List.of("Belt assistant and grab gear", "Abandon gear"));
            if (i == 0) {
                options.add(0, "Attempt explicit description");
            }
            if (model.getParty().getGold() >= 250) {
                options.add(0, "Pay 250 gold");
            }
            int count = multipleOptionArrowMenu(model, 24, 24, options);
            if (options.get(count).contains("Belt")) {
                beltAndGrab(model, main);
                break;
            } else if (options.get(count).contains("description")) {
                if (attemptDescription(model, main)) {
                    break;
                }
            } else if (options.get(count).contains("Abandon")) {
                abandonGear(model, main);
                break;
            } else {
                partyMemberSay(main, "FINE. I'll pay...");
                println(main.getFirstName() + " hands over the money.");
                model.getParty().spendGold(250);
                portraitSay("Thank you. Here is your item.");
                ((EquipableItem) item).equipYourself(main);
                partyMemberSay(main, "What a crock...");
                break;
            }
        }
        model.getParty().unbenchAll();
    }

    private void beltAndGrab(Model model, GameCharacter main) {
        SkillCheckResult result = main.testSkill(model, Skill.UnarmedCombat, 7);
        if (result.isSuccessful()) {
            println(main.getName() + " knocks the assistant out and grabs the " + item.getName() + ".");
            partyMemberSay(main, "Don't take my stuff!");
            ((EquipableItem)item).equipYourself(main);
            showExplicitPortrait(model, manager, "Manager");
            portraitSay("Hey! What's going on here. HEY! Stop thief!");
        } else {
            println(main.getName() + " makes an awkward tackle towards the assistant, who quickly moves out of the way.");
            portraitSay("Help! I'm being assaulted!");
            GeneralInteractionEvent.addToNotoriety(model, this, GeneralInteractionEvent.ASSAULT_NOTORIETY);
        }
        partyMemberSay(main, "Uh-oh...");
        println("Realizing that things are going south quickly, " + main.getFirstName() + " makes a run for it.");
    }

    private boolean attemptDescription(Model model, GameCharacter main) {
        SkillCheckResult result = main.testSkill(model, Skill.Logic, 9);
        if (result.isSuccessful()) {
            partyMemberSay(main, "Just listen. Turn it over. You'll see a scratch shaped like a diamond next to a grease stain.");
            portraitSay("Hmm... that's... you're correct. How did you know that?");
            partyMemberSay(main, "I keep telling you, It's mine.");
            portraitSay("Yes it does seem so. Uhm, I don't know if I have the authority to...");
            partyMemberSay(main, "I'm sure we can settle it with the constables. I see one right over there. Why don't we call " +
                    himOrHer(MyRandom.flipCoin()) + " over?");
            println("Another person who has been standing near by suddenly joins the conversation.");
            showExplicitPortrait(model, manager, "Manager");
            portraitSay("That won't be necessary. There's obviously been some mistake. Here, take the item as a token of our " +
                    "good will.");
            partyMemberSay(main, "Hmmph. Well... alright.");
            println("The assistant hands over the lost gear. " + main.getFirstName() + " takes it and finds " +
                    hisOrHer(main.getGender()) + " way back to the party.");
            ((EquipableItem) item).equipYourself(main);
            return true;
        }
        partyMemberSay(main, "But really... I know the thing like the back of my hand!");
        portraitSay("Oh really. What color is it on the bottom here.");
        partyMemberSay(main, "Uhm... pink?");
        println("Another person who has been standing near by suddenly joins the conversation.");
        showExplicitPortrait(model, manager, "Manager");
        portraitSay("Sir. We must insist that you pay the sum you bid now. We will have no funny business here.");
        return false;
    }

    private boolean giveUp(Model model, GameCharacter main) {
        if (!yesNoInput()) {
            abandonGear(model, main);
            return true;
        }
        return false;
    }

    private void abandonGear(Model model, GameCharacter main) {
        partyMemberSay(main, "Ah... it's not worth the fuss. I'm out of here.");
        println(main.getFirstName() + " leaves the auction, to go find the party.");
        removePortraitSubView(model);
        model.getParty().unbenchAll();
    }
}
