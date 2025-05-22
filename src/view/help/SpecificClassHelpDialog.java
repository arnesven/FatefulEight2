package view.help;

import model.Model;
import model.classes.CharacterClass;
import model.classes.Skill;
import view.GameView;
import view.party.DrawableObject;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class SpecificClassHelpDialog extends SubChapterHelpDialog {
    private final CharacterClass charClass;

    public SpecificClassHelpDialog(GameView view, CharacterClass characterClass) {
        super(view, characterClass.getFullName() + " (" + characterClass.getShortName() + ")",
                "\n\n\n\n\n" + characterClass.getDescription());
        this.charClass = characterClass;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        int yOff = 3;
        textContent.add(new DrawableObject(xStart + 16, yStart + yOff) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                Sprite icon = charClass.getIconSprite();
                model.getScreenHandler().register(icon.getName(), new Point(x, y), icon);
            }
        });
        yOff += 6 + getHeightForText(charClass.getDescription());
        int xOff = 8;
        textContent.add(new DrawableObject(xStart + xOff, yStart + yOff) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                print(model.getScreenHandler(), x, y, "Base Health Points: " + charClass.getHP());
                print(model.getScreenHandler(), x, y + 1, "Base Speed: " + charClass.getSpeed());
            }
        });
        yOff += 3;
        textContent.add(new DrawableObject(xStart + xOff, yStart + yOff) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                int i = 0;
                for (Skill s : charClass.getSkills()) {
                    print(model.getScreenHandler(), x, y + (i++),
                            String.format("%-16sW%1d", s.getName(), charClass.getWeightForSkill(s).getWeight()));
                }
            }
        });
        yOff += 1 + charClass.getSkills().size();
        textContent.add(new DrawableObject(xStart + xOff, yStart + yOff) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                print(model.getScreenHandler(), x, y,
                        "Armor Class " + (charClass.canUseHeavyArmor() ? "HEAVY" : "LIGHT"));
            }
        });

        return textContent;
    }

}
