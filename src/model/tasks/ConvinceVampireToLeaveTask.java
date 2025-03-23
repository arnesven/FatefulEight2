package model.tasks;

import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CombatAdvantage;
import model.combat.conditions.VampirismCondition;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.VampireAttackBehavior;
import model.map.UrbanLocation;
import model.states.events.VampireProwlNightEvent;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.combat.MansionTheme;
import view.subviews.CollapsingTransition;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.List;

public class ConvinceVampireToLeaveTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;
    private static GameCharacter vampire;

    public ConvinceVampireToLeaveTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
        vampire = VampireProwlNightEvent.generateVampireCharacter();
    }

    @Override
    protected void doEvent(Model model) {
        portraitSay("There's a matter a would like you to investigate. There's a noble " +
                (vampire.getGender() ? "lady" : "gentleman") + " living here, " + heOrShe(vampire.getGender()) +
                " comes from a very old and respected family. It's said members of that family where the ones who " +
                "founded this town, long ago.");
        leaderSay("How nice. Is this descendant as dedicated as " + hisOrHer(vampire.getGender()) + " forebears?");
        portraitSay("Not at all. " + heOrSheCap(vampire.getGender()) + " is quite the recluse, keeps to " +
                himOrHer(vampire.getGender()) + "self. In fact, people were beginning to forget about " +
                himOrHer(vampire.getGender()) + "... until recently.");
        leaderSay("What's changed?");
        portraitSay(heOrSheCap(vampire.getGender()) + " has been spotted skulking around late at night, and there " +
                "have been reports of " + himOrHer(vampire.getGender()) + " entering people's houses.");
        leaderSay("So this noble has suddenly taken up a new hobby, cat burgling.");
        portraitSay("One could think so, but it doesn't make any sense. " + heOrSheCap(vampire.getGender()) +
                " is well known to be incredibly wealthy, with no need to steal.");
        boolean said = randomSayIfPersonality(PersonalityTrait.mischievous, List.of(), "Some people steal just for the fun of it...");
        if (!said) {
            leaderSay("Some people steal just for the fun of it...");
        }
        portraitSay("Perhaps.... Or, it could be something more sinister... Like...");
        leaderSay("Like what?");
        portraitSay("Like... maybe " + heOrShe(vampire.getGender()) + " is a vampire.");
        leaderSay("A vampire? Well, anything's possible I suppose.");
        portraitSay("In any case, I would like you to investigate. I want to know what " +
                heOrShe(vampire.getGender()) + " is up to. If it's just common larceny, we can press charges. " +
                "If we're dealing with a vampire, please get rid of it!");
        leaderSay("I understand. We'll see what we can do. Where does this noble live?");
        print("The " + location.getLordTitle() + " gives you direction to the residence of the noble. Do you want to go there now? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("We'll head over there right now.");
            portraitSay("Good luck.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            SubView lordSubView = model.getSubView();
            boolean success = goToHouse(model);
            if (model.getParty().isWipedOut()) {
                return;
            }
            model.getParty().unbenchAll();
            CollapsingTransition.transition(model, lordSubView);
            showExplicitPortrait(model, model.getLordPortrait(location), location.getLordName());
            portraitSay("Were you able to find out anything about the peculiar noble?");
            if (success) {
                leaderSay("Yes. " + heOrSheCap(vampire.getGender()) + " was a vampire, but " +
                        iOrWe() + "'ve dealt with " + himOrHer(vampire.getGender()) + ". " +
                        heOrSheCap(vampire.getGender()) + " won't be troubling this town anymore.");
                portraitSay("Oh, I can't tell you how relieved I am. Let me fetch your reward.");
                println("The " + location.getLordTitle() + " brings out a purse and hands you 40 gold.");
                model.getParty().addToGold(40);
                if (model.getParty().getLeader().hasPersonality(PersonalityTrait.greedy)) {
                    leaderSay("What? Not more for handling that dangerous monster for you?");
                    portraitSay("Hmm... I suppose the reward is a little too small. Here.");
                    println("The " + location.getLordTitle() + " hands you an additional 15 gold.");
                    model.getParty().addToGold(15);
                    portraitSay("I really can't give you more. My budget is quite tight.");
                    leaderSay("It will have to suffice I guess.");
                } else {
                    leaderSay("Thank you!");
                    randomSayIfPersonality(PersonalityTrait.greedy, List.of(), "Hmph... That does not seem adequate...");
                }
            } else {
                leaderSay("The investigation is still ongoing.");
                portraitSay("I understand.");
            }
        } else {
            leaderSay("I think we'll deal with this later.");
            portraitSay("Okay, but please don't forget about this matter, it is very important to me.");
        }

    }

    private boolean goToHouse(Model model) {
        setCurrentTerrainSubview(model);
        println("You arrive at the house of the noble.");
        randomSayIfPersonality(PersonalityTrait.intellectual, List.of(), "This is some old architecture, this building must " +
                "have been standing here for a while. It may be older than the town itself!");
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Knock on the door", "Attempt to break in", "Hide outside", "Walk away"));
        if (choice == 3) {
            println("You walk away from the house and return to the " + location.getLordTitle() + ".");
            return false;
        }
        if (choice == 2) {
            println("You hide outside the house and hope the noble will come out. However, after several hours, " +
                    heOrShe(vampire.getGender()) + " still haven't emerged from the house.");
            leaderSay("This was rather fruitless.");
            randomSayIfPersonality(PersonalityTrait.gluttonous, List.of(), "I'm getting hungry.");
            leaderSay("Let's get out of here.");
            println("You walk away from the house and return to the " + location.getLordTitle() + ".");
            // TODO: Give option to wait through the night, then catch the vampire while he/she is on the prowl.
            return false;
        }
        if (choice == 1) {
            println("You sneak around the back and find a back door to the house. However, the door is locked.");
            boolean success = model.getParty().doSoloLockpickCheck(model, this, 6);
            if (!success) {
                leaderSay("Hmm... we won't be able to force our way in.");
                print("Do you want to knock on the front door? (Y/N) ");
                if (!yesNoInput()) {
                    leaderSay("Let's just come back another time.");
                    println("You walk away from the house and return to the " + location.getLordTitle() + ".");
                    return false;
                }
            } else {
                return sneakInside(model);
            }
        }
        println("You knock on the front door of the house.");
        println("Nobody comes to the door.");
        leaderSay("Maybe nobody is home?");
        println("You wait a little longer.");
        println("You're just about to turn away when the door opens slightly. A pale figure appears in the crack of the door.");
        return talkToVampire(model, false);
    }

    private boolean talkToVampire(Model model, boolean breakIn) {
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, vampire.getAppearance(), "Noble");
        if (breakIn) {
            println("The pale person seems annoyed.");
            portraitSay("What are you doing in my house!?");
        } else {
            println("The pale person is very calm.");
            portraitSay("Who are you?");
        }
        print("Do you lie about your identity and the purpose of your visit? (Y/N) ");
        if (yesNoInput()) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, model.getParty().getLeader(),
                    Skill.Persuade, 8, 10, 0);
            if (result.isSuccessful()) {
                leaderSay(imOrWere() + " from a society of historians. We want to document the heraldry of this town.");
            } else {
                leaderSay(imOrWere() + " from the fire brigade, here to check the fire safety of your house.");
            }
            portraitSay("That's ridiculous. You just look like a bunch of brigands.");
            leaderSay("Fine...");
        }
        leaderSay("We're here on behalf of " + location.getLordTitle() + " " + location.getLordName() + ". " +
                heOrSheCap(location.getLordGender()) + " has contracted us to investigate your recent strange behavior.");
        portraitSay("Ahh... yes, young " + location.getLordName() +
                "... I remember when " + heOrShe(location.getLordGender()) + " was just a little...");
        leaderSay("Let's nog get sidetracked here you old " + (vampire.getGender() ? "crone" : "codger") + "!");
        portraitSay("Alright. Yes... I've heard the rumours. " + heOrSheCap(location.getLordGender()) +
                " thinks I'm a vampire.");
        println("The old noble seems amused.");
        leaderSay("Well... are you?");
        portraitSay("Why yes, no sense in denying it anymore. I'm getting too old for the whole cloak and dagger charade.");
        print("Do you attack the vampire (Y) or do you attempt to talk " + himOrHer(vampire.getGender()) + " into leaving town (N)? ");
        if (yesNoInput()) {
            leaderSay("Then it's time to say goodbye vampire!");
            portraitSay("Oh bother...");
            model.getLog().waitForAnimationToFinish();
            return combatVampireInside(model, false);
        }

        leaderSay("Wow, I didn't expect you to admit it so openly. Would you consider leaving this town and never coming back?");
        MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 12);
        if (pair.first) {
            partyMemberSay(pair.second, "You know, now that people suspect there is a vampire in this town, " +
                    "they'll be much more careful, it will be so much harder for you to find good victims.");
            leaderSay("And you'll be shunned, and the townspeople may come after you with garlic and stakes.");
        } else {
            partyMemberSay(pair.second, iOrWe() + "... uh... really want you to leave.");
        }

        if (partyHasVampire(model)) {
            if (makePartyVampireTheLeader(model)) {
                return true;
            }
        } else {
            // No vampires in party
            if (pair.first) {
                portraitSay("Those are some fair points. Alright, why not. I'll pack my things and move on.");
                vampireConvinced(model);
                return true;
            }

            portraitSay("That's not very convincing.");
            if (askToMakeVampire(model)) {
                return true;
            }
        }
        portraitSay("I've lived here for a hundred and fifty years, and I don't think I'll be leaving anytime this century.");
        leaderSay("That's disappointing.");
        print("Do you attack the vampire? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("In that case, we'll just have to convince another way!");
            portraitSay("You can try...");
            model.getLog().waitForAnimationToFinish();
            return combatVampireInside(model, false);
        }
        portraitSay("Now, will you get out of my house?");
        leaderSay("Fine... But this isn't the last you'll see of us.");
        println("You leave the vampire's house.");
        return false;
    }

    private boolean makePartyVampireTheLeader(Model model) {
        if (model.getParty().getLeader().hasCondition(VampirismCondition.class)) {
            println("The vampire looks intently at " + model.getParty().getLeader() + ".");
            portraitSay("Hmmm... okay, I'll pack my things");
            vampireConvinced(model);
            return true;
        }
        GameCharacter gc = MyLists.find(model.getParty().getPartyMembers(), ch -> ch.hasCondition(VampirismCondition.class));
        println("The vampire looks intently at " + gc.getFullName() + ".");
        portraitSay("I'll go, on one condition. Make, that " + gc.getRace().getName().toLowerCase() + " the leader of your party.");
        leaderSay("Uh... " + gc.getFirstName() + "? Why do you want " + himOrHer(gc.getGender()) + " to be our leader?");
        portraitSay("That's my business. Do you accept?");
        print("Do you accept making " + gc.getFullName() + " the party leader? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Okay. " + gc.getFirstName() + ", you take the helm.");
            model.getParty().setLeader(gc);
            println(gc.getName() + " is now the party leader.");
            leaderSay("Okay vampire... Are you happy now?");
            portraitSay("Yes. I'm satisfied. I'll pack my things and leave.");
            vampireConvinced(model);
            return true;
        }
        leaderSay("Nope. Only party members have a say in who's our leader.");
        portraitSay("Then I'm staying here.");
        return false;
    }

    private boolean askToMakeVampire(Model model) {
        if (model.getParty().size() > 1) {
            portraitSay("There's quite a bunch of you. Maybe if I could talk to you alone?");
            print("Have " + model.getParty().getLeader().getFirstName() + " talk to the vampire alone? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Okay. Clear out guys. I'll talk to this vampire alone.");
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, "I'm not sure it's safe to leave you alone with " + himOrHer(vampire.getGender()) + ".");
                leaderSay("Don't worry. Just wait right outside. If " + heOrShe(vampire.getGender()) + " tries anything, I'll scream like hell.");
                partyMemberSay(other, "Alright.");
                List<GameCharacter> allButLeader = MyLists.filter(model.getParty().getPartyMembers(),
                        ch -> ch != model.getParty().getLeader());
                model.getParty().benchPartyMembers(allButLeader);
                println("The rest of the party leaves the house.");
                leaderSay("Okay, now we're alone. Can we talk now?");
            } else {
                leaderSay("No way, we're not falling for that one!");
                portraitSay("Then I'm staying here.");
            }
        }
        portraitSay("I'll make you a deal. Let me feed on you. It will turn you into a vampire. " +
                "Then I'll leave town, and you can tell " + location.getLordTitle() + " " + location.getLordName() +
                " that you got rid of the vampire.");
        leaderSay("But, in reality, there will still be a vampire in town. Me.");
        portraitSay("Yes. Hehehe. What do you say?");
        print("Accept to become a vampire? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Okay. I accept.");
            portraitSay("Excellent...");
            println("The vampire comes close to " + model.getParty().getLeader().getFirstName() + ".");
            leaderSay("Wait!");
            portraitSay("What is it?");
            leaderSay("Will this hurt?");
            portraitSay("Maybe just a little, at first, but then, It's quite extraordinary.");
            leaderSay("Alright, drink your fill.");
            portraitSay("Ahh... the pleasure.");
            model.getLog().waitForAnimationToFinish();
            if (model.getSubView() instanceof PortraitSubView) {
                ((PortraitSubView) model.getSubView()).forceVampireFeedingLook();
            }
            model.getParty().forceEyesClosed(model.getParty().getLeader(), true);
            VampireProwlNightEvent.makeCharacterIntoVampire(model, this, model.getParty().getLeader(), false);
            println("A short while later, " + model.getParty().getLeader().getFirstName() + " is roused by the pale noble.");
            model.getLog().waitForAnimationToFinish();
            model.getParty().forceEyesClosed(model.getParty().getLeader(), false);
            if (model.getSubView() instanceof PortraitSubView) {
                ((PortraitSubView) model.getSubView()).removeVampireFeedingLook();
            }
            portraitSay("Wake up... and face a new world, as a vampire.");
            leaderSay("Ugh... what a hangover.");
            portraitSay("You're week now, but once you've rested, and had some refreshments.");
            leaderSay("Alright. Any other tips on being a vampire?");
            portraitSay("Hmm... Yes. Perhaps be more careful than me. If you really want an easy meal, seek out a farm. " +
                    "People are less careful and suspicious on the countryside.");
            leaderSay("I'll keep it in mind. Now, you'll keep your end of the bargain? You'll leave town?");
            portraitSay("Yes.");
            vampireConvinced(model);
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            if (model.getParty().size() > 1) {
                model.getParty().unbenchAll();
                println("Outside, the rest of the party is waiting for " + himOrHer(model.getParty().getLeader().getGender()) + ".");
                leaderSay("It's done. That vampire won't bother these townspeople anymore.");
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, "What did you say to " + himOrHer(vampire.getGender()) +
                        " to make " + himOrHer(vampire.getGender()) + " change " + hisOrHer(vampire.getGender()) + " mind?");
                leaderSay("That's between me and the vampire. Now let's get back to the " + location.getLordTitle() + ".");
            }
            return true;
        }
        leaderSay("Thanks for the offer, but no. I don't want to become a vampire.");
        portraitSay("Then I don't feel like moving.");
        return false;
    }

    private void vampireConvinced(Model model) {
        leaderSay("Really? You'll go?");
        portraitSay("Yes. You've convinced me. I was getting a little bit bored. Not so strange actually, " +
                "I've lived here for a hundred and fifty years you know.");
        leaderSay("Wow, that's a long time. Okay, " + iOrWe() + " hold you to your word. If you don't go, " +
                "we'll be back with those stakes.");
        portraitSay("You can trust me.");
        leaderSay("Farewell then.");
        println("You leave the house.");
    }

    private boolean partyHasVampire(Model model) {
        return !MyLists.filter(model.getParty().getPartyMembers(),
                gc -> gc.hasCondition(VampirismCondition.class)).isEmpty();
    }

    private boolean sneakInside(Model model) {
        println("The door unlocks and you sneak inside the house.");
        List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Sneak, 6);
        if (failers.isEmpty()) {
            println("Moving quite stealthily throughout the house, you come upon a person who is occupied by " +
                    MyRandom.sample(List.of("reading a book", "writing a letter", "cooking a meal",
                            "getting dressed", "getting undressed", "playing a violin")) + ".");
            print("Do you attack the person from behind (Y) or do you make your presence known to them (N)?");
            if (yesNoInput()) {
                println("You quickly draw your weapons and charge the unsuspecting person!");
                return combatVampireInside(model, true);
            }
            leaderSay("Excuse " + meOrUs() + "... are you the resident of this house?");
            println("The person turns around. You can see it is an old pale person.");
            return talkToVampire(model, true);
        }
        println(MyRandom.sample(failers).getFirstName() + " clumsily stumbles and brakes and " +
                "old vase, which makes a terribly loud noise. In an instance a pale " +
                "figure appears before the party.");
        return talkToVampire(model, true);
    }

    private boolean combatVampireInside(Model model, boolean isAmbush) {
        Enemy enm = new VampireFromCharacterEnemy(vampire);
        enm.setAttackBehavior(new VampireAttackBehavior());
        runCombat(List.of(enm), new MansionTheme(), true, isAmbush ? CombatAdvantage.Party : CombatAdvantage.Neither, List.of());
        setCurrentTerrainSubview(model);
        if (haveFledCombat()) {
            println("Valuing your lives, you flee from the house. When you have calmed down a bit, " +
                    "and made sure nobody has followed you, you return to the " + location.getLordTitle() + ".");
            return false;
        }
        if (enm.isDead()) {
            println("The vampire lies slain on the floor.");
            leaderSay("One less vampire for the world to worry about. Let's report back to " +
                    location.getLordTitle() + " " + location.getLordName() + ".");
            return true;
        }
        leaderSay("Darn it " + heOrShe(vampire.getGender()) +
                " got away! We'll have to come back and finish the job later.");
        boolean said = randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()), "Do we have to? I think " +
                heOrShe(vampire.getGender()) + " was pretty darn scary.");
        if (said) {
            leaderSay("We made a commitment. We have to follow through, our reputation is at stake you know.");
        }
        return false;
    }

    @Override
    public String getJournalDescription() {
        return "Investigate a noble who " + location.getLordTitle() + " " +
                location.getLordName() + " believes might be a vampire.";
    }

    private static class VampireFromCharacterEnemy extends FormerPartyMemberEnemy {
        public VampireFromCharacterEnemy(GameCharacter vampire) {
            super(vampire);
            setCurrentHp(getMaxHP());
        }
    }
}
