package model.states.mine;

import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.items.Equipment;
import model.items.clothing.LeatherTunic;
import model.items.weapons.*;
import model.races.Race;
import model.states.GameState;
import util.MyRandom;
import view.sprites.AnimationManager;

import java.util.List;

public class ActiveMine extends LogicalMine {

    private static final List<Weapon> WEAPONS = List.of(new RustyPickaxe(), new CommonPickaxe(), new Warhammer(), new Sicle());

    public ActiveMine(boolean enteredFromSurface) {
        super(enteredFromSurface);
    }

    @Override
    public void addNPCs(SteppingMatrix<MineObject> matrix, int level) {
        if (level > 5) {
            return;
        }
        for (int i = MyRandom.randInt(0, 4); i > 0; --i) {
            GameCharacter gc = GameState.makeRandomCharacter(1, MyRandom.randInt(4) == 0 ?
                    Race.randomRace() : Race.DWARF);
            gc.setClass(Classes.MIN);
            Weapon w = (Weapon) MyRandom.sample(WEAPONS).copy();
            gc.setEquipment(new Equipment(w, new LeatherTunic(), null));
            placeRandomly(matrix, new MinerMineObject(gc, MyRandom.randInt(4) == 0));
        }
        AnimationManager.synchAnimations();
    }
}
