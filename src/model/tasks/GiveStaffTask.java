package model.tasks;

import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.items.weapons.BluntWeapon;
import model.items.weapons.Weapon;
import model.map.UrbanLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GiveStaffTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public GiveStaffTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"I would really like to give you a tour of the castle, but my bones have" +
                " become so weak with age. If only I had a staff to lean on.\"");
        Weapon staff = findStaff(model);
        if (staff == null) {
            println("Unfortunately you have no staffs to spare.");
            println(location.getLordName() + ": \"Oh, my poor old bones...\"");
        } else {
            print("Nobody in your party is currently using the " + staff.getName() +
                    ", are you willing to give it to the " + location.getLordTitle() + "? (Y/N) ");
            if (yesNoInput()) {
                println("You hand over the " + staff.getName() + " to " + location.getLordName() + ".");
                model.getParty().getInventory().remove(staff);
                summon.increaseStep();
                println(location.getLordName() + ": \"Heavens bless you! Now I can finally move around again. " +
                        "Why don't we take that tour of the castle now? There is so much to see and tell!\"");
                println("Each party member gains 50 experience!");
                model.getLog().waitForAnimationToFinish();
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    model.getParty().giveXP(model, gc, 50);
                }
            } else {
                println(location.getLordName() + ": \"Oh, my poor old bones...\"");
            }
        }
    }

    private Weapon findStaff(Model model) {
        List<Weapon> candidates = new ArrayList<>();

        for (Weapon w : model.getParty().getInventory().getWeapons()) {
            if (w.getName().contains("Staff")) {
                candidates.add(w);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }
        Collections.sort(candidates, new Comparator<Weapon>() {
            @Override
            public int compare(Weapon weapon, Weapon t1) {
                return weapon.getCost() - t1.getCost();
            }
        });
        return candidates.get(0);
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs a staff.";
    }
}
