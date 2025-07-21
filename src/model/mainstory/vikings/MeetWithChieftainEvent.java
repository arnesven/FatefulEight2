package model.mainstory.vikings;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.DispelSpell;
import model.mainstory.GainSupportOfVikingsTask;
import model.map.CastleLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.SpellCastException;
import util.MyPair;
import view.subviews.CollapsingTransition;
import view.subviews.GuildHallImageSubView;

import java.util.List;

import static model.mainstory.GainSupportOfVikingsTask.CHIEFTAIN_NAME;
import static model.mainstory.GainSupportOfVikingsTask.CHIEFTAIN;

public class MeetWithChieftainEvent extends DailyEventState {
    private final boolean withIntro;
    private final GainSupportOfVikingsTask task;

    public MeetWithChieftainEvent(Model model, boolean withIntro, GainSupportOfVikingsTask task) {
        super(model);
        this.withIntro = withIntro;
        this.task = task;
    }

    private void showChieftainPortrait(Model model) {
        showExplicitPortrait(model, task.getChieftainPortrait(), CHIEFTAIN);
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("The guards posted outside the entrance to the longhouse smirk at you as you approach, but " +
                    "they do not stop you as you enter the longhouse.");
        }
        model.getLog().waitForAnimationToFinish();
        CollapsingTransition.transition(model, GuildHallImageSubView.getInstance("Longhouse"));

        if (task.isCompleted()) {
            lokiReminisces(model);
            return;
        }
        meetLoki(model);
        portraitSay("There are tests which must be passed. You must overcome several challenges!");
        println("You can hear the vikings around you sniggering.");
        randomSayIfPersonality(PersonalityTrait.irritable, List.of(), "What's so funny?");
        leaderSay("Go on " + CHIEFTAIN_NAME + ". What are these challenges?");

        portraitSay("Hmmm... let me think. Let me recall. What were the ancient rites...");
        randomSayIfPersonality(PersonalityTrait.critical, List.of(),
                "Something about this doesn't seem quite right.");
        leaderSay("...");
        if (!task.isEatingContestDone()) {
            if (new EatingContest(model, this).partyPassesTest()) {
                task.setEatingContestDone();
            } else {
                println("You leave the longhouse.");
                return;
            }
        }

        if (!task.isSprintingContestDone()) {
            if (new SprintingContest(model, this).partyPassesTest()) {
                task.setSprintingContestDone();
            } else {
                println("You leave the longhouse.");
                return;
            }
        }

