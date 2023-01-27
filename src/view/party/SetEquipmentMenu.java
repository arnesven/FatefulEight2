package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import sound.SoundEffects;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SetEquipmentMenu extends FixedPositionSelectableListMenu {

    private final GameCharacter person;

    public SetEquipmentMenu(PartyView view, GameCharacter gc, int x, int y) {
        super(view, 20, 10, x, y);
        this.person = gc;
    }


    protected abstract void doUnequipAction(Model model, GameCharacter person);

    protected abstract boolean doAction(Model model, Item item, GameCharacter person);

    protected abstract Collection<? extends Item> getSpecificInventory(Model model);

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        content.add(new SelectableListContent(xStart + 1, ++yStart, "Unequip") {
            @Override
            public void performAction(Model model, int x, int y) {
                SetEquipmentMenu.this.doUnequipAction(model, person);
                setTimeToTransition(true);
            }
        });
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(getSpecificInventory(model));
        for (Item item : inventory) {
            content.add(new SelectableListContent(xStart + 1, ++yStart, item.getName()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    boolean success = SetEquipmentMenu.this.doAction(model, item, person);
                    if (success) {
                        SoundEffects.playSound(item.getSound());
                        setTimeToTransition(true);
                    }
                }
            });
        }
        return content;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return new ArrayList<>();
    }
}
