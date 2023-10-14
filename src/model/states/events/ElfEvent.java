package model.states.events;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.enemies.SwordsmanEnemy;
import model.races.Race;
import model.races.WoodElf;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

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
            showRandomPortrait(model, Classes.SPY, Race.DARK_ELF, "Agent");
            changeClass(model, Classes.SPY, "n agent of a powerful organization. He offers to train you in the ways of spycraft, ");
        } else if (dieRoll <= 6) {
            showRandomPortrait(model, Classes.CAP, Race.DARK_ELF, "Swordsman");
            print(" swordsman who brags about his exploits and the gold he has made. Do you wish to challenge the swordsman? ");
            if (yesNoInput()) {
                List<Enemy> list = new ArrayList<>();
                list.add(new SwordsmanEnemy('A', Race.DARK_ELF));
                runCombat(list);
            }
        } else if (dieRoll <= 9) {
            CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.MAGE, Race.DARK_ELF);
            showExplicitPortrait(model, app, "Mage");
            print(" mage who offers to sell you a spells.");
            MageEvent event = new MageEvent(model, false, app);
            event.setPortraitSubView(this);
            event.doEvent(model);
        } else {
            adventurerWhoMayJoin(model, Race.DARK_ELF);
        }
    }

    private void woodElfEvent(Model model) {
        print("wood elf. This particular elf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" skilled archer who shows the party a few tricks with her a bow");
            ArcherEvent event = new ArcherEvent(model, false, PortraitSubView.makeRandomPortrait(Classes.MAR, Race.WOOD_ELF));
            event.doEvent(model);
        } else if (dieRoll <= 6) {
            CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.MERCHANT, Race.WOOD_ELF);
            showExplicitPortrait(model, app, "Merchant");
            print(" merchant. Do you wish to approach " + himOrHer(app.getGender()) + "? (Y/N) ");
            if (yesNoInput()) {
                MerchantEvent me = new MerchantEvent(model, false, app);
                me.setPortraitSubView(this);
                me.doEvent(model);
            }
        } else if (dieRoll <= 9) {
            println(" stranger.");
            new ThiefEvent(model).doEvent(model);
        } else {
            adventurerWhoMayJoin(model, Race.WOOD_ELF);
        }
    }

    private void highElfEvent(Model model) {
        print("high elf. This particular elf is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 3) {
            println(" priest.");
            showRandomPortrait(model, Classes.PRI, "Priest");
            PriestEvent pri = new PriestEvent(model, false);
            pri.setPortraitSubView(this);
            pri.doEvent(model);
        } else if (dieRoll <= 6) {
            println(" courier.");
            CourierEvent courier = new CourierEvent(model, false);
            courier.setRace(Race.HIGH_ELF);
            courier.doEvent(model);
        } else if (dieRoll <= 9) {
            showRandomPortrait(model, Classes.PAL, Race.HIGH_ELF, "Paladin");
            println(" a paladin.");
            new PaladinEvent(model).changeClass(model);
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
