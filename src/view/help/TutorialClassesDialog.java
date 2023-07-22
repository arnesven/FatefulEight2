package view.help;

import model.Model;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import view.GameView;
import view.PartyAttitudesDialog;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TutorialClassesDialog extends ExpandableHelpDialog {
    private static final String text =
            "A character's class defines that characters baseline of Health Points and Speed, " +
            "their skills and whether or not that character can wear heavy armor. A character's " +
            "class also affects how much gold they contribute to the party when joining and " +
            "how much gold they would claim if being dismissed.\n\n" +
            "Each character has four classes which he or she may assume. Various events will allow " +
            "characters to change their class. Think carefully before switching classes, you may not " +
            "get a chance to switch back soon!";

    public TutorialClassesDialog(GameView view) {
        super(view, "Classes", text);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> subsections = new ArrayList<>();
        for (CharacterClass characterClass : Classes.allClasses) {
            if (characterClass != Classes.None) {
                subsections.add(new SpecificClassHelpDialog(view, characterClass));
            }
        }
        return subsections;
    }

    private static class SpecificClassHelpDialog extends SubChapterHelpDialog {
        private final CharacterClass charClass;

        public SpecificClassHelpDialog(GameView view, CharacterClass characterClass) {
            super(view, 30, characterClass.getFullName() + " (" + characterClass.getShortName() + ")",
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
            textContent.add(new DrawableObject(xStart+xOff, yStart+yOff) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    print(model.getScreenHandler(), x, y, "Base Health Points: " + charClass.getHP());
                    print(model.getScreenHandler(), x, y+1, "Base Speed: " + charClass.getSpeed());
                }
            });
            yOff += 3;
            textContent.add(new DrawableObject(xStart+xOff, yStart+yOff) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    int i = 0;
                    for (Skill s : charClass.getSkills()) {
                        print(model.getScreenHandler(), x, y + (i++),
                                String.format("%-16s%2d", s.getName(), charClass.getWeightForSkill(s)));
                    }
                }
            });
            yOff += 1 + charClass.getSkills().size();
            textContent.add(new DrawableObject(xStart+xOff, yStart+yOff) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    print(model.getScreenHandler(), x, y,
                            "Armor Class " + (charClass.canUseHeavyArmor() ? "HEAVY" : "LIGHT"));
                }
            });

            return textContent;
        }

    }
}
