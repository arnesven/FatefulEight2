package view.subviews;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.quests.Quest;
import model.quests.QuestDifficulty;
import util.Arithmetics;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class SelectQuestSubView extends SubView {
    private static final int LIST_START_Y = 27;
    private static final int QUEST_CARD_Y_START = Y_OFFSET;
    private static final int QUEST_MAP_Y_START = QUEST_CARD_Y_START + 12;
    private final List<Quest> quests;
    private int index;

    public SelectQuestSubView(SubView previous, List<Quest> quests) {
        this.quests = quests;
        this.index = 0;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+LIST_START_Y-1, blackBlock);
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET+LIST_START_Y, Y_MAX, blueBlock);

        if (index < quests.size()) {
            List<Point> path = getRemotePath(model);
            Point mid = path.get(path.size()/2);
            model.getWorld().drawYourself(model, mid, model.getParty().getPosition(),
                    4, 4, X_OFFSET, QUEST_MAP_Y_START, path.get(path.size()-1), true,
                    null);
            drawSelectedQuest(model);
        }
        drawQuestList(model);
        drawCursor(model);
    }

    private List<Point> getRemotePath(Model model) {
        Quest selectedQuest = quests.get(index);
        if (model.getParty().questIsHeld(selectedQuest)) {
            return model.getParty().getHeldDataFor(selectedQuest).getRemotePath();
        }
        return quests.get(index).getRemotePath();
    }


    private CharacterAppearance getPortrait(Model model) {
        Quest selectedQuest = quests.get(index);
        if (model.getParty().questIsHeld(selectedQuest)) {
            return model.getParty().getHeldDataFor(selectedQuest).getAppearance();
        }
        return quests.get(index).getPortrait();
    }

    private void drawSelectedQuest(Model model) {
        Quest quest = quests.get(index);
        int xStart = X_OFFSET;
        int yStart = QUEST_CARD_Y_START;
        BorderFrame.drawString(model.getScreenHandler(), quest.getName(), xStart, yStart,
                MyColors.WHITE, MyColors.BLACK);
        drawProvider(model, xStart, yStart, quest);

        drawDifficulty(model, quest.getName().length() + xStart + 2, yStart, quest);
        drawRewards(model, xStart, yStart, quest);
        drawDetails(model, xStart, yStart, quest);
    }

    private void drawCursor(Model model) {
        model.getScreenHandler().put(X_OFFSET, Y_OFFSET + LIST_START_Y + index + 2,
                ArrowSprites.MOVING_RIGHT_BLUE);
    }

    private void drawQuestList(Model model) {
        int y = 0;
        BorderFrame.drawString(model.getScreenHandler(), "QUESTS",
                X_OFFSET + 1, Y_OFFSET + LIST_START_Y + 1 + (y++),
                MyColors.WHITE, MyColors.BLUE);
        for (Quest q : quests) {
            BorderFrame.drawString(model.getScreenHandler(), q.getName(),
                    X_OFFSET + 1, Y_OFFSET + LIST_START_Y + 1 + y,
                    MyColors.WHITE, MyColors.BLUE);

            if (model.getParty().questIsHeld(q)) {
                BorderFrame.drawString(model.getScreenHandler(), "HELD",
                        X_OFFSET+24, Y_OFFSET + LIST_START_Y + 1 + y,
                        MyColors.ORANGE, MyColors.BLUE);
            }
            y++;
        }
        BorderFrame.drawString(model.getScreenHandler(), "DON'T GO",
                X_OFFSET + 1, Y_OFFSET + LIST_START_Y + 1 + (y++),
                MyColors.WHITE, MyColors.BLUE);
    }

    private void drawDetails(Model model, int xStart, int yStart, Quest quest) {
        int row = 2;
        for (String detail : quest.getDetails()) {
            String toPrint = detail.replace("(", "").replace(")", "");
            BorderFrame.drawString(model.getScreenHandler(), detail,
                    xStart + 17, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
        }
    }

    private void drawRewards(Model model, int xStart, int yStart, Quest quest) {
        int row = quest.drawQuestOfferCardMiddle(model, xStart + 10, yStart + 2);

        if (quest.getReward().getReputation() != 0) {
            int rep = quest.getReward().getReputation();
            BorderFrame.drawString(model.getScreenHandler(), "  " + MyStrings.withPlus(rep),
                    xStart + 10, row++, (rep < 0 ? MyColors.RED : MyColors.WHITE), MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, row - 1, TopText.REP_ICON_SPRITE);
        }
        if (quest.getReward().getGold() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "  " + quest.getReward().getGold(),
                    xStart + 10, row++, MyColors.WHITE, MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, row - 1, TopText.GOLD_ICON_SPRITE);
        }
        if (quest.getReward().getExp() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "XP " + quest.getReward().getExp(),
                    xStart + 10, row++, MyColors.WHITE, MyColors.BLACK);
        }
        if (quest.getReward().getNotoriety() != 0) {
            model.getScreenHandler().put(xStart + 10, row++, TopText.NOTORIETY_SPRITE);
            BorderFrame.drawString(model.getScreenHandler(), quest.getReward().getNotoriety()+"",
                    xStart+12, row - 1, MyColors.RED, MyColors.BLACK);
        }
    }

    private void drawDifficulty(Model model, int xStart, int yStart, Quest quest) {
        MyColors diffColor = MyColors.RED;
        if (quest.getDifficulty() == QuestDifficulty.MEDIUM) {
            diffColor = MyColors.YELLOW;
        } else if (quest.getDifficulty() == QuestDifficulty.EASY) {
            diffColor = MyColors.GREEN;
        }
        BorderFrame.drawString(model.getScreenHandler(), quest.getDifficulty().toString(), xStart + 1, yStart, diffColor, MyColors.BLACK);

    }

    private void drawProvider(Model model, int xStart, int yStart, Quest quest) {
        CharacterAppearance portrait = getPortrait(model);
        portrait.drawYourself(model.getScreenHandler(), xStart + 2, yStart+2);
        String provider = quest.getProvider();
        String[] providerParts = MyStrings.partition(provider, 11);
        for (int i = 0; i < providerParts.length; ++i) {
            String stripped = providerParts[i];
            if (stripped.endsWith(" ")) {
                stripped = stripped.substring(0, stripped.length()-1);
            }
            int x = xStart + 5 - stripped.length() / 2;
            BorderFrame.drawString(model.getScreenHandler(), stripped, x, yStart+9+i, MyColors.WHITE, MyColors.BLACK);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "Use arrow keys to select a quest to go on or hold.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENING - QUESTS";
    }

    public boolean didSelectQuest() {
        return index != quests.size();
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            index = Arithmetics.incrementWithWrap(index, quests.size()+1);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            index = Arithmetics.decrementWithWrap(index, quests.size()+1);
            return true;
        }
        return false;
    }

    public Quest getSelectedQuest() {
        if (index == quests.size()) {
            return null;
        }
        return quests.get(index);
    }

    public Point getCursorPoint() {
        return new Point(X_OFFSET+4, Y_OFFSET + LIST_START_Y + index);
    }
}
