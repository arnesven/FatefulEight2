package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.DispelSpell;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.mainstory.vikings.HugiAppearance;
import model.mainstory.vikings.LagiAppearance;
import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.map.locations.EasternPalaceLocation;
import model.map.locations.VikingVillageLocation;
import model.quests.SavageVikingsQuest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.SpellCastException;
import util.MyPair;
import util.MyTriplet;
import view.subviews.CollapsingTransition;
import view.subviews.GuildHallImageSubView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfVikingsTask extends GainSupportOfRemotePeopleTask {
    public static final int INITIAL_STEP = 0;
    private static final int QUEST_DONE = 1;
    private static final int LOKI_MET = 2;
    private static final int EATING_CONTEST_DONE = 3;
    private static final int SPRINTING_CONTEST_DONE = 4;
    private static final int WRESTLING_CONTEST_DONE = 5;

    private final AdvancedAppearance chieftainPortrait;

    private boolean completed = false;
    private int step = INITIAL_STEP;

    public GainSupportOfVikingsTask(Model model) {
        super(WorldBuilder.VIKING_VILLAGE_LOCATION);
        chieftainPortrait = PortraitSubView.makeRandomPortrait(Classes.VIKING_CHIEF, Race.NORTHERN_HUMAN, false);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Vikings") {
            @Override
            public String getText() {
                String first = "Gain the support of the Vikings of the North.";
                if (step > INITIAL_STEP) {
                    first += "\n\nYou have managed to persuade the vikings you are not an enemy.";
                }
                if (step >= LOKI_MET) {
                    first += "\n\nIn order to ally with the Vikings, you must prove to Chieftain Loki that you are " +
                            "a true viking by passing certain tests.";
                }
                if (step >= EATING_CONTEST_DONE) {
                    first += "\n\nYou have passed Loki's 'Eating Contest'.";
                }
                return first;
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfVikingsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfVikingsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }


    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        if (model.getCurrentHex().getLocation() instanceof VikingVillageLocation &&
                step == INITIAL_STEP) {
            return new MyTriplet<>(SavageVikingsQuest.QUEST_NAME,
                    model.getParty().getLeader().getAppearance(), "Yourself");
        }
        return null;
    }

    @Override
    public void setQuestSuccessful() {
        step = QUEST_DONE;
    }

    @Override
    public void addFactionString(List<MyPair<String, String>> result) {
        // TODO
    }

    public DailyEventState generateEvent(Model model, boolean fromLonghouse) {
        if (fromLonghouse) {
            if (step == INITIAL_STEP) {
                return new NotAdmittedToLonghouseEvent(model);
            } else if (step >= QUEST_DONE) {
                return new MeetWithChieftainEvent(model, true);
            }
        }
        if (step == INITIAL_STEP) {
            return new JustArrivedInVikingTownEvent(model);
        }
        return null;
    }

    private class NotAdmittedToLonghouseEvent extends DailyEventState {
        public NotAdmittedToLonghouseEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("Two large, very aggressive looking vikings are guarding the entrance to the longhouse.");
            print("Do you attempt to persuade them to let you enter? (Y/N) ");
            if (yesNoInput()) {
                boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 1); // TODO: 20
                if (success) {
                    println("Just as the guards are about to pound you to oblivion, you manage to explain " +
                            "the reason for your presence in a concise and convincing manner. Baffled by your eloquence " +
                            "the guards let you into the longhouse.");
                    new MeetWithChieftainEvent(model, false).run(model);
                    return;
                }
                println("The guards are unconvinced and come at you with weapons drawn. Other vikings who are passing by " +
                        "are taking note of the situation.");
                leaderSay(iOrWeCap() + "'d better leave now before this gets ugly.");
            } else {
                leaderSay("Hmm... These guys don't look too friendly. Provoking them would be unwise.");
                randomSayIfPersonality(PersonalityTrait.cowardly, List.of(), "A very wise standpoint!");
            }
            leaderSay("In fact, everybody in this village seems pretty upset with " + myOrOur() + " presence. " +
                    "Somehow " + iOrWe() + " must convince them that we are worthy of an alliance.");
            println("You turn away from the longhouse.");
        }
    }

    private class MeetWithChieftainEvent extends DailyEventState {
        private static final int ILLUSION_DIFFICULTY = 30;
        private static final int ACTUAL_DIFFICULTY = 14;
        private static final int MAGIC_DIFFICULTY = 1; // TODO: 10
        private final boolean withIntro;

        public MeetWithChieftainEvent(Model model, boolean withIntro) {
            super(model);
            this.withIntro = withIntro;
        }


        private void showChieftainPortrait(Model model) {
            showExplicitPortrait(model, chieftainPortrait, "Chieftain Loki");
        }

        @Override
        protected void doEvent(Model model) {
            if (withIntro) {
                println("The guards posted outside the entrance to the longhouse smirk at you as you approach, but " +
                        "they do not stop you as you enter the longhouse.");
            }
            model.getLog().waitForAnimationToFinish();
            CollapsingTransition.transition(model, GuildHallImageSubView.getInstance("Longhouse"));
            meetLoki(model);
            portraitSay("There are tests which must be passed. You must overcome several challenges!");
            println("You can hear the vikings around you sniggering.");
            randomSayIfPersonality(PersonalityTrait.irritable, List.of(), "What's so funny?");
            leaderSay("Go on Loki. What are these challenges?");

            portraitSay("Hmmm... let me think. Let me recall. What were the ancient rites...");
            randomSayIfPersonality(PersonalityTrait.critical, List.of(),
                    "Something about this doesn't seem quite right.");
            leaderSay("...");
            if (step < EATING_CONTEST_DONE) {
                if (eatingContest(model)) {
                    step = EATING_CONTEST_DONE;
                } else {
                    println("You leave the longhouse.");
                    return;
                }
            }

            if (step < SPRINTING_CONTEST_DONE) {
                if (sprintingContest(model)) {
                    step = SPRINTING_CONTEST_DONE;
                } else {
                    println("You leave the longhouse.");
                    return;
                }
            }

            if (step < WRESTLING_CONTEST_DONE) {
                if (wrestlingContest(model)) {
                    step = WRESTLING_CONTEST_DONE;
                } else {
                    println("You leave the longhouse.");
                    return;
                }
            }
        }

        private void meetLoki(Model model) {
            println("You step into a grand hall. Two long tables extend on either side of a roaring fire, above which " +
                    "some animal is roasting on a spit.");
            if (step <= QUEST_DONE) {
                randomSayIfPersonality(PersonalityTrait.gluttonous, List.of(), "My mouth is watering. Do you think they'd mind if...");
                println("Many viking men and women are sitting at the tables. They are watching you silently as you approach the " +
                        "man sitting at the end of the hall. The person you assume to be the chieftain of the Vikings.");
                model.getLog().waitForAnimationToFinish();
                showChieftainPortrait(model);
                portraitSay("So you're the outsider who's been causing all the commotion in our village.");
                leaderSay("Yes. But we are friends, not foes.");
                portraitSay("Indeed? Outsiders rarely come here. Those that do are often not friendly. Those that are " +
                        "are seldom prepared for this harsh environment.");
                println("With the last remark, Loki smiles cruelly. And laughs are heard from the other vikings in the hall.");
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
                println("The vikings in the hall laugh heartily at Loki's joke.");
                leaderSay("We do not seek refuge, but allies. " + iOrWeCap() + " aim to rally the support of the kingdoms surrounding " +
                        kingdom + ". We intend to return to " + kingdom + " to overthrow " +
                        bogdown.getLordName() + " and root out the evil presence.");
                leaderSay("If we could rely on your support as well. Nothing could oppose us.");
                portraitSay("And what would be in it for us?");
                leaderSay("When we sack " + bogdown.getPlaceName() + " you can take all the spoils you want. Surely you must have fantasized " +
                        "about such an opportunity?");
                portraitSay("Perhaps I have...");
                leaderSay("So. What will it be Chieftain Loki? Will you partake in history? Or will you sit here, by your frozen hearth, " +
                        "and wait for a better chance?");
                portraitSay("This crusade of yours, it has some appeal, but there is a problem.");
                leaderSay("What's the problem?");
                portraitSay("Only a true viking could compile our tribe to undertake such a venture. And you, are no viking.");
                leaderSay("That is true. But how does one become a true viking?");
                step = LOKI_MET;
            } else {
                println("Many viking men and women are sitting at the tables. " +
                        "You approach Chieftain Loki, who is sitting in his chair at the end of the hall.");
                model.getLog().waitForAnimationToFinish();
                showChieftainPortrait(model);
            }
        }

        private boolean eatingContest(Model model) {
            portraitSay("Ah... now I remember. A viking fights with extreme vigor! To fuel this, he or she " +
                    "must eat properly. You must compete with one of us in an eating contest!");
            GameCharacter eater = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                portraitSay("Who among you do you choose as champion?");
                eater = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            partyMemberSay(eater, "I'm ravenous. I can perform this task.");
            portraitSay("Good. I will choose one from my tribe. Don't worry, I won't pick the best, that would be unfair. " +
                    "Now who should I choose. Hmmm...");
            println("Loki gazes around upon the vikings sitting in the hall.");
            String lagiName = "L" + (char)(0x85) + "gi";
            portraitSay("Ah! Yes, young " + lagiName + ", he will do well.");
            model.getLog().waitForReturn();
            System.out.println("Letter: " + (int)'å');
            AdvancedAppearance lagiAppearance = new LagiAppearance();
            showExplicitPortrait(model, lagiAppearance, lagiName);
            println("A young man emerges from the back of the crowd. You're surprised you didn't notice him earlier, he " +
                    "has a certain radiance about him. He is however rather skinny.");
            partyMemberSay(eater, "He doesn't look like a very big eater. This should be easy.");
            showChieftainPortrait(model);
            portraitSay("Yes, most of us can eat faster than " + lagiName +
                    ". If you are a true viking, it should be no trouble for you.");
            model.getLog().waitForAnimationToFinish();
            println("Once again, laughter erupts in the Longhouse.");
            showExplicitPortrait(model, lagiAppearance, lagiName);

            model.getSpellHandler().acceptSpell(new DispelSpell().getName());
            boolean passedTest = false;
            try {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicBlue, MAGIC_DIFFICULTY);
                if (result.first) {
                    partyMemberSay(result.second, "This is not what it seems. That Loki guy is using some kind of spell here, " +
                            "but I can't quite figure out what.");
                }
                println("Two huge troughs of roasted meat are brought out and put in front of " + lagiName + " and " + eater.getFirstName() + ".");
                println(eater.getName() + " is preparing to compete in an eating contest with " + lagiName + ", press enter to continue.");
                waitForReturn(true);
                println(lagiName + " starts eating with inhuman rapidity...");
                SkillCheckResult eatingResult = model.getParty().doSkillCheckWithReRoll(model, this, eater,
                        Skill.Endurance, ILLUSION_DIFFICULTY, 0, getEatingBonus(eater));
                println("And " + eater.getName() + " tries to keep up! But " + lagiName + " is eating with infernal abandon. He even devours the wooden trough.");
                if (eatingResult.isSuccessful()) {
                    println("Somehow though, " + eater.getName() + " manages to finish eating quicker than " + lagiName + ". Loki seems completely surprised!");
                    showChieftainPortrait(model);
                    portraitSay("That... that was amazing.");
                } else {
                    println("There is no way " + eater.getName() + " can match the speed at which " + lagiName + " eats.");
                    model.getLog().waitForAnimationToFinish();
                    showChieftainPortrait(model);
                    if (eatingResult.getModifiedRoll() >= ACTUAL_DIFFICULTY) {
                        println("But " + eater.getFirstName() + " did at least finish the whole trough. " +
                                eater.getFirstName() + " slaps " + hisOrHer(eater.getGender()));
                        partyMemberSay(eater, "Yum yum!");
                        passedTest = passedTest(model, lagiName);
                    } else {
                        println(eater.getFirstName() + " has barely eaten half of the trough.");
                        partyMemberSay(eater, "I'm stuffed. Can't eat another bite.");
                        failedTest(model, " I know " + lagiName + " is always hungry!");
                    }
                }
            } catch (SpellCastException sce) {
                if (sce.getSpell().getName().equals(new DispelSpell().getName())) {
                    if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                        removePortraitSubView(model);
                        println("As if a veil is pulled back, the effect of Loki's spell dissipates. " +
                                lagiName + " fades away and it becomes clear that " + eater.getFirstName() +
                                "'s opponent is nothing more than the roaring fire in the middle of the longhouse.");
                        leaderSay("Hey, what are you trying to pull Loki?");
                        model.getLog().waitForAnimationToFinish();
                        showChieftainPortrait(model);
                        portraitSay("Haha! You got me! Forgive me for adding some magical spice to this " +
                                "otherwise rather bland affair. I thought, what better opponent than the ravenous hunger of fire itself? " +
                                "But no matter, no matter! This was not the real test of a true viking, oh no.");
                        leaderSay("Hmph, then what is the real test?");
                        passedTest = true;
                    }
                }
            }
            model.getSpellHandler().unacceptSpell(new DispelSpell().getName());
            return passedTest;
        }

        private boolean passedTest(Model model, String opponentName) {
            println("Loki actually seems somewhat impressed");
            portraitSay("Alas, you could not beat " + opponentName + ". Still, your effort is commendable.");
            leaderSay("Does this mean " + iOrWe() + " aren't true vikings?");
            portraitSay("Perhaps not. But at least the guests of my hall are entertained, perhaps you would like to take another test anyway?");
            print("Do you indulge the chieftain? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay("We don't have time for idle games. Let's leave now.");
                portraitSay("What a shame. Oh well. Until we meet again.");
                return false;
            }
            leaderSay("Why not? " + iOrWe() + "'ve got nowhere else to be.");
            return true;
        }

        private void failedTest(Model model, String extra) {
            println("Chieftain Loki doesn't seem very impressed.");
            portraitSay("I expected more! I don't think you can call yourself a true viking.");
            leaderSay("So that's it? " + iOrWeCap() + " failed?");
            portraitSay("That's it for tonight at least. Why don't you come back another night and try your " +
                    "luck then. " + extra);
            println("Roaring laughter erupts from the vikings in the longhouse as you are escorted outside.");
        }

        private int getEatingBonus(GameCharacter eater) {
            int result = 0;
            if (eater.getRace().id() == Race.HALF_ORC.id() ||
                eater.getRace().id() == Race.DWARF.id()) {
                result += 1;
            } else if (eater.getRace().id() == Race.HALFLING.id()) {
                result -= 1;
            }
            if (eater.hasPersonality(PersonalityTrait.gluttonous)) {
                result += 2;
            }
            return result;
        }


        private boolean sprintingContest(Model model) {
            portraitSay("Let me see. What else? Oh, right! " +
                    "A viking is nothing without his or her speed. " +
                    "You must compete with one of us in a sprint!");
            GameCharacter sprinter = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                portraitSay("Who among you do you choose as champion?");
                sprinter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            partyMemberSay(sprinter, "I could use the exercise. I can perform this task.");
            portraitSay("Good. Now let's make some room in here while I think about who among us should be " +
                    "your opponent in this race.");
            println("As Loki appears to think hard, the vikings in the hall quickly pull the tables back against the walls to " +
                    "form platforms for them to sit upon. In no time at all, the longhouse has turned into a miniature stadium.");
            portraitSay("Ah yes, I have it! I know just the one who shall oppose you. Young Hugi is quick. Don't worry, " +
                    "she's not the quickest of us. Many of us can outrun Hugi. But for you, an outsider, she will be a " +
                    "suitable match.");
            model.getLog().waitForAnimationToFinish();
            AdvancedAppearance hugiAppearance = new HugiAppearance();
            removePortraitSubView(model);
            showExplicitPortrait(model, hugiAppearance, "Hugi");
            println("A young woman steps out from behind Loki. She's a wisp of a girl, definitely not the athletic type.");
            partyMemberSay(sprinter, "She doesn't look very fast. This should be easy.");
            println("The vikings on tables laugh out loud.");
            partyMemberSay(sprinter, "Did I say something funny?");
            model.getSpellHandler().acceptSpell(new DispelSpell().getName());
            boolean passedTest = false;
            try {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicBlue, MAGIC_DIFFICULTY);
                if (result.first) {
                    partyMemberSay(result.second, "Something's off about this. Loki must be using some kind of spell here, " +
                            "but I can't quite figure out what.");
                }
                println(sprinter.getName() + " and Hugi line up at one end of the longhouse. They are to dash over to the other side, around " +
                        "Loki's chair, and back again.");
                println(sprinter.getName() + " is preparing to compete in race against Hugi, press enter to continue.");
                waitForReturn(true);
                model.getParty().benchPartyMembers(List.of(sprinter));
                removePortraitSubView(model);
                println("Hugi takes off at an inhuman speed...");
                SkillCheckResult sprintingResult = model.getParty().doSkillCheckWithReRoll(model, this, sprinter,
                        Skill.Acrobatics, ILLUSION_DIFFICULTY, 0, getSprintingBonus(sprinter));
                println("And " + sprinter.getName() + " tries to keep up! But Hugi is literally " +
                        "flying past the noses of the spectators.");
                model.getLog().waitForAnimationToFinish();
                if (sprintingResult.isSuccessful()) {
                    model.getParty().unbenchAll();
                    println("Somehow though, " + sprinter.getName() + " manages to outrun Hugi. Loki appears totally flabbergasted!");
                    partyMemberSay(sprinter, "By golly, she's fast...");
                    if (model.getParty().getLeader() != sprinter) {
                        leaderSay("Good work " + sprinter.getName() + "!");
                    }
                    showChieftainPortrait(model);
                    portraitSay("That... that was incredible!");
                } else {
                    println(sprinter.getFirstName() + " has only taken a few steps, and Hugi has already rounded Loki's chair.");
                    if (model.getParty().getLeader() != sprinter) {
                        leaderSay("Come on " + sprinter.getName() + "!");
                    }

                    if (sprintingResult.getModifiedRoll() >= ACTUAL_DIFFICULTY) {
                        println(sprinter.getFirstName() + " has just rounded Loki's chair when Hugi is back at the start.");
                        model.getParty().unbenchAll();
                        println(sprinter.getFirstName() + " returns, completely out of breath.");
                        partyMemberSay(sprinter, "She's too fast... I just can't...");
                        model.getLog().waitForAnimationToFinish();
                        showChieftainPortrait(model);
                        passedTest = passedTest(model, "Hugi");
                    } else {
                        println("But there is no way " + sprinter.getFirstName() + " can catch up with Hugi. " +
                                heOrSheCap(sprinter.getGender()) + " has barely gotten half way when Hugi is back where she started!");
                        model.getLog().waitForAnimationToFinish();
                        model.getParty().unbenchAll();
                        println(sprinter.getFirstName() + " returns, completely out of breath.");
                        partyMemberSay(sprinter, "She's too fast... I just can't...");
                        model.getLog().waitForAnimationToFinish();
                        showChieftainPortrait(model);
                        failedTest(model, "I think Hugi is up for another challenge.");
                    }
                }

            } catch (SpellCastException sce) {
                if (sce.getSpell().getName().equals(new DispelSpell().getName())) {
                    if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                        removePortraitSubView(model);
                        println("As if a veil is pulled back, the effect of Loki's spell dissipates. " +
                                "Hugi fades into a whiff of smoke and appears to be sucked into Loki's forehead.");
                        leaderSay("Hey, what kind of illusion is this Loki?");
                        model.getLog().waitForAnimationToFinish();
                        showChieftainPortrait(model);
                        portraitSay("Haha! You got me! I just wanted to make the race a little more interesting. " +
                                "My thinking was, what better opponent than thought itself? So you see, Hugi was just as " +
                                "quick as a thought, a figment of my imagination. But no matter, no matter! " +
                                "This was not the real test of a true viking, oh no.");
                        leaderSay("Hmph, then what is the real test?");
                        passedTest = true;
                    }
                }
            }
            model.getSpellHandler().unacceptSpell(new DispelSpell().getName());
            return passedTest;
        }

        private int getSprintingBonus(GameCharacter sprinter) {
            return sprinter.getSpeed() / 3;
        }


        private boolean wrestlingContest(Model model) {
            leaderSay("I'm getting annoyed by these tests of yours Loki. Are you sure they are necessary?");
            portraitSay("Of course! The ancient rites must be obeyed. But your anger comes at a good moment, " +
                    "because for this test you may need some.");
            leaderSay("What do you mean?");
            portraitSay("A true viking must be able to handle any physical confrontation. In this test, you must wrestle " +
                    "one of us, and prevail.");
            randomSayIfPersonality(PersonalityTrait.aggressive, List.of(), "Finally, a test suitable for me!");
            GameCharacter wrestler = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                portraitSay("Who among you do you choose as champion?");
                wrestler = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            partyMemberSay(wrestler, "Let me tussle with these vikings. They've been irking me.");
            portraitSay("But who among us would be a suitable match?");
            partyMemberSay(wrestler, "Why don't you face me yourself?");
            portraitSay("Me? Oh no. I'm afraid I would be too much for you. It wouldn't be a fair fight at all.");
            partyMemberSay(wrestler, "Then just pick anybody. I'm ready for anything!");
            portraitSay("Indeed? Well...");
            println("Loki looks around.");
            portraitSay("I dare say, there is nobody here which wouldn't make a fool out of you, so much are we your betters.");
            partyMemberSay(wrestler, "Seriously?");
            portraitSay("Ah, but wait. Who's that over by the door? See that crouched figure? It's my old grandmother Elle. " +
                    "Sure, she was a strong woman in her day... but now I fear she's lost most of her vitality. Come over here Elle.");
            println("A venerable old woman, who walks with a cane, slowly approaches Loki.");
            model.getLog().waitForAnimationToFinish();
            CharacterAppearance elleAppearance = PortraitSubView.makeOldPortrait(Classes.AMZ, Race.DARK_ELF, true);
            showExplicitPortrait(model, elleAppearance, "Old Elle");
            partyMemberSay(wrestler, "You want me to fight this old woman?");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            showChieftainPortrait(model);
            portraitSay("Yes. She's still a viking after all. I think it will be a fair match. " +
                    "Of all the foes you have faced, and will face, the last one you should underestimate is old Elle.");
            println("The vikings around the hall, laugh at this last remark, as if it were some kind of joke.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            showExplicitPortrait(model, elleAppearance, "Old Elle");
            partyMemberSay(wrestler, "Well. If you insist. This test will be over in no time.");
            println("The viking crowd hoots with laughter.");
            partyMemberSay(wrestler, "Huh? What was so funny about that?");
            model.getSpellHandler().acceptSpell(new DispelSpell().getName());
            boolean passedTest = false;
            try {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicBlue, MAGIC_DIFFICULTY);
                if (result.first) {
                    partyMemberSay(result.second, "There's something funny going on here. Loki is casting some kind of illusion, " +
                            "but I can't quite figure out what.");
                }
            } catch (SpellCastException sce) {
                if (sce.getSpell().getName().equals(new DispelSpell().getName())) {
                    if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                        removePortraitSubView(model);
                        println("As if a veil is pulled back, the effect of Loki's spell dissipates. " +
                                "Old Elle seems to turn to dry paper, then collapses into a dust pile on the floor.");
                        leaderSay("Hey, what kind of magic trick is this Loki?");
                        model.getLog().waitForAnimationToFinish();
                        showChieftainPortrait(model);
                        portraitSay("Haha! You got me! I just wanted to make the fight a little bit more fun. " +
                                "You see, who is a more formidable opponent than time herself? The one adversary no warrior can escape." +
                                " But no matter, no matter! " +
                                "This was not the real test of a true viking, oh no.");
                        leaderSay("Hmph, then what is the real test?");
                        passedTest = true;
                    }
                }
            }


            return passedTest;
        }
    }
}