        if (!task.isWrestlingContestDone()) {
            if (new WrestlingContest(model, this).partyPassesTest()) {
                task.setWrestlingContestDone();
                lokiExplainsRealTest(model);
            } else {
                println("You leave the longhouse.");
            }
        }
    }

    private void lokiReminisces(Model model) {
        showChieftainPortrait(model);
        portraitSay("We meet again friend.");
        if (task.isMonastaryRaided()) {
            portraitSay("That was a legendary raid. We should do one again sometime.");
        } else {
            portraitSay("It's too bad you didn't come with us for the raid on the Sixth Order monastary. " +
                    "We could have needed your help on that one.");
            leaderSay("I'm sorry. " + iOrWeCap() + " had other matters to attend to.");
            portraitSay("Some other time then?");
        }
        leaderSay("Perhaps. See you around Loki.");
        portraitSay("Farewell friend.");
    }

    private void meetLoki(Model model) {
        println("You step into a grand hall. Two long tables extend on either side of a roaring fire, above which " +
                "some animal is roasting on a spit.");
        if (!task.isLokiMet()) {
            randomSayIfPersonality(PersonalityTrait.gluttonous, List.of(), "My mouth is watering. Do you think they'd mind if...");
            println("Many viking men and women are sitting at the tables. They are watching you silently as you approach the " +
                    "man sitting at the end of the hall. The person you assume to be the chieftain of the Vikings.");
            model.getLog().waitForAnimationToFinish();
            showChieftainPortrait(model);
            portraitSay("So you're the outsider who's been causing all the commotion in our village. " +
                    "I'm " + CHIEFTAIN_NAME + ", the chieftain of our tribe.");
            leaderSay("Yes. But we are friends, not foes.");
            portraitSay("Indeed? Outsiders rarely come here. Those that do are often not friendly. Those that " +
                    "are seldom prepared for this harsh environment.");
            println("With the last remark, " + CHIEFTAIN_NAME + " smiles cruelly. And laughs are heard from the other vikings in the hall.");
            portraitSay("Now please explain why you have come.");
            leaderSay("As you have surely noticed, the regions south of here have been more tumultuous lately.");
            CastleLocation bogdown = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
            String kingdom = CastleLocation.placeNameToKingdom(bogdown.getPlaceName());
            portraitSay("We have heard some such rumors. Orcish activity is it? It does not bother us so much. " +
                    "The forces of the " + kingdom +
                    " have been spotted more north than usual though. This is not to my liking. Are you an envoy of " +
                    bogdown.getLordTitle() + " " + bogdown.getLordName() + "?");
            leaderSay("No, or yes... It's complicated.");
            portraitSay("I don't understand. Are you or are you not?");
            leaderSay(iOrWeCap() + " were investigating a matter for the " + bogdown.getLordTitle() + ". The quest " +
                    "finally brought " + meOrUs() + " to the ancient stronghold to the east of these lands. But when " + iOrWe() + " returned " +
                    iOrWe() + " were wrongfully imprisoned. " + iOrWeCap() + " narrowly escaped " + kingdom +
                    " alive. It seems the " + bogdown.getLordTitle() + " has been possessed or controlled by an evil force known as the Quad. " +
                    "Because of this, the kingdom has been descended into disorder.");
            portraitSay("I understand. But why seek refuge here? Some call us madmen for settling in this land.");
            println("The vikings in the hall laugh heartily at " + CHIEFTAIN_NAME + "'s joke.");
            leaderSay("We do not seek refuge, but allies. " + iOrWeCap() + " aim to rally the support of the kingdoms surrounding " +
                    kingdom + ". We intend to return to " + kingdom + " to overthrow " +
                    bogdown.getLordName() + " and root out the evil presence.");
            leaderSay("If we could rely on your support as well. Nothing could oppose us.");
            portraitSay("And what would be in it for us?");
            leaderSay("When we sack " + bogdown.getPlaceName() + " you can take all the spoils you want. Surely you must have fantasized " +
                    "about such an opportunity?");
            portraitSay("Perhaps I have...");
            leaderSay("So. What will it be " + CHIEFTAIN +
                    "? Will you partake in history? Or will you sit here, by your frozen hearth, " +
                    "and wait for a better chance?");
            portraitSay("You think we are craven outcasts, shuddering way out here in the cold, with no prospects? We are explorers, " +
                    "traders and feared warriors. Our voyages take us far and wide, and we carry out many raids, bringing back riches. " +
                    "For instance, soon we will carry out a raid on the monastary on the Isle of Faith.");
            leaderSay("How great of you.");
            portraitSay("Still, this crusade of yours, it has some appeal. But there is a problem.");
            leaderSay("What's the problem?");
            portraitSay("Only a true viking could compel our tribe to undertake such a venture. And you, are no viking.");
            leaderSay("That is true. But how does one become a true viking?");
            task.setLokiMet();
        } else {
            println("Many viking men and women are sitting at the tables. " +
                    "You approach " + CHIEFTAIN
                    + ", who is sitting in his chair at the end of the hall.");
            model.getLog().waitForAnimationToFinish();
            showChieftainPortrait(model);
        }
    }

    private void lokiExplainsRealTest(Model model) {
        portraitSay("I like your spirit! The final test is the most straight forward. You must join us on " +
                "our upcoming raid.");
        leaderSay("A raid? Like, with pillaging?");
        portraitSay("Yes. We will sail south to the Isle of Faith and raid the monastary of the Sixth Order Monks. " +
                "Those pious bastards have gotten fat from donations from gullible pilgrims.");
        leaderSay("And, if " + iOrWe() + " do this, " + iOrWe() +
                " will be considered true viking" + (model.getParty().size() > 1 ? "s":"") + "?");
        portraitSay("Yes of course! Then we shall be brothers for all time, and we will stand by you in your " +
                "return to depose the King of Bogdown.");
        leaderSay("I understand. When do we leave?");
        portraitSay("At first light tomorrow. Join us down by the docks whenever you are ready.");
    }

    // Wrappers
    public void portraitSay(String line) {
        super.portraitSay(line);
    }

    public void showExplicitPortrait(Model model, CharacterAppearance app, String name) {
        super.showExplicitPortrait(model, app, name);
    }

    @Override
    public void removePortraitSubView(Model model) {
        super.removePortraitSubView(model);
    }

    private static abstract class LokiIllusionContest {

        private static final int ACTUAL_DIFFICULTY = 13;
        private static final int MAGIC_DIFFICULTY = 10;

        private final Model model;
        private final MeetWithChieftainEvent event;
        private final int illusionDifficulty;
        private Skill contestSkill;

        private LokiIllusionContest(Model model, MeetWithChieftainEvent event, Skill contestSkill, int illusionDifficulty) {
            this.model = model;
            this.event = event;
            this.contestSkill = contestSkill;
            this.illusionDifficulty = illusionDifficulty;
        }

        public boolean partyPassesTest() {
            lokiAnnouncesTest(event);
            GameCharacter partyMember = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                event.portraitSay("Who among you do you choose as champion?");
                partyMember = model.getParty().partyMemberInput(model, event, model.getParty().getPartyMember(0));
            }
            contenderAcceptsAndLokiSelectsOpponent(model, event, partyMember);

            model.getSpellHandler().acceptSpell(new DispelSpell().getName());
            boolean passedTest = false;
            try {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(
                        model, event, Skill.MagicBlue, MAGIC_DIFFICULTY);
                if (result.first) {
                    event.partyMemberSay(result.second, getBlueMagicHint());
                }
                prepareForContest(model, event, partyMember);
                SkillCheckResult contestResult = model.getParty().doSkillCheckWithReRoll(model, event, partyMember,
                        contestSkill, illusionDifficulty, 0, getSkillBonus(partyMember));
                contestInProgress(event, partyMember);
                model.getLog().waitForAnimationToFinish();
                if (contestResult.isSuccessful()) {
                    illusionDifficultyPassed(model, event, partyMember);
                } else {
                    illusionDifficultyFailed(model, event, partyMember);
                    if (contestResult.getModifiedRoll() >= ACTUAL_DIFFICULTY) {
                        passedTest = passedTest(actualDifficultyPassed(model, event, partyMember));
                    } else {
                        failedTest(actualDifficultyFailed(model, event, partyMember));
                    }
                }
            } catch (SpellCastException sce) {
                if (sce.getSpell().getName().equals(new DispelSpell().getName())) {
                    if (sce.getSpell().castYourself(model, event, sce.getCaster())) {
                        event.removePortraitSubView(model);
                        illusionDispelled(model, event, partyMember);
                        passedTest = true;
                    }
                }
            }
            model.getSpellHandler().unacceptSpell(new DispelSpell().getName());
            return passedTest;
        }

        protected abstract void lokiAnnouncesTest(MeetWithChieftainEvent event);

        protected abstract void contenderAcceptsAndLokiSelectsOpponent(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract String getBlueMagicHint();

        protected abstract void prepareForContest(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract int getSkillBonus(GameCharacter partyMember);

        protected abstract void contestInProgress(MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract void illusionDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract void illusionDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract String actualDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract String actualDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        protected abstract void illusionDispelled(Model model, MeetWithChieftainEvent event, GameCharacter partyMember);

        private boolean passedTest(String opponentName) {
            event.println("" + CHIEFTAIN_NAME + " actually seems somewhat impressed");
            event.portraitSay("Alas, you could not beat " + opponentName + ". Still, your effort is commendable.");
            event.leaderSay("Does this mean I'm not a true viking?");
            event.portraitSay("Perhaps not. But at least the guests of my hall are entertained, perhaps you would like to take another test anyway?");
            event.print("Do you indulge the chieftain? (Y/N) ");
            if (!event.yesNoInput()) {
                event.leaderSay("We don't have time for idle games. Let's leave now.");
                event.portraitSay("What a shame. Oh well. Until we meet again.");
                return false;
            }
            event.leaderSay("Why not? I've got nowhere else to be.");
            return true;
        }

        private void failedTest(String extra) {
            event.println(CHIEFTAIN + " doesn't seem very impressed.");
            event.portraitSay("I expected more! I don't think you can call yourself a true viking.");
            event.leaderSay("So that's it? I failed?");
            event.portraitSay("That's it for tonight at least. Why don't you come back another night and try your " +
                    "luck then. " + extra);
            event.println("Roaring laughter erupts from the vikings in the longhouse as you are escorted outside.");
        }
    }

    private static class EatingContest extends LokiIllusionContest {
        private static final String lagiName = "L" + (char) (0x85) + "gi";

        private EatingContest(Model model, MeetWithChieftainEvent event) {
            super(model, event, Skill.Endurance, 30);
        }

        @Override
        protected void lokiAnnouncesTest(MeetWithChieftainEvent event) {
            event.portraitSay("Ah... now I remember. A viking fights with extreme vigor! To fuel this, he or she " +
                    "must eat properly. You must compete with one of us in an eating contest!");
        }

        @Override
        protected void contenderAcceptsAndLokiSelectsOpponent(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.partyMemberSay(partyMember, "I'm ravenous. I can perform this task.");
            event.portraitSay("Good. I will choose one from my tribe. Don't worry, I won't pick the best, that would be unfair. " +
                    "Now who should I choose. Hmmm...");
            event.println("" + CHIEFTAIN_NAME + " gazes around upon the vikings sitting in the hall.");

            event.portraitSay("Ah! Yes, young " + lagiName + ", he will do well.");
            model.getLog().waitForReturn();
            System.out.println("Letter: " + (int) 'å');
            AdvancedAppearance lagiAppearance = new LagiAppearance();
            event.showExplicitPortrait(model, lagiAppearance, lagiName);
            event.println("A young man emerges from the back of the crowd. You're surprised you didn't notice him earlier, he " +
                    "has a certain radiance about him. He is however rather skinny.");
            event.partyMemberSay(partyMember, "He doesn't look like a very big eater. This should be easy.");
            event.showChieftainPortrait(model);
            event.portraitSay("Yes, most of us can eat faster than " + lagiName +
                    ". If you are a true viking, it should be no trouble for you.");
            model.getLog().waitForAnimationToFinish();
            event.println("Once again, laughter erupts in the Longhouse.");
            event.showExplicitPortrait(model, lagiAppearance, lagiName);
        }

        @Override
        protected String getBlueMagicHint() {
            return "This is not what it seems. That " + CHIEFTAIN_NAME + " guy is using some kind of spell here, " +
                    "but I can't quite figure out what.";
        }

        @Override
        protected void prepareForContest(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("Two huge troughs of roasted meat are brought out and put in front of " + lagiName +
                    " and " + partyMember.getFirstName() + ".");
            event.println(partyMember.getName() + " is preparing to compete in an eating contest with " +
                    lagiName + ", press enter to continue.");
            event.waitForReturn(true);
            event.println(lagiName + " starts eating with inhuman rapidity...");
        }

        @Override
        protected int getSkillBonus(GameCharacter partyMember) {
            int result = 0;
            if (partyMember.getRace().id() == Race.HALF_ORC.id() ||
                    partyMember.getRace().id() == Race.DWARF.id()) {
                result += 1;
            } else if (partyMember.getRace().id() == Race.HALFLING.id()) {
                result -= 1;
            }
            if (partyMember.hasPersonality(PersonalityTrait.gluttonous)) {
                result += 2;
            }
            return result;

        }

        @Override
        protected void contestInProgress(MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("And " + partyMember.getName() + " tries to keep up! But " + lagiName +
                    " is eating with infernal abandon. He even devours the wooden trough.");
        }

        @Override
        protected void illusionDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("Somehow though, " + partyMember.getName() + " manages to finish eating quicker than " +
                    lagiName + ". " + CHIEFTAIN_NAME + " seems completely surprised!");
            event.removePortraitSubView(model);
            event.showChieftainPortrait(model);
            event.portraitSay("That... that was amazing.");
        }

        @Override
        protected void illusionDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("There is no way " + partyMember.getName() + " can match the speed at which " + lagiName + " eats.");
            model.getLog().waitForAnimationToFinish();
            event.removePortraitSubView(model);
            event.showChieftainPortrait(model);
        }

        @Override
        protected String actualDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("But " + partyMember.getFirstName() + " did at least finish the whole trough. " +
                    partyMember.getFirstName() + " slaps " + GameState.hisOrHer(partyMember.getGender()));
            event.partyMemberSay(partyMember, "Yum yum!");
            return lagiName;
        }

        @Override
        protected String actualDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println(partyMember.getFirstName() + " has barely eaten half of the trough.");
            event.partyMemberSay(partyMember, "I'm stuffed. Can't eat another bite.");
            return " I know " + lagiName + " is always hungry!";
        }

        @Override
        protected void illusionDispelled(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("As if a veil is pulled back, the effect of " + CHIEFTAIN_NAME + "'s spell dissipates. " +
                    lagiName + " fades away and it becomes clear that " + partyMember.getFirstName() +
                    "'s opponent is nothing more than the roaring fire in the middle of the longhouse.");
            event.leaderSay("Hey, what are you trying to pull " + CHIEFTAIN_NAME + "?");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            event.portraitSay("Haha! You got me! Forgive me for adding some magical spice to this " +
                    "otherwise rather bland affair. I thought, what better opponent than the ravenous hunger of fire itself? " +
                    "But no matter, no matter! This was not the real test of a true viking, oh no.");
            event.leaderSay("Hmph, then what is the real test?");
        }
    }

    private static class SprintingContest extends LokiIllusionContest {

        private SprintingContest(Model model, MeetWithChieftainEvent event) {
            super(model, event, Skill.Acrobatics, 50);
        }

        @Override
        protected void lokiAnnouncesTest(MeetWithChieftainEvent event) {
            event.portraitSay("Let me see. What else? Oh, right! A viking is nothing without his or her speed. " +
                    "You must compete with one of us in a sprint!");
        }

        @Override
        protected void contenderAcceptsAndLokiSelectsOpponent(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.partyMemberSay(partyMember, "I could use the exercise. I can perform this task.");
            event.portraitSay("Good. Now let's make some room in here while I think about who among us should be " +
                    "your opponent in this race.");
            event.println("As " + CHIEFTAIN_NAME + " appears to think hard, the vikings in the hall quickly pull the tables back against the walls to " +
                    "form platforms for them to sit upon. In no time at all, the longhouse has turned into a miniature stadium.");
            event.portraitSay("Ah yes, I have it! I know just the one who shall oppose you. Young Hugi is quick. Don't worry, " +
                    "she's not the quickest of us. Many of us can outrun Hugi. But for you, an outsider, she will be a " +
                    "suitable match.");
            model.getLog().waitForAnimationToFinish();
            AdvancedAppearance hugiAppearance = new HugiAppearance();
            event.removePortraitSubView(model);
            event.showExplicitPortrait(model, hugiAppearance, "Hugi");
            event.println("A young woman steps out from behind " + CHIEFTAIN_NAME + ". She's a wisp of a girl, definitely not the athletic type.");
            event.partyMemberSay(partyMember, "She doesn't look very fast. This should be easy.");
            event.println("The vikings on tables laugh out loud.");
            event.partyMemberSay(partyMember, "Did I say something funny?");
        }

        @Override
        protected String getBlueMagicHint() {
            return "Something's off about this. " + CHIEFTAIN_NAME + " must be using some kind of spell here, " +
                    "but I can't quite figure out what.";
        }

        @Override
        protected void prepareForContest(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println(partyMember.getName() + " and Hugi line up at one end of the longhouse. They are to dash over to the other side, around " +
                    "" + CHIEFTAIN_NAME + "'s chair, and back again.");
            event.println(partyMember.getName() + " is preparing to compete in race against Hugi, press enter to continue.");
            event.waitForReturn(true);
            model.getParty().benchPartyMembers(List.of(partyMember));
            event.removePortraitSubView(model);
            event.println("Hugi takes off at an inhuman speed...");
        }

        @Override
        protected int getSkillBonus(GameCharacter partyMember) {
            return partyMember.getSpeed() / 3;
        }

        @Override
        protected void contestInProgress(MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("And " + partyMember.getName() + " tries to keep up! But Hugi is literally " +
                    "flying past the noses of the spectators.");
        }

        @Override
        protected void illusionDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            model.getParty().unbenchAll();
            event.println("Somehow though, " + partyMember.getName() + " manages to outrun Hugi. " + CHIEFTAIN_NAME + " appears totally flabbergasted!");
            event.partyMemberSay(partyMember, "By golly, she's fast...");
            if (model.getParty().getLeader() != partyMember) {
                event.leaderSay("Good work " + partyMember.getName() + "!");
            }
            event.showChieftainPortrait(model);
            event.portraitSay("That... that was incredible!");
        }

        @Override
        protected void illusionDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println(partyMember.getFirstName() + " has only taken a few steps, and Hugi has already rounded " + CHIEFTAIN_NAME + "'s chair.");
            if (model.getParty().getLeader() != partyMember) {
                event.leaderSay("Come on " + partyMember.getFirstName() + "!");
            }
        }

        @Override
        protected String actualDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println(partyMember.getFirstName() + " has just rounded " + CHIEFTAIN_NAME + "'s chair when Hugi is back at the start.");
            model.getParty().unbenchAll();
            event.println(partyMember.getFirstName() + " returns, completely out of breath.");
            event.partyMemberSay(partyMember, "She's too fast... I just can't...");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            return "Hugi";
        }

        @Override
        protected String actualDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("But there is no way " + partyMember.getFirstName() + " can catch up with Hugi. " +
                    GameState.heOrSheCap(partyMember.getGender()) + " has barely gotten half way when Hugi is back where she started!");
            model.getLog().waitForAnimationToFinish();
            model.getParty().unbenchAll();
            event.println(partyMember.getFirstName() + " returns, completely out of breath.");
            event.partyMemberSay(partyMember, "She's too fast... I just can't...");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            return "I think Hugi is up for another challenge.";
        }

        @Override
        protected void illusionDispelled(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("As if a veil is pulled back, the effect of " + CHIEFTAIN_NAME + "'s spell dissipates. " +
                    "Hugi fades into a whiff of smoke and appears to be sucked into " + CHIEFTAIN_NAME + "'s forehead.");
            event.leaderSay("Hey, what kind of illusion is this " + CHIEFTAIN_NAME + "?");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            event.portraitSay("Haha! You got me! I just wanted to make the race a little more interesting. " +
                    "My thinking was, what better opponent than thought itself? So you see Hug is an old word for thought and, " +
                    "Hugi was just as quick as a thought, but no more than a figment of my imagination. But no matter, no matter! " +
                    "This was not the real test of a true viking, oh no.");
            event.leaderSay("Hmph, then what is the real test?");
        }
    }

    private static class WrestlingContest extends LokiIllusionContest {

        private WrestlingContest(Model model, MeetWithChieftainEvent event) {
            super(model, event, Skill.UnarmedCombat, 99);
        }

        @Override
        protected void lokiAnnouncesTest(MeetWithChieftainEvent event) {
            event.leaderSay("I'm getting annoyed by these tests of yours " + CHIEFTAIN_NAME + ". Are you sure they are necessary?");
            event.portraitSay("Of course! The ancient rites must be obeyed. But your anger comes at a good moment, " +
                    "because for this test you may need some.");
            event.leaderSay("What do you mean?");
            event.portraitSay("A true viking must be able to handle any physical confrontation. In this test, you must wrestle " +
                    "one of us, and prevail.");
            event.randomSayIfPersonality(PersonalityTrait.aggressive, List.of(), "Finally, a test suitable for me!");
        }

        @Override
        protected void contenderAcceptsAndLokiSelectsOpponent(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.partyMemberSay(partyMember, "Let me tussle with these vikings. They've been irking me.");
            event.portraitSay("But who among us would be a suitable match?");
            event.partyMemberSay(partyMember, "Why don't you face me yourself?");
            event.portraitSay("Me? Oh no. I'm afraid I would be too much for you. It wouldn't be a fair fight at all.");
            event.partyMemberSay(partyMember, "Then just pick anybody. I'm ready for anything!");
            event.portraitSay("Indeed? Well...");
            event.println("" + CHIEFTAIN_NAME + " looks around.");
            event.portraitSay("I dare say, there is nobody here which wouldn't make a fool out of you, so much are we your betters.");
            event.partyMemberSay(partyMember, "Seriously?");
            event.portraitSay("Ah, but wait. Who's that over by the door? See that crouched figure? It's my old grandmother Elle. " +
                    "Sure, she was a strong woman in her day... but now I fear she's lost most of her vitality. Come over here Elle.");
            event.println("A venerable old woman, who walks with a cane, slowly approaches " + CHIEFTAIN_NAME + ".");
            model.getLog().waitForAnimationToFinish();
            CharacterAppearance elleAppearance = new OldElleAppearance();
            event.showExplicitPortrait(model, elleAppearance, "Old Elle");
            event.partyMemberSay(partyMember, "You want me to fight this old woman?");
            model.getLog().waitForAnimationToFinish();
            event.removePortraitSubView(model);
            event.showChieftainPortrait(model);
            event.portraitSay("Yes. She's still a viking after all. I think it will be a fair match. " +
                    "Of all the foes you have faced, and will face, the last one you should underestimate is old Elle.");
            event.println("The vikings around the hall, laugh at this last remark, as if it were some kind of joke.");
            model.getLog().waitForAnimationToFinish();
            event.removePortraitSubView(model);
            event.showExplicitPortrait(model, elleAppearance, "Old Elle");
            event.partyMemberSay(partyMember, "Well. If you insist. This test will be over in no time.");
            event.println("The viking crowd hoots with laughter.");
            event.partyMemberSay(partyMember, "Huh? What was so funny about that?");
        }

        @Override
        protected String getBlueMagicHint() {
            return "There's something funny going on here. " + CHIEFTAIN_NAME + " is casting some kind of illusion, " +
                    "but I can't quite figure out what.";
        }

        @Override
        protected void prepareForContest(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println(partyMember.getName() + " and Elle approach each other.");
            event.println(partyMember.getFirstName() + " is preparing to wrestle with Elle, press enter to continue.");
            event.waitForReturn(true);
            event.println("Elle and " + partyMember.getFirstName() + " grab ahold of one another...");
        }

        @Override
        protected int getSkillBonus(GameCharacter partyMember) {
            return partyMember.getRankForSkill(Skill.Acrobatics) + partyMember.getRankForSkill(Skill.Endurance);
        }

        @Override
        protected void contestInProgress(MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("And " + partyMember.getFirstName() + " tries to wrestler her down, but the old woman " +
                    "is much tougher than she looks.");
        }

        @Override
        protected void illusionDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("Amazingly enough, after an epic struggle" + partyMember.getName() +
                    " manages to pin Elle to the floor. " + CHIEFTAIN_NAME + " seems shocked.");
            event.showChieftainPortrait(model);
            event.portraitSay("That... that was... that... that should not have been possible. " +
                    "How did you do that?");
            model.getParty().partyMemberSay(model, partyMember, "I've had some training.");
        }

        @Override
        protected void illusionDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("And time after time, Old Elle escapes " + partyMember.getFirstName() + "'s grip. " +
                    "Finally it is " + partyMember.getFirstName() + " " + GameState.himOrHer(partyMember.getGender()) +
                    "self who is beaten down into submission.");
        }

        @Override
        protected String actualDifficultyPassed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("Still, it was no easy fight, and it is clear " + partyMember.getFirstName() +
                    " performed admirably.");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            return "Old Elle";
        }

        @Override
        protected String actualDifficultyFailed(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("The match was rather quick, and " + partyMember.getFirstName() + " seems quite confounded.");
            return "Old Elle will always be here to meet you.";
        }

        @Override
        protected void illusionDispelled(Model model, MeetWithChieftainEvent event, GameCharacter partyMember) {
            event.println("As if a veil is pulled back, the effect of " + CHIEFTAIN_NAME + "'s spell dissipates. " +
                    "Old Elle seems to turn to dry paper, then collapses into a dust pile on the floor.");
            event.leaderSay("Hey, what kind of magic trick is this " + CHIEFTAIN_NAME + "?");
            model.getLog().waitForAnimationToFinish();
            event.showChieftainPortrait(model);
            event.portraitSay("Haha! You got me! I just wanted to make the fight a little bit more fun. " +
                    "You see, who is a more formidable opponent than time itself? Elle is just an old word for age, " +
                    "The one adversary no warrior can escape, old age." +
                    " But no matter, no matter! " +
                    "This was not the real test of a true viking, oh no.");
            event.leaderSay("I'm getting very weary of these tests " + CHIEFTAIN_NAME + ". I'm getting the feeling you're " +
                    "just wasting our time for your own entertainment.");
        }
    }
}
