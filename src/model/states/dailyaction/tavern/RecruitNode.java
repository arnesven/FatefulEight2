package model.states.dailyaction.tavern;

import model.Model;
import model.RecruitableCharacter;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.GameState;
import model.states.RecruitState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyRandom;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.subviews.TavernSubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RecruitNode extends DailyActionNode {
    public static final Sprite TABLE = new Sprite32x32("table", "world_foreground.png", 0x04,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN, MyColors.WHITE);
    private final List<Point> offsets;
    private final ArrayList<RecruitableCharacter> defaultGuys;
    private RecruitState recruitState;

    public RecruitNode(Model model) {
        super("Recruit Adventurers");
        this.offsets = new ArrayList<>(List.of(new Point(-4, 0),
                new Point(-4, 4), new Point(0, 4)));
        Collections.shuffle(offsets);
        this.defaultGuys = new ArrayList<>();
        for (int i = MyRandom.randInt(1, 3); i >= 0; --i) {
            CharacterClass randClass = Classes.None;
            if (MyRandom.randInt(3) > 0) {
                randClass = MyRandom.sample(Arrays.asList(Classes.allClasses));
            }
            CharacterAppearance randApp = PortraitSubView.makeRandomPortrait(randClass);
            GameCharacter gc = new GameCharacter("", "", randApp.getRace(),
                    randClass, randApp, Classes.NO_OTHER_CLASSES);
            defaultGuys.add(new RecruitableCharacter(gc));
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (recruitState == null) {
            if (model.getParty().getRecruitmentPersistence() == null) {
                this.recruitState = new RecruitState(model);
            } else {
                this.recruitState = new RecruitState(model, model.getParty().getRecruitmentPersistence());
            }
        }
        return recruitState;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return TABLE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        model.getScreenHandler().register("objectforeground", p, fg);
        Set<RecruitableCharacter> recruitables = new HashSet<>(model.getLingeringRecruitables());
        if (this.recruitState != null) {
             recruitables.addAll(recruitState.getRecruitables());
        }
        if (recruitables.isEmpty()) {
            recruitables.addAll(defaultGuys);
        }
        int i = 0;
        for (RecruitableCharacter rgc : recruitables) {
            GameCharacter gc = rgc.getCharacter();
            if (!model.getParty().getPartyMembers().contains(gc)) {
                AvatarSprite avatarSprite = gc.getAvatarSprite();
                avatarSprite.synch();
                Point pos = new Point(p.x + offsets.get(i).x, p.y + offsets.get(i).y);
                model.getScreenHandler().register(avatarSprite.getName(), pos, avatarSprite);
                i++;
                if (i == 3) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("It's too late in the day for that.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}
