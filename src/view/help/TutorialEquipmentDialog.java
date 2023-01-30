package view.help;

import view.GameView;

public class TutorialEquipmentDialog extends HelpDialog {
    private static final String text =
            "Each character can equip three items: A weapon, an article of clothing, and an accessory. " +
            "Weapons are used to deal damage in combat. Clothing normally gives your character armor but " +
            "sometimes give skill bonuses instead. Some heavy armor incur skill penalties. Accessories can modify " +
            "a characters stats in many different ways.\n\n" +
            "You can equip characters from the Party Menu, or the Equipment Menu. Please note that items which " +
            "are currently equipped on other party members do not appear as available when equipping. You must " +
            "first unequip an item to equip it to somebody else.\n\n" +
            "Only characters who's class permit heavy armor can equip heavy armor.\n\n" +
            "No character may equip a two-handed weapon and a shield at the same time.";

    public TutorialEquipmentDialog(GameView view) {
        super(view, "Equipment", text);
    }
}
