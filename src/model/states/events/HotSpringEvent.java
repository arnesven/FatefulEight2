package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.SwimAttire;
import model.enemies.BearEnemy;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.map.MountainHex;
import model.map.MountainLocation;
import model.map.TundraMountain;
import util.MyLists;
import util.MyRandom;

import java.util.List;
import java.util.function.Predicate;

public class HotSpringEvent extends PersonalityTraitEvent {
    public HotSpringEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex() instanceof MountainHex ||
                    model.getCurrentHex() instanceof TundraMountain;
    }

    @Override
    protected void doEvent(Model model) {
        partyMemberSay(model.getParty().getRandomPartyMember(), "Hey... there's steam coming from over there!");
        leaderSay("A hut?");
        println("When you get closer, you realize the steam is coming from a small pond.");
        leaderSay("It's a hot spring!");
        GameCharacter main = getMainCharacter();
        partyMemberSay(main, "Okay... so what?");
        leaderSay("Well... I'm not letting this opportunity pass me by. I'm getting in.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader() && gc != main) {
                    partyMemberSay(gc, MyRandom.sample(List.of("Me too!", "So am I.", "Count me in too.")));
            }
        }
        println("Everybody quickly disrobes and gets in the hot spring.");
        model.getLog().waitForAnimationToFinish();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != main) {
                undress(gc);
            }
        }
        println("Everybody except " + main.getFirstName() + ".");
        leaderSay("! It's really nice!");
        partyMemberSay(main, "Uhh... no thank you. I think I'll sit this one out.");
        leaderSay("Why? How often do you get a chance like this out in the wilderness?");
        partyMemberSay(main, "I don't...");
        leaderSay("Come on! Live a little! What's wrong?");
        partyMemberSay(main, "I don't like taking my clothes off!");
        leaderSay("Oh... okay...");
        partyMemberSay(main, "Just leave me alone.");
        leaderSay("No reason to get upset.");
        partyMemberSay(main, "I think I'll take a walk.");
        leaderSay("Alright. Don't get lost!");
        println(main.getName() + " leaves the hot spring to take a walk.");
        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(),
                gameCharacter -> gameCharacter != main));
        println(main.getFirstName() + " is now the temporary leader of the party.");
        GameCharacter oldLeader = model.getParty().getLeader();
        model.getParty().setLeader(main);

        println("As " + heOrShe(main.getGender()) + " wanders, " + heOrShe(main.getGender()) + " mutters angrily to "
                    + hisOrHer(main.getGender()) + "self.");
        leaderSay("'Don't get lost...'");
        model.getLog().waitForAnimationToFinish();
        leaderSay("Like I could get lost around here...");
        leaderSay("They're probably splashing around in that pool like a bunch of juvenile...");
        println("After walking for a little while, " + main.getFirstName() + " looks up and realizes " + heOrShe(main.getGender()) +
                " is standing in front of another hot spring.");
        leaderSay("Maybe I should take a dip...");
        println("At that moment, " + main.getFirstName() + " hears a shuffling sound. It's a bear shuffling down the " +
                "path toward the hot spring. It seems like it hasn't noticed " + main.getFirstName() + " yet, but it " +
                "certainly will unless " + heOrShe(main.getGender()) + " hides quickly.");
        int chosen = multipleOptionArrowMenu(model, 24, 30,
                List.of("Jump into pool immediately",
                        "Undress, then jump into pool",
                        "Stand and face the bear."));
        if (chosen == 0) {
            jumpInWithClothesOn(model, main, oldLeader);
        } else if (chosen == 1){
            UndressThenJumpIn(model, main, oldLeader);
        } else {
            fightBear(model, main, oldLeader);
        }
    }

    private void undress(GameCharacter gc) {
        gc.setSpecificClothing(new SwimAttire());
        gc.unequipWeapon();
        gc.unequipArmor();
        gc.unequipAccessory();
    }

    private void jumpInWithClothesOn(Model model, GameCharacter main, GameCharacter oldLeader) {
        println(main.getFirstName() + " quickly jumps into the warm pool of water. When the bear comes close, " +
                heOrShe(main.getGender()) + " submerges.");
        if (main.getEquipment().getWeapon() != null) {
            print("Thankfully, the bear only lingers a little to sniff " +
                    main.getFirstName() + "'s " + main.getEquipment().getWeapon().getName().toLowerCase());
        } else {
            print("Thankfully, the bear only lingers a little while ");
        }
        println(" before leaving.");
        leaderSay("Phew... that was close.");
        println("As soon as " + main.getFirstName() + " gets out of the water " + heOrShe(main.getGender()) + " realizes " +
                hisOrHer(main.getGender()) + " mistake. The wet clothes are quickly cooling off in the chilly air.");
        println("Soaked and cold " + main.getFirstName() + " starts walking back to the other hot spring.");

        int spBefore = main.getSP();
        main.addToSP(-main.getSP());
        if (spBefore > main.getSP()) {
            println(main.getName() + " loses " + spBefore + " Stamina Points.");
        } else if (main.getHP() > 2) {
            main.addToHP(-2);
            println(main.getName() + " loses 2 Health Points.");
        }

        returnToParty(model, oldLeader);
        leaderSay(main.getFirstName() + ", there you are. Why are you hugging yourself?");
        partyMemberSay(main, "Cold... so cold...");
        leaderSay("Why are you soaking wet?");
        partyMemberSay(main, "Bear... bear...");
        leaderSay("I'm can't quite understand you, your teeth are clattering. " +
                "But you got to get out of these wet clothes, you'll get pneumonia.");
        partyMemberSay(main, "No... no...");
        leaderSay("Yes, don't be silly. Undress, get in the hot spring while your clothes dry by the fire. Don't worry, " +
                "we'll look away.");
        println(main.getFirstName() + " is to cold and tired to refuse. " + heOrSheCap(main.getGender()) + " disrobes and gets " +
                "into the hot spring, bright pink in the face.");
        leaderSay("I guess we'll make camp here tonight.");
        println("Without a ferocious bear to worry about " + main.getFirstName() + " can actually seize the moment and appreciate " +
                "the nice hot spring, even though " + heOrShe(main.getGender()) +
                " does worry that the rest of the party members will sneak a peak at " + himOrHer(main.getGender()) + ".");
    }


    private void UndressThenJumpIn(Model model, GameCharacter main, GameCharacter oldLeader) {
        println(main.getFirstName() + " quickly drops " + hisOrHer(main.getGender()) + " clothes and gear next to the pool " +
                "and jumps into the warm water.");
        Clothing clothes = main.getEquipment().getClothing();
        main.getEquipment().setClothing(new JustClothes());
        undress(main);
        println("When the bear comes close, " + heOrShe(main.getGender()) + " submerges, hoping against hope that the " +
                "bear will leave quickly.");
        println("The bear stops and sniffs " + main.getFirstName() + "'s gear. Then, to " + main.getFirstName() + "'s utter " +
                "devastation, it grabs a hold of " + main.getFirstName() + "'s clothes with its mouth and walks away.");
        leaderSay("Typical...");
        model.getLog().waitForAnimationToFinish();
        int chosen = multipleOptionArrowMenu(model, 24, 30,
                List.of("Get out and return (naked)",
                        "Stay in the hot spring"));
        if (chosen == 0) {
            println("Reluctantly " + main.getFirstName() + " gets out of the pool, collects the rest of " +
                    hisOrHer(main.getGender()) + " gear and starts making " + hisOrHer(main.getGender()) + " way back " +
                    "to the other hot spring.");
            returnToParty(model, oldLeader);
            leaderSay(main.getFirstName() + ", there you are! But, where are your clothes?");
            println("With a bright pink face " + main.getFirstName() + " recounts the episode with the bear while " +
                    "trying to hide " + hisOrHer(main.getGender()) + "self behind a large boulder.");
            leaderSay("I understand. How terrible. Maybe we should stay together as a group from now on?");
            partyMemberSay(main, "Fine... uhm, does anybody have any spare clothes?");
            leaderSay("Yes. I think I have an extra pair of pants and a spare tunic here somewhere...");
            main.removeSpecificClothing();
            partyMemberSay(main, "Thank you " + model.getParty().getLeader().getFirstName() + ".");
        } else {
            println(main.getFirstName() + " stays in the water for a long time.");
            int spBefore = main.getSP();
            main.addToSP(-main.getSP());
            if (spBefore > main.getSP()) {
                println(main.getName() + " loses " + spBefore + " Stamina Points.");
            }
            model.getLog().waitForAnimationToFinish();
            println(heOrSheCap(main.getGender()) + " is almost about to faint from the heat when the rest of the " +
                    "party finally finds " + himOrHer(main.getGender()) + ".");
            returnToParty(model, oldLeader);
            leaderSay(main.getFirstName() + ", we've been looking all over for you? This is where you've " +
                    "been this whole time, having a private bath?");
            println("While trying to escape from the party's ogling stares, " + main.getFirstName() + " tries to explain " +
                    "what had happened with the bear.");
            leaderSay("Huh? It was probably the same bear we encounter a little while ago. " +
                    "It ran off when we approached and left something it had been chewing on on the ground. I thought it " +
                    "looked like your clothes. I have them here.");
            partyMemberSay(main, "Thank you. Uhm can you look away while I get out?");
            println("The party members turn around as " + main.getName() + " gets out of the pool and puts her gnawed on " +
                    "clothes back on.");
            main.removeSpecificClothing();
            main.getEquipment().setClothing(clothes);
            leaderSay("Let's move on. And let's keep together from now on.");
            partyMemberSay(main, "Fine... as long as I get to keep my clothes on.");
        }

    }

    private void fightBear(Model model, GameCharacter main, GameCharacter oldLeader) {
        println("Less than a minute later the bear spots " + main.getFirstName() + ".");
        println("It roars loudly, then pounces on " + main.getFirstName() + "!");
        runCombat(List.of(new BearEnemy('B')));
        setCurrentTerrainSubview(model);
        if (haveFledCombat()) {
            println("With the bear slain, " + main.getFirstName() + " returns to the rest of the party.");
            returnToParty(model, oldLeader);
            leaderSay("Did you have a nice walk?");
            partyMemberSay(main, "Yes... fine. Good exercise.");
        } else {
            println("You frantically flee from the bear and make your way back to the party, who are just getting out of the hot spring.");
            returnToParty(model, oldLeader);
            leaderSay("Why so out of breath?");
            partyMemberSay(main, "Bear... bear...");
            leaderSay("No, we're not bare anymore. Look, we're all dressed.");
            partyMemberSay(main, "No..., a bear... it attacked me...");
            leaderSay("Good heavens. Are you alright?");
            partyMemberSay(main, "Yes... I think so, just a little riled.");
            leaderSay("Maybe we should stick together out here.");
            partyMemberSay(main, "Fine. Let's just get out of here, before that bear shows up again.");
        }
        leaderSay("Let's move on then.");
    }

    private void returnToParty(Model model, GameCharacter oldLeader) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader()) {
                gc.removeSpecificClothing();
            }
        }
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
        println(oldLeader.getName() + " is now the leader again.");
        model.getParty().setLeader(oldLeader);
    }

    @Override
    public boolean haveFledCombat() {
        return false;
    }
}
