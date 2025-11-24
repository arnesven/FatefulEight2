package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.ArmorItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import util.BeforeAndAfterLine;
import util.MyPair;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnalyzeSkillDialog extends AnalyzeDialog {

    private final List<List<BeforeAndAfterLine<Integer>>> content;
    private final Accessory accessory;

    public AnalyzeSkillDialog(Model model, Accessory accessory) {
        super(model, 12 + (model.getParty().size()-1) * accessory.getSkillBonuses().size());
        this.accessory = accessory;
        content = analyzeSkill(model, accessory.getSkillBonuses());
    }

    private List<List<BeforeAndAfterLine<Integer>>> analyzeSkill(Model model, List<MyPair<Skill, Integer>> bonuses) {
        List<List<BeforeAndAfterLine<Integer>>> outerResult = new ArrayList<>();
        for (MyPair<Skill, Integer> bonus : bonuses) {
            List<BeforeAndAfterLine<Integer>> result = new ArrayList<>();
            Skill skill = bonus.first;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (cantEquip(gc, accessory)) {
                    result.add(new BeforeAndAfterLine<>(gc.getFirstName(), -1, -1));
                } else {
                    int diff = bonus.second;
                    if (gc.getEquipment().getAccessory() != null) {
                        diff -= getBonusFor(gc.getEquipment().getAccessory(), skill);
                    }
                    result.add(new BeforeAndAfterLine<>(gc.getFirstName(), gc.getRankForSkill(skill),
                            gc.getRankForSkill(skill) + diff));
                }
            }
            outerResult.add(result);
        }
        return outerResult;
    }

    private int getBonusFor(Accessory accessory, Skill skill) {
        for (MyPair<Skill, Integer> bonuses : accessory.getSkillBonuses()) {
            if (bonuses.first == skill) {
                return bonuses.second;
            }
        }
        return 0;
    }

    private boolean cantEquip(GameCharacter gc, ArmorItem item) {
        return (!gc.getCharClass().canUseHeavyArmor() && item.isHeavy()) ||
                (!gc.canChangeClothing() && item instanceof Clothing) ||
                (!gc.canChangeAccessory() && item instanceof Accessory);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>(makeHeader(accessory, xStart, yStart));
        yStart += 9;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    private List<DrawableObject> makeDrawableObjects(List<List<BeforeAndAfterLine<Integer>>> content, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (int i = 0; i < content.size(); ++i) {
            List<BeforeAndAfterLine<Integer>> contentList = content.get(i);
            Skill skill = accessory.getSkillBonuses().get(i).first;
            objs.add(new TextDecoration(skill.getName() + ":", xStart + 2, yStart, MyColors.WHITE, MyColors.BLUE, false));
            yStart++;
            for (BeforeAndAfterLine<Integer> line : contentList) {
                if (line.getBefore() == -1) {
                    objs.add(new TextDecoration(line.getLabel(), xStart + 2, yStart,
                            MyColors.WHITE, MyColors.BLUE, false));
                    objs.add(new TextDecoration("can't equip!", xStart + 12, yStart,
                            MyColors.RED, MyColors.BLUE, false));
                } else {
                    String text = String.format("%-11s %2d  " + ((char) 0xB0), line.getLabel(), line.getBefore());
                    objs.add(new TextDecoration(text, xStart + 2, yStart,
                            MyColors.WHITE, MyColors.BLUE, false));
                    MyColors afterColor = MyColors.WHITE;
                    if (line.getAfter() > line.getBefore()) {
                        afterColor = MyColors.LIGHT_GREEN;
                    } else if (line.getAfter() < line.getBefore()) {
                        afterColor = MyColors.LIGHT_RED;
                    }
                    objs.add(new TextDecoration(String.format("%2d", line.getAfter()), xStart + 21, yStart,
                            afterColor, MyColors.BLUE, false));
                }
                yStart++;
            }
            yStart++;
        }
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(analyzeSkill(model, it.getSkillBonuses()), xStart, yStart);
    }
}
