package model.states.events;

import model.Model;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.SwordsmanEnemy;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class ElfEvent extends DailyEventState {
    public ElfEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        int dieRoll = MyRandom.rollD6();
        print("The party encounters a ");
        if (3 <= dieRoll && dieRoll <= 4 ) {
            woodElfEvent(model);
        } else if (dieRoll > 4) {
            highElfEvent(model);
        } else {
            darkElfEvent(model);
        }
    }

    private void darkElfEvent(Model model) {
        print("dark elf. This particular elf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            changeClass(model, Classes.SPY, "n agent of a powerful organization. He offers to train you in the ways of spycraft, ");
        } else if (dieRoll <= 6) {
            print(" swordsman who brags about his exploits and the gold he has made. Do you wish to challenge the swordsman? ");
            if (yesNoInput()) {
                List<Enemy> list = new ArrayList<>();
                list.add(new SwordsmanEnemy('A', Race.DARK_ELF));
                runCombat(list);
            }
        } else if (dieRoll <= 9) {
            print(" mage who offers to sell you a spell for 15 gold. Do you accept? ");
            if (yesNoInput()) {
                // TODO: Buy a spell
                println("You buy a spell from the dark elf mage.");
            }
        } else {
            adventurerWhoMayJoin(model, Race.DARK_ELF);
        }
    }

    private void woodElfEvent(Model model) {
        print("wood elf. This particular elf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            changeClass(model, Classes.MAR,
                    " skilled archer who shows the party a few tri with her a bow. He offers to train you in the ways of marksmanship, ");
        } else if (dieRoll <= 6) {
            print(" merchant. Do you wish to trade with her? ");
            if (yesNoInput()) {
                MerchantEvent me = new MerchantEvent(model, false);
                me.doEvent(model);
            }
        } else if (dieRoll <= 9) {
            println(" thief."); // TODO: Implement Thief event.
        } else {
            adventurerWhoMayJoin(model, Race.WOOD_ELF);
        }
    }

    private void highElfEvent(Model model) {
        print("high elf. This particular elf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" priest.");
            new PriestEvent(model, false).doEvent(model);
        } else if (dieRoll <= 6) {
            println(" courier.");
            new CourierEvent(model, false).doEvent(model);
        } else if (dieRoll <= 9) {
            println(" a paladin."); // TODO: Implement Palading event.
        } else {
            adventurerWhoMayJoin(model, Race.HIGH_ELF);
        }
    }


    private void changeClass(Model model, CharacterClass cls, String text) {
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, cls);
        print(text);
        changeClassEvent.areYouInterested(model);
    }


}
