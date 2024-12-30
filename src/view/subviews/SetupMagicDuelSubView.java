package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.states.duel.gauges.*;
import util.Arithmetics;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SetupMagicDuelSubView extends SubView {
    private static final Sprite BLUE_BLOCK = new FilledBlockSprite(MyColors.BLUE);
    private static final MyColors[] COLOR_SET = new MyColors[]{
            MyColors.RED, MyColors.BLUE, MyColors.GREEN, MyColors.BLACK, MyColors.WHITE};
    private final GameCharacter opponent;
    private final GameCharacter player;
    private final String opposGauge;
    private final String opposColor;
    private int rowIndex;
    private int selectedColorIndex;
    private int selectedGaugeIndex;
    private PowerGauge[] gauges;

    public SetupMagicDuelSubView(GameCharacter upperChar, GameCharacter lowerChar,
                                 MyColors opposColor, PowerGauge opposGauge) {
        this.opponent = upperChar;
        this.player = lowerChar;
        if (opposColor == null) {
            this.opposColor = "???";
        } else {
            this.opposColor = MyStrings.capitalize(opposColor.name());
        }
        if (opposGauge == null) {
            this.opposGauge = "?";
        } else {
            this.opposGauge = opposGauge.getName();
        }
        this.selectedColorIndex = 0;
        this.selectedGaugeIndex = 1;
        this.rowIndex = 0;
        gauges = new PowerGauge[]{
                new ATypePowerGauge(true),
                new BTypePowerGauge(),
                new CTypePowerGauge(true),
                new KTypePowerGauge(true),
                new STypePowerGauge(true),
                new TTypePowerGauge(true),
                new VTypePowerGauge(true)};
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, BLUE_BLOCK);

        drawDuelist(model, opponent, new Point(X_OFFSET+1, Y_OFFSET+4), opposColor, opposGauge, MyColors.WHITE);
        BorderFrame.drawCentered(model.getScreenHandler(), "VERSUS", Y_OFFSET + 15,
                MyColors.WHITE, MyColors.BLUE);
        drawDuelist(model, player, new Point(X_OFFSET+1, Y_MAX - 14), MyStrings.capitalize(getSelectedColor().name()),
                getSelectedGauge().getName(), MyColors.YELLOW);

        BorderFrame.drawCentered(model.getScreenHandler(), "START", Y_MAX - 2,
                textForeground(MyColors.YELLOW, 2), textBackground(MyColors.YELLOW, 2));

    }

    private void drawDuelist(Model model, GameCharacter chara, Point pos, String color, String gauge,
                             MyColors textColor) {
        model.getScreenHandler().clearSpace(pos.x, pos.x + 7, pos.y, pos.y + 7);
        chara.getAppearance().drawYourself(model.getScreenHandler(), pos.x, pos.y);
        BorderFrame.drawString(model.getScreenHandler(), chara.getFullName(), pos.x, pos.y-1,
                MyColors.WHITE, MyColors.BLUE);

        BorderFrame.drawString(model.getScreenHandler(), "Magic Skill: ", pos.x + 8, pos.y+1,
                MyColors.WHITE, MyColors.BLUE);
        if (textColor != MyColors.WHITE) {
            model.getScreenHandler().put(pos.x + 21, pos.y + 1, ArrowSprites.LEFT);
            model.getScreenHandler().put(pos.x + 27, pos.y + 1, ArrowSprites.RIGHT);
        }
        BorderFrame.drawString(model.getScreenHandler(), color, pos.x + 22, pos.y+1,
                textForeground(textColor, 0), textBackground(textColor, 0));

        BorderFrame.drawString(model.getScreenHandler(), "Gauge Type: ", pos.x + 8, pos.y+3,
                MyColors.WHITE, MyColors.BLUE);
        if (textColor != MyColors.WHITE) {
            model.getScreenHandler().put(pos.x + 21, pos.y + 3, ArrowSprites.LEFT);
            model.getScreenHandler().put(pos.x + 23, pos.y + 3, ArrowSprites.RIGHT);
        }
        BorderFrame.drawString(model.getScreenHandler(), gauge, pos.x + 22, pos.y+3,
                textForeground(textColor, 1), textBackground(textColor, 1));
    }

    private MyColors textBackground(MyColors textColor, int thisIndex) {
        if (textColor != MyColors.WHITE && rowIndex == thisIndex) {
            return MyColors.LIGHT_YELLOW;
        }
        return MyColors.BLUE;
    }

    private MyColors textForeground(MyColors textColor, int thisIndex) {
        if (textColor != MyColors.WHITE && rowIndex == thisIndex) {
            return MyColors.BLACK;
        }
        return textColor;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            rowIndex = Arithmetics.decrementWithWrap(rowIndex, 3);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            rowIndex = Arithmetics.incrementWithWrap(rowIndex, 3);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            if (rowIndex == 0) {
                selectedColorIndex = Arithmetics.decrementWithWrap(selectedColorIndex, COLOR_SET.length);
            } else if (rowIndex == 1) {
                selectedGaugeIndex = Arithmetics.decrementWithWrap(selectedGaugeIndex, gauges.length);
            }
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (rowIndex == 0) {
                selectedColorIndex = Arithmetics.incrementWithWrap(selectedColorIndex, COLOR_SET.length);
            } else if (rowIndex == 1) {
                selectedGaugeIndex = Arithmetics.incrementWithWrap(selectedGaugeIndex, gauges.length);
            }
            return true;
        }

        return super.handleKeyEvent(keyEvent, model);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Select your magic type and gauge.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - MAGIC DUEL";
    }

    public PowerGauge getSelectedGauge() {
        return gauges[selectedGaugeIndex];
    }

    public MyColors getSelectedColor() {
        return COLOR_SET[selectedColorIndex];
    }

    public boolean isOnStart() {
        return rowIndex == 2;
    }
}
