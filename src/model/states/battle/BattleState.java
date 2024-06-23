package model.states.battle;

import model.Model;
import model.SteppingMatrix;
import model.map.DeepWoodsLocation;
import model.map.HexLocation;
import model.map.HillsLocation;
import model.map.WoodsLocation;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.subviews.BattleSubView;
import view.subviews.StripedTransition;
import view.subviews.SubView;

public class BattleState extends GameState {
    public static final int BATTLE_GRID_WIDTH = 8;
    public static final int BATTLE_GRID_HEIGHT = 9;
    private final SteppingMatrix<BattleTerrain> terrain;
    private final SteppingMatrix<BattleUnit> units;

    public BattleState(Model model) {
        super(model);
        this.terrain = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        terrain.addElement(3, 3, new WoodsBattleTerrain());
        terrain.addElement(4, 4, new HillsBattleTerrain());
        terrain.addElement(5, 5, new DenseWoodsBattleTerrain());

        this.units = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        units.addElement(5, 8, new SwordsmanUnit(10));
    }

    @Override
    public GameState run(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.combatSong);
        SubView subView = new BattleSubView(terrain, units);
        StripedTransition.transition(model, subView); // TODO: Make new transition for this.

        waitForReturn();
        return model.getCurrentHex().getEveningState(model, false, false);
    }
}
