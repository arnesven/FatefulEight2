package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.Collection;
import java.util.List;

public class CowardlyCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC1), MyColors.YELLOW, MyColors.BLACK, MyColors.CYAN);
    private final List<Enemy> team;

    public CowardlyCondition(List<Enemy> enemies) {
        super("Cowardly", "CWD");
        this.team = enemies;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        super.endOfCombatRoundTrigger(model, state, comb);
        if (state instanceof CombatEvent) {
            if (goodGuysHasTheAdvantage(model, team, ((CombatEvent) state).getAllies())) {
                state.println(comb.getName() + " flees from combat!");
                ((CombatEvent) state).retreatEnemy(comb);
            }
        }
    }

    public static boolean goodGuysHasTheAdvantage(Model model, List<Enemy> enemyTeam, List<GameCharacter> allies) {
        return calculatePartyCombatStrength(model, allies) > calculateBadGuyCombatStrength(enemyTeam);
    }

    private static int calculateBadGuyCombatStrength(List<Enemy> team) {
        int badGuyCombatPower = 0;
        for (Enemy e : team) {
            badGuyCombatPower += e.getThreat();
        }
        return badGuyCombatPower;
    }

    private static int calculatePartyCombatStrength(Model model, List<GameCharacter> allies) {
        int goodGuyCombatPower = 0;
        for (GameCharacter gc : allies) {
            if (!gc.isDead()) {
                goodGuyCombatPower += gc.getCharacterStrength();
            }
        }
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!model.getParty().getBench().contains(gc) && !gc.isDead()) {
                goodGuyCombatPower += gc.getCharacterStrength();
            }
        }
        return goodGuyCombatPower;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "An enemy with the Cowardly condition will flee from combat if " +
                        "the enemy team is less powerful than the party and its allies.");
    }
}
