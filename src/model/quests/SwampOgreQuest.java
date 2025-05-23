package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.OlegOgreEnemy;
import model.enemies.PetSpiderEnemy;
import model.items.spells.ErodeSpell;
import model.items.spells.HarmonizeSpell;
import model.items.spells.Spell;
import model.quests.scenes.*;
import model.races.Race;
import model.states.QuestState;
import sound.BackgroundMusic;
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
                new Reward(1, 175), 2, INTRO, ENDING);
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
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
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
}
