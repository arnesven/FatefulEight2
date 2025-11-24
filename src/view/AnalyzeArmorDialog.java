package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.ArmorItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import util.BeforeAndAfterLine;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeArmorDialog extends AnalyzeDialog {
    private final List<BeforeAndAfterLine<Integer>> content;
    private final ArmorItem armorItem;

    public AnalyzeArmorDialog(Model model, ArmorItem item) {
        super(model, 12, "Armor Analysis");
        this.armorItem = item;
        this.content = analyzeArmor(model, item);
    }

    private List<BeforeAndAfterLine<Integer>> analyzeArmor(Model model, ArmorItem item) {
        List<BeforeAndAfterLine<Integer>> result = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (cantEquip(gc, item)) {
                result.add(new BeforeAndAfterLine<>(gc.getFirstName(), -1, -1));
            } else {
                int diff;
                if (item instanceof Clothing) {
                    diff = item.getAP() - gc.getEquipment().getClothing().getAP();
                } else if (item instanceof Accessory && gc.getEquipment().getAccessory() != null) {
                    diff = item.getAP() - gc.getEquipment().getAccessory().getAP();
                } else {
                    diff = item.getAP();
                }
                result.add(new BeforeAndAfterLine<>(gc.getFirstName(), gc.getAP(), gc.getAP() + diff));
            }
        }
        return result;
    }

    private boolean cantEquip(GameCharacter gc, ArmorItem item) {
        return (!gc.getCharClass().canUseHeavyArmor() && item.isHeavy()) ||
                (!gc.canChangeClothing() && item instanceof Clothing) ||
                (!gc.canChangeAccessory() && item instanceof Accessory);
    }


    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        Item item = (Item)armorItem;
        objs.addAll(makeHeader(item, xStart, yStart));
        yStart += 9;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    private List<DrawableObject> makeDrawableObjects(List<BeforeAndAfterLine<Integer>> content, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (BeforeAndAfterLine<Integer> line : content) {
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
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(analyzeArmor(model, (ArmorItem)it), xStart, yStart);
    }
}
