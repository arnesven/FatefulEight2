package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.Weapon;
import util.BeforeAndAfterLine;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeWeaponDialog extends AnalyzeDialog {

    private static final int DIALOG_HEIGHT_BASE = 13;
    private final List<BeforeAndAfterLine<Double>> content;
    private final Weapon weapon;

    public AnalyzeWeaponDialog(Model model, Weapon weapon) {
        super(model, DIALOG_HEIGHT_BASE);
        this.weapon = weapon;
        this.content = analyzeWeapon(model, weapon);
    }

    public static List<BeforeAndAfterLine<Double>> analyzeWeapon(Model model, Weapon weapon) {
        List<BeforeAndAfterLine<Double>> content = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            GameCharacter wouldBe = gc.copy();
            wouldBe.setEquipment(new Equipment(weapon, gc.getEquipment().getClothing(),
                    gc.getEquipment().getAccessory()));
            content.add(new BeforeAndAfterLine<>(gc.getFirstName(), gc.calcAverageDamage(), wouldBe.calcAverageDamage()));
        }
        return content;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.addAll(super.makeHeader(weapon, xStart, yStart));
        yStart += 9;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    private static List<DrawableObject> makeDrawableObjects(List<BeforeAndAfterLine<Double>> content, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (BeforeAndAfterLine<Double> line : content) {
            String text =  String.format("%-11s %2.1f  " + ((char) 0xB0), line.getLabel(), line.getBefore());
            objs.add(new TextDecoration(text, xStart+2, yStart,
                    MyColors.WHITE, MyColors.BLUE, false));
            MyColors afterColor = MyColors.WHITE;
            if (line.getAfter() > line.getBefore()) {
                afterColor = MyColors.LIGHT_GREEN;
            } else if (line.getAfter() < line.getBefore()) {
                afterColor = MyColors.LIGHT_RED;
            }
            objs.add(new TextDecoration(String.format("%1.1f", line.getAfter()), xStart+21, yStart,
                    afterColor, MyColors.BLUE, false));
            yStart++;
        }
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(analyzeWeapon(model, (Weapon)it), xStart, yStart);
    }
}
