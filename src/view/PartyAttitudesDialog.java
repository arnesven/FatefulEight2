package view;

import model.Model;
import model.characters.GameCharacter;
import view.sprites.AnimationManager;
import view.sprites.AttitudeSprite;
import view.sprites.Sprite;
import view.subviews.SubView;

import java.awt.*;
import java.awt.event.KeyEvent;


public class PartyAttitudesDialog extends SubView {
    public static final Sprite NEUTRAL_SPRITE = new AttitudeSprite(0x00, MyColors.GRAY);
    public static final Sprite HAPPY_SPRITE = new AttitudeSprite(0x01, MyColors.LIGHT_GREEN);
    public static final Sprite SAD_SPRITE = new AttitudeSprite(0x02, MyColors.PEACH);
    public static final Sprite VERY_HAPPY_SPRITE = new AttitudeSprite(0x10, MyColors.GREEN);
    public static final Sprite VERY_SAD_SPRITE = new AttitudeSprite(0x11, MyColors.RED);
    public static final Sprite HATE_SPRITE = new AttitudeSprite(0x12, MyColors.DARK_RED);
    public static final Sprite LOVE_SPRITE = new AttitudeSprite(0x20, MyColors.RED);
    private boolean showNumbers;

    public PartyAttitudesDialog(Model model) {
        super();
        this.showNumbers = false;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        drawOfAndVs(model);
        drawHeaders(model);
        drawContent(model);
        AnimationManager.synchAnimations();
    }

    private void drawOfAndVs(Model model) {
        Point p = translateToScreen(0, 0);
        BorderFrame.drawString(model.getScreenHandler(), " OF->", p.x, p.y-1, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), " VS", p.x, p.y+0, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "  |", p.x, p.y+1, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "  V", p.x, p.y+2, MyColors.WHITE, MyColors.BLUE);
    }

    private void drawHeaders(Model model) {
        int count = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            Point p = translateToScreen(count, 1);
            String name = gc.getFirstName().substring(0, Math.min(9, gc.getFirstName().length()));
            for (int i = 0; i < name.length(); i++) {
                BorderFrame.drawString(model.getScreenHandler(),
                        ""+name.charAt(name.length()-i-1),
                        p.x + 5, p.y - i + 3,
                        model.getParty().getColorForPartyMember(gc), MyColors.BLUE);
            }
            Point p2 = translateToScreen(0, 2 + count++);
            model.getScreenHandler().register(gc.getAvatarSprite().getName(), p2, gc.getAvatarSprite());
        }
    }


    private void drawContent(Model model) {
        int y = 0;
        for (GameCharacter vs : model.getParty().getPartyMembers()) {
            int x = 0;
            for (GameCharacter of : model.getParty().getPartyMembers()) {
                Point p = translateToScreen(x+1, y+2);
                p.x += 1;
                p.y += 1;
                if (of == vs) {
                    p.y += 1;
                    BorderFrame.drawString(model.getScreenHandler(), ((char)0x80 + "" + (char)0x81), p.x, p.y, MyColors.WHITE, MyColors.BLUE);
                } else if (showNumbers) {
                    BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", of.getAttitude(vs)), p.x-1, p.y+1, MyColors.WHITE, MyColors.BLUE);
                } else {
                    Sprite symbol = getSymbolForAttitude(of.getAttitude(vs));
                    model.getScreenHandler().register(symbol.getName(), p, symbol);
                }
                x++;
            }
            y++;
        }
    }

    public static Sprite getSymbolForAttitude(int i) {
        if (i >= 30) {
            return LOVE_SPRITE;
        } else if (i >= 20) {
            return VERY_HAPPY_SPRITE;
        } else if (i >= 10) {
            return HAPPY_SPRITE;
        } else if (i <= -30) {
            return HATE_SPRITE;
        } else if (i <= -20) {
            return VERY_SAD_SPRITE;
        } else if (i <= -10) {
            return SAD_SPRITE;
        }
        return NEUTRAL_SPRITE;
    }

    private Point translateToScreen(int x, int y) {
        return new Point(X_OFFSET + x * 3 + 2, Y_OFFSET + y * 3 + 2);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Press SPACE to toggle numerical display.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "PARTY ATTITUDES";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            showNumbers = !showNumbers;
        }

        return super.handleKeyEvent(keyEvent, model);
    }
}
