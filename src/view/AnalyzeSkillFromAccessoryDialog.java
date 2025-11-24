package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.accessories.Accessory;

public class AnalyzeSkillFromAccessoryDialog extends AnalyzeSkillDialog {
    public AnalyzeSkillFromAccessoryDialog(Model model, Accessory accessory) {
        super(model, accessory, new ItemGetter() {
            @Override
            public Item get(GameCharacter gc) {
                return gc.getEquipment().getAccessory();
            }
        });
    }
}
