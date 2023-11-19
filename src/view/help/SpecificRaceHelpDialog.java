package view.help;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.races.Race;
import view.GameView;
import view.party.DrawableObject;

import java.util.List;

public class SpecificRaceHelpDialog extends SubChapterHelpDialog {
    private final CharacterAppearance portrait;

    public SpecificRaceHelpDialog(GameView view, Race race, CharacterAppearance appearance) {
        super(view, race.getQualifiedName(), makeText(race));
        this.portrait = appearance;
    }

    private static String makeText(Race race) {
        return "\n\n\n\n\n\n\n\n" + healthAndSpeedBonuses(race) + "Skill Bonuses: " + skillsAsString(race.getSkills()) + "\n\n" + race.getDescription();
    }

    private static String healthAndSpeedBonuses(Race race) {
        return "Health Bonus: " + race.getHPModifier() + "\n" +
                "Speed Bonus: " + race.getSpeedModifier() + "\n" +
                "Carry Cap: " + race.getCarryingCapacity() + "\n";
    }

    private static String skillsAsString(List<Skill> skills) {
        StringBuilder bldr = new StringBuilder();
        for (Skill s : skills) {
            bldr.append(s.getName() + ", ");
        }
        return bldr.substring(0, bldr.length()-2);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = super.buildDecorations(model, xStart, yStart);
        objs.add(new DrawableObject(xStart, yStart) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                portrait.drawYourself(model.getScreenHandler(), x+14, y+3);
            }
        });
        return objs;
    }
}
