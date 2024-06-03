package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.ruins.themes.DarknessDungeonTheme;
import model.ruins.themes.DungeonTheme;
import model.ruins.themes.GardenDungeonTheme;
import model.states.ExploreRuinsState;
import util.MyLists;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class DousableFire extends CampfireDungeonObject {
    private static final Sprite DOUSED_CAMP_FIRE_SPRITE = new Sprite32x32(
            "dousedcampfire", "dungeon.png", 0x100,
            MyColors.BROWN, MyColors.BLACK, MyColors.BLACK, MyColors.BLACK);
    private final FatueSunDialObject sundial;
    private boolean doused = false;
    private List<DousableFire> allFires;

    public DousableFire(int x, int y, FatueSunDialObject sunDialObject) {
        setInternalPosition(new Point(x, y));
        this.sundial = sunDialObject;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        if (doused) {
            return DOUSED_CAMP_FIRE_SPRITE;
        }
        return super.getSprite(theme);
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (doused) {
            state.print("A campfire was burning here. Relight it? (Y/N) ");
            doused = !state.yesNoInput();
        } else {
            state.println("A campfire, fully ablaze. Who lit it?");
            state.print("Put out the fire? (Y/N) ");
            doused = state.yesNoInput();
        }
        if (MyLists.all(allFires, DousableFire::isDoused)) {
            state.getDungeon().getLevel(state.getCurrentLevel()).setTheme(new DarknessDungeonTheme());
            sundial.setDark(true);
            state.leaderSay("Whoa... that's dark... But wait, I see something glimmering.");
            model.getLog().waitForAnimationToFinish();
            sundial.setShiny(true);
            state.leaderSay("I one of the marks of the sundial. It's luminescent!");
        }
        if (MyLists.any(allFires, Predicate.not(DousableFire::isDoused))) {
            state.getDungeon().getLevel(state.getCurrentLevel()).setTheme(new GardenDungeonTheme());
            sundial.setDark(false);
            sundial.setShiny(false);
        }
    }

    public boolean isDoused() { return doused; }

    public void setAllFires(List<DousableFire> fires) {
        this.allFires = fires;
    }

    @Override
    public String getDescription() {
        if (doused) {
            return "An extinguished camp fire.";
        }
        return super.getDescription();
    }
}
