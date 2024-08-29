package view;

import model.Model;
import model.items.Item;
import model.items.SocketedItem;
import model.items.weapons.PairableWeapon;
import model.items.weapons.Weapon;
import model.items.weapons.WeaponPair;
import view.party.ConfigureSocketedItemMenu;
import view.party.DrawableObject;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class WeaponPairingDialog extends ConfigureSocketedItemMenu {
    private final Weapon mainWeapon;
    private WeaponPair preview = null;

    public WeaponPairingDialog(GameView innerView, Weapon mainHandWeapon) {
        super(innerView, new IntermediateWeaponPair(mainHandWeapon), 30);
        this.mainWeapon = mainHandWeapon;
    }

    @Override
    protected boolean escapeDisposesMenu() {
        return false;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = super.buildContent(model, xStart, yStart);
        content.remove(content.size()-1);

        content.add(new SelectableListContent(xStart + getWidth() / 2 - 3, yStart + getHeight() - 2, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                WeaponPairingDialog.this.setTimeToTransition(true);
                SocketedItem socketedItem = getSocketItem();
                if (socketedItem.getInnerItem(0) != null) {
                    socketedItem.getInnerItem(0).addYourself(model.getParty().getInventory());
                }
                if (socketedItem.getInnerItem(1) != null) {
                    socketedItem.getInnerItem(1).addYourself(model.getParty().getInventory());
                }
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });
        content.add(new SelectableListContent(xStart + getWidth() / 2 - 1, yStart + getHeight() - 1, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                WeaponPairingDialog.this.setTimeToTransition(true);
                if (preview != null) {
                    preview.addYourself(model.getParty().getInventory());
                }
            }

            @Override
            public boolean isEnabled(Model model) {
                return preview != null;
            }
        });
        return content;
    }

    private WeaponPair makePreview() {
        SocketedItem socketedItem = getSocketItem();
        if (socketedItem.getInnerItem(0) == null || socketedItem.getInnerItem(1) == null) {
            return null;
        }
        return new WeaponPair((Weapon) socketedItem.getInnerItem(0),
                (Weapon) socketedItem.getInnerItem(1));
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> list = super.buildDecorations(model, xStart, yStart);
        list.add(new DrawableObject(xStart + 4, yStart+12) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                print(model.getScreenHandler(), x, y, "Result:");
                int finalY = y+2;
                if (preview != null) {
                    preview.drawYourself(model.getScreenHandler(), x, finalY);
                    InventoryView.printItemText(model, preview, x + 6, finalY);
                } else {
                    model.getScreenHandler().clearSpace(x, x+4, finalY, finalY + 4);
                    model.getScreenHandler().put(x, finalY, Item.EMPTY_ITEM_SPRITE);
                }
            }
        });
        return list;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        super.handleKeyEvent(keyEvent, model);
        preview = makePreview();
    }

    @Override
    public void transitionedTo(Model model) {
        model.getTutorial().weaponPairing(model);
        System.out.println("Removed main handed weapon from inventory");
        model.getParty().getInventory().remove(mainWeapon);
    }

    private static class IntermediateWeaponPair implements SocketedItem {
        private List<Weapon> weapons;

        public IntermediateWeaponPair(Weapon weapon1) {
            weapons = new ArrayList<>();
            weapons.add(weapon1);
            weapons.add(null);
        }

        @Override
        public int getNumberOfSockets() {
            return 2;
        }

        @Override
        public Item getInnerItem(int index) {
            return weapons.get(index);
        }

        @Override
        public void setInnerItem(int index, Item it) {
            weapons.set(index, (Weapon)it);
        }

        @Override
        public String getName() {
            return "Paired Weapons";
        }

        @Override
        public String getSocketLabels() {
            return "Main hand   Off-hand   ";
        }

        @Override
        public List<Item> getItemsForSlot(Model model, int index) {
            List<Item> weapons = new ArrayList<>(model.getParty().getInventory().getWeapons());
            weapons.removeIf((Item w) -> !(w instanceof PairableWeapon && ((PairableWeapon) w).pairingAllowed()));
            return weapons;
        }
    }
}
