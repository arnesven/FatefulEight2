package model.horses;

import model.Model;
import model.characters.GameCharacter;
import model.races.AllRaces;
import util.MyRandom;
import view.LogView;

import java.util.ArrayList;
import java.util.List;

public class HorseHandler extends ArrayList<Horse> {
    private static final List<Horse> HORSES = List.of(
            new Pony(), new Pony(), new Pony(),
            new Prancer(), new Regal(), new Sphinx(),
            new Merrygold(), new Faxe());

    private int horsesFullBlood = 0;
    private int ponies = 0;
    private int lastBoughtHorseOnDay = -1;
    private Horse nextAvailableHorse = generateHorse();
    private int timedRaceRecord = 60;

    public void addHorse(Horse horse) {
        this.add(horse);
        if (horse instanceof Pony) {
            ponies++;
        } else {
            horsesFullBlood++;
        }
    }

    public boolean canRide(List<GameCharacter> partyMembers) {
        int shortsLeft = 0;
        for (GameCharacter gc : partyMembers) {
            if (gc.getRace().id() == AllRaces.HALFLING.id() || gc.getRace().id() == AllRaces.DWARF.id()) {
                shortsLeft++;
            }
        }
        int tallsLeft = partyMembers.size() - shortsLeft;

        if (tallsLeft > horsesFullBlood) { // Enough steeds for tall guys?
            return false;
        }
        shortsLeft -= Math.min(shortsLeft, ponies); // Put short guys on ponies.
        return tallsLeft >= shortsLeft;
    }

    public int getFullBloods() {
        return horsesFullBlood;
    }

    public int getPonies() {
        return ponies;
    }

    public Horse getAvailableHorse(Model model) {
        if (lastBoughtHorseOnDay == model.getDay()) {
            return null;
        }
        return nextAvailableHorse;
    }


    public static Horse generateHorse() {
        return MyRandom.sample(HORSES).copy();
    }

    public void setHorseBoughtOn(int day) {
        lastBoughtHorseOnDay = day;
    }

    public void buyAvailableHorse(Model model, int specialPrice) {
        model.getParty().addToGold(-specialPrice);
        addHorse(nextAvailableHorse);
        setHorseBoughtOn(model.getDay());
        nextAvailableHorse = generateHorse();
    }

    public void buyAvailableHorse(Model model) {
        buyAvailableHorse(model, nextAvailableHorse.getCost());
    }

    public List<HorseItemAdapter> getHorsesAsItems() {
        List<HorseItemAdapter> list = new ArrayList<>();
        for (Horse h : this) {
            list.add(new HorseItemAdapter(h));
        }
        return list;
    }

    public void sellHorse(Model model, Horse horse) {
        removeHorse(horse);
        model.getParty().addToGold(horse.getCost()/2);
    }

    private void removeHorse(Horse horse) {
        remove(horse);
        if (horse instanceof Pony) {
            ponies--;
        } else {
            horsesFullBlood--;
        }
    }

    public void someHorsesRunAway(Model model) {
        if (size() > 0) {
            int times = size();
            for (int i = 0; i < times; ++i) {
                if (MyRandom.flipCoin()) {
                    removeHorse(get(MyRandom.randInt(size())));
                }
            }
            if (isEmpty()) {
                model.getLog().addAnimated(LogView.RED_COLOR + "You have lost your horses!\n" + LogView.DEFAULT_COLOR);
            } else {
                model.getLog().addAnimated(LogView.RED_COLOR + "Some of your horses have run away!\n" + LogView.DEFAULT_COLOR);
            }
        }
    }

    public void abandonHorses(Model model) {
        if (size() > 0) {
            model.getLog().addAnimated(LogView.RED_COLOR + "You have lost your horses!\n" + LogView.DEFAULT_COLOR);
            clear();
            ponies = 0;
            horsesFullBlood = 0;
        }
    }

    public void newAvailableHorse() {
        nextAvailableHorse = generateHorse();
    }

    public int getTimedRaceRecord() {
        return timedRaceRecord;
    }

    public void setTimedRaceRecord(int time) {
        timedRaceRecord = time;
    }
}
