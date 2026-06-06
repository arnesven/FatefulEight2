package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.FacialExpression;
import model.characters.preset.DeniseBoyd;
import model.characters.GameCharacter;
import model.characters.appearance.DefaultAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.states.events.GeneralInteractionEvent;
import sound.BackgroundMusic;
import util.MyTriplet;
import view.combat.TownCombatTheme;
import model.enemies.InterloperEnemy;
import model.items.spells.FireworksSpell;
import model.items.spells.Spell;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;
import view.subviews.PortraitSubView;
import view.subviews.TownSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnsuspectingLoversQuest extends Quest {

    private static final String text = "Jason and Tamara are in love with " +
            "each other, but neither knows about the " +
            "other's feelings. Their parents want a " +
            "good matchmaker to set the mood and " +
            "remove sleazy interlopers. Finally, a " +
            "perfect date will seal their love forever!";

    private static final String endText = "Jason and Tamara both thank you for bringing them together. What a happy ending!";
    private static final Race JASONS_RACE = Race.SOUTHERN_HUMAN;
    private static final Race TAMARAS_RACE = Race.WOOD_ELF;
    private static List<QuestBackground> bgSprites = makeBgSprites();

    public UnsuspectingLoversQuest() {
        super("Unsuspecting Lovers", "Jason and Tamara's parents", QuestDifficulty.EASY,
                new Reward(125, 50), 0, text, endText);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "You fixed up the lovebirds Jason and Tamara.");
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new TownCombatTheme();
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.gentleMemory;
    }

    @Override
    public QuestIntroEventState getIntroEvent(Model model) {
        return new IntroEvent(model);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Confessions",
                List.of(new CollaborativeSkillCheckSubScene(0, 1, Skill.Persuade, 8,
                        "Okay, first step, get these two talking..."))),
                new QuestScene("Remove Interloper",
                        List.of(new SoloSkillCheckSubScene(5, 2, Skill.Persuade, 10,
                                "Perhaps we can persuade him to direct his ardor elsewhere."),
                                new ReducePartyRepCombatSubScene(4, 2))),
                new QuestScene("Date Cuisine",
                        List.of(new CollaborativeSkillCheckSubScene(3, 4, Skill.Survival, 10,
                                "I'm sure we can whip something up ourselves."),
                                new PayGoldSubScene(4, 4, 5,
                                        "We should probably cater."))),
                new QuestScene("Date Entertainment",
                        List.of(new CollaborativeSkillCheckSubScene(2, 7, Skill.Entertain, 10,
                                "How about a love song?"),
                                new PayGoldSubScene(3, 7, 5, "Can we hire a troubadour?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qd1 = new QuestDecisionPoint(2, 0,
                List.of(new QuestEdge(scenes.get(1).get(0)),
                        new QuestEdge(scenes.get(1).get(1))),
                "Now, how to deal with this meddlesome interloper.");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(4, 3,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(1))),
                "For a romantic dinner, the entrees must be supreme!");
        QuestDecisionPoint qd3 = new QuestDecisionPoint(3, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1))),
                        "To set the mood, some proper entertainment is required.");
        qd3.addSpellCallback(new FireworksSpell().getName(), new SpellCallback() {
            @Override
            public QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster) {
                state.println(caster.getFirstName() + " conjures up a magical fireworks display!");
                return new QuestEdge(getSuccessEndingNode());
            }
        });

        DecorativeJunction jason = new SpriteDecorativeJunction(7, 0,
                Classes.None.getAvatar(JASONS_RACE, new DefaultAppearance()), "Jason");

        DecorativeJunction tamara = new SpriteDecorativeJunction(6, 6,
                Classes.PRI.getAvatar(TAMARAS_RACE, new DeniseBoyd()), "Tamara");

        return List.of(new PauseQuestJunction(0, 0, new QuestEdge(scenes.get(0).get(0)),
                "We'll get these two lovebirds together in no time."),
                qd1, qd2, qd3, jason, tamara);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(3));
        scenes.get(2).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    private static List<QuestBackground> makeBgSprites() {
        List<QuestBackground> list = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            list.add(new QuestBackground(new Point(col, 0), TownSubView.STREET, true));
        }
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 8; col++) {
                list.add(new QuestBackground(new Point(col, row), TownSubView.STREET, false));
            }
        }
        Sprite townHouse = new Sprite32x32("townhousequest", "quest.png", 0x50,
                TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.CYAN);
        list.add(new QuestBackground(new Point(1, 0), townHouse, false));
        list.add(new QuestBackground(new Point(3, 1), townHouse, false));
        list.add(new QuestBackground(new Point(2, 3), townHouse, false));
        list.add(new QuestBackground(new Point(5, 4), townHouse, false));
        list.add(new QuestBackground(new Point(6, 4), townHouse, false));
        list.add(new QuestBackground(new Point(5, 5), townHouse, false));
        list.add(new QuestBackground(new Point(6, 5), townHouse, false));
        list.add(new QuestBackground(new Point(2, 5), townHouse, false));
        return list;
    }

    private static class ReducePartyRepCombatSubScene extends CombatSubScene {
        public ReducePartyRepCombatSubScene(int col, int row) {
            super(col, row, List.of(new InterloperEnemy('A')), true);
        }

        @Override
        protected String getCombatDetails() {
            return "an interloper infatuated with Tamara (gain Notoriety)";
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            List<Enemy> enemies = getEnemies();
            QuestEdge qe = super.run(model, state);
            if (enemies.get(0).isDead()) {
                GeneralInteractionEvent.addMurdersToNotoriety(model, state, 1);
            } else {
                GeneralInteractionEvent.addAssaultsToNotoriety(model, state, 1);
            }
            return qe;
        }
    }

    private class IntroEvent extends QuestIntroEventState {
        private final AdvancedAppearance jasonsFather;
        private final AdvancedAppearance tamarasMother;

        public IntroEvent(Model model) {
            super(model);
            this.jasonsFather = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, false);
            this.tamarasMother = PortraitSubView.makeRandomPortrait(Classes.None, Race.WOOD_ELF, true);
        }

        @Override
        protected void runQuestIntro(Model model) {
            println("You are just about to turn in for the night when two people approach you.");

            showFather();
            portraitSay("Excuse me, could we have a minute of your time?");
            leaderSay("Uh, yes. How can " + iOrWe() + " be of service?");
            showMother();
            portraitSay("The word is going around about you. That you are quite capable, up for any task?");
            leaderSay("That's correct! " + getCompanyName() + " can get it done. What seems to be the trouble folks? " +
                    "A monster that needs slaying? Some bandits that need to be disposed of? Somebody gone missing?",
                    FacialExpression.wicked);
            showFather();
            portraitSay("Oh no no. It's nothing like that. It's our kids...");
            showMother();
            portraitSay("They need some help...");
            leaderSay("Yes...?");
            showFather();
            portraitSay("Well, we feel they need a little match making. They are obviously in love with each other " +
                    "but they just don't seem to be brave enough to take that next step.");
            randomPersonalityNonLeaderComment(List.of(
                    new MyTriplet<>(PersonalityTrait.romantic, "That's so sweet. Of course we'll help", FacialExpression.relief),
                    new MyTriplet<>(PersonalityTrait.cold, "This is not our job. To little skull bashing.", FacialExpression.disappointed),
                    new MyTriplet<>(PersonalityTrait.benevolent, "This could be a gould change of scene for us. An opportunity to do some good.", FacialExpression.relief),
                    new MyTriplet<>(PersonalityTrait.jovial, "Just toss them in a hot spring together. That should get things cooking!", FacialExpression.relief)));
            leaderSay("Why don't you do it yourselves?", FacialExpression.questioning);
            showMother();
            portraitSay("We've tried! Subtly at first, but they were impervious to our guidance.", FacialExpression.sad);
            showFather();
            portraitSay("When we tried more direct interference... Well, it hasn't been appreciated.", FacialExpression.sad);
            leaderSay("I see the dilemma. What are their names?", FacialExpression.questioning);
            showMother();
            portraitSay("Tamara and Jason. They live here in town. Will you try to fix them up? They're already good friends, " +
                    "but they'd make a really cute couple!", FacialExpression.relief);
            leaderSay(iOrWe() + "'ll look into it, but " + iOrWe() + " can't promise anything. These things tend to " +
                    "be more complicated than they look.");
            portraitSay("Indeed, there's also the matter of Bobby.");
            showFather();
            portraitSay("*Sigh*, yes Bobby.", FacialExpression.disappointed);
            leaderSay("Who's Bobby?");
            showMother();
            portraitSay("Tamara's ex-boyfriend. He was never any good to her. He's a rascal who lives here in town. " +
                    "Fancies himself a poet. Tamara fell for his boyish charms and suggestive rhymes when she was younger. " +
                    "But she's moved on.", FacialExpression.sad);
            showFather();
            portraitSay("He on the other hand, doesn't seem to understand that it's over. It does indeed complicate things.", FacialExpression.disappointed);
            leaderSay("I think " + iOrWe() + " can probably get through to him.");
            portraitSay("That's good to hear. Give it your best shot. If you can't get Jason and Tamara together, " +
                    "than perhaps it's fate that they shall remain friends.", FacialExpression.relief);
            model.getLog().waitForAnimationToFinish();
            println("The two parents walk away.");
            randomSayIfPersonality(PersonalityTrait.prudish, List.of(model.getParty().getLeader()),
                    "Parents fixing up their kids? Embarrassing!");
        }

        private void showMother() {
            getModel().getLog().waitForAnimationToFinish();
            showExplicitPortrait(getModel(), tamarasMother, "Tamara's Father");
        }

        private void showFather() {
            getModel().getLog().waitForAnimationToFinish();
            showExplicitPortrait(getModel(), jasonsFather, "Jason's Father");
        }
    }
}
