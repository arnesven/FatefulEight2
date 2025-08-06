package model.states.fatue;

import model.Model;
import model.items.special.PieceOfStaffItem;
import model.ruins.DungeonRoom;
import model.ruins.objects.CenterDungeonObject;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class FatueStaffRoom extends DungeonRoom {
    private static final Sprite STAFF_PIECE_SPRITE = new StaffPieceSprite();

    public FatueStaffRoom() {
        addObject(new StaffPieceObject());
    }

    private static class StaffPieceObject extends CenterDungeonObject {
        @Override
        protected Sprite getSprite(DungeonTheme theme) {
            return STAFF_PIECE_SPRITE;
        }

        @Override
        public void doAction(Model model, ExploreRuinsState state) {
            state.println("You got a piece of staff!");
            state.getCurrentRoom().removeObject(this);
            state.getDungeon().setCompleted(true);
            state.setDungeonExited(true);
            model.getParty().getInventory().add(new PieceOfStaffItem());
            state.leaderSay("Nice. One step closer to getting through that door.");
            state.print("Press enter to continue.");
            state.waitForReturn();
        }

        @Override
        public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
            Sprite spr = getSprite(theme);
            drawer.register(spr.getName(), new Point(xPos, yPos), spr);
        }

        @Override
        public String getDescription() {
            return "A piece of staff";
        }
    }

    private static class StaffPieceSprite extends LoopingSprite {
        public StaffPieceSprite() {
            super("staffpiecesprite", "fatue_plan.png", 0x90, 32, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_RED);
            setColor3(MyColors.GRAY);
            setDelay(12);
        }
    }
}
