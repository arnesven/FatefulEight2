package model.mainstory;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.*;
import model.combat.CombatAdvantage;
import model.enemies.BanditArcherEnemy;
import model.enemies.BanditEnemy;
import model.enemies.BanditLeaderEnemy;
import model.enemies.Enemy;
import model.items.Item;
import model.items.potions.HealthPotion;
import model.items.spells.Spell;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.mainstory.honorable.*;
import model.map.WorldBuilder;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.PickSamuraiSwordState;
import model.states.swords.SamuraiSword;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import util.MyTriplet;
import view.MyColors;
import view.combat.MountainCombatTheme;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GainSupportOfHonorableWarriorsTask extends GainSupportOfRemotePeopleTask {

    private static final int INITIAL_STEP = 0;

    private static final int NO_SUBTASK_PERFORMED = 1;
    private static final int MIKOS_TASK_DONE = 2;
    private static final int SHINGEN_MET = 3;
    private static final int SMITH_TIP_GOTTEN = 4;
    private final boolean completed;

    private final List<SubTask> subTasks;

    private final MikoAppearance mikoAppearance;
    private final boolean shingenLikesInscriptions;
    private AdvancedAppearance shingenPortrait = null;
    private final MyColors swordColor;
    private final ShingenWeapon shingenWeapon;
    private int step = INITIAL_STEP;

    public GainSupportOfHonorableWarriorsTask() {
        super(WorldBuilder.EASTERN_PALACE_LOCATION);
        this.completed = false;
        this.subTasks = new ArrayList<>();
        this.mikoAppearance = new MikoAppearance();
        this.swordColor = MyRandom.sample(PickSamuraiSwordState.SWORD_COLORS);
        this.shingenWeapon = ShingenWeapon.values()[MyRandom.randInt(ShingenWeapon.values().length)];
        this.shingenLikesInscriptions = MyRandom.flipCoin();
        subTasks.add(new RepairHousesSubTask(swordColor));
        subTasks.add(new FightBanditsSubTask());
        subTasks.add(new HealTheSickSubTask());
        subTasks.add(new TeachSpellCastingSubStask());
    }

    public boolean hasDoneMikosTask() {
        return step > NO_SUBTASK_PERFORMED;
    }

    public void setShingenMet() {
        step = SHINGEN_MET;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Honorable Warriors") {
            @Override
            public String getText() {
                if (step == INITIAL_STEP) {
                    return "Gain the support of the Honorable Warriors in the Far Eastern town.";
                } else if (step <= NO_SUBTASK_PERFORMED) {
                    return "Complete one of Miko's tasks to gain enough trust to get an audience with Lord Shingen.";
                } else if (step <= MIKOS_TASK_DONE) {
                    return "Request an audience with Lord Shingen.";
                }
                String extra = "";
                if (step == SMITH_TIP_GOTTEN) {
                    extra = " Get a special weapon from the weapon smith who lives north east of the Eastern Palace.";
                }
                return "Find a suitable sword to present to Lord Shingen as a symbol of your commitment to one another." +
                        extra;
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfHonorableWarriorsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfHonorableWarriorsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public CharacterAppearance getShingenPortrait() {
        if (shingenPortrait == null) {
            this.shingenPortrait = new ShingenAppearance(swordColor);
        }
        return shingenPortrait;
    }

    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        return null;
    }

    public DailyEventState makeLordShingenEvent(Model model) {
        if (step >= SHINGEN_MET) {
            return new PresentSwordToShingenEvent(model, this);
        }
        return new MeetLordShingenEvent(model, this);
    }

    public DailyEventState generateEvent(Model model, boolean waterMill) {
        if (waterMill) {
            return new VisitMikosHomeEvent(model, true);
        } else {
            if (step == INITIAL_STEP) {
                return new JustArrivedInTownEvent(model);
            }
        }
        return null;
    }

    public ShingenWeapon getShingenWeapon() {
        return shingenWeapon;
    }

    public boolean doesShingenLikeInscriptions() {
        return shingenLikesInscriptions;
    }

    public MyColors getShingenColor() { return swordColor; }

    private class JustArrivedInTownEvent extends DailyEventState {
        public JustArrivedInTownEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("As you" + (model.getParty().size() > 1 ? "r party" : "") +
                    " stroll into this exotic town you notice everybody is staring at you. " +
                    "Children are pointing, some laughing, some clinging to their parents. A man approaches you.");
            showRandomPortrait(model, Classes.FARMER, Race.EASTERN_HUMAN,"Villager");
            portraitSay("You don't belong here outsider. What's your business?");
            leaderSay("We've come to conduct diplomatic negotiations with your people. Who among you do you call leader?");
            portraitSay("Our Lord Shingen rules this land. But he will surely not see a lowly outsider as yourself.");
            leaderSay("Why not? " + imOrWere() + " not just some scruffy vagabond" + (model.getParty().size() > 1 ? "s." : "."));
            portraitSay("Nevertheless, I'm sure our lord will not meet with you. " +
                    "You had better go see old Miko, he is wise and knows most about western affairs among us. He will advise you.");
            print("Do you follow the villager to Miko's home? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay(iOrWeCap() + " don't have time to banter with some old codger. Show " + meOrUs() + " to your leader!");
                println("The villager is affronted by your brusque manner, and walks off.");
                return;
            }
            leaderSay("Fine, show " + meOrUs() + " to this Miko fellow.");
            println("The villager shows you to an old mill. Inside, an old, blind emaciated man sits on the floor.");
            portraitSay("Miko, these outsiders are from the west. They say they have urgent business with Lord Shingen.");
            model.getLog().waitForReturn();
            removePortraitSubView(model);
            new VisitMikosHomeEvent(model, false).doEvent(model);

        }
    }

    private class VisitMikosHomeEvent extends DailyEventState {
        private final boolean withIntro;

        public VisitMikosHomeEvent(Model model, boolean withIntro) {
            super(model);
            this.withIntro = withIntro;
        }

        @Override
        protected void doEvent(Model model) {
            if (step == INITIAL_STEP) {
                if (withIntro) {
                    setCurrentTerrainSubview(model);
                    println("You step inside the old mill. The turning of the big wooden wheel thumps eerily. " +
                            "Upstairs you find an old, blind emaciated man sitting on the floor");
                }
                step = NO_SUBTASK_PERFORMED;
                model.getLog().waitForAnimationToFinish();
                showMiko(model);
                forcePortraitEyes(true);
                if (withIntro) {
                    portraitSay("Greetings travellers, what is your business in our peaceful town?");
                    leaderSay("We've come to conduct diplomatic negotiations with your people. Who among you do you call leader?");
                    portraitSay("Our Lord Shingen rules this land. But he will surely not see a lowly outsider as yourself.");
                    leaderSay("Why not? " + imOrWere() + " not just some scruffy vagabond" + (model.getParty().size() > 1 ? "s." : "."));
                    portraitSay("Nevertheless, I'm sure our lord will not meet with you.");
                } else {
                    leaderSay("Yes, urgent indeed.");
                    portraitSay("I see... It's been a long time since we've had westerners here.");
                }
                portraitSay( "I'm afraid Lord Shingen is of the opinion that all westerners are mad barbarians.");
                leaderSay("What has given him that impression?");
                portraitSay("We've had some bad experiences with envoys from the kingdoms of Arkvale and Ardh. " +
                        "And more recently, there's been troop movement near Arkvale.");
                leaderSay("Yes. Arkvale is in a state of turmoil at the moment. And the people there are suffring. " +
                        "That's actually the reason we've travelled to this land.");
                portraitSay("Oh? Go on.");
                leaderSay(iOrWeCap() + " believe an evil force has corrupted the regent in Arkvale. " + imOrWere() + " rallying the " +
                        "neighboring kingdoms to depose them. But we also need the support of your people.");
                portraitSay("I understand, it's a noble cause, and Shingen does not have a heart of stone. But it will " +
                        "require some kind of act to prove that your intentions are honorable.");
                leaderSay("What can we do to show that?");
                portraitSay("There are several things you could do to show you are a friend of our people. " +
                        "What are you proficient at?");

                List<String> options = MyLists.transform(subTasks, st -> st.partySkills.split(",")[0]);
                int count = multipleOptionArrowMenu(model, 24, 24, options);
                SubTask selected = subTasks.get(count);
                leaderSay(selected.partySkills);
                portraitSay("I see. " + selected.description);

                print("Do you want to " + selected.name.toLowerCase() + " now? (Y/N) ");
                if (!yesNoInput()) {
                    leaderSay("Understood. But first there are things that " + iOrWe() + " must tend to.");
                    portraitSay("Goodbye for now then.");
                    leaderSay("Bye Miko.");
                }

                println("Miko gives you directions to the place where you must perform your task.");
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
                boolean success = selected.performTask(model, this);
                if (success) {
                    portraitSay("Your help to our village will surely have been noticed by Shingen.");
                    subTasks.remove(selected);
                    step = MIKOS_TASK_DONE;
                }
            } else { // Step 1 => Not performed any subtask or SUBTASK PERFORMED
                setCurrentTerrainSubview(model);
                showMiko(model);
                if (subTasks.isEmpty()) {
                    portraitSay("Thanks for all the help you've provided for us in the village. " +
                            "I'm sure Shingen must have heard of all your deeds.");
                    leaderSay("Is there anything else we can help with?");
                    portraitSay("Nothing of consequence that I know of.");
                    leaderSay("Okay. Thanks Miko.");
                    portraitSay("Goodbye");
                } else {
                    secondTimeDoTasks(model);
                }
            }
        }

        private void secondTimeDoTasks(Model model) {
            println("You approach old Miko. He's sitting still on the floor.");
            portraitSay("You have returned.");

            if (step >= SHINGEN_MET) {
                step = SMITH_TIP_GOTTEN;
                leaderSay("Lord Shingen wants us to bring him a fine sword. Do you have any suggestions?");
                portraitSay("Don't get the rubbish from the local peddlers. " +
                        "There's a weapon smith who lives in the hills north east of here. She makes " +
                        "very fine weapons. One of those should be suitable for lord Shingen.");
                leaderSay("Sounds ideal. Is it far?");
                portraitSay("The trip takes a day or two.");
                leaderSay(imOrWere() + " off then.");
                if (doesShingenLikeInscriptions()) {
                    portraitSay("Make sure you get a weapon with an inscription, that will please Lord Shingen.");
                } else {
                    portraitSay("Just make sure the weapon doesn't have an inscription. I happen to know Lord " +
                            "Shingen finds inscriptions on weapons tacky, and a waste of time.");
                }
                leaderSay("Thanks for the advice.");
            }

            leaderSay("What were the tasks that needed to be done?");
            for (SubTask st : subTasks) {
                portraitSay(st.description);
            }
            println("Which task do you want to do?");
            List<String> options = MyLists.transform(subTasks, st -> st.name);
            options.add("Cancel");
            int count = multipleOptionArrowMenu(model, 24, 24, options);
            if (count == subTasks.size()) {
                println("Goodbye Miko.");
                portraitSay("Bye.");
                return;
            }
            SubTask selected = subTasks.get(count);
            println("Miko gives you directions to the place where you must perform your task.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            boolean success = selected.performTask(model, this);
            if (step == NO_SUBTASK_PERFORMED && success) {
                portraitSay("Your help to our village will surely have been noticed by Shingen.");
                subTasks.remove(selected);
                step = MIKOS_TASK_DONE;
            }
        }

        public boolean runBanditCombat(Model model) {
            CombatAdvantage adv = CombatAdvantage.Neither;
            print("Do you attempt to sneak into the camp to take the bandits by surprise? (Y/N) ");
            if (yesNoInput()) {
                boolean success = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 7);
                if (success) {
                    println("You manage to position yourself well for combat, then completely unexpectedly you attack the bandits!");
                    adv = CombatAdvantage.Party;
                } else {
                    println("Failing to sneak up on the bandits, some of you get separated and confusion arises " +
                            "at the start of combat, giving the bandits the edge.");
                    adv = CombatAdvantage.Enemies;
                }
            }
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                enemies.add(new BanditEnemy('B'));
                enemies.add(new BanditArcherEnemy('A'));
            }
            enemies.add(new BanditLeaderEnemy('C'));
            Collections.shuffle(enemies);
            runCombat(enemies, new MountainCombatTheme(), true, adv, new ArrayList<>());
            setCurrentTerrainSubview(model);
            if (haveFledCombat() || model.getParty().isWipedOut()) {
                return false;
            }

            showMiko(model);
            print("A fierce skirmish huh? This reminds me when Shingen was a young lad. He would go into battle, armed with ");
            switch (shingenWeapon) {
                case Wakisashis:
                    print("two wakizashis");
                case Katana:
                    print("a simple katana");
                case DaiKatana:
                    print("a large dai-katana");
            }
            println(" fearless as a tiger.");
            return true;
        }

        public void showMiko(Model model) {
            showExplicitPortrait(model, mikoAppearance, "Miko");
        }
    }

    private abstract static class SubTask implements Serializable {
        public String name;
        public String partySkills;
        public String description;

        public SubTask(String name, String partySkills, String description) {
            this.name = name;
            this.partySkills = partySkills;
            this.description = description;
        }

        public abstract boolean performTask(Model model, VisitMikosHomeEvent event);
    }

    private static class RepairHousesSubTask extends SubTask {
        private final MyColors bannerColor;

        public RepairHousesSubTask(MyColors bannerColor) {
            super("Repair houses", "We are skilled craftsmen, and good laborers.",
                    "For some time we've had trouble finding " +
                    "materials to restore some of our buildings. You could assist us in repairing them.");
            this.bannerColor = bannerColor;
        }

        @Override
        public boolean performTask(Model model, VisitMikosHomeEvent event) {
            event.println("You find the buildings Miko's described, and indeed, they are in a dilapidated state.");
            if (model.getParty().getInventory().getMaterials() < 10) {
                event.println("Unfortunately, you lack the amount of materials needed to assist in repairing the buildings (10).");
                return false;
            }
            event.print("Repairing the buildings will cost 10 materials. Do you want to attempt to? (Y/N) ");
            if (!event.yesNoInput()) {
                return false;
            }
            if (!model.getParty().doCollaborativeSkillCheck(model, event, Skill.Labor, 12)) {
                event.println("You have made no progress repairing the run down buildings.");
            }
            model.getParty().getInventory().addToMaterials(-10);
            event.println("After a full days labor, and using up the materials you brought, " +
                    "you have many improvements and repairs to the buildings. You even manage to hang some of the " +
                    "Honorable Warriors " + SamuraiSword.colorToString(bannerColor) + " banners, flapping in the wind.");
            event.println("Now that the task is completed, you return to Miko's home.");
            event.showMiko(model);
            return true;
        }
    }

    private static class FightBanditsSubTask extends SubTask {
        public FightBanditsSubTask() {
            super("Fight bandits", "We are capable fighters, and we're well equipped.",
                    "There are brigands up in the hills. They've been " +
                    "troubling our farmers for some time. You could drive them off for good.");
        }

        @Override
        public boolean performTask(Model model, VisitMikosHomeEvent event) {
            event.println("You head out of town and up into the hills. After a little while you " +
                    "spot the bandit camp Miko was talking about.");
            return event.runBanditCombat(model);
        }
    }

    private static class HealTheSickSubTask extends SubTask {
        public HealTheSickSubTask() {
            super("Heal sickly", "We are good healers, skilled in medicinal arts.",
                    "Our best doctor has passed away and we've been lacking a good healer. " +
                            "Many of the villagers are sick or injured. You could tend to the ailing.");
        }

        @Override
        public boolean performTask(Model model, VisitMikosHomeEvent event) {
            event.println("You find the primitive hospital in the village. Inside there are indeed " +
                    "many sick and injured people.");
            event.print("In what way do you attempt to aid the people in the hospital?");
            List<String> options = new ArrayList<>();
            List<Item> potions = MyLists.filter(model.getParty().getInventory().getAllItems(), it -> it instanceof HealthPotion);
            if (potions.size() >= 3) {
                options.add("Heal using health potions");
            }
            options.add("Heal using survival skill");
            options.add("Heal using green magic");
            options.add("Heal using white magic");
            final String[] selected = {null};
            model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 24, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selected[0] = options.get(cursorPos);
                    model.setSubView(getPrevious());
                }
            });
            event.waitForReturn();
            boolean taskSuccess = false;
            if (selected[0].contains("potions")) {
                event.println("You distribute your health potions among the sick and wounded, who immediately consume " +
                        "the magic brews. The villagers recover quickly and pretty soon the hospital is empty.");
                int potionsLost = Math.min(3, potions.size());
                for (int i = potionsLost; i > 0; --i) {
                    model.getParty().getInventory().remove(potions.get(0));
                    potions.remove(0);
                }
                event.println("You have lost " + MyStrings.numberWord(potionsLost) + " health potions.");
                taskSuccess = true;
            } else {
                Skill skillToUse;
                int difficulty = 10;
                if (selected[0].contains("survival")) {
                    skillToUse = Skill.Survival;
                } else if (selected[0].contains("green")) {
                    skillToUse = Skill.MagicGreen;
                } else {
                    skillToUse = Skill.MagicWhite;
                    difficulty -= 2;
                }
                int successes = 0;
                for (int i = 0; i < 4; ++i) {
                    if (model.getParty().doSoloSkillCheck(model, event, skillToUse, difficulty)) {
                        event.println("You successfully treat one of the villagers in the hospital.");
                        successes++;
                    } else {
                        event.println("You try to treat one of the villagers, " +
                                "but you just can't seem to find a way to help " + GameState.himOrHer(MyRandom.flipCoin()) + ".");
                    }
                    if (i < 3) {
                        event.println("However, there " + (i == 2 ? "is" : "are") + " still " + MyStrings.numberWord(3 - i) +
                                " villagers needing treatment in the hospital.");
                    }
                }
                taskSuccess = successes >= 3;
            }
            model.getLog().waitForAnimationToFinish();
            if (taskSuccess) {
                event.showMiko(model);
                event.println("You return to Miko and tell about your success at the village hospital.");
            }
            return taskSuccess;
        }
    }

    private static class TeachSpellCastingSubStask extends SubTask {
        public TeachSpellCastingSubStask() {
            super("Teach spell casting", "We are powerful mages, with many useful spells.",
                    "Our people are not very good at magic. If you would be willing to instruct " +
                            "us that would be much appreciated. You could host a demonstration of the basics of magic.");
        }

        @Override
        public boolean performTask(Model model, VisitMikosHomeEvent event) {
            event.println("You head over to the local school. With a little help from the teachers are soon able to set up " +
                    "a lecture about the fundamentals of magic. Many villagers are in attendance.");
            event.print("Which of the party members should be the speaker during the lecture?");
            GameCharacter lecturer = model.getParty().partyMemberInput(model, event, model.getParty().getPartyMember(0));
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, event, lecturer, Skill.SpellCasting, 8, 20, 0);
            if (result.isSuccessful()) {
                event.println("At the end of the lecture you feel the need to demonstrate a couple of spells. " +
                        "After all, one cannot be a magic user by only dealing with theory!");
                List<Spell> spells = new ArrayList<>(model.getParty().getSpells());
                if (spells.isEmpty()) {
                    event.println("However, since you do not know a single spell, you attempt to perform a cheap trick instead. " +
                            "The villagers are not impressed, and some of them leave early.");
                    return false;
                }
                for (int i = 0; i < 2; ++i) {
                    event.print("What spell would you like to cast?");
                    List<String> options = MyLists.transform(spells, Item::getName);
                    final Spell[] selected = {null};
                    model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 24, ArrowMenuSubView.NORTH_WEST) {
                        @Override
                        protected void enterPressed(Model model, int cursorPos) {
                            selected[0] = spells.get(cursorPos);
                            model.setSubView(getPrevious());
                        }
                    });
                    event.waitForReturn();
                    boolean castSuccess = selected[0].castYourself(model, event, lecturer);
                    if (lecturer.isDead() || !castSuccess) {
                        return false;
                    }
                    event.print("The crowd is impressed by the display of magic!");
                    spells.remove(selected[0]);
                }
            } else {
                return false;
            }
            event.showMiko(model);
            event.println("You return to miko and tell all about the magic lecture.");
            return true;
        }
    }
}
