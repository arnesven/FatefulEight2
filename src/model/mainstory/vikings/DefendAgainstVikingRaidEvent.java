package model.mainstory.vikings;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.CombatAdvantage;
import model.combat.Combatant;
import model.enemies.*;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.enemies.behaviors.NoAttackCombatBehavior;
import model.items.Equipment;
import model.items.accessories.LargeShield;
import model.items.clothing.LeatherArmor;
import model.items.clothing.StuddedJerkin;
import model.items.weapons.HuntersBow;
import model.items.weapons.Scepter;
import model.map.CastleLocation;
import model.map.locations.BogdownCastle;
import model.map.locations.VikingVillageLocation;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;
import view.combat.DungeonTheme;
import view.combat.MonastaryWallCombatTheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefendAgainstVikingRaidEvent extends DailyEventState {
    private CombatAdvantage advantage;
    private final int level;

    public DefendAgainstVikingRaidEvent(Model model, CombatAdvantage combatAdvantage, int trainingLevel) {
        super(model);
        advantage = combatAdvantage;
        level = trainingLevel;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You get up ready for another day of training the monks.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.PRI, "Sixth Monk");
        portraitSay("A longship! In the inlet! They're here!");
        leaderSay("How many of them?");
        portraitSay("Many, roughly fifty!");
        leaderSay("Okay, everybody calm down. Remember your training.");
        portraitSay("But, but, but... fifty!");
        leaderSay("fifty, a hundred, two hundred - it makes no difference now! Close the gates, draw your swords, " +
                "man the walls! This is it people!");
        println("The vikings rush up the hill toward the monastary. They seem a little bewildered to not find the " +
                "anybody out in the grounds, but continue rapidly toward the monastary gates. The gates however " +
                "are firmly shut and barricaded.");
        if (advantage == CombatAdvantage.Party) {
            println("The monks have quickly found there stations on the walls and start shooting arrows on the viking raiders.");
        } else {
            println("The monks seem a little confused, but with your direction they find their positions on the walls and" +
                    " start shooting arrows on the vikings.");
        }
        println("The vikings with bows return their fire, while the others get busy battering down the monastary gate with a " +
                "battering ram.");
        model.getLog().waitForAnimationToFinish();

        List<Enemy> enemies = makeEnemies();
        List<GameCharacter> reserves = makeAllies(4);
        CombatEvent combat = new CombatEvent(model, enemies, new MonastaryWallCombatTheme(reserves), false, advantage);
        List<GameCharacter> allies = makeAllies(16);
        combat.addAllies(allies);
        combat.setTimeLimit(4 + (advantage == CombatAdvantage.Party ? 1 : 0));
        combat.run(model);
        if (model.getParty().isWipedOut()) {
            return;
        }
        setCurrentTerrainSubview(model);
        if (MyLists.all(enemies, enemy -> enemy.isDead())) {
            raidIsOver(model);
            return;
        }
        println("The battering ram breaks the gate down and the vikings rush inside the monastary. " +
                "The monks start to panic.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.PRI, "Sixth Monk");
        portraitSay("We're done for! Flee, flee for your lives!");
        leaderSay("Quiet you! Everybody, hold your ground!");
        advantage = CombatAdvantage.Neither;
        if (model.getParty().doCollaborativeSkillCheck(model, this, Skill.Leadership, 14)) {
            println("The party manages to rally the monks and face the onslaught of marauders.");
        } else {
            println("The party shouts orders at the routed monks, but the vikings are on them " +
                    "before they can form ranks.");
            advantage = CombatAdvantage.Enemies;
        }

        enemies.removeIf(Combatant::isDead);
        MyLists.forEach(enemies, e -> e.setFortified(false));
        MyLists.forEach(MyLists.filter(enemies, e -> e.getAttackBehavior() instanceof NoAttackCombatBehavior),
                e -> e.setAttackBehavior(new MeleeAttackBehavior()));
        enemies.addAll(makeEnemyReinforcements(Math.min(8, Math.max(0, 32 - enemies.size()))));

        allies.removeIf(Combatant::isDead);
        while (allies.size() < 16 && !reserves.isEmpty()) {
            allies.add(reserves.removeFirst());
        }

        CombatEvent combat2 = new CombatEvent(model, enemies, new DungeonTheme(), false, advantage);
        combat2.addAllies(allies);
        combat2.run(model);
        if (model.getParty().isWipedOut()) {
            return;
        }
        raidIsOver(model);
    }

    private void raidIsOver(Model model) {
        println("With vikings being cut down left and right, they suddenly realize they're loosing the battle. " +
                "At once they start to turn and flee.");

        GainSupportOfVikingsTask task = VikingVillageLocation.getVikingTask(model);
        if (task != null) {
            task.setMonastaryDefended();
        }
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.PRI, "Sixth Monk");
        portraitSay("We did it! I, I can't believe it.");
        leaderSay("They'll think twice about coming back here.");
        portraitSay("Yes. But still. This whole ordeal has shown us how vulnerable we are. We must " +
                "keep up our combat training, and ensure we are always well prepared. How can we help others if " +
                "we ourselves are undone?");
        leaderSay("That is wise.");
        portraitSay("But how can we ever repay you? We have no gold to give, and as you can see, " +
                "there is not much else.");
        CastleLocation loc = model.getWorld().getCastleByName(BogdownCastle.CASTLE_NAME);
        leaderSay("Well, if you expand the ranks of your order, and train well, I would " +
                "be grateful if you could oppose " + loc.getLordTitle() + " " + loc.getLordName() + "'s forces. " +
                "I'm sure you will encounter them soon. If you haven't already.");
        portraitSay("Yes. They've been showing up here more frequently. Mostly just for seeking shelter, " +
                "but there has been much more activity from " + CastleLocation.placeNameToKingdom(loc.getPlaceName()) + ".");
        leaderSay("Things are very bad there. " + iOrWeCap() + " believe a dark power has taken hold of the regent. " +
                "It is " + myOrOur() + " mission to root it out.");
        portraitSay("That is a noble cause. We will support you in this.");
        leaderSay("That means a lot. Farewell friends.");
        portraitSay("Farewell.");
    }

    private List<GameCharacter> makeAllies(int amount) {
        List<GameCharacter> result = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            GameCharacter rando = GameState.makeRandomCharacter(level);
            rando.setClass(Classes.PRI);
            if (i >= amount / 2) {
                rando.setEquipment(new Equipment(new HuntersBow(), new StuddedJerkin(), null));
            } else {
                rando.setEquipment(new Equipment(new Scepter(), new LeatherArmor(), new LargeShield()));
            }
            result.add(rando);
        }
        return result;
    }

    private List<Enemy> makeEnemyReinforcements(int amount) {
        List<Enemy> list = new ArrayList<>();
        Enemy enm;
        for (int i = amount; i > 0; --i) {
            int dieRoll = MyRandom.rollD6();
            if (dieRoll < 3) {
                enm = new VikingSwordsmanEnemy('B');
            } else if (dieRoll < 5) {
                enm = new VikingAxeWielderEnemy('B');
            } else {
                enm = new VikingBerserkerEnemy('C');
            }
            list.add(enm);
        }
        return list;
    }

    public List<Enemy> makeEnemies() {
        List<Enemy> list = new ArrayList<>();
        Enemy enm;
        for (int i = 16; i > 0; --i) {
            if (i > 11) {
                enm = new VikingSwordsmanEnemy('B');
            } else if (i > 4) {
                enm = new VikingAxeWielderEnemy('B');
            } else {
                enm = new VikingBerserkerEnemy('C');
            }
            enm.setFortified(true);
            enm.setAttackBehavior(new NoAttackCombatBehavior());
            list.add(enm);
        }
        Collections.shuffle(list);
        for (int i = 8; i > 0; --i) {
            enm = new VikingArcherEnemy('A');
            enm.setFortified(true);
            list.add(enm);
        }
        return list;
    }
}
