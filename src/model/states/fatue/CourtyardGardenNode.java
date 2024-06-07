package model.states.fatue;

import model.Model;
import model.items.accessories.GardeningGloves;
import model.items.special.FashionableSash;
import model.ruins.DungeonLevel;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.configs.CourtyardGardenDungeonLevelConfig;
import model.ruins.factories.FatueGardenMonsterFactory;
import model.ruins.objects.HiddenChestObject;
import model.ruins.themes.GardenDungeonTheme;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.GardenCombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourtyardGardenNode extends KeyRequiredFatueDungeonNode {
    private final MyColors givesKeyColor;

    public CourtyardGardenNode(MyColors gold, MyColors givesKeyColor) {
        super("Courtyard Garden", true, gold,
                "This passage leads to the dark and mysterious courtyard garden. Do you want to enter?");
        this.givesKeyColor = givesKeyColor;
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new GardenCombatTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();

        DungeonLevel level;
        do {
            CourtyardGardenDungeonLevelConfig config = new CourtyardGardenDungeonLevelConfig(new FatueGardenMonsterFactory(), givesKeyColor);
            config.addRequiredDeadEndObject(new HiddenChestObject(new GardeningGloves()), 0.33);
            level = new DungeonLevel(model, random, false, 11, config);
            System.err.println("Key did not spawn for courtyard garden, retrying");
            if (config.allRequiredObjectsPlaced()) {
                break;
            }
        } while (true);
        levels.add(level);
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(model, random, new GardenDungeonTheme());
        finalLevel.setFinalRoom(new FatueStaffRoom());
        levels.add(finalLevel);
        return new RuinsDungeon(levels);
    }

}
