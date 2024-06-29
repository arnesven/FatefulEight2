package model.states.battle;

import model.map.HexLocation;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.Sprite;
import view.sprites.Sprite32x16;

public class WaterBattleTerrain extends BattleTerrain {
    private static final Sprite WATER_SPRITE_UPPER = new Sprite32x16("battlewaterupper", "riding.png",
            0x60, MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.CYAN, MyColors.PINK);
    private static final Sprite WATER_SPRITE_LOWER = new Sprite32x16("battlewaterlower", "riding.png",
            0x70, MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.CYAN, MyColors.PINK);

    public WaterBattleTerrain() {
        super("Water", new LakeBattleHexLocation());
    }

    @Override
    public int getMoveCost() {
        return BattleTerrain.IMPASSIBLE_TERRAIN_MOVE_COST;
    }

    @Override
    public String getHelpNote() {
        return "";
    }

    private static class LakeBattleHexLocation extends HexLocation {
        public LakeBattleHexLocation() {
            super("Battle-Water");
        }

        @Override
        protected Sprite getLowerSprite() {
            return WATER_SPRITE_LOWER;
        }

        @Override
        protected Sprite getUpperSprite() {
            return WATER_SPRITE_UPPER;
        }

        @Override
        public boolean isDecoration() {
            return false;
        }

        @Override
        public HelpDialog getHelpDialog(GameView view) {
            return null;
        }
    }
}
