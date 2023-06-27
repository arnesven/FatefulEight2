package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.SplitPartySubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SplitPartyJunction extends QuestJunction {
    private static final Sprite32x32 SPRITE = new Sprite32x32("splitjunc", "quest.png", 0x0D,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BROWN);
    private final QuestEdge pathA;
    private final QuestEdge pathB;
    private final String leaderTalk;
    private List<GameCharacter> groupA = null;
    private List<GameCharacter> groupB = null;
    private boolean enableAvatar = true;

    public SplitPartyJunction(int col, int row, QuestEdge pathA, QuestEdge pathB, String leaderTalk) {
        super(col, row);
        this.pathA = pathA;
        this.pathB = pathB;
        connectTo(pathA);
        connectTo(pathB);
        this.leaderTalk = leaderTalk;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
        if (groupB != null && groupA != null && enableAvatar && !groupB.isEmpty()) {
            GameCharacter otherLeader = groupA.get(0);
            if (groupA.contains(model.getParty().getLeader())) {
                otherLeader = groupB.get(0);
            }
            model.getScreenHandler().register(otherLeader.getAvatarSprite().getName(), new Point(xPos, yPos),
                    otherLeader.getAvatarSprite(), 3);
        }
    }

    @Override
    public String getDescription() {
        return "Party splits up";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.leaderSay(leaderTalk);
        this.groupA = new ArrayList<>();
        groupA.addAll(model.getParty().getPartyMembers());
        this.groupB = new ArrayList<>();
        SubView oldSubView = model.getSubView();
        model.setSubView(new SplitPartySubView(oldSubView, groupA, groupB));
        do {
            state.waitForReturnSilently();
            if (!groupA.isEmpty() && !groupB.isEmpty()) {
                model.setSubView(oldSubView);
                if (groupA.contains(model.getParty().getLeader())) {
                    return pathA;
                }
                return pathB;
            } else {
                state.println("Each group must contain at least one person!");
            }
        } while (true);
    }

    public List<GameCharacter> getGroupA() {
        return groupA;
    }

    public List<GameCharacter> getGroupB() {
        return groupB;
    }

    public List<GameCharacter> getNonLeaderGroup(GameCharacter leader) {
        if (groupB.contains(leader)) {
            return groupA;
        }
        return groupB;
    }

    public List<GameCharacter> getLeaderGroup(GameCharacter leader) {
        if (groupB.contains(leader)) {
            return groupB;
        }
        return groupA;
    }

    public void setAvatarEnabled(boolean b) {
        enableAvatar = b;
    }

    public boolean groupADead() {
        return groupDead(groupA);
    }

    public boolean groupBDead() {
        return groupDead(groupB);
    }

    private static boolean groupDead(List<GameCharacter> group) {
        for (GameCharacter gc : group) {
            if (!gc.isDead()) {
                return false;
            }
        }
        return true;
    }
}
