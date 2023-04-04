package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;

import java.util.List;

public class SorcerersTowerEvent extends DailyEventState {
    public SorcerersTowerEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.SOR, "Sorcerer");
        println("A tall stone structure stands silently in front of the party. " +
                "Within is a sorcerer who only accepts persons who are " +
                "acquainted with magic.");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.MagicAny, 7);
        if (success) {
            print("The sorcerer admits you into the tower. He offers to train you further in the ways of sorcery, ");
            ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.SOR);
            changeClassEvent.areYouInterested(model);
            println("The sorcerer also offers to sell you a spell at a discount of 5 gold.");
            List<Item> items = List.of(model.getItemDeck().getRandomSpell());
            ShopState shop = new ShopState(model, "sorcerer", items,
                    new int[]{Math.max(1, items.get(0).getCost() - 5)});
            shop.setSellingEnabled(false);
            shop.run(model);
        } else {
            println("The sorcerer sneers at your feeble attempts to impress him. The door to his tower remains firmly shut.");
        }
    }
}
