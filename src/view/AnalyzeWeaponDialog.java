package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import util.BeforeAndAfterLine;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnalyzeWeaponDialog extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 25;
    private static final int DIALOG_HEIGHT_BASE = 13;
    private final List<BeforeAndAfterLine<Double>> content;
    private final Weapon weapon;

    public AnalyzeWeaponDialog(Model model, Weapon weapon) {
        super(model.getView(), DIALOG_WIDTH, DIALOG_HEIGHT_BASE + model.getParty().size());
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
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.add(new TextDecoration("Damage Analysis for", xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart+=2;
        objs.add(new DrawableObject(xStart, yStart++) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                weapon.drawYourself(model.getScreenHandler(), 37, y);
            }
        });
        yStart+=2;
        objs.add(new TextDecoration(weapon.getName(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart += 2;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));

        return objs;
    }

    public static List<DrawableObject> makeDrawableObjects(List<BeforeAndAfterLine<Double>> content, int xStart, int yStart) {
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
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = new ArrayList<>();
        list.add(new SelectableListContent(xStart+getWidth()/2-1, yStart+getHeight()-2, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return list;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
