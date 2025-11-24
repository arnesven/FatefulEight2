package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.accessories.Accessory;

public class AnalyzeSkillFromAccessoryDialog extends AnalyzeSkillDialog {
    public AnalyzeSkillFromAccessoryDialog(Model model, Accessory accessory) {
        super(model, accessory);
    }

    @Override
    protected Item getClothingOrAccessory(GameCharacter gc) {
        return gc.getEquipment().getAccessory(); // Can return null
    }
}
