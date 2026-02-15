package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.ArmorItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import util.BeforeAndAfterLine;
import util.MyLists;
import util.MyPair;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AnalyzeSkillDialog extends AnalyzeDialog {
    private static final String NA_STRING = (char)0x80 + "" + (char)0x81;
    private final List<MyPair<Skill, List<Integer>>> content;
    private final Item item;

    public AnalyzeSkillDialog(Model model, Item item, ItemGetter getter) {
        super(model, 15 + 3 * model.getParty().size(),
                18 - model.getParty().size() + analyzeSkill(model, item, getter).size(), "Skill Analysis for");
        this.item = item;
        content = analyzeSkill(model, item, getter);
    }

    private static List<MyPair<Skill, List<Integer>>> analyzeSkill(Model model, Item item, ItemGetter getter) {
        List<MyPair<Skill, List<Integer>>> outerResult = new ArrayList<>();

        for (Skill skill : Skill.values()) {
            if (skill.areEqual(Skill.MagicAny)) {
                continue;
            }
            List<Integer> resultForSkill = new ArrayList<>();
            for (GameCharacter gc : model.getParty().getPartyMembers()) {

                MyPair<Skill, Integer> bonus = MyLists.find(item.getSkillBonuses(), pair -> pair.first == skill);
                int diff = 0;
                if (bonus != null) {
                    diff += bonus.second;
                }
                diff -= getBonusFor(getter.get(gc), skill);

                if (item instanceof ArmorItem && cantEquip(gc, (ArmorItem) item)) {
                    resultForSkill.add(null);
                } else {
                    resultForSkill.add(diff);
                }
            }
            if (MyLists.any(resultForSkill, val -> !(val == null || val == 0))) {
                outerResult.add(new MyPair<>(skill, resultForSkill));
            }
        }
        return outerResult;
    }

    private static int getBonusFor(Item item, Skill skill) {
        if (item == null) {
            return 0;
        }
        for (MyPair<Skill, Integer> bonuses : item.getSkillBonuses()) {
            if (bonuses.first == skill) {
                return bonuses.second;
            }
        }
        return 0;
    }

    private static boolean cantEquip(GameCharacter gc, ArmorItem item) {
        return (!gc.getCharClass().canUseHeavyArmor() && item.isHeavy()) ||
                (!gc.canChangeClothing() && item instanceof Clothing) ||
                (!gc.canChangeAccessory() && item instanceof Accessory);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.add(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                BorderFrame.drawString(model.getScreenHandler(), "Skill Bonus", x+1, y, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Analysis for", x+1, y+1, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), item.getName(), x+1, y+2, MyColors.WHITE, MyColors.BLUE);
                item.drawYourself(model.getScreenHandler(), x + 3, y+3);
                BorderFrame.drawString(model.getScreenHandler(), "Increase", x+1, y+8, MyColors.LIGHT_GREEN, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Decrease", x+1, y+9, MyColors.LIGHT_RED, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Can't Equip", x+1, y+10, MyColors.RED, MyColors.BLUE);
            }
        });
        objs.add(new DrawableObject(xStart + 1, yStart + 1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                SkillsView.drawCharacterNames(model, x, y);
            }
        });
        objs.add(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                int row = SkillsView.ROW_OFFSET - 1;
                for (MyPair<Skill, List<Integer>> pair : content) {
                    int finalY = y + row;
                    BorderFrame.drawString(model.getScreenHandler(), String.format("%-14s", pair.first.getName()),
                            x, finalY, MyColors.WHITE, MyColors.BLUE);

                    int charNum = 0;
                    for (GameCharacter gc : model.getParty().getPartyMembers()) {
                        int finalX = 15 + x + 3 * charNum;
                        if (pair.second.get(charNum) == null) {
                            BorderFrame.drawString(model.getScreenHandler(), NA_STRING, finalX, finalY, MyColors.RED, MyColors.BLUE);
                        } else {
                            MyColors fgColor = MyColors.WHITE;
                            int diff = pair.second.get(charNum);
                            int finalRank = gc.getRankForSkill(pair.first) + diff;
                            if (diff < 0) {
                                fgColor = MyColors.LIGHT_RED;
                            } else if (diff > 0) {
                                fgColor = MyColors.LIGHT_GREEN;
                            }
                            if (diff != 0) {
                                BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", finalRank), finalX, finalY, fgColor, MyColors.BLUE);
                            }
                        }

                        charNum++;
                    }
                    row++;
                }
            }
        });
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return List.of(); // Not used for this type of analysis
    }

    protected static abstract class ItemGetter {
        public abstract Item get(GameCharacter gc);
    }
}
