package model.states.events;

import model.Model;
import model.characters.BungoDarkwood;
import model.characters.GameCharacter;
import model.characters.MiklosAutumntoft;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import model.enemies.SoldierEnemy;
import model.items.Equipment;
import model.items.accessories.HeavyRing;
import model.items.accessories.LeatherCap;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.clothing.OutlawArmor;
import model.items.clothing.ScaleArmor;
import model.items.weapons.*;
import model.races.AllRaces;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class OrcRaidEvent extends DailyEventState {
    private boolean didFlee = false;
    private CombatEvent combat;

    public OrcRaidEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Suddenly you hear a bell clamoring. You hear shouting and people start " +
                "running into their houses, shutting doors and windows.");
        showRandomPortrait(model, Classes.CONSTABLE, "Militia");
        portraitSay(model, "Orc Raid! Everybody, take up arms to defend our town!");
        print("Do you help to defend the town? (Y/N) ");
        if (yesNoInput()) {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                enemies.add(new OrcWarrior('A'));
            }
            this.combat = new CombatEvent(model, enemies, model.getCurrentHex().getCombatTheme(), true, false);
            List<GameCharacter> allies = new ArrayList<>();
            for (int i = 0; i < 4; ++i) {
                GameCharacter militia = generateMilitia();
                allies.add(militia);
            }
            combat.addAllies(allies);
            combat.run(model);
            // TODO: some kind of reward.
            println("You have fended off the orcish marauders. There is much confusion " +
                    "in the aftermath but there doesn't seem to be many injured or any damage to the town.");
        } else {
            println("You escape from town.");
            this.didFlee = true;
        }
    }

    private GameCharacter generateMilitia() {
        int raceIndex = MyRandom.randInt(AllRaces.allRaces.length);
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 3) {
            return new GameCharacter("Militia", "Swordsman", AllRaces.allRaces[raceIndex], Classes.CAP, new BungoDarkwood(),
                    new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                    new Equipment(new Longsword(), new LeatherArmor(), new SkullCap()));
        }
        if (dieRoll < 6) {
            return new GameCharacter("Militia", "Spearman", AllRaces.allRaces[raceIndex], Classes.CAP, new BungoDarkwood(),
                    new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                    new Equipment(new Spear(), new LeatherArmor(), new SkullCap()));
        }
        if (dieRoll < 9) {
            return new GameCharacter("Militia", "Bowman", AllRaces.allRaces[raceIndex], Classes.MAR, new BungoDarkwood(),
                    new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                    new Equipment(new ShortBow(), new OutlawArmor(), new LeatherCap()));
        }
        return new GameCharacter("Militia", "Elite", AllRaces.allRaces[raceIndex], Classes.PAL, new BungoDarkwood(),
                new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                new Equipment(new Glaive(), new ScaleArmor(), new HeavyRing()));

    }

    @Override
    public boolean haveFledCombat() {
        return didFlee || (combat != null && combat.haveFledCombat());
    }
}
