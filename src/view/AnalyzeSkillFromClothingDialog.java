package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.clothing.Clothing;

public class AnalyzeSkillFromClothingDialog extends AnalyzeSkillDialog {
    public AnalyzeSkillFromClothingDialog(Model model, Clothing clothing) {
        super(model, clothing);
    }

    @Override
    protected Item getClothingOrAccessory(GameCharacter gc) {
        return gc.getEquipment().getClothing();
    }
}
