package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.ChildAppearance;
import model.characters.appearance.FacialExpression;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.OlegOgreEnemy;
import model.enemies.PetSpiderEnemy;
import model.items.books.BookItem;
import model.items.books.OlegsPoemBook;
import model.items.spells.ErodeSpell;
import model.items.spells.HarmonizeSpell;
import model.items.spells.Spell;
import model.quests.scenes.*;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SwampOgreQuest extends Quest {
    private static final String INTRO = "Albedan the mage has been taken prisoner by a Oleg, an ogre in the Dank\n" +
            "Swamp. The party sets out to rescue Albedan but the sleeping ogre is a " +
            "formidable foe, can it be circumvented?";
    private static final String ENDING = "Albedan is ecstatic over your rescue and pays you well for your assistance.";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.MAGE, Race.HIGH_ELF);
    private static final Sprite woods = new Sprite32x32("woodsqmb", "quest.png", 0x53,
            MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);

    private static final List<QuestBackground> BACKGROUND = makeBackgroundSprites();

    public SwampOgreQuest() {
        super("Swamp Ogre", "Albedan the Mage", QuestDifficulty.MEDIUM,
                new Reward(175), 2, INTRO, ENDING);
        getScenes().get(2).get(0).addSpellCallback(new HarmonizeSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println(caster.getName() + " pacifies the beast with the Harmonize spell.");
                return new QuestEdge(getJunctions().get(1));
            }
        });
        getScenes().get(3).get(0).addSpellCallback(new ErodeSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println("The cage erodes to dust before your eyes!");
                return new QuestEdge(getSuccessEndingNode());
            }
        });
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "You rescued the mage " + getProvider() + " from a Oleg, the smelly swamp ogre.");
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    public QuestIntroEventState getIntroEvent(Model model) {
        return new IntroEvent(model);
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }


    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Through the Swamp", List.of(
                        new CollaborativeSkillCheckSubScene(1, 1, Skill.Survival, 10,
                        "First we must traverse the Dank Swamp. Anybody know the way?"))),
                new QuestScene("Oleg's Stench", List.of(
                        new CollectiveSkillCheckSubScene(2, 2, Skill.Endurance, 3,
                                "Good Golly, what is that unearthly smell? Ogre sweat? Yuck!"))),
                new QuestScene("Oleg's Pet Spider", List.of(
                        new CollectiveSkillCheckSubScene(4, 3, Skill.Sneak, 3,
                                "Watch it! That's a fierce looking arachnid right there. Let's not disturb it."),
                        new PetSpiderCombatScene(5, 3))),
                new QuestScene("Albedan's Cage", List.of(
                        new SoloSkillCheckSubScene(3, 5, Skill.Labor, 12, "Is anyone strong enough to bend it open?"),
                        new SoloLockpickingSubScene(3, 6, 11, "Can the lock be picked?"),
                        new SoloSkillCheckSubScene(3, 7, Skill.Sneak, 12, "Oleg is sleeping nearby, can we snatch the key of his chain?"))),
                new QuestScene("Oleg's Fury", List.of(
                        new CombatOlegSubScene(2, 7)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPointWithoutDecision start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Seems like Albedan has gotten himself into trouble again. It's up to us to get him out.");
        QuestDecisionPoint cage = new QuestDecisionPoint(4, 5, List.of(
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(1), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(3).get(2), QuestEdge.VERTICAL)),
                "Oh no, That ogre has got Albedan locked up in a cage!");
        return List.of(start, cage);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(getFailEndingNode());
        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));

        scenes.get(2).get(0).connectFail(scenes.get(2).get(1));
        scenes.get(2).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(scenes.get(4).get(0));
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectFail(getFailEndingNode());
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(2).connectFail(scenes.get(4).get(0));
        scenes.get(3).get(2).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);

        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            new OutroEvent(model).doEvent(model);
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {
                result.add(new QuestBackground(new Point(x, y), woods, true));
            }
        }

        return result;
    }

    private static class PetSpiderCombatScene extends CombatSubScene {
        public PetSpiderCombatScene(int col, int row) {
            super(col, row, List.of(new PetSpiderEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Oleg's Pet Spider";
        }
    }

    private static class CombatOlegSubScene extends CombatSubScene {
        public CombatOlegSubScene(int col, int row) {
            super(col, row, List.of(new OlegOgreEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Oleg the ogre";
        }
    }

    private class IntroEvent extends QuestIntroEventState {
        public IntroEvent(Model model) {
            super(model);
        }

        @Override
        protected void runQuestIntro(Model model) {
            boolean albedanGender = getPortrait().getGender();
            println("You're browsing some stands at the square when...");
            model.getLog().waitForAnimationToFinish();
            ChildAppearance childApp = PortraitSubView.makeChildAppearance(Race.randomRace(), MyRandom.flipCoin());
            childApp.setBigMouth();
            String childName = "Errand " + MyStrings.capitalize(boyOrGirl(childApp.getGender()));
            showExplicitPortrait(model, childApp, childName);
            String misterOrMaam = model.getParty().getLeader().getGender() ? "ma'am" : "mister";
            portraitSay(MyStrings.capitalize(misterOrMaam) + " " + misterOrMaam + "!");
            leaderSay("What's the matter " + boyOrGirl(childApp.getGender()) + "?", FacialExpression.questioning);
            portraitSay("It's my master. " + heOrSheCap(albedanGender) + "'s, " + heOrShe(albedanGender) +
                    "'s, " + heOrShe(albedanGender)+ "'s...");
            println("The flustered child struggles to get the words out. " +
                    "Out of breath, " + heOrShe(childApp.getGender()) + " has apparently rushed to find you.");
            leaderSay("Calm down. Take a deep breath. Now, calmly, what's happened to your master? " +
                    "Who is your master anyway?", FacialExpression.questioning);
            portraitSay("I run errands for Albedan the Mage. But I also help " + himOrHer(albedanGender) + " sometimes when he goes collecting " +
                    " ingredients outside the walls. Today we were in a nearby grove and a big ogre appeared!");
            leaderSay("An ogre? They're usually not too aggressive, and they rarely come close to towns.");
            model.getLog().waitForAnimationToFinish();
            childApp.setEyebrowsDown();
            childApp.setNormalMouth();
            portraitSay("Oh this ogre is kind of special. His name is Oleg. " +
                    "I think Albedan made some kind of deal with him " +
                    "in the past, but Albedan never held up " + hisOrHer(albedanGender) + " end of the bargain. I think Oleg wanted revenge.");
            leaderSay("I see.");
            model.getLog().waitForAnimationToFinish();
            childApp.setEyebrowsUp();
            childApp.setMouthFrown();
            portraitSay("Oleg took Albedan on his back and shuffled off toward the Dank Swamp. You're an " +
                    "adventurer right? You've got to rescue " + himOrHer(albedanGender) + "!");
            leaderSay("Of course we will! But you know, we don't work for free right?", FacialExpression.relief);
            portraitSay("Albedan will pay you, I'm sure. Just get " + himOrHer(albedanGender) + " away from Oleg.");
            leaderSay("Don't worry, " + iOrWe() + "'ll take care of it.");
            model.getLog().waitForAnimationToFinish();
            childApp.setNormalMouth();
            portraitSay("Thank you, thank you, thank you, thank you, thank you, thank you!");
            model.getLog().waitForReturn();
            leaderSay("Six 'thank-you's? Kids...", FacialExpression.relief);
        }
    }

    private class OutroEvent extends DailyEventState {
        public OutroEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, getPortrait(), getProvider());
            portraitSay("Whoa. I'm glad you showed up. I'm certain Oleg would have fed me to that pet spider of his. " +
                    "How did you know I was here.", FacialExpression.questioning);
            leaderSay("Your little helper was looking out for you.");
            portraitSay("That little angel. I'll be sure to prepare a special treat when I get back to town.");
            leaderSay("For sure. My I ask what you did to upset Oleg?");
            portraitSay("I came across Oleg a while back when I was lost in this swamp. He offered to help " +
                    "me find my way out if I helped him woo a troll he's infatuated with.");
            randomSayIfPersonality(PersonalityTrait.romantic, List.of(model.getParty().getLeader()), "Aww, how sweet!");
            leaderSay("Uh-huh...");
            portraitSay("Since I'm not familiar with any love-related enchantments, " +
                    "I said I'd promise to teach him how to write romantic poetry over a series of lessons.");
            leaderSay("And then you never did?");
            portraitSay("Well, once I was back home I didn't want to risk going back to the swamp and get lost again." +
                    " Oleg apparently blames me for his subsequent romantic humiliation.");
            leaderSay("He wasn't able to woo the troll?");
            portraitSay("I'm afraid so. He said he tried writing the poetry himself. While I was in the cage, " +
                    "he through his poem at me. Here's a copy of what he wrote.");
            BookItem book = new OlegsPoemBook();
            println("You got " + book.getName() + ".");
            book.addYourself(model.getParty().getInventory());
            portraitSay("It's short, and uh... utterly awful.");
            model.getLog().waitForReturn();
            println("You return to town together with " + getProvider() + ".");
        }
    }
}
