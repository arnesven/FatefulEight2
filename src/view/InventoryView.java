package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.*;
import model.items.spells.DragonTamingSpell;
import model.items.spells.Spell;
import sound.SoundEffects;
import util.Arithmetics;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.FixedPositionSelectableListMenu;
import view.party.SelectableListMenu;
import view.sprites.AnimatedCharSprite;
import view.sprites.MovingRightArrow;
import view.widget.ItemTab;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class InventoryView extends SelectableListMenu {
    private static final int WIDTH = 48;
    private static final int HEIGHT = 30;
    private static AnimatedCharSprite arrowSprite = new MovingRightArrow(MyColors.WHITE, MyColors.BLUE);
    private static ItemTab[] tabNames = makeTabs();
    private int selectedTab = 0;

    public InventoryView(GameView previous) {
        super(previous, WIDTH-29, HEIGHT-4);
    }

    @Override
    protected void clearPreviousForeground(Model model, int xStart, int yStart) {
        model.getScreenHandler().clearForeground(xStart, xStart + WIDTH, yStart-2, yStart + HEIGHT);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected boolean isValid(Model model) {
        return model.getParty().size() > 0;
    }

    @Override
    protected int getXStart() {
        return super.getXStart()-13;
    }

    @Override
    protected int getYStart() {
        return super.getYStart()+1;
    }

    @Override
    protected void drawNonScrollingParts(Model model, int x, int y) {
        BorderFrame.drawFrame(model.getScreenHandler(),
                x-1, y-3, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, false);

        int offset = 0;
        for (int i = 0; i < tabNames.length; ++i) {
            int row = y-2;
            if (i > 4) {
                row++;
            }
            int col = x+offset;
            print(model.getScreenHandler(), col, row, " " + tabNames[i].getName() + " ");
            if (selectedTab == i) {
                model.getScreenHandler().put(col, row, arrowSprite);
            }
            offset += tabNames[i].getName().length() + 2;
            if (i == 4) {
                offset = 0;
            }
        }

        int rightTabX = x + 20;
        BorderFrame.drawFrame(model.getScreenHandler(),
                rightTabX, y, WIDTH - 22, HEIGHT - 4,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        if (tabNames[selectedTab].getItems(model).size() > 0) {
            Item it = tabNames[selectedTab].getItems(model).get(getSelectedRow());
            it.drawYourself(model.getScreenHandler(), rightTabX + 11, y + 2);
            String text = it.getName() + ", " + it.getShoppingDetails() +
                    ", Value: " + it.getCost() +
                    ", Weight: " + (it.getWeight() / 1000.0) + " Kg";
            String[] parts = text.split(", ");
            int row = y + it.getSpriteSize() + 3;
            for (String s : parts) {
                String[] innerParts = MyStrings.partition(s, 24);
                for (String s2 : innerParts) {
                    print(model.getScreenHandler(), rightTabX + 1, row++, s2);
                }
            }
            row++;
            if (it.isAnalyzable()) {
                print(model.getScreenHandler(), rightTabX+1, row++, it.getAnalysisType() + ":");
                List<DrawableObject> objs = (it.getAnalysisDialog(model)).getAnalysisDrawableObjects(model, it, rightTabX, row);
                for (DrawableObject obj : objs) {
                    obj.drawYourself(model, obj.position.x, obj.position.y);
                }
            }
        }
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return new ArrayList<>();
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> contents = new ArrayList<>();
        int row = yStart+1;
        for (Item it : tabNames[selectedTab].getItems(model)) {
            if (it.canBeUsedFromMenu()) {
                contents.add(new SelectableListContent(xStart + 1, row++, makeItemTitle(it)) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new EquipItemMenu(InventoryView.this, x, y, it), model);
                    }
                });
            } else {
                contents.add(new ListContent(xStart + 1, row++, makeItemTitle(it)));
            }
        }
        if (tabNames[selectedTab].getItems(model).size() == 0) {
            contents.add(new ListContent(xStart+1, yStart+1, "*No Items*"));
        }
        return contents;
    }

    private String makeItemTitle(Item it) {
        String result = it.getName();
        for (String prefix : Item.TIER_PREFIXES) {
            if (it.getName().contains(prefix)) {
                result = result.replace(prefix, prefix.substring(0, 3).toUpperCase());
                break;
            }
        }
        return result.substring(0, Math.min(result.length(), WIDTH - 26));
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedTab = Arithmetics.incrementWithWrap(selectedTab, tabNames.length);
            checkForSelectedRowReset(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedTab = Arithmetics.decrementWithWrap(selectedTab, tabNames.length);
            checkForSelectedRowReset(model);
            madeChanges();
        }
    }

    private static ItemTab[] makeTabs() {
        return new ItemTab[]{
                new ItemTab("All") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        List<Item> result = new ArrayList<>(model.getParty().getInventory().getAllItems());
                        addResources(model, result);
                        return result;
                    }
                },
                new ItemTab("Weapons") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return model.getParty().getInventory().getWeapons();
                    }
                },
                new ItemTab("Clothing") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return model.getParty().getInventory().getClothing();
                    }
                },
                new ItemTab("Accessories") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return model.getParty().getInventory().getAccessories();
                    }
                },
                new ItemTab("Spells  ") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return model.getParty().getInventory().getSpells();
                    }
                },
                new ItemTab("Potions") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return model.getParty().getInventory().getPotions();
                    }
                },
                new ItemTab("Parchments") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        List<Item> result = new ArrayList<>(model.getParty().getInventory().getRecipes());
                        result.addAll(model.getParty().getInventory().getCraftingDesigns());
                        result.addAll(model.getParty().getInventory().getScrolls());
                        result.addAll(model.getParty().getInventory().getBooks());
                        return result;
                    }
                },
                new ItemTab("Mounts") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        List<Item> mounts = new ArrayList<>(model.getParty().getHorseHandler().getHorsesAsItems());
                        mounts.addAll(DragonTamingSpell.tamedDragonsAsItems(model));
                        return mounts;
                    }
                },
                new ItemTab("Other           ") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        List<Item> result = new ArrayList<>();
                        addResources(model, result);
                        result.addAll(model.getParty().getInventory().getPearls());
                        result.addAll(model.getParty().getInventory().getFish());
                        result.addAll(model.getParty().getInventory().getParcels());
                        return result;
                    }
                }};
    }

    private static void addResources(Model model, List<Item> result) {
        if (model.getParty().getInventory().getLockpicks() > 0) {
            result.add(0, new LockpicksDummyItem(model.getParty().getInventory().getLockpicks()));
        }
        if (model.getParty().getInventory().getMaterials() > 0) {
            result.add(0, new MaterialsDummyItem(model.getParty().getInventory().getMaterials()));
        }
        if (model.getParty().getInventory().getIngredients() > 0) {
            result.add(0, new IngredientsDummyItem(model.getParty().getInventory().getIngredients()));
        }
        if (model.getParty().getFood() > 0) {
            result.add(0, new FoodDummyItem(model.getParty().getFood()));
        }
        if (model.getParty().getObols() > 0) {
            result.add(0, new ObolsDummyItem(model.getParty().getObols()));
        }
        if (model.getParty().getGold() > 0) {
            result.add(0, new GoldDummyItem(model.getParty().getGold()));
        }
    }

    private class EquipItemMenu extends FixedPositionSelectableListMenu {
        private final Item itemToEquip;

        public EquipItemMenu(SelectableListMenu selectableListContent, int x, int y, Item it) {
            super(selectableListContent, 12, 9, x, y);
            this.itemToEquip = it;
        }

        @Override
        public void transitionedFrom(Model model) {

        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            String label = "Equip";
            if (itemToEquip instanceof Spell || itemToEquip instanceof Scroll) {
                label = "Cast";
            } else if (itemToEquip instanceof UsableItem) {
                label = ((UsableItem)itemToEquip).getUsageVerb();
            }
            return List.of(new TextDecoration(label+"?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int i = 2;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (model.getParty().getBench().contains(gc)) {
                    continue;
                }
                content.add(new SelectableListContent(xStart + 1, yStart + i, gc.getFirstName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        if (itemToEquip instanceof EquipableItem) {
                            String errorMessage = Equipment.canEquip(itemToEquip, gc);
                            if (errorMessage.equals("")) {
                                ((EquipableItem) itemToEquip).equipYourself(gc);
                                setTimeToTransition(true);
                                InventoryView.this.checkForSelectedRowReset(model);
                                SoundEffects.playSound(itemToEquip.getSound());
                            } else {
                                setInnerMenu(new SimpleMessageView(EquipItemMenu.this, errorMessage), model);
                            }
                        } else if (itemToEquip instanceof Spell) {
                            setInnerMenu(new SimpleMessageView(EquipItemMenu.this,
                                    ((Spell) itemToEquip).castFromMenu(model, gc)), model);
                        } else if (itemToEquip instanceof Scroll) {
                            setInnerMenu(new SimpleMessageView(EquipItemMenu.this,
                                    ((Scroll) itemToEquip).castFromMenu(model, gc)), model);
                        } else if (itemToEquip.opensViewFromInventoryMenu()) {
                            setTimeToTransition(true);
                            model.transitionToDialog(itemToEquip.getViewFromInventoryMenu(model, InventoryView.this, itemToEquip));
                        } else if (itemToEquip instanceof UsableItem) {
                            if (((UsableItem) itemToEquip).canBeUsedOn(model, gc)) {
                                String message = ((UsableItem) itemToEquip).useYourself(model, gc);
                                if (((UsableItem)itemToEquip).removeAfterUse()) {
                                    model.getParty().getInventory().remove(itemToEquip);
                                }
                                checkForSelectedRowReset(model);
                                InventoryView.this.checkForSelectedRowReset(model);
                                if (!message.equals("")) {
                                    InventoryView.this.setInnerMenu(new SimpleMessageView(EquipItemMenu.this, message), model);
                                }
                                setTimeToTransition(true);
                            } else {
                                InventoryView.this.setInnerMenu(new SimpleMessageView(EquipItemMenu.this, itemToEquip.getName() + " cannot be used on " + gc.getName() + "."), model);
                            }
                        } else {
                            setTimeToTransition(true);
                        }
                    }
                });
                i++;
            }
            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }
    }
}
