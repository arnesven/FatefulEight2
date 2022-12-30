package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.EquipableItem;
import model.items.Equipment;
import model.items.Item;
import model.items.spells.Spell;
import util.Arithmetics;
import util.MyStrings;
import view.party.SelectableListMenu;
import view.sprites.AnimatedCharSprite;
import view.sprites.MovingRightArrow;
import view.widget.ItemTab;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellsView extends SelectableListMenu {
    private static final int ROW_HEIGHT = 9;
    private static final int COLUMN_WIDTH = 20;
    private static AnimatedCharSprite arrowSprite = new MovingRightArrow(MyColors.WHITE, MyColors.BLUE);
    private ItemTab[] tabNames;
    private int selectedTab = 0;
    private Map<MyColors, List<Spell>> spellMap = new HashMap<>();

    public SpellsView(GameView previous) {
        super(previous, 44, 28);
        tabNames = makeTabs();
    }

    @Override
    protected void drawNonScrollingParts(Model model, int x, int y) {
        int offset = 1;
        for (int i = 0; i < tabNames.length; ++i) {
            print(model.getScreenHandler(), x+offset, y+1, " " + tabNames[i].getName() + " ");
            if (selectedTab == i) {
                model.getScreenHandler().put(x+offset, y+1, arrowSprite);
            }
            offset += tabNames[i].getName().length() + 2;
        }
        print(model.getScreenHandler(), x+1, y+2, "___________________________________________");

    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        for (MyColors color : Spell.spellColors) {
            spellMap.put(color, new ArrayList<>());
        }

        for (Spell spell : model.getParty().getInventory().getSpells()) {
            boolean found = false;
            for (Spell sp : spellMap.get(spell.getColor())) {
                if (sp.getName().equals(spell.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                spellMap.get(spell.getColor()).add(spell);
            }
        }

        int max = 0;
        int bestIndex = 0;
        int i = 0;
        for (MyColors color : Spell.spellColors) {
            if (spellMap.get(color).size() > max) {
                bestIndex = i;
                max = spellMap.get(color).size();
            }
            i++;
        }
        selectedTab = bestIndex;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> result = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (Item item : tabNames[selectedTab].getItems(model)) {
            result.add(new DrawableObject(xStart + col * COLUMN_WIDTH + 2, yStart + row * ROW_HEIGHT + 3) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    item.drawYourself(model.getScreenHandler(), x, y);
                    Spell sp = (Spell)item;
                    int i = 1;
                    for (String s : MyStrings.partition(sp.getShoppingDetails(), 16)) {
                        print(model.getScreenHandler(),x + 5, y + (i++), s);
                    }
                }
            });
            ++row;
            if (row > 2) {
                row = 0;
                col++;
            }
        }
        return result;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (Item item : tabNames[selectedTab].getItems(model)) {
            result.add(new SelectableListContent(xStart + col*COLUMN_WIDTH + 7, yStart + row*ROW_HEIGHT + 3, item.getName()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new CastByWhomMenu(SpellsView.this, x, y, (Spell)item), model);
                }
            });
            ++row;
            if (row > 2) {
                row = 0;
                col++;
            }
        }
        if (tabNames[selectedTab].getItems(model).isEmpty()) {
            result.add(new ListContent(xStart + 1, yStart + 3, "*No Spells*"));
        }
        return result;
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

    private ItemTab[] makeTabs() {
        return new ItemTab[]{
                new ItemTab("White|") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return spellMap.get(MyColors.WHITE);
                    }
                },
                new ItemTab("Red  |") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return spellMap.get(MyColors.RED);
                    }
                },
                new ItemTab("Blue  |") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return spellMap.get(MyColors.BLUE);
                    }
                },
                new ItemTab("Green|") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return spellMap.get(MyColors.GREEN);
                    }
                },
                new ItemTab("Black") {
                    @Override
                    public List<? extends Item> getItems(Model model) {
                        return spellMap.get(MyColors.BLACK);
                    }
                }};
    }

    private class CastByWhomMenu extends SelectableListMenu {
        private final Spell spell;

        public CastByWhomMenu(SpellsView spellsView, int x, int y, Spell item) {
            super(spellsView, 12, 9);
            this.spell = item;
        }

        @Override
        public void transitionedFrom(Model model) {

        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new TextDecoration("Cast?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int i = 2;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                content.add(new SelectableListContent(xStart + 1, yStart + i, gc.getFirstName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        if (!model.getSpellHandler().tryCast(spell, gc)) {
                            setInnerMenu(new SimpleMessageView(CastByWhomMenu.this,
                                    "You cannot cast " + spell.getName() + " right now."), model);
                        } else {
                            SpellsView.this.setInnerMenu(new SimpleMessageView(CastByWhomMenu.this,
                                    gc.getFirstName() + " is casting " + spell.getName() + "..."), model);
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
