package view.help;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.combat.abilities.AbilityCombatAction;
import model.combat.abilities.SkillAbilityCombatAction;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import view.GameView;
import view.party.DrawableObject;
import view.sprites.Sprite;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        textContent.add(new DrawableObject(xStart + xOff - 3, yStart + yOff) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                int i = 0;
                for (Skill s : charClass.getSkills()) {
                    String skillString = s.getName() + " (" + s.getShortName() + ")";
                    print(model.getScreenHandler(), x, y + (i++),
                            String.format("%-20sW%s", skillString, charClass.getWeightForSkill(s).asString()));
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
        bld.append("LVL SKILL ADVANCEMENTS\n");

        List<SkillAbilityCombatAction> skillAbilities = new ArrayList<>();
        skillAbilities.addAll(MyLists.transform(MyLists.filter(AbilityCombatAction.getAllCombatAbilities(null),
                combAb -> combAb instanceof SkillAbilityCombatAction),
               combAb -> (SkillAbilityCombatAction)combAb));
        skillAbilities.addAll(MyLists.transform(MyLists.filter(AbilityCombatAction.getAllPassiveCombatActions(),
                passAb -> passAb instanceof SkillAbilityCombatAction),
                passAb -> (SkillAbilityCombatAction)passAb));

        Set<SkillAbilityCombatAction> oldAbilities = new HashSet<>();
        for (int level = 1; level <= 10; ++level) {
            bld.append(String.format("%2d  ", level));
            List<MyPair<Skill, Integer>> newRanks = new ArrayList<>();
            Set<SkillAbilityCombatAction> newAbilities = new HashSet<>();
            for (Skill s : Skill.values()) {
                int rank = characterClass.getWeightForSkill(s).getRank(level);
                if (rank > 0) {
                    if (!oldRanks.containsKey(s.getShortName()) || oldRanks.get(s.getShortName()) != rank) {
                        oldRanks.put(s.getShortName(), rank);
                        newRanks.add(new MyPair<>(s, rank));
                    }
                    newAbilities.addAll(findNewAbilitiesForLevel(skillAbilities, s, rank, level));
                }
            }

            Set<SkillAbilityCombatAction> setIntersection = new HashSet<>(newAbilities);
            setIntersection.removeAll(oldAbilities);
            if (newRanks.isEmpty() && setIntersection.isEmpty()) {
                bld.append("-\n");
            } else {
                if (!newRanks.isEmpty()) {
                    printRanks(bld, newRanks);
                }
                if (!setIntersection.isEmpty()) {
                    printAbilities(bld, setIntersection);
                }
            }
            bld.append("\n");
            oldAbilities.addAll(newAbilities);
        }
        return bld.toString();
    }

    private static void printRanks(StringBuilder bld, List<MyPair<Skill, Integer>> newRanks) {
        int column = 0;
        for (MyPair<Skill, Integer> newRank : newRanks) {
            if (column == 4) {
                bld.append("\n    ");
                column = 0;
            }
            bld.append(String.format("%-4s%2d ", newRank.first.getShortName(), newRank.second));
            column++;
        }
        if (column > 0) {
            bld.append("\n    ");
        }
    }

    private static void printAbilities(StringBuilder bld, Set<SkillAbilityCombatAction> setIntersection) {
        String abiStr = MyStrings.makeString(setIntersection.stream().toList(), e -> e.getName() + ", ");
        for (String part : MyStrings.partition(abiStr, DIALOG_WIDTH - 5)) {
            if (part.endsWith(", ")) {
                part = part.substring(0, part.length() - 2);
            }
            bld.append(part).append("\n    ");
        }
    }

    private static List<SkillAbilityCombatAction> findNewAbilitiesForLevel(List<SkillAbilityCombatAction> skillAbilities, Skill s, int rank, int level) {
        List<SkillAbilityCombatAction> result = new ArrayList<>();
        for (SkillAbilityCombatAction skiAb : skillAbilities) {
            if (skiAb.getLinkedSkills().contains(s)) {
                boolean hasRequiredRanks = rank >= skiAb.getRequiredRanks();
                if (skiAb.hasRequiredLevel()) {
                    if (level >= skiAb.getRequiredLevel() && hasRequiredRanks) {
                        result.add(skiAb);
                    }
                } else if (hasRequiredRanks) {
                    result.add(skiAb);
                }
            }
        }
        return result;
    }

}
