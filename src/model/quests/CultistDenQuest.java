package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.PersonCombatLoot;
import model.enemies.CultistEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.AllRaces;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CultistDenQuest extends Quest {
    private static final int TIME_MINUTES = 15;
    private static final String INTRO = "A group of cultists are reportedly performing some dark ritual to resurrect " +
            "an other-wordly demigod. Stop them.\n" +
            "!! This is a timed quest. You have " + TIME_MINUTES + " minutes until the ritual is complete.";;
    private static final String ENDING = "You have cleared out the cultist den. The cleric thanks you for dealing with the cultist threat.";
    private static final CharacterAppearance PORTRAIT = PortraitSubView.makeRandomPortrait(Classes.TEMPLE_GUARD, AllRaces.ALL);

    public CultistDenQuest() {
        super("Cultist Den", "Cleric", QuestDifficulty.MEDIUM, 1, 15, 0, INTRO, ENDING);
    }

    @Override
    public int getTimeLimitSeconds() {
        return TIME_MINUTES * 60;
    }

    @Override
    public boolean clockEnabled() {
        return true;
    }

    @Override
    public boolean clockTimeOutFailsQuest() {
        return false;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Trapped Locked Door", List.of( // TODO: Diff 11
                    new SoloSkillCheckSubScene(1, 1, Skill.Security, 1, "Can somebody disable the trap mechanism?"),
                    new CollaborativeSkillCheckSubScene(3, 1, Skill.Security, 1, "Can we jimmie the lock?"))),
                new QuestScene("Cultists", List.of(
                        new CultistCombatSubScene(3, 2, 4)
                )),
                new QuestScene("More Cultists", List.of(
                        new CultistCombatSubScene(3, 3, 5))),
                new QuestScene("Room With Loot", List.of(
                        new LootRomSubScene(4, 4)
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qsp = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(1).get(0))),
                "Hmm. Maybe we don't have to rush into this?");
        return List.of(qsp);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1));
        scenes.get(0).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(1).connectSuccess(scenes.get(3).get(0));

        scenes.get(1).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(1).get(0).connectFail(getFailEndingNode());

        scenes.get(2).get(0).connectSuccess(scenes.get(3).get(0));
        scenes.get(2).get(0).connectFail(getFailEndingNode());

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }


    private static List<Enemy> makeCultistList(int noOfCultists) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < noOfCultists; ++i) {
            result.add(new CultistEnemy('A'));
        }
        return result;
    }

    private static class CultistCombatSubScene extends CombatSubScene {
        private final int noOfCultists;

        public CultistCombatSubScene(int col, int row, int noOfCultists) {
            super(col, row, makeCultistList(noOfCultists), true);
            this.noOfCultists = noOfCultists;
        }


        @Override
        protected String getCombatDetails() {
            return (noOfCultists>4?"More ":"") + "Cultists";
        }
    }
    private static final Sprite32x32 SPRITE = new Sprite32x32("lootsubscene", "quest.png", 0x26,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private static class LootRomSubScene extends QuestSubScene {
        public LootRomSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return "Loot";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Room with loot";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                    "Wow, these cultists sure have collected some loot!");
            model.getParty().randomPartyMemberSay(model, List.of("Yeah, but I see a lot of worthless junk too..."));
            state.print("There may be valuables in this room. Do you wish to attempt to search for loot? (Y/N) ");
            if (state.yesNoInput()) {
                int maxTimes = model.getParty().size() * 4;
                for (int i = 0; i < maxTimes; ++i) {
                    boolean result = model.getParty().doCollaborativeSkillCheck(model, state, Skill.Search, 12);
                    if (result) {
                        PersonCombatLoot loot = new PersonCombatLoot(model);
                        state.println("You have found something valuable, " + loot.getText() + ".");
                        loot.giveYourself(model.getParty());
                    }
                    state.print("There may still be something of value here. Do you wish to search? (Y/N) ");
                    if (!state.yesNoInput()) {
                        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Come on team, we can't spend more time here.");
                        break;
                    }
                }
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Come on team, we need to press on.");
            }
            return getSuccessEdge();
        }
    }
}
