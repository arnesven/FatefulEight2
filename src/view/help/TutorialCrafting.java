package view.help;

import view.GameView;

public class TutorialCrafting extends HelpDialog {
    private static final String TEXT =
            "During your travels you will collect many materials with which " +
            "you may craft items. Items can be crafted at work benches which can " +
            "be found in towns and castles.\n\n" +
            "You can only craft items which are already in your inventory or currently " +
            "equipped to a party member. Spells and potions cannot be crafted. Crafting an item " +
            "requires a single party member, the crafter, to complete three successful steps - three skill checks. " +
            "First the crafter must inspecting the item which " +
            "they are trying to replicate (Perception), then they must figure out how to achieve the desired " +
            "results (Logic) and finally they must make the item out of the materials (Labor).\n\n" +
            "The difficulty and material cost of crafting items is dependent on value of the item.\n\n" +
            "Materials can be found in many locations in the world and can often be collected from defeated " +
            "humanoid enemies. There is no limit to how many materials you can carry.";

    public TutorialCrafting(GameView view) {
        super(view, "Crafting", TEXT);
    }
}