package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.items.special.FatueKeyItem;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import model.states.fatue.SouthGardenNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.List;

public class FatueSunDialObject extends DungeonObject {
    private static final Sprite SUNDIAL_SPRITE = new Sprite32x32("fatuesundial", "fatue_plan.png", 0x58,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.DARK_GRAY);
    private static final Sprite SHINY_SUNDIAL_SPRITE = new Sprite32x32("sundialshiny", "fatue_plan.png", 0x58,
            MyColors.CYAN, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private static final Sprite DARK_DUNDIAL_SPRITE = new Sprite32x32("sundialshiny", "fatue_plan.png", 0x58,
            MyColors.BLACK, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE);
    private final MyColors keyColor;
    private boolean shiny;
    private boolean dark;
    private boolean firstTime = true;

    public FatueSunDialObject(MyColors keyColor) {
        super(3, 3);
        this.keyColor = keyColor;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        if (shiny) {
            return SHINY_SUNDIAL_SPRITE;
        }
        if (dark) {
            return DARK_DUNDIAL_SPRITE;
        }
        return SUNDIAL_SPRITE;
    }

    @Override
    public String getDescription() {
        return "Sundial";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (state.getDungeon().isCompleted()) {
            return;
        }
        if (shiny) {
            state.print("Turn the sundial to point at the luminescent mark? (Y/N) ");
            if (state.yesNoInput()) {
                state.println("A secret compartment opens below the sundial. Inside is a key!");
                FatueKeyItem key = new FatueKeyItem(keyColor);
                model.getParty().getInventory().add(key);
                state.println("You got a " + key.getName() + ".");
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("Hurray!", "Jackpot!",
                        "We solved it!", "That was really cool.", "Ingenious!"));
                state.println("Dungeon completed. Press enter to continue.");
                state.waitForReturn();
                state.getDungeon().setCompleted(true);
                state.setDungeonExited(true);
            }
            return;
        }
        if (firstTime) {
            state.leaderSay("A sundial, down here? Now that just doesn't make any sense. " +
                    "There's no sunlight down here. If there hadn't been for these campfires there wouldn't " +
                    "be a shadow on the dial at all.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.testSkillHidden(Skill.Logic, 9, 0).isSuccessful()) {
                    state.partyMemberSay(gc, "Maybe that's just it. Why don't we put out the fires and see what happens?");
                    state.randomSayIfPersonality(PersonalityTrait.anxious, List.of(gc),
                            "But that will make it so dark... Do we have to?");
                    break;
                }
            }
            firstTime = false;
        } else {
            state.leaderSay("A wonder what this is here for...");
        }
    }

    public void setShiny(boolean b) {
        this.shiny = b;
    }

    public void setDark(boolean b) {
        this.dark = b;
    }
}
