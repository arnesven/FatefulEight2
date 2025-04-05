package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.special.PirateClass;
import model.items.Equipment;
import model.items.clothing.LeatherTunic;
import model.items.weapons.Cutlass;
import model.items.weapons.Pistol;
import model.mainstory.*;
import model.mainstory.pirates.PotentialMutineer;
import model.map.WorldBuilder;
import model.map.locations.*;
import model.races.Race;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;

public class MainStorySpawnWest extends MainStorySpawnLocation {

    private static final List<String[]> ALL_PIRATE_NAMES = List.of(
            new String[]{"Avery", "Alex", "August"},
            new String[]{"Bailey", "Blake", "Brook"},
            new String[]{"Cameron", "Carter", "Charlie"},
            new String[]{"Devon", "Drew", "Dana"},
            new String[]{"Emerson", "Ellis", "Elliot"},
            new String[]{"Finley", "Flynn", "Fallon"},
            new String[]{"Gray", "Gabriel", "Gale"},
            new String[]{"Harley", "Harper", "Hayden"});

    private final List<PotentialMutineer> potentialMutineers;

    public MainStorySpawnWest() {
        super(new LittleErindeTown().getName(),
              new SunblazeCastle().getName(),
              new Point(22, 21),
              new AckervilleTown().getName(),
              WorldBuilder.EXPAND_WEST,
              new Point(11, 25),
              new Point(19, 27),
              "Pirates",
               WorldBuilder.PIRATE_HAVEN_LOCATION);
        this.potentialMutineers = makePotentialMutineers();
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfPiratesTask(model);
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new ArdhCastle().getName();
        String castle2 = new BogdownCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(25, 25));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(11, 12));
        return List.of(task1, task2);
    }

    private List<PotentialMutineer> makePotentialMutineers() {
        List<String> names = MyLists.transform(ALL_PIRATE_NAMES, arr -> arr[MyRandom.randInt(arr.length)]);
        Collections.shuffle(names);
        List<Race> races = new ArrayList<>(List.of(Race.HIGH_ELF, Race.DARK_ELF, Race.WOOD_ELF,
                Race.HALF_ORC, Race.HALFLING, Race.DWARF, Race.NORTHERN_HUMAN, Race.SOUTHERN_HUMAN));
        List<MyColors> colorList = new ArrayList<>(List.of(MyColors.DARK_RED, MyColors.DARK_GREEN, MyColors.ORANGE,
                MyColors.RED, MyColors.DARK_BLUE, MyColors.CYAN, MyColors.PURPLE, MyColors.GOLD));
        Collections.shuffle(races);
        List<PotentialMutineer> list = new ArrayList<>();
        int transIndex = MyRandom.randInt(names.size());
        for (int i = 0; i < names.size(); ++i) {
            boolean femaleBody = i < 4;
            boolean likesRum = (i / 2) % 2 == 0;
            boolean usesPistol = i % 2 == 0;
            CharacterClass cls = new PirateClass(colorList.get(i));
            AdvancedAppearance app = PortraitSubView.makeRandomPortrait(cls, races.get(i), femaleBody);
            GameCharacter chara = new GameCharacter(names.get(i), "", races.get(i),
                    cls, app, Classes.NO_OTHER_CLASSES,
                    new Equipment(usesPistol ? new Pistol() : new Cutlass(), new LeatherTunic(), null));
            list.add(new PotentialMutineer(chara, i == transIndex, likesRum));
        }
        return list;
    }
}
