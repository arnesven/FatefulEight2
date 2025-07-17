package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.spells.MasterySpell;
import model.items.spells.Spell;
import util.Arithmetics;
import util.MyStrings;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.ArrowSprites;
import view.widget.ItemTab;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellsView extends SelectableListMenu {
    private static final int ROW_HEIGHT = 9;
    private static final int COLUMN_WIDTH = 23;
    private static final int NUMBER_OF_ROWS = 4;

    private ItemTab[] tabNames;
    private int selectedTab = 0;
    private Map<MyColors, List<Spell>> spellMap = new HashMap<>();

    public SpellsView(GameView previous) {
        super(previous, 3 * COLUMN_WIDTH + 4, 37);
        tabNames = makeTabs();
    }

    @Override
    protected void drawNonScrollingParts(Model model, int x, int y) {
        int offset = 1;
        for (int i = 0; i < tabNames.length; ++i) {
            print(model.getScreenHandler(), x+offset, y+1, " " + tabNames[i].getName() + " ");
            if (selectedTab == i) {
                model.getScreenHandler().put(x+offset, y+1, ArrowSprites.MOVING_RIGHT_BLUE);
            }
            offset += tabNames[i].getName().length() + 2;
        }
        print(model.getScreenHandler(), x+1, y+2, "________________________________________________________________________");
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        for (MyColors color : Spell.spellColors) {
            spellMap.put(color, new ArrayList<>());
        }

        for (Spell spell : model.getParty().getSpells()) {
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
        for (Item item : tabNames[selectedTab].getItemsFromSource(model)) {
            result.add(new DrawableObject(xStart + col * COLUMN_WIDTH + 2, yStart + row * ROW_HEIGHT + 3) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    item.drawYourself(model.getScreenHandler(), x, y);
                    Spell sp = (Spell)item;
                    print(model.getScreenHandler(), x, y+4, ""+sp.getDifficulty());
                    BorderFrame.drawString(model.getScreenHandler(), ""+sp.getHPCost(), x+3, y+4, MyColors.RED, MyColors.BLUE);
                    int i = 1;
                    for (String s : MyStrings.partition(sp.getDescription(), COLUMN_WIDTH-4)) {
                        print(model.getScreenHandler(),x + 5, y + (i++), s);
                    }
                    if (sp instanceof MasterySpell && ((MasterySpell) sp).masteriesEnabled()) {
                        BorderFrame.drawString(model.getScreenHandler(), "M", x, y+5, MyColors.GOLD, MyColors.BLUE);
                    }
                }
            });
            ++row;
            if (row > NUMBER_OF_ROWS-1) {
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
        for (Item item : tabNames[selectedTab].getItemsFromSource(model)) {
            int finalX = xStart + col*COLUMN_WIDTH + 7;
            int finalY = yStart + row*ROW_HEIGHT + 3;
            result.add(new SelectableListContent(finalX, finalY, item.getName()) {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SpellMidMenu(SpellsView.this, finalX + 3, finalY + 1, (Spell)item), model);
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
                }
            });
            ++row;
            if (row > NUMBER_OF_ROWS-1) {
                row = 0;
                col++;
            }
        }
        if (tabNames[selectedTab].getItemsFromSource(model).isEmpty()) {
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
                new ItemTab("White |") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(MyColors.WHITE);
                    }
                },
                new ItemTab("Red   |") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(MyColors.RED);
                    }
                },
                new ItemTab("Blue  |") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(MyColors.BLUE);
                    }
                },
                new ItemTab("Green |") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(MyColors.GREEN);
                    }
                },
                new ItemTab("Black |") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(MyColors.BLACK);
                    }
                },
                new ItemTab("Colorless") {
                    @Override
                    public List<? extends Item> getItemsFromSource(Model model) {
                        return spellMap.get(Spell.COLORLESS);
                    }
                }};
    }

    private static class CastByWhomMenu extends SelectableListMenu {
        private final Spell spell;
        private final int xStart;
        private final int yStart;

        public CastByWhomMenu(SelectableListMenu spellsView, int x, int y, Spell item) {
            super(spellsView, 12, 10);
            this.spell = item;
            this.xStart = x + 1;
            this.yStart = y + 1;
        }

        @Override
        protected int getXStart() {
            return xStart;
        }

        @Override
        protected int getYStart() {
            return yStart;
        }

        @Override
        public void transitionedFrom(Model model) { }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new TextDecoration("By whom?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int i = 2;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (!model.getParty().getBench().contains(gc)) {
                    content.add(new SelectableListContent(xStart + 1, yStart + i, gc.getFirstName()) {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new SimpleMessageView(CastByWhomMenu.this, spell.castFromMenu(model, gc)), model);
                        }
                    });
                    i++;
                }
            }
            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }
    }

    private class SpellMidMenu extends SelectableListMenu {
        private final Spell spell;
        private final int xStart;
        private final int yStart;

        public SpellMidMenu(SelectableListMenu previous, int xStart, int yStart, Spell spell) {
            super(previous, 10, 4);
            this.spell = spell;
            this.xStart = xStart;
            this.yStart = yStart;
        }

        @Override
        protected int getXStart() {
            return xStart;
        }

        @Override
        protected int getYStart() {
            return yStart;
        }

        @Override
        public void transitionedFrom(Model model) { getPrevious().setTimeToTransition(true);}

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            content.add(new SelectableListContent(xStart + 1, yStart + 1, "Cast") {
                           @Override
                           public void performAction(Model model, int x, int y) {
                               setInnerMenu(new CastByWhomMenu(SpellMidMenu.this, x, y, spell), model);
                           }

                            @Override
                            public boolean isEnabled(Model model) {
                                return model.getParty().getBench().size() < model.getParty().size();
                            }
            });
            content.add(new SelectableListContent(xStart + 1, yStart + 2, "Analyze") {
                            @Override
                            public void performAction(Model model, int x, int y) {
                                setInnerMenu(new AnalyzeSpellDialog(model, spell), model);
                            }
                            @Override
                            public boolean isEnabled(Model model) {
                                return true;
                            }
                        });
            if (spell instanceof MasterySpell && ((MasterySpell) spell).masteriesEnabled()) {
                content.add(new SelectableListContent(xStart + 1, yStart + 3, "Masteries") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new MasteriesView(model, (MasterySpell)spell), model);
                    }
                    @Override
                    public boolean isEnabled(Model model) {
                        return true;
                    }
                });
            }

            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }
}
