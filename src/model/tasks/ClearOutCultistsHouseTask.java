package model.tasks;

import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CultistEnemy;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyPair;
import view.combat.MansionTheme;
import view.subviews.CollapsingTransition;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.List;

public class ClearOutCultistsHouseTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public ClearOutCultistsHouseTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        portraitSay("People in this town are nervous, afraid really!");
        leaderSay("What's the trouble?");
        portraitSay("Well, there's a house here. It's kind of run down, and always been kind of spooky. " +
                "Nobody lived there for years. Then, recently, some people moved in and...");
        leaderSay("Trouble with the neighbors? Really?");
        portraitSay("No, it's not like that. I think they're cultists!");
        leaderSay("Cultists? What makes you think that?");
        portraitSay("First of all, they keep to themselves. And they wear dark cloaks, always with their hoods up.");
        leaderSay("Well, that's no proof that they are cultists. They're probably just a bit odd, and like their privacy.");
        portraitSay("Yes, yes... That's what I thought first as well. But then one night when I passed by the house, " +
                "I heard this strange chanting.");
        leaderSay("So, they enjoy music. How nice!");
        portraitSay("But the next day I went by the house again and peeked inside...");
        leaderSay("You know, I don't think they are...");
        portraitSay("And I saw lots of candles, and a strange statues in the middle of the room...");
        leaderSay("Perhaps they're just...");
        portraitSay("And an altar, with the carcass of a goat that looked like it had been ritually sacrificed. There was lots of blood!");
        leaderSay("Okay, you've convinced me. They're cultists.");
        portraitSay("You seem the capable sort. Could you perhaps, you know, get rid of them? I'll reward you!");
        print("Do you wish to go and evict the cultists now? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Not right now. We have some other business to attend to.");
            portraitSay("Okay, but please hurry back. I saw them leading another goat in their yesterday. I'm freaking out a little bit!");
            return;
        }
        leaderSay("What's the address?");
        portraitSay("It's right on the outskirts of town. It's the one that looks like it's haunted. You can't miss it.");
        leaderSay("Okay, we'll deal with them.");
        SubView oldSubview = model.getSubView();
        model.getLog().waitForAnimationToFinish();
        ClearOutCultistsDailyEvent clearOutEvent = new ClearOutCultistsDailyEvent(model);
        clearOutEvent.doEvent(model);

        if (!clearOutEvent.cultistRemoved()) {
            println("You return to the " + location.getLordTitle() + ".");
            leaderSay("I guess we'll have to try something else to get the cultists out.");
            CollapsingTransition.transition(model, oldSubview);
            portraitSay("Well, how did it go? Are the cultists gone?");
            leaderSay("Not yet. But we aren't giving up yet.");
            portraitSay("That's great.");
            model.getLog().waitForAnimationToFinish();
        } else {
            summon.increaseStep();
            println("With the cultists disposed of, the house is now empty. You promptly return to the " + location.getLordTitle() + ".");
            CollapsingTransition.transition(model, oldSubview);
            leaderSay("The cultists are gone.");
            portraitSay("Really? I mean... great! I didn't really expect you to succeed, but never mind that now.");
            leaderSay("Your welcome. Now, you mentioned a reward.");
            portraitSay("Uhm, yes, I did... A reward...");
            if (model.getParty().getHeadquarters() == null) {
                leaderSay("...");
                portraitSay("Hey, I just had a splendid idea. Now that the house is empty, why don't you move in?");
                leaderSay("You'd give the house to " + meOrUs() + "?");
                portraitSay("Yeah, why not. That way you would make sure the cultists wouldn't come back, and besides, " +
                        "we'd love having stand up citizens like you in our town. How about it?");
                print("The " + location.getLordTitle() + " is offering you the house. " +
                        location.getRealEstate().presentYourself() +
                        ". It will become your headquarters in this town. Do you accept the offer? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().setHeadquarters(model, this, location.getRealEstate());
                } else {
                    leaderSay("Sorry, not interested.");
                    portraitSay("How unfortunate. Well...");
                    goldReward(model);
                }
            } else {
                goldReward(model);
            }
        }
    }

    private void goldReward(Model model) {
        portraitSay("Will you accept a monetary reward of 50 gold?");
        leaderSay("Yes, of course.");
        portraitSay("Good. Here you go. Well then our business have thus concluded.");
        println("You got 50 gold from the " + location.getLordTitle() + ".");
        model.getParty().addToGold(50);
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs help clearing out a house of cultists in town.";
    }

    private class ClearOutCultistsDailyEvent extends DailyEventState {
        private boolean cultistRemoved;

        public ClearOutCultistsDailyEvent(Model model) {
            super(model);
            this.cultistRemoved = false;
        }

        public boolean cultistRemoved() {
            return cultistRemoved;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            println("You head to the house on the outskirts of town. As the " + location.getLordTitle() + " said, it looks haunted. " +
                    "You're surprised you haven't noticed it before. You step up to the door and knock hard, to assure anyone inside that you mean business.");
            model.getLog().waitForAnimationToFinish();
            CharacterAppearance cultist = PortraitSubView.makeRandomPortrait(Classes.CULTIST);
            showExplicitPortrait(model, cultist, "Cultist");
            println("After a short while, a " + manOrWoman(cultist.getGender()) + " opens the door. " + heOrSheCap(cultist.getGender()) + " speaks in a hoarse voice.");
            portraitSay("Can I help you?");
            leaderSay("In the name of the " + location.getLordTitle() + " of " + location.getPlaceName() +
                    ". You have been commanded to evict these premises at once.");
            portraitSay("Refuse.");
            println("How would you like to deal with the cultists?");
            int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Attack", "Bribe", "Coerce", "Persuade", "Not at all"));
            if (choice == 0) {
                cultistRemoved = attackCultists(model);
            } else if (choice == 1) {
                cultistRemoved = bribeCultists(model);
            } else if (choice == 2) {
                cultistRemoved = coerceCultists(model);
            } else if (choice == 3) {
                cultistRemoved = persuadeCultists(model);
            }
        }

        private boolean persuadeCultists(Model model) {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 12);
            if (pair.first) {
                partyMemberSay(pair.second, "Look. I know what you're doing here, and I'm all for it! " +
                        "Even I like to get into some rhythmic chanting, ritual sacrifice and preying to otherworldly deities every once in a while." +
                        "But this town isn't a good place for it. It doesn't have the right feel about it. Why don't you find another house " +
                        "somewhere, where you can practice your worship in peace?");
                portraitSay("That... that actually makes a lot of sense.");
                partyMemberSay(pair.second, "I'm glad we found common ground.");
                portraitSay("Okay. We'll move out.");
                partyMemberSay(pair.second, "Splendid. Everybody wins.");
                return true;
            }
            partyMemberSay(pair.second, "Uhm, please get out?");
            portraitSay("Uhm, no?");
            return false;
        }

        private boolean coerceCultists(Model model) {
            println("You push by the cultist by the door and enter the house. In a booming voice you start shouting.");
            leaderSay("I'm going to give you to the count of ten to get your behinds out of the house. You hear me?");
            int avgLevel = (int)calculateAverageLevel(model);
            boolean result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Persuade, Math.max(2, 7 - avgLevel));
            leaderSay("TEN, NINE, EIGHT...");
            if (result) {
                portraitSay("Okay okay, stop shouting. We'll be on our way then.");
                return true;
            }
            println("Before " + model.getParty().getLeader().getFirstName() + " can get to 'seven', the cultists storm out you!");
            return attackCultists(model);
        }

        private boolean bribeCultists(Model model) {
            int bribe = Math.min(125, model.getParty().getGold() / 2);
            leaderSay("How about you find another, nicer house somewhere else? Want a contribution to get you started? How about " + bribe + " gold?");
            if (bribe < 100) {
                portraitSay("What a pitiful bag coins. Take it with you and leave.");
                print("Do you leave the house (Y), or do you push the door open and root the cultists out by force (N)? ");
                if (yesNoInput()) {
                    println("You back off, turn around, and leave the house.");
                    return false;
                }
                return attackCultists(model);
            }
            portraitSay("Hmm... alright, we'll take the money and leave. You have our word");
            leaderSay("Good.");
            println("You handed " + bribe + " gold over to the cultist.");
            model.getParty().addToGold(-bribe);
            return true;
        }

        private boolean attackCultists(Model model) {
            runCombat(List.of(
                    new CultistEnemy('A'), new CultistEnemy('A'), new CultistEnemy('A'),
                    new CultistEnemy('A'), new CultistEnemy('A'), new CultistEnemy('A'),
                    new CultistEnemy('A'), new CultistEnemy('A')), new MansionTheme(), true);
            return haveFledCombat();
        }
    }
}
