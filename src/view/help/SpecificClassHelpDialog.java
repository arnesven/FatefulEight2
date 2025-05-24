package view.help;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.WeightedSkill;
import util.MyStrings;
import view.GameView;
import view.party.DrawableObject;
import view.sprites.Sprite;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecificClassHelpDialog extends SubChapterHelpDialog {
    private final CharacterClass charClass;

    public SpecificClassHelpDialog(GameView view, CharacterClass characterClass) {
        super(view, characterClass.getFullName() + " (" + characterClass.getShortName() + ")",
                new String[]{"\n\n\n\n\n" + characterClass.getDescription(),
                            makeClassTable(characterClass)});
        this.charClass = characterClass;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        if (getCurrentPage() != 0) {
            return textContent;
        }
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

    private static String makeClassTable(CharacterClass characterClass) {
        StringBuilder bld = new StringBuilder();
        Map<String, Integer> oldRanks = new HashMap<>();
        for (int level = 1; level <= 9; ++level) {
            bld.append("Level ").append(level).append("\n");
            StringBuilder skillBldr = new StringBuilder();
            for (Skill s : Skill.values()) {
                int rank = characterClass.getWeightForSkill(s).getRank(level);
                if (rank > 0) {
                    if (!oldRanks.containsKey(s.getShortName()) || oldRanks.get(s.getShortName()) != rank) {
                        oldRanks.put(s.getShortName(), rank);
                        skillBldr.append(s.getShortName()).append(" ").append(rank).append(", ");
                    }
                }
            }
            if (skillBldr.isEmpty()) {
                bld.append("-\n");
            } else {
                String[] parts = MyStrings.partition(skillBldr.toString(), 34);
                for (String part : parts) {
                    bld.append(part).append("\n");
                }
            }
            bld.append("\n");
        }
        return bld.toString();
    }

}
