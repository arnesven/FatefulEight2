package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.special.WitchKingAppearance;
import model.classes.Classes;
import model.combat.loot.SingleItemCombatLoot;
import model.enemies.Enemy;
import model.enemies.RisenWarriorEnemy;
import model.enemies.UltimateAdversaryEnemy;
import model.items.spells.TeleportSpell;
import model.items.weapons.AxeOfDismemberment;
import model.items.weapons.LightningJavelins;
import model.items.weapons.SwordOfVigor;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.fatue.FortressAtUtmostEdgeState;
import view.LogView;
import view.combat.DungeonTheme;

import java.util.ArrayList;
import java.util.List;

public class ConfrontUltimateAdversaryEvent extends DailyEventState {
    private CharacterAppearance ultimateAdversaryPortrait = new UltimateAdversaryAppearance();

    public ConfrontUltimateAdversaryEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You take the staff and put it in the hole in front of the large stone door. It fits perfectly into the hole." +
                " The light which shines down from the cavern ceiling passes through the crystal in the top piece of the staff. " +
                "The refracted light then passes through a tiny slot in the center of the door.");
        println("Suddenly the ground shakes and the large door starts to move to the side, revealing a dark chamber inside.");
        leaderSay("Ominous...");
        println("You retrieve the staff and carefully enter the room.");
        println("As your torches illuminate the room you can see that it is filled with stone statues, warriors of a bygone age. " +
                "In the center of the room a large stone sarcophagus stands. As you approach the lid of the sarcophagus suddenly slides off " +
                "and a dark figure rises from it.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, ultimateAdversaryPortrait, "Ultimate Adversary");
        leaderSay("Uh-oh");
        portraitSay("At last..."); // TODO: Add special dialog for Witch King
        leaderSay("Hey there... sorry... did we wake you up?");
        portraitSay("At last, some mortal fool has come to release me...");
        leaderSay("Actually we just thought there was some loot in here. We're not here to set you free.");
        portraitSay("But you have already done so. Ah, and I see that you have also restored the Staff of Deimos. " +
                "With it, all the realms of this world shall be mine.");
        leaderSay("I guess we'll just have to refrain from giving it to you then.");
        portraitSay("Hehehehe... so sure of yourself... Well I have no qualms about taking it off your corpse.");
        leaderSay("Oh yeah. You and what army?");
        println("The Ultimate Adversary smiles. Then extends his hands and a green light beams from each one onto the surrounding statues. " +
                "Within moments, the come alive. With drawn weapons they advance on the party.");
        leaderSay("Looks like we're in for a bit of a fight people... Get ready!");
        model.getLog().waitForReturn();
        UltimateAdversaryEnemy ultimateAdversary = new UltimateAdversaryEnemy('A');
        List<Enemy> enemies = new ArrayList<>(List.of(new RisenWarriorEnemy('B'), new RisenWarriorEnemy('B'),
                ultimateAdversary, new RisenWarriorEnemy('B'), new RisenWarriorEnemy('B')));
        do {
            CombatEvent combatEvent = new CombatEvent(model, enemies, new DungeonTheme(), true, false);
            combatEvent.setTimeLimit(3);
            combatEvent.run(model);
            setCurrentTerrainSubview(model);
            if (ultimateAdversary.isDead()) {
                combatSuccess(model);
                break;
            } else if (combatEvent.haveFledCombat()) {
                println("Somehow you manage to get away from the fray. You hear the Ultimate Adversary cackling " +
                        "behind you as you leave in a hurry.");
                break;
            }
            println("The Ultimate Adversary once again holds up his hands. Suddenly more of his henchmen come to life and rushes " +
                    "to his side to defend him!");
            ultimateAdversary.removeCombatConditions();
            enemies.add(0, new RisenWarriorEnemy('B'));
            enemies.add(0, new RisenWarriorEnemy('B'));
            enemies.add(new RisenWarriorEnemy('B'));
            enemies.add(new RisenWarriorEnemy('B'));
            print("Press enter to continue.");
            waitForReturn();
        } while (true);
    }

    private void combatSuccess(Model model) {
        leaderSay("Ugh... that was tough.");
        if (model.getParty().size() > 1) {
            GameCharacter rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(rando, "Is he dead?");
            leaderSay("Yes... or, I'm not sure he was living before... He looked about a thousand years old.");
            partyMemberSay(rando, "He certainly put up a good fight for his age.");
            leaderSay("Let's get out of here before more of his minions come to life.");
            partyMemberSay(rando, "Wait... there's some stuff over here...");
            new SingleItemCombatLoot(new SwordOfVigor()).giveYourself(model.getParty());
            new SingleItemCombatLoot(new LightningJavelins()).giveYourself(model.getParty());
            new SingleItemCombatLoot(new AxeOfDismemberment()).giveYourself(model.getParty());
            FortressAtUtmostEdgeState.setFatueCleared(model);
            println(LogView.GOLD_COLOR + "Congratulations! You have defeated the Ultimate " +
                    "Adversary in the Fortress at the Utmost Edge!" + LogView.DEFAULT_COLOR);
            // TODO: Add some nice unique items for loot.
        }
    }

    private static class UltimateAdversaryAppearance extends WitchKingAppearance {
        public UltimateAdversaryAppearance() {
            setClass(Classes.SOR);
        }
    }
}
