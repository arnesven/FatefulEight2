package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.GoldDummyItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.accessories.ShieldItem;
import model.items.books.BookItem;
import model.items.clothing.Clothing;
import model.items.potions.Potion;
import model.items.spells.Spell;
import model.items.weapons.Weapon;
import model.quests.QuestEdge;
import model.quests.scenes.DummyQuestNode;
import model.races.Race;
import model.ruins.objects.DungeonChest;
import model.states.events.LottoHouseChest;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static view.subviews.TownHallSubView.DOOR;

public class LottoHouseSubView extends AvatarSubView {

    public static final Sprite COACH3_SPRITE = new Sprite32x32("coach3", "world_foreground.png", 0x5B,
            MyColors.BLACK, MyColors.PURPLE, Race.NORTHERN_HUMAN.getColor(), MyColors.ORANGE);
    public static final Sprite BAR = new Sprite32x32("bar", "world_foreground.png", 0x5A,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    private Point avatarInitialPos;
    private final SteppingMatrix<Item> matrix;
    private final Map<Item, LottoHouseChest> chests = new HashMap<>();
    private boolean avatarEnabled = true;
    private boolean cursorEnabled = false;
    private Point itemRevealPoint;
    private Sprite itemRevealSprite;
    private int itemRevealCount = 0;

    public LottoHouseSubView(SteppingMatrix<Item> matrix) {
        this.matrix = matrix;
        Random random = new Random();
        for (Item it : matrix.getElementList()) {
            chests.put(it, new LottoHouseChest(random));
        }
        resetAvatarPosition();
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    protected void specificDrawArea(Model model) {
        DailyActionSubView.drawRoom(model, 0, 8, 0, 6, TavernSubView.LOWER_WALL);
        Point p = convertToScreen(new Point(4, 6));
        model.getScreenHandler().put(p.x, p.y, DOOR);
        model.getScreenHandler().register("coach3", convertToScreen(new Point(6, 4)),
                COACH3_SPRITE);
        model.getScreenHandler().register("bar", convertToScreen(new Point(6, 5)),
                BAR);
        drawChests(model);
        if (avatarEnabled) {
            drawAvatar(model);
        }
        if (cursorEnabled) {
            drawCursor(model);
        }
        if (itemRevealSprite != null) {
            drawItemReveal(model);
        }
    }

    private void drawItemReveal(Model model) {
        model.getScreenHandler().register(itemRevealSprite.getName(), itemRevealPoint, itemRevealSprite,
                3, 8,  -itemRevealCount/3);
        itemRevealCount++;
        if (itemRevealCount > 48) {
            itemRevealSprite = null;
        }
    }

    private void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p.translate(1, 0);
            Point shifted = convertToScreen(p);
            shifted.translate(2, 0);
            model.getScreenHandler().register("lottocursor", shifted, cursor, 5);
        }
    }

    private void drawChests(Model model) {
        DungeonDrawer drawer = DungeonDrawer.getInstance(model.getScreenHandler());
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (matrix.getElementAt(x, y) != null) {
                    Point p = convertToScreen(new Point(x+1, y+1));
                    p.translate(2, 0);
                    chests.get(matrix.getElementAt(x, y)).drawYourself(drawer, p.x, p.y, null);
                }
            }
        }
    }

    private void drawAvatar(Model model) {
        if (model.getParty().getLeader() != null) { // If party has been wiped out and this is just before game over screen
            Point p = convertToScreen(avatarInitialPos);
            model.getScreenHandler().register("lottoavatar", p, model.getParty().getLeader().getAvatarSprite(), 2);
        }
    }

    public Item goToChestAndOpenIt(Model model) {
        Item selectedItem = matrix.getSelectedElement();
        DungeonChest chest = chests.get(selectedItem);
        if (chest.isOpen()) {
            return null;
        }
        Point selected = matrix.getSelectedPoint();
        avatarEnabled = false;
        cursorEnabled = false;
        int xPos = selected.x+1;
        if (selected.x < matrix.getColumns()-1) {
            xPos++;
        }
        QuestSubView.animateAvatarAlongEdge(this, avatarInitialPos,
                new QuestEdge(new DummyQuestNode(xPos, selected.y+1)), model.getParty().getLeader().getAvatarSprite());
        avatarInitialPos.x = xPos;
        avatarInitialPos.y = selected.y+1;
        avatarEnabled = true;
        chest.unlockYourself();
        chest.openYourself();
        animateItemReveal(model, selectedItem, matrix.getSelectedPoint().x+1, matrix.getSelectedPoint().y+1);
        return selectedItem;
    }

    private void animateItemReveal(Model model, Item selectedItem, int x, int y) {
        Point position = convertToScreen(new Point(x, y));
        position.translate(2, 0);
        Sprite spr = getMiniSpriteForItem(selectedItem);
        itemRevealPoint = position;
        itemRevealSprite = spr;
        itemRevealCount = 0;
    }

    private Sprite getMiniSpriteForItem(Item selectedItem) {
        if (selectedItem instanceof Weapon) {
            return new MiniItemSprite(0, MyColors.LIGHT_GRAY, MyColors.BROWN);
        }
        if (selectedItem instanceof Clothing) {
            return new MiniItemSprite(1, MyColors.PINK, MyColors.PINK);
        }
        if (selectedItem instanceof ShieldItem) {
            return new MiniItemSprite(3, MyColors.RED, MyColors.LIGHT_GRAY);
        }
        if (selectedItem instanceof Accessory) {
            return new MiniItemSprite(2, MyColors.GOLD, MyColors.BEIGE);
        }
        if (selectedItem instanceof Potion) {
            return new MiniItemSprite(4, MyColors.RED, MyColors.BEIGE);
        }
        if (selectedItem instanceof Spell || selectedItem instanceof BookItem) {
            return new MiniItemSprite(6, MyColors.DARK_BLUE, MyColors.BEIGE);
        }
        if (selectedItem instanceof GoldDummyItem) {
            return new MiniItemSprite(5, MyColors.YELLOW, MyColors.BEIGE);
        }
        return new MiniItemSprite(7, MyColors.BROWN, MyColors.BEIGE);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (cursorEnabled) {
            return matrix.handleKeyEvent(keyEvent);
        }
        return false;
    }

    @Override
    protected String getUnderText(Model model) {
        return "You are in a 'Lotto House'.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - LOTTO HOUSE";
    }

    public void enableCursor() {
        this.cursorEnabled = true;
    }

    public void resetAvatarPosition() {
        avatarInitialPos = new Point(4, 5);
    }

}
