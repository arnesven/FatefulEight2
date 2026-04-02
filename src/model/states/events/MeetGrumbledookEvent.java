package model.states.events;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.*;
import model.characters.special.GrumbledookAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.conditions.PermanentlyInvisibleCondition;
import model.combat.conditions.SnakeCondition;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import util.MyPair;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeetGrumbledookEvent extends DailyEventState {
    private final GameCharacter victim;
    private final FindGrumbledookTask task;
    private CharacterAppearance enchanter;

    public MeetGrumbledookEvent(Model model, GameCharacter victim, FindGrumbledookTask task) {
        super(model);
        this.victim = victim;
        this.task = task;
        enchanter = new GrumbledookAppearance();
        enchanter.setClass(Classes.None);
        enchanter.setSpecificClothing(new SwimAttire());
    }

    public static Achievement.Data getAchievementData() {
        return new Achievement.Data(MeetGrumbledookEvent.class.getCanonicalName(),
                "Invisible!", "You cured a party member's permanent invisibility " +
                "with help from the Great Grumbledook.");
    }


    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        if (task.isGrumbledookMet()) {
            enchanter.setClass(Classes.ENCHANTRESS);
            showExplicitPortrait(model, enchanter, "Grumbledook");
            portraitSay("You're back. Do you have something of bone?");
            checkForBoneItem(model);
            return;
        }
        leaderSay("Let's see if we can find this enchanter fellow.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 8);
        if (!success) {
            println("You spend all day asking around for Grumbledook but nobody seems to know who you're talking about.");
            partyMemberSay(model.getParty().getRandomPartyMember(), "Maybe we're pronouncing the name wrong?");
            if (model.getParty().size() > 1) {
                partyMemberSay(model.getParty().getRandomPartyMember(victim),
                        "You know... I've quite forgotten what you look like.");
                partyMemberSay(victim, "Shut up.");
            }
            return;
        }

        println("You quickly learn that the Great Grumbledook lives on the outskirts of town. A while later, you locate the " +
                "cottage and knock on the door.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, enchanter, "Grumbledook");
        task.setGrumbledookMet(true);

        portraitSay("Enter!");
        println("An old man opens the door. He is not wearing any clothing.");
        leaderSay("Uhm, excuse us... We're looking for Grumbledook the Great.");
        portraitSay("Yes, that's me. How can I help you?", FacialExpression.relief);
        leaderSay("Uhm, well... Are we interrupting something?", FacialExpression.questioning);
        portraitSay("No. Not at all. Why?", FacialExpression.questioning);
        leaderSay("You are not wearing any clothes.", FacialExpression.questioning);
        println("The man looks surprised to find you are right.");
        portraitSay("Oh! Just a moment!", FacialExpression.surprised);
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("The man quickly shuts the door. Several minutes pass.");
        partyMemberSay(victim, "Are we really at the right house? He just seems like an old cook.");
        model.getLog().waitForAnimationToFinish();
        enchanter.setClass(Classes.ENCHANTRESS);
        showExplicitPortrait(model, enchanter, "Grumbledook");
        portraitSay("There. Now, how can I help?");

        partyMemberSay(victim, "Look here. I've been turned invisible.");
        portraitSay("Goodness gracious! So you have. Hmmm... how extraordinary.");
        leaderSay("We were told you were the person to see about these kinds of problems.");
        portraitSay("I beg your pardon? Oh, yes! Of course... Please come inside.");
        println("The party steps into the cottage and encounters an extreme mess. " +
                "They stand in a large room which is overflowing with books, scrolls, " +
                "alchemical ingredients and strange bottles of liquids.");
        partyMemberSay(victim, "Okay, this is probably the right place.");
        portraitSay("What's your name?");
        partyMemberSay(victim, "I'm " + victim.getName() + ".");
        if (model.getParty().size() > 1) {
            portraitSay("Okay " + victim.getFirstName() + ", stand still. " +
                    "The rest of you can sit down. This will only take a moment.");
        } else {
            portraitSay("Okay " +  victim.getFirstName() + ", stand still. " +
                    "This will only take a moment.");
        }
        partyMemberSay(victim, "What are you going to do?");
        println("Grumbledook start to look through one of his bookcases.");
        portraitSay("It's here somewhere...");
        partyMemberSay(victim, "What is?");
        portraitSay("Where is that book... Yes here it is!");
        println("Grumbledook brings out a very dusty tome. As he opens it, half its pages fall out on the floor.");
        portraitSay("Oh dear me. Haven't opened this one in a while.", FacialExpression.relief);
        portraitSay("Now let me see... 'Painful Breathing', no... 'Paralysis Maximus', no... Here, 'Permanent Invisibility'.");
        portraitSay("Just let me memorize the cantrip.");
        partyMemberSay(victim, "Can you cure me?");
        portraitSay("Yes. Now be quite while I concentrate.");
        model.getLog().waitForAnimationToFinish();
        forcePortraitEyes(true);
        portraitSay("Aummmm...");
        println("Grumbledook closes his eyes and presses his palms together.");
        model.getLog().waitForAnimationToFinish();
        forcePortraitEyes(false);
        println("With a dramatic flourish, Grumbledook opens his eyes and throws his open hands forward, as if pushing somebody away.");
        portraitSay("Raz-ash-al-ca-carap!");
        delay(3000);
        println(victim.getFirstName() + " is suddenly pushed backward, as if a gust of wind knocked " + himOrHer(victim.getGender()) + " back.");
        model.getLog().waitForAnimationToFinish();
        partyMemberSay(victim, "Oooof!");
        println(victim.getName() + " loses 2 stamina.");
        victim.addToSP(-2);
        partyMemberSay(victim, "That was rough. I don't feel much different though. Was that supposed to do something?");
        portraitSay("Yes. That was suppose to lift the curse that was cast on you.");
        partyMemberSay(victim, "Well it didn't work... Wait, what do you mean 'cast on you'?");
        portraitSay("The curse that was cast on you. I'm assuming you ticked off some hedge wizard or warlock.");
        partyMemberSay(victim, "No... I ate a piece of fruit, an Ogeon.");
        portraitSay("Why didn't you say so from the start!? Ogeon poisoning can be cured by a simple draught.");
        partyMemberSay(victim, "Okay...");
        portraitSay("I'm sure I have some here somwhere...");
        println("Grumbledook starts rummaging around among dozens of bottles on desk by the window.");
        portraitSay("Yes, here!");
        println("He holds up a tiny vial, and looks at it with some consideration.");
        leaderSay("Is something wrong?", FacialExpression.questioning);
        portraitSay("Uh, no. Nothing. Here, drink it.");
        println("He uncorks the vial and hands it to " + victim.getFirstName() +
                ". The vial floats in mid air.");
        println("The vial floats upward toward where " + victim.getFirstName() + "'s invisible face.");
        portraitSay("WAIT! Wait! Just a moment! Give it back to me!", FacialExpression.surprised);
        partyMemberSay(victim, "What is it?");
        println("Grumbledook sniffs the vial and scowls.");
        portraitSay("No it's fine. Drink it now.");
        partyMemberSay(victim, "Are you sure this will cure me?");
        portraitSay("Yes, positive.");
        partyMemberSay(victim, "Alright. Cheers.");
        println("The vial is upended and its contents quickly pour out and disappear.");
        delay(4000);
        int hpLoss = victim.getHP() - 1;
        victim.addToHP(-hpLoss);
        println("The potion burns " + victim.getFirstName() + "'s intestines!");
        println(victim.getName() + " takes " + hpLoss + " damage.");
        partyMemberSay(victim, "Aaaarrgh! It burns!");
        portraitSay("Yes, that's an unfortunate side effect while the antidote neutralizes the poison.", FacialExpression.relief);
        partyMemberSay(victim, "Uuuuh... ow, ow, ouchiiie! Will it take long? Aaaaarrrghhh...");
        portraitSay("Not long, just a few seconds.");
        delay(4000);
        partyMemberSay(victim, "I think... the pain is subsiding... ouch... Ugh...");
        delay(2000);
        partyMemberSay(victim, "Can you see me?");
        portraitSay("Uhm. No.");
        partyMemberSay(victim, "But I drank that horrible potion...");
        portraitSay("Perhaps the poison has already inflicted permanent damage...");
        randomSayIfPersonality(PersonalityTrait.jovial, List.of(victim),
                "Honestly, I think you've inflicted more damage on " + victim.getFirstName() + " at this point.");
        partyMemberSay(victim, "What does that mean? You mean I'll be invisible for the rest of my life?");
        portraitSay("Not necessarily. If we can neutralize the poison, or draw it out, " +
                "then we'll need something to counter its effects.");
        partyMemberSay(victim, "Uh-huh. Don't tell me it's another potion.");
        portraitSay("No no... I have just the thing!");
        println("Grumbledook disappears into the next room. The party can see him scrounging for something among heaps of junk. " +
                "He finally finds a basket with a lid, standing in a corner.");
        portraitSay("Her she is.");
        partyMemberSay(victim, "She?");
        println("Grumbledook opens the basket and brings out a yellow snake.");
        portraitSay("Put this around your neck.");
        partyMemberSay(victim, "I don't know... Is she venomous?");
        portraitSay("Yes, very. But she is quite tame. I'm sure she won't bite you. " +
                "This snake has magical properties and will nullify the invisibility effect of the Ogeon poison.");

        partyMemberSay(victim, "Fine... I'll put her on.");
        InvisibleAppearance invisAppearance = (InvisibleAppearance) victim.getAppearance();
        AdvancedAppearance originalAppearance = (AdvancedAppearance) invisAppearance.getOriginalAppearance();
        originalAppearance.addFaceDetail(task.getSnake());
        originalAppearance.setDetailColor(MyColors.YELLOW);
        victim.removeCondition(PermanentlyInvisibleCondition.class);
        victim.addCondition(new SnakeCondition());
        victim.setAppearance(originalAppearance);

        portraitSay("There we are!");
        partyMemberSay(victim, "I'm visible! Finally. What a relief!");
        println(victim.getFirstName() + " carefully takes the snake off again.");
        model.getLog().waitForAnimationToFinish();
        delay(1000);
        victim.setAppearance(invisAppearance);
        partyMemberSay(victim, "Wait... what just happened?");
        leaderSay((model.getParty().getLeader() == victim ? "I":"You") + " turned invisible again.");
        portraitSay("Ah yes...");
        println("Grumbledook carefully places the snake back on " + victim.getFirstName() + ".");
        portraitSay("You'll have to keep her on.");
        model.getLog().waitForAnimationToFinish();
        victim.setAppearance(originalAppearance);
        partyMemberSay(victim, "What? For how long.");
        portraitSay("For as long as you want to stay visible.");
        partyMemberSay(victim, "But that's preposterous! I can't walk around with a snake on my shoulders for the rest of my life!");
        portraitSay("Hmm... well, I guess we could try to make an entanglement between you and the snake.");
        leaderSay("What do you mean 'entanglement'?");
        portraitSay("It's a magical ritual. If we're successful, it will create a magical bond between you and the snake. " +
                "You will be permanently connected, even when you are apart. The snake could stay here, and you could go on with your life.");
        partyMemberSay(victim, "Fine. Let's do that.");
        portraitSay("Unfortunately it's a rather complicated ritual and I need special supplies.");
        partyMemberSay(victim, "What kind?");
        portraitSay("Specifically, I need an item made from bone.");
        leaderSay("Don't you have something like that lying around here?");
        portraitSay("Perhaps. But in any case, it must belong to you for the ritual to work.");
        checkForBoneItem(model);
    }

    private void checkForBoneItem(Model model) {
        if (!hasSomethingOfBone(model)) {
            leaderSay("I don't think " + iOrWe() + " have anything like that.");
            portraitSay("Then you'll have to find something like that and bring it here. " +
                    "I can't perform the ritual without it. Keep the snake for now.");
        }
        if (!askForBoneItem(model)) {
            return;
        }
        portraitSay("I have prepared the other things needed for the entanglement ritual. " +
                "Just hold still " + victim.getFirstName() + ".");
        println("Grumbledook places the item you gave him in a bowl with many herbs and spices. " +
                "He then pours a dark liquid into the bowl and sets it aflame with flint and tinder. Then he closes his eyes.");
        model.getLog().waitForAnimationToFinish();
        forcePortraitEyes(true);
        delay(2000);
        println("After a long while of humming and waving his hands, he finally opens his eyes.");
        forcePortraitEyes(false);
        portraitSay("There. I have now established a magical link between you and the snake. " +
                "You should now be able to take it off without going invisible.");
        partyMemberSay(victim, "Alright. Let's give this another shot.");
        model.getLog().waitForAnimationToFinish();
        AdvancedAppearance app = ((AdvancedAppearance)victim.getAppearance());
        app.removeDetail(task.getSnake());
        victim.removeCondition(SnakeCondition.class);
        victim.setAppearance(app);
        waitForReturn();
        leaderSay("It worked!");
        portraitSay("Of course it worked. Your item however, I fear has been reduced to dust.");
        partyMemberSay(victim, "A small price to pay!");
        portraitSay("Speaking of price. Now comes the matter of my fee.");
        leaderSay("Yes of course. We owe you something for the trouble. What is your standard fee?");
        portraitSay("For matters such as these, I normally take 100 gold pieces. " +
                "But I will consider giving you a discount. " +
                "250 gold should be a fair price.");
        task.setCompleted(true);
        completeAchievement(MeetGrumbledookEvent.class.getCanonicalName());
        leaderSay("250 gold? Are you mad?", FacialExpression.surprised);
        if (victim != getModel().getParty().getLeader()) {
            partyMemberSay(victim, "Pay the man. Worth every penny.");
        }

        for (int asking : new int[]{250, 150, 75, 50, 25, 10, 1}) {
            if (asking < 250) {
                portraitSay("Hmm... Well, I guess I could lower it somewhat. How does " + asking + " gold sound?");
            }
            if (model.getParty().getGold() < asking) {
                leaderSay("I'm afraid " + iOrWe() + " just don't have that much.");
            } else {
                println("Pay " + asking + " gold to Grumbledook? (Y/N) ");
                if (!yesNoInput()) {
                    leaderSay("We just can't give away that much gold. It doesn't make any sense.");
                } else {
                    model.getParty().spendGold(asking);
                    println("You reluctantly hand over " + asking + " gold to Grumbledook.");
                    portraitSay("Nice doing business with you. Now please stay away from Ogeon in the future.");
                    leaderSay(iOrWeCap() + " will.");
                    println("You leave Grumbledook's cottage.");
                    return;
                }
            }
        }

        portraitSay("Well, in that case. I suppose the experience itself will have to be my compensation.");
        leaderSay("That's very wise of you. I see now why you are called the 'Great'.");
        portraitSay("Yes. I suppose I am pretty good. Good luck in your future endeavours. And please return " +
                "if you have any other magical issues that need to be resolved.");
        leaderSay("We will. Bye Grumbledook.");
        println("You leave Grumbledook's cottage.");
    }

    private boolean hasSomethingOfBone(Model model) {
        return !getBoneItems(model).isEmpty();
    }

    private List<MyPair<Item, GameCharacter>> getBoneItems(Model model) {
        List<MyPair<Item, GameCharacter>> boneItems = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            Equipment eq = gc.getEquipment();
            if (hasBoneName(eq.getWeapon())) {
                boneItems.add(new MyPair<>(eq.getWeapon(), gc));
            }
            if (hasBoneName(eq.getClothing())) {
                boneItems.add(new MyPair<>(eq.getClothing(), gc));
            }
            if (eq.getAccessory() != null && hasBoneName(eq.getAccessory())) {
                boneItems.add(new MyPair<>(eq.getAccessory(), gc));
            }
        }
        boneItems.addAll(MyLists.transform(
                MyLists.filter(model.getParty().getInventory().getAllItems(), this::hasBoneName),
                it -> new MyPair<>(it, null)));
        return boneItems;
    }

    private boolean hasBoneName(Item it) {
        return it.getName().toLowerCase().contains("bone");
    }

    private boolean askForBoneItem(Model model) {
        leaderSay("I think " + iOrWe() + " have something like that.");
        println("Which item do you want to present to Grumbledook?");
        List<MyPair<Item, GameCharacter>> boneItems = getBoneItems(model);
        List<String> options = MyLists.transform(boneItems, pair ->
                pair.second == null ?
                        pair.first.getName() + " (Inventory)" :
                        pair.second.getFirstName() + "'s " + pair.first.getName());
        options.add("CANCEL");
        int chosen = multipleOptionArrowMenu(model, 24, 24, options);
        if (chosen >= boneItems.size()) {
            leaderSay("But we just can't give it up right now. We'll have to think about something else.");
            portraitSay("Then the snake stays where it is.");
            return false;
        }
        
        MyPair<Item, GameCharacter> chosenPair = boneItems.get(chosen);
        if (chosenPair.second != null) {
            if (chosenPair.first instanceof Weapon) {
                chosenPair.second.unequipWeapon();
            } else if (chosenPair.first instanceof Clothing) {
                chosenPair.second.unequipArmor();
            } else if (chosenPair.first instanceof Accessory) {
                chosenPair.second.unequipAccessory();
            } else {
                throw new IllegalStateException("Unexpected item was equipped?");
            }
        }
        model.getParty().getInventory().remove(chosenPair.first);
        leaderSay(iOrWeCap() + " have this " + chosenPair.first.getName() + ", will that work?");
        portraitSay("Let me see it. Yes, I think it will.");
        return true;
    }
}
