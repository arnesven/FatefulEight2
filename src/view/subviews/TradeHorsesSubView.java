package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.horses.HorseItemAdapter;
import model.states.CombatEvent;
import util.MyLists;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.CombatCursorSprite;
import view.sprites.HorseSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class TradeHorsesSubView extends TopMenuSubView {
    private SteppingMatrix<HorseItemAdapter> matrix;
    private boolean isBuying = true;
    private static final Sprite BACKGROUND =
            new HorseSprite(1, 3, MyColors.LIGHT_GRAY, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);


    public TradeHorsesSubView(List<Horse> availableHorses) {
        super(4, new int[]{X_OFFSET + 3, X_OFFSET+13, X_OFFSET+24});
        setHorses(availableHorses);
        setTopCursorIndex(0);
    }

    @Override
    protected String getUnderText(Model model) {
        if (getTopIndex() != -1) {
            return "";
        }
        Point p = matrix.getSelectedPoint();
        HorseItemAdapter horseItemAdapter = matrix.getElementAt(p.x, p.y);
        if (horseItemAdapter == null) {
            return "";
        }
        return horseItemAdapter.getName() + ", " + horseItemAdapter.getCost() + " gold" +
                horseItemAdapter.getShoppingDetails();
    }

    @Override
    protected String getTitleText(Model model) {
        return "TRADE HORSES";
    }

    @Override
    protected void drawCursor(Model model) {
        Point p = new Point(matrix.getSelectedPoint());
        p = convertToScreen(p.x, p.y);
        p.y -= 3;
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        model.getScreenHandler().register("tradehorsecursor", p, cursor, 2);
    }

    @Override
    protected void drawInnerArea(Model model) {
        drawHorses(model);
    }

    private synchronized void drawHorses(Model model) {
        for (int x = 0; x < matrix.getColumns(); ++x) {
            for (int y = 0; y < matrix.getRows(); ++y) {
                Point p = convertToScreen(x, y);
                if (matrix.getElementAt(x, y) != null) {
                    matrix.getElementAt(x, y).drawYourself(model.getScreenHandler(),
                            p.x, p.y);
                } else {
                    model.getScreenHandler().put(p.x-2, p.y, BACKGROUND);
                }
            }
        }
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + 2 + 8 * x, Y_OFFSET + 2 + y * 8);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        if (i == 0) {
            return MyColors.YELLOW;
        }
        if (i == 2) {
            return MyColors.LIGHT_RED;
        }
        return model.getParty().hasHorses() ? MyColors.LIGHT_BLUE : MyColors.GRAY;
    }

    @Override
    protected String getTitle(int i) {
        switch (i) {
            case 0 : return "BUY";
            case 1 : return "SELL";
            default : return "EXIT";
        }
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public synchronized void setHorses(List<Horse> horseList) {
        this.matrix = new SteppingMatrix<>(4, 4);
        int col = 0;
        int row = 0;
        for (int i = 0; i < horseList.size(); ++i) {
            matrix.addElement(col++, row, new HorseItemAdapter(horseList.get(i)));
            if (col == matrix.getColumns()) {
                col = 0;
                row++;
            }
        }
    }

    public Horse getSelectedHorse() {
        return matrix.getSelectedElement().getHorse();
    }

    public void removeHoses(Horse horse) {
        HorseItemAdapter adapter = MyLists.find(matrix.getElementList(),
                (HorseItemAdapter hia) -> hia.getHorse() == horse);
        matrix.remove(adapter);
    }

    public void setInSell() {
        setTopCursorIndex(1);
        isBuying = false;
    }

    public void setInBuy() {
        setTopCursorIndex(0);
        isBuying = true;
    }

    protected int getDefaultIndex() {
        return isBuying ? 0:1;
    }
}
