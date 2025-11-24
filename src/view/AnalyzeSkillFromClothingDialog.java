package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.clothing.Clothing;

public class AnalyzeSkillFromClothingDialog extends AnalyzeSkillDialog {
    public AnalyzeSkillFromClothingDialog(Model model, Clothing clothing) {
        super(model, clothing, new ItemGetter() {
            @Override
            public Item get(GameCharacter gc) {
                return gc.getEquipment().getClothing();
            }
        });
    }
}
