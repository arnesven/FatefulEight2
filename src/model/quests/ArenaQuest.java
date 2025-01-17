package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.enemies.*;
import model.quests.scenes.CombatSubScene;
import model.races.Race;
import model.states.CombatEvent;
import model.states.QuestState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.subviews.TavernSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArenaQuest extends Quest {
    private static final String INTRO =
            "In a shady part of town, under a seedy bar, there's a fighting ring. The format " +
            "is one on one and to the death. Does he party have what it takes to rise to " +
            "the top and take down the house team?";
    private static final String ENDING =
            "The arena promoter grudgingly pays you your winnings.";

    private static final List<String> ANNOUNCER = List.of(
            "Ladies and gentlemen, please take your seats! You are in for a treat! Tonight a fresh new team of " +
            "challengers is taking on the house team. Ahh the excitement! As always we will give them a rough start! " +
            "Our brawler Groff, far off from mount Moff. Can these newcomers handle this brute and his furious mettle?",
            "Groff has been defeated! But don't worry, don't fret. The next fighter is deadly. She's the fastest bowman " +
            "you've seen! Our elven maid Gaelwyn, from the forests of Ophlia. Can these newcomers handle this devilish dead-eye?",
            "Gaelwyn has been defeated! My dear folks, may I say it. These new challengers have some spunk to them! But now, here " +
            "comes a fighter few have been able to handle. His flips, feints and ferocious techniques make him one of a kind! Give " +
            "it up for Helbo the halfling martial artist! He may be small, but remember, death sometimes come in small packages!",
            "I don't believe it! Helbo is down! What will stop this fighting team? Well, most of you know, that the best is yet to come! " +
            "Up next, we have a seasoned warrior, a woman of skill, a master-at-arms. She boasts of the experience of many wars and " +
            "she wields many types of weapons. Freya the veteran is ready to put these newcomers in their place!",
            "Freya finally met her match! Well you had a good haul newcomers, but I am afraid this is the end. Nobody, and I repeat, " +
            "nobody has every defeated our grandmaster Gorn. Gorn is tough. Gorn is sharp. Gorn is the meanest dwarf who's ever been born!" +
            "He'll smack you down so fast you won't know what hit you! Don't tell me I didn't warn you!");
    private static final Sprite STAIRS = new Sprite32x32("stairs", "world_foreground.png", 0x54,
            MyColors.DARK_GRAY, TavernSubView.FLOOR_COLOR, MyColors.BROWN);
    private static final List<QuestBackground> BG_SPRITES = makeBgSprites();
    public static final Sprite[] SPECTATORS = makeSpectators();

    private static final List<QuestBackground> DECORATIONS = makeDecorations();
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.CHARLATAN, Race.ALL);

    public ArenaQuest() {
        super("The Arena", "Arena Promoter", QuestDifficulty.HARD,
                new Reward(1, 225, 0), 0, INTRO, ENDING);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BG_SPRITES;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Orcish Brawler",
                        List.of(new SingleCharacterCombatSubScene(4, 2, new OrcishBrawlerEnemy('A')))),
                       new QuestScene("Elven Gladiator",
                               List.of(new SingleCharacterCombatSubScene(3, 3, new ElvenGladiatorEnemy('A')))),
                       new QuestScene("Halfling Martial Artist",
                               List.of(new SingleCharacterCombatSubScene(4, 4, new HalflingMartialArtist('A')))),
                       new QuestScene("Human Veteran",
                               List.of(new SingleCharacterCombatSubScene(3, 5, new HumanVeteranEnemy('A')))),
                       new QuestScene("Dwarven Grandmaster",
                               List.of(new SingleCharacterCombatSubScene(4, 6, new DwarvenGrandMasterEnemy('A')))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Let's take on the challenge of the Arena!"));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));
        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));
        scenes.get(3).get(0).connectSuccess(scenes.get(4).get(0));
        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }


    private static List<QuestBackground> makeBgSprites() {
        List<QuestBackground> result = new ArrayList<>();
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (row == 0) {
                    if (col == 0) {
                        result.add(new QuestBackground(new Point(col, row), STAIRS, true));
                    } else {
                        result.add(new QuestBackground(new Point(col, row), TavernSubView.WALL, true));
                    }
                } else if (row == 8) {
                    result.add(new QuestBackground(new Point(col, row), TavernSubView.SIDE_WALL, true));
                } else {
                    result.add(new QuestBackground(new Point(col, row), TavernSubView.FLOOR, true));
                }
            }
        }
        return result;
    }

    private static Sprite[] makeSpectators() {
        return new Sprite[]{
                new SpectatorSprite(Race.NORTHERN_HUMAN, MyColors.BLUE),
                new SpectatorSprite(Race.SOUTHERN_HUMAN, MyColors.RED),
                new SpectatorSprite(Race.WOOD_ELF, MyColors.GREEN),
                new SpectatorSprite(Race.HIGH_ELF, MyColors.DARK_GRAY),
                new SpectatorSprite(Race.DARK_ELF, MyColors.LIGHT_GRAY),
                new SpectatorSprite(Race.HALF_ORC, MyColors.TAN),
                new SpectatorSprite(Race.HALFLING, MyColors.GOLD),
                new SpectatorSprite(Race.DWARF, MyColors.BROWN)};
    }

    private static List<QuestBackground> makeDecorations() {
        Random random = new Random(431);
        List<QuestBackground> result = new ArrayList<>();
        String[] placement = new String[]{
          "        ",
          " xxx xxx",
          "xx    xx",
          "xx    xx",
          "xx    xx",
          "xx    xx",
          "xx    xx",
          "xxxx xxx",
          "        "};
        for (int y = 0; y < placement.length; ++y) {
            for (int x = 0; x < placement[0].length(); ++x) {
                if (placement[y].charAt(x) == 'x') {
                    result.add(new QuestBackground(new Point(x, y), SPECTATORS[random.nextInt(SPECTATORS.length)]));
                }
            }
        }
        return result;
    }

    private class SingleCharacterCombatSubScene extends CombatSubScene {
        private final Enemy enemy;
        private final int step;

        public SingleCharacterCombatSubScene(int col, int row, Enemy enemy) {
            super(col, row, List.of(enemy), false);
            this.enemy = enemy;
            this.step = row - 2;
        }


        @Override
        protected String getCombatDetails() {
            return enemy.getName();
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            // TODO: Fix this so you can't cheat with elixir.
            // Also, should be able to flee -> quest failed
            state.printQuote("Announcer", ANNOUNCER.get(step));

            state.print("Who will fight " + getCombatDetails() + "?");
            GameCharacter fighter = model.getParty().partyMemberInput(model, state, model.getParty().getLeader());

            List<GameCharacter> benchers = new ArrayList<>();
            benchers.addAll(model.getParty().getPartyMembers());
            benchers.remove(fighter);
            model.getParty().benchPartyMembers(benchers);
            CombatEvent combat = new CombatEvent(model, List.of(enemy), state.getCombatTheme(), false, CombatAdvantage.Neither);
            combat.run(model);
            model.getParty().unbenchAll();
            state.transitionToQuestView(model);
            if (combat.fled() || fighter.isDead()) {
                return new QuestEdge(ArenaQuest.this.getFailEndingNode());
            }
            setDefeated(true);
            return getSuccessEdge();
        }

        @Override
        public String getDetailedDescription() {
            return "Combat *";
        }
    }

    private static class SpectatorSprite extends LoopingSprite {
        public SpectatorSprite(Race race, MyColors shirtColor) {
            super("arenaspectator", "quest.png", 0x83 + (race.isShort()?2:0), 32);
            setFrames(2);
            setDelay(32);
            setColor1(MyColors.BLACK);
            setColor2(shirtColor);
            setColor3(race.getColor());
            if (shirtColor.hashCode() % 2 == 0) {
                setCurrentFrame(1);
            }
        }
    }
}
