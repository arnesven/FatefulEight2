package view.subviews;

import model.Model;
import model.quests.Quest;
import model.quests.QuestDifficulty;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.widget.TopText;

import java.awt.event.KeyEvent;
import java.util.List;

public class SelectQuestSubView extends SubView {
    private final SubView previous;
    private final List<Quest> quests;
    private static final int WIDTH = 33;
    private static final int HEIGHT = 14;
    private int index;

    public SelectQuestSubView(SubView previous, List<Quest> quests) {
        this.previous = previous;
        this.quests = quests;
        this.index = quests.size();
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int xStart = X_OFFSET + (X_MAX - X_OFFSET - WIDTH) / 2 - 1;
        int yStart = Y_OFFSET + 14;
        if (quests.size() > 1) {
            yStart = Y_OFFSET + 5;
        }
        int index = 0;
        for (Quest quest : quests) {
            BorderFrame.drawFrame(model.getScreenHandler(),
                    xStart, yStart-1, WIDTH, HEIGHT,
                    MyColors.BLACK, MyColors.WHITE, MyColors.BLACK, true);
            BorderFrame.drawString(model.getScreenHandler(), quest.getName(), xStart + 2, yStart, MyColors.WHITE, MyColors.BLACK);
            drawProvider(model, xStart, yStart, quest);

            drawDifficulty(model, quest.getName().length() + xStart + 2, yStart, quest);
            drawRewards(model, xStart, yStart, quest);
            drawDetails(model, xStart, yStart, quest);
            drawAcceptButton(model, xStart, yStart, index);
            yStart += 14;
            index++;
        }

        drawRejectButton(model, yStart);
    }

    private void drawRejectButton(Model model, int yStart) {
        MyColors fgColor = MyColors.YELLOW;
        MyColors bgColor = MyColors.BLACK;
        if (index == quests.size()) {
            fgColor = MyColors.BLACK;
            bgColor = MyColors.WHITE;

        }
        int x = X_OFFSET+8;
        int y = yStart-1;
        BorderFrame.drawFrame(model.getScreenHandler(), x, y, 13, 4,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLACK, true);
        BorderFrame.drawString(model.getScreenHandler(), "REJECT ALL", x+2, y+2, fgColor, bgColor);

    }

    private void drawAcceptButton(Model model, int xStart, int yStart, int index) {
        MyColors fgColor = MyColors.YELLOW;
        MyColors bgColor = MyColors.BLACK;
        if (this.index == index) {
            fgColor = MyColors.BLACK;
            bgColor = MyColors.WHITE;

        }
        BorderFrame.drawString(model.getScreenHandler(), "ACCEPT", xStart + 10,
                yStart + 7, fgColor, bgColor);
    }

    private void drawDetails(Model model, int xStart, int yStart, Quest quest) {
        int row = 2;
        for (String detail : quest.getDetails()) {
            String toPrint = detail.replace("(", "").replace(")", "");
            BorderFrame.drawString(model.getScreenHandler(), detail,
                    xStart + 18, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
        }
    }

    private void drawRewards(Model model, int xStart, int yStart, Quest quest) {
        int row = 2;
        BorderFrame.drawString(model.getScreenHandler(), "Rewards", xStart + 10, yStart + row++, MyColors.WHITE, MyColors.BLACK);
        if (quest.getReward().getReputation() != 0) {
            int rep = quest.getReward().getReputation();
            BorderFrame.drawString(model.getScreenHandler(), "  " + MyStrings.withPlus(rep),
                    xStart + 10, yStart + (row++), (rep < 0 ? MyColors.RED : MyColors.WHITE), MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, yStart + (row-1), TopText.REP_ICON_SPRITE);
        }
        if (quest.getReward().getGold() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "  " + quest.getReward().getGold()*model.getParty().size(),
                    xStart + 10, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, yStart + (row-1), TopText.GOLD_ICON_SPRITE);
        }
        if (quest.getReward().getExp() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "XP " + quest.getReward().getExp(),
                    xStart + 10, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
        }
        quest.drawSpecialReward(model, xStart + 10, yStart + (row++));
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
        quest.getPortrait().drawYourself(model.getScreenHandler(), xStart + 2, yStart+2);
        String provider = quest.getProvider();
        String[] providerParts = MyStrings.partition(provider, 12);
        for (int i = 0; i < providerParts.length; ++i) {
            int x = xStart + 1 + 5 - providerParts[i].length() / 2;
            BorderFrame.drawString(model.getScreenHandler(), providerParts[i], x, yStart+9+i, MyColors.WHITE, MyColors.BLACK);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENING - QUEST OFFERED";
    }

    public boolean didAcceptQuest() {
        return index != quests.size();
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            index = (index + 1) % (quests.size()+1);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            index = index - 1;
            if (index < 0) {
                index = quests.size(); // number of quests.
            }
        }
        return false;
    }

    public Quest getSelectedQuest() {
        if (index == quests.size()) {
            return null;
        }
        return quests.get(index);
    }
}
