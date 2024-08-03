package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.RandomAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.HeavyRing;
import model.items.accessories.LeatherCap;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.clothing.OutlawArmor;
import model.items.clothing.ScaleArmor;
import model.items.potions.Potion;
import model.items.spells.Spell;
import model.items.weapons.*;
import model.map.UrbanLocation;
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
        portraitSay("Orc Raid! Everybody, take up arms to defend our town!");
        randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                "We must protect these innocent people!");
        randomSayIfPersonality(PersonalityTrait.cold, List.of(model.getParty().getLeader()),
                "Why should we stick our necks out for these people? Let's just get out of town while there's still time.");
        randomSayIfPersonality(PersonalityTrait.calm, List.of(model.getParty().getLeader()),
                "Let's just remain calm and try to keep our heads, okay?");
        print("Do you help to defend the town? (Y/N) ");
        if (yesNoInput()) {
            List<Enemy> enemies = new ArrayList<>();
            int numberOfOrcs = MyRandom.randInt(5, 9);
            for (int i = 0; i < numberOfOrcs; ++i) {
                enemies.add(new OrcWarrior('A'));
            }
            this.combat = new CombatEvent(model, enemies, model.getCurrentHex().getCombatTheme(), true, CombatAdvantage.Neither);
            List<GameCharacter> allies = new ArrayList<>();
            int numberOfMilitias = MyRandom.randInt(2, 4);
            for (int i = 0; i < numberOfMilitias; ++i) {
                GameCharacter militia = generateMilitia();
                allies.add(militia);
            }
            combat.addAllies(allies);
            combat.run(model);
            if (combat.fled()) {
                return;
            }
            if (model.getParty().isWipedOut()) {
                return;
            }
            println("You have fended off the orcish marauders. There is much confusion " +
                    "in the aftermath but there doesn't seem to be many injured or any damage to the town.");
            showRandomPortrait(model, Classes.CONSTABLE, "Militia");
            portraitSay("Thank you for defending our town from those horrible beasts.");
            randomSayIfPersonality(PersonalityTrait.narcissistic, new ArrayList<>(),
                    "Oh it was nothing, I barely broke a sweat!");
            portraitSay("Please accept this equipment as a small token of our gratitude.");
            int numberOfItems = MyRandom.randInt(3, 5);
            for (int i = 0; i < numberOfItems; ++i) {
                Item it;
                do {
                    it = model.getItemDeck().getRandomItem();
                } while (it instanceof Spell || it instanceof WandWeapon || it instanceof Potion);
                println("The party receives " + it.getName() + ".");
                model.getParty().getInventory().addItem(it);
            }
            if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
                UrbanLocation loc = (UrbanLocation) model.getCurrentHex().getLocation();
                portraitSay("Oh, and I was supposed to tell you. " + loc.getLordName() + " has requested your presence. " +
                        "You should visit Town Hall while you're in town.");
                model.getParty().addSummon(loc);
            }
        } else {
            println("You escape from town.");
            this.didFlee = true;
        }
    }

    private GameCharacter generateMilitia() {
        int raceIndex = MyRandom.randInt(AllRaces.allRaces.length);
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 3) {
            return new GameCharacter("Militia", "Swordsman", AllRaces.allRaces[raceIndex], Classes.CAP, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                    Classes.NO_OTHER_CLASSES,
                    new Equipment(new Longsword(), new LeatherArmor(), new SkullCap()));
        }
        if (dieRoll < 6) {
            return new GameCharacter("Militia", "Spearman", AllRaces.allRaces[raceIndex], Classes.CAP, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                    Classes.NO_OTHER_CLASSES,
                    new Equipment(new Spear(), new LeatherArmor(), new SkullCap()));
        }
        if (dieRoll < 9) {
            return new GameCharacter("Militia", "Bowman", AllRaces.allRaces[raceIndex], Classes.MAR, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                    Classes.NO_OTHER_CLASSES,
                    new Equipment(new ShortBow(), new OutlawArmor(), new LeatherCap()));
        }
        return new GameCharacter("Militia", "Elite", AllRaces.allRaces[raceIndex], Classes.PAL, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Glaive(), new ScaleArmor(), new HeavyRing()));
    }

    @Override
    public boolean haveFledCombat() {
        return didFlee || (combat != null && combat.fled());
    }
}
