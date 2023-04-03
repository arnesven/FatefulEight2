package model.ruins;

import model.Model;
import model.items.potions.Potion;
import model.items.potions.UnstablePotion;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

import static model.ruins.DungeonRoom.BRICK_COLOR;
import static model.ruins.DungeonRoom.FLOOR_COLOR;

public class CrackedWall extends DungeonDoor {

    private static final Sprite32x32 HORI_CRACK = new Sprite32x32("horicrack", "dungeon.png", 0x26,
            MyColors.BLACK, BRICK_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_CRACK = new Sprite32x32("verticrack", "dungeon.png", 0x27,
            MyColors.BLACK, FLOOR_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);
    private ExplosionAnimation explo;
    private final Sprite sprite;
    private final String direction;

    public CrackedWall(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.sprite = isHorizontal ? HORI_CRACK : VERTI_CRACK;
        this.direction = direction;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getDescription() {
        return "A cracked wall";
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        super.drawYourself(model, xPos, yPos);
        if (explo != null && !explo.isDone()) {
            model.getScreenHandler().register(explo.getName(), new Point(xPos, yPos), explo);
        }
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        Potion pot = null;
        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (p instanceof UnstablePotion) {
                pot = p;
            }
        }
        if (pot == null) {
            state.println("This wall is cracked and could probably be breached if you had some kind of explosives.");
        } else {
            state.print("Use " + pot.getName() + " on Cracked Wall? (Y/N) ");
            if (state.yesNoInput()) {
                model.getParty().getInventory().remove(pot);
                explo = new ExplosionAnimation();
                SoundEffects.playBoom();
                while (!explo.isDone()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                state.println("The " + pot.getName() + " explodes on contact with the wall. " +
                        "The wall crumbles and opens a passage into the next room.");
                state.unlockDoor(model, direction);
            }
        }
    }

    private static class ExplosionAnimation extends RunOnceAnimationSprite {
        public ExplosionAnimation() {
            super("explosion", "dungeon.png", 0, 8, 32, 32, 6, MyColors.ORANGE);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.YELLOW);
        }
    }
}
