package model.quests.scenes;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.quests.QuestEdge;
import model.quests.QuestSubScene;
import model.states.CombatEvent;
import model.states.GameState;
import model.states.QuestState;
import model.states.SpellCastException;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CombatSubScene extends QuestSubScene {

    private static final Sprite32x32 SPRITE = new Sprite32x32("combatsubscene", "quest.png", 0x03,
            MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.BROWN);
    private List<Enemy> enemies;
    private final boolean fleeingEnabled;
    private boolean defeated = false;
    private int timeLimit = 0;
    private boolean surprise = false;

    public CombatSubScene(int col, int row, List<Enemy> enemies, boolean fleeingEnabled) {
        super(col, row);
        this.enemies = enemies;
        this.fleeingEnabled = fleeingEnabled;
    }

    public CombatSubScene(int col, int row, List<Enemy> enemies) {
        this(col, row, enemies, false);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        if (!hasBeenDefeated()) {
            Sprite enemyAvatar = enemies.get(0).getAvatar();
            int xOff = 0;
            if (enemyAvatar.getWidth() > SPRITE.getWidth()) {
                xOff = (SPRITE.getWidth() - enemyAvatar.getWidth()) / 8 / 2;
            }
            model.getScreenHandler().register(enemyAvatar.getName(), new Point(xPos+xOff, yPos), enemyAvatar, 1);
        }
    }

    protected boolean hasBeenDefeated() {
        return defeated;
    }
    public void setDefeated(boolean d) {
        defeated = d;
    }

    @Override
    public final String getDescription() {
        return "Combat " + getCombatDetails();
    }

    protected abstract String getCombatDetails();

    @Override
    public QuestEdge run(Model model, QuestState state) {
        acceptAllSpells(model);
        do {
            state.print("The party encounters " + getCombatDetails() + "! Press enter to continue.");
            try {
                state.waitForReturn(true);
                break;
            } catch (SpellCastException spe) {
                QuestEdge edge = tryCastSpell(model, state, spe);
                if (edge != null) {
                    unacceptAllSpells(model);
                    return edge;
                }
            }
        } while (true);
        unacceptAllSpells(model);

        CombatEvent combat = new CombatEvent(model, getEnemies(), state.getCombatTheme(), fleeingEnabled,
                surprise ? CombatAdvantage.Party : CombatAdvantage.Neither);
        List<GameCharacter> allies = getAllies();
        if (!allies.isEmpty()) {
            combat.addAllies(allies);
        }
        if (timeLimit > 0) {
            combat.setTimeLimit(timeLimit);
        }
        GameCharacter gc = setTemporaryLeader(model, state);
        combat.run(model);
        if (!model.getParty().isWipedOut()) {
            if (!gc.isDead()) {
                model.getParty().setLeader(gc);
            }
            state.transitionToQuestView(model);
        }
        if (combat.fled() || model.getParty().isWipedOut()) {
            return getFailEdge();
        }
        if (combat.didTimeOut()) {
            if (timeLimit == 0) { // Quest timed out from other reason => Escape spell
                return new QuestEdge(state.getQuest().getFailEndingNode());
            }
        } else {
            defeated = true;
        }
        return getSuccessEdge();
    }

    private GameCharacter setTemporaryLeader(Model model, GameState state) {
        GameCharacter leader = model.getParty().getLeader();
        if (model.getParty().getBench().contains(leader)) {
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (!model.getParty().getBench().contains(gc)){
                    state.println(gc.getName() + " is the temporary leader of the party.");
                    model.getParty().setLeader(gc);
                    break;
                }
            }
        }
        return leader;
    }

    public void setTimeLimit(int limit) {
        this.timeLimit = limit;
    }



    protected List<GameCharacter> getAllies() {
        return new ArrayList<>();
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    protected void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setSurpriseAttack(boolean surprise) {
        this.surprise = surprise;
    }

    @Override
    public String getDetailedDescription() {
        int threatSum = 0;
        for (Enemy e : getEnemies()) {
            threatSum += e.getThreat();
        }
        String diffStr = "E";
        if (threatSum > 120) {
            diffStr = "H";
        } else if (threatSum > 50) {
            diffStr = "M";
        }
        return "Combat " + diffStr;
    }
}
