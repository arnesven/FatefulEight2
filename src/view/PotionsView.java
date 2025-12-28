package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.ItemDeck;
import model.items.PotionRecipe;
import model.items.potions.*;
import model.items.spells.AlchemySpell;
import model.items.spells.Spell;
import util.Arithmetics;
import util.MyLists;
import util.MyPair;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class PotionsView extends SelectableListMenu {
    private static final int VIEW_WIDTH = 54;
    private static final int VIEW_HEIGHT = 38;
    private static final int COLUMN_WIDTH = 14;
    private static final int ROW_HEIGHT = 9;

    private List<String> headers;
    private List<List<MyPair<Potion, Integer>>> allLists;

    private Set<String> recipes;
    private int otherPotionsCount;
    private Spell alchemy;
    private int currentTier;

    public PotionsView(GameView previous) {
        super(previous, VIEW_WIDTH, VIEW_HEIGHT);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        currentTier = model.getItemDeck().getStandardItemTier();
        countPotions(model);
        findRecipes(model);

        Spell templateAlchemy = new AlchemySpell();
        alchemy = MyLists.find(model.getParty().getLearnedSpells(), sp -> sp.getName().equals(templateAlchemy.getName()));
        if (alchemy == null) {
            alchemy = MyLists.find(model.getParty().getSpells(), sp -> sp.getName().equals(templateAlchemy.getName()));
        }
        setSelectedRow(findFirstNonZeroRow());
    }

    private List<List<MyPair<Potion, Integer>>> makeAllLists(Model model) {
        List<MyPair<Potion, Integer>> conditionRemedies = List.of(
                new MyPair<>(new AntidotePotion(), 0),
                new MyPair<>(new RevivingElixir(), 0),
                new MyPair<>(new AntiParalysisPotion(), 0)
        );

        List<MyPair<Potion, Integer>> attributePotions = List.of(
                new MyPair<>(new StrengthPotion(), 0),
                new MyPair<>(new DexterityPotion(), 0),
                new MyPair<>(new CharismaPotion(), 0),
                new MyPair<>(new WitsPotion(), 0));

        List<MyPair<Potion, Integer>> miscPotions = List.of(
                new MyPair<>(new InvisibilityPotion(), 0),
                new MyPair<>(new SleepingPotion(), 0),
                new MyPair<>(new UnstablePotion(), 0),
                new MyPair<>(new CommonPoison(), 0)
        );

        return List.of(makeBasicPotionsList(), conditionRemedies, attributePotions, miscPotions);
    }

    private List<MyPair<Potion, Integer>> makeBasicPotionsList() {
        List<MyPair<Potion, Integer>> basicPotions = List.of(
                new MyPair<>(new HealthPotion(), 0),
                new MyPair<>(new StaminaPotion(), 0),
                new MyPair<>(new RejuvenationPotion(), 0));
        if (currentTier > 0) {
            basicPotions = List.of(
                    new MyPair<>((Potion)new HealthPotion().makeHigherTierCopy(currentTier), 0), // IF standard tier > 0, these should be higher tier.
                    new MyPair<>((Potion)new StaminaPotion().makeHigherTierCopy(currentTier), 0),
                    new MyPair<>((Potion)new RejuvenationPotion().makeHigherTierCopy(currentTier), 0));
        }
        return basicPotions;
    }

    private int findFirstNonZeroRow() {
        int row = 0;
        for (List<MyPair<Potion, Integer>> sublist : allLists) {
            for (MyPair<Potion, Integer> p : sublist) {
                if (p.second > 0) {
                    return row;
                }
                row++;
            }
        }
        return row;
    }

    private void findRecipes(Model model) {
        recipes = new HashSet<>();
        for (PotionRecipe pr : model.getParty().getLearnedPotionRecipes()) {
            recipes.add(pr.getBrewable().getName());
        }
        for (PotionRecipe pr : model.getParty().getPotionRecipes()) {
            recipes.add(pr.getBrewable().getName());
        }

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> decorations = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (List<MyPair<Potion, Integer>> subList : allLists) {
            decorations.add(new TextDecoration(headers.get(row), xStart + 1, yStart + 1 + row * ROW_HEIGHT,
                    MyColors.WHITE, MyColors.BLUE, false));
            for (MyPair<Potion, Integer> pair : subList) {
                Point drawPos = convertToScreen(col, row);
                if (isKnown(pair)) {
                    decorations.add(new DrawableObject(xStart + drawPos.x, yStart + drawPos.y + 2) {
                        @Override
                        public void drawYourself(Model model, int x, int y) {
                            pair.first.drawYourself(model.getScreenHandler(), x, y);
                        }
                    });
                    decorations.add(new TextDecoration("Own " + pair.second,
                            xStart + drawPos.x + 5, yStart + drawPos.y + 2,
                            MyColors.WHITE, MyColors.BLUE, false));
                    if (hasAlchemy()) {
                        decorations.add(new TextDecoration(AlchemySpell.calcCost(pair.first, hasRecipe(pair.first)) + " " + (char)0x12,
                                xStart + drawPos.x + 5, yStart + drawPos.y + 3,
                                MyColors.WHITE, MyColors.BLUE, false));
                    }
                    if (hasRecipe(pair.first)) {
                        decorations.add(new TextDecoration("Recipe",
                                xStart + drawPos.x + 5, yStart + drawPos.y + 4,
                                MyColors.WHITE, MyColors.BLUE, false));
                    }
                    String[] parts = pair.first.getName().split(" ");
                    for ( int i = 1; i < parts.length; ++i) {
                        decorations.add(new TextDecoration(parts[i], xStart + drawPos.x, yStart + drawPos.y + 6 + i,
                                MyColors.LIGHT_GRAY, MyColors.BLUE, false));
                    }
                } else {
                    decorations.add(new DrawableObject(xStart + drawPos.x, yStart + drawPos.y + 2) {
                        @Override
                        public void drawYourself(Model model, int x, int y) {
                            model.getScreenHandler().clearSpace(x, x+4, y, y + 4);
                            model.getScreenHandler().put(x, y, Item.EMPTY_ITEM_SPRITE);
                        }
                    });
                }
                col++;
            }
            row++;
            col = 0;
        }
        decorations.add(new TextDecoration("Other Potions: " + otherPotionsCount + ", (F3 = Change Tier)",
                xStart + 1, yStart + VIEW_HEIGHT-1, MyColors.WHITE, MyColors.BLUE, false));
        return decorations;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        int row = 0;
        int col = 0;
        for (List<MyPair<Potion, Integer>> subList : allLists) {
            for (MyPair<Potion, Integer> pair : subList) {
                String[] parts = pair.first.getName().split(" ");
                Point drawPos = convertToScreen(col, row);
                if (isKnown(pair)) {
                    content.add(new SelectableListContent(
                            xStart + drawPos.x, yStart + drawPos.y + 6, parts[0]) {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new InnerPotionsMenu(PotionsView.this, pair,
                                    xStart + drawPos.x + 2, yStart + drawPos.y + 6), model);
                        }
                    });
                } else {
                    content.add(new SelectableListContent(
                            xStart + drawPos.x, yStart + drawPos.y + 6, "???") {
                        @Override
                        public void performAction(Model model, int x, int y) {};

                        @Override
                        public boolean isEnabled(Model model) {
                            return false;
                        }
                    });
                }
                col++;
            }
            row++;
            col = 0;
        }
        return content;
    }

    private boolean isKnown(MyPair<Potion, Integer> pair) {
        return pair.second > 0 || hasRecipe(pair.first);
    }

    private boolean hasRecipe(Potion first) {
        return recipes.contains(first.getName());
    }

    private boolean hasAlchemy() {
        return alchemy != null;
    }

    private Point convertToScreen(int col, int row) {
        return new Point( + 1 + col * COLUMN_WIDTH, row * ROW_HEIGHT);
    }

    private void countPotions(Model model) {
        System.out.println("Counting potions");
        otherPotionsCount = 0;
        allLists = makeAllLists(model);
        headers = List.of("Basic Drafts (Tier " + currentTier + ")", "Condition Remedies", "Attribute Boosters", "Miscellaneous");
        Map<String, MyPair<Potion, Integer>> countMap = new HashMap<>();

        for (List<MyPair<Potion, Integer>> subList : allLists) {
            for (MyPair<Potion, Integer> pair : subList) {
                countMap.put(pair.first.getName(), pair);
            }
        }

        for (Potion p : model.getParty().getInventory().getPotions()) {
            if (countMap.containsKey(p.getName())) {
                countMap.get(p.getName()).second = countMap.get(p.getName()).second + 1;
            } else {
                otherPotionsCount++;
            }
        }

    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F3) {
            currentTier = Arithmetics.incrementWithWrap(currentTier, Item.MAX_TIER + 1);
            countPotions(model);
            madeChanges();
        }
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    private class InnerPotionsMenu extends SelectableListMenu {
        private final int xStart;
        private final int yStart;
        private final Potion potion;
        private final Integer count;

        public InnerPotionsMenu(PotionsView prev, MyPair<Potion, Integer> pair, int xStart, int yStart) {
            super(prev, 8, 5);
            this.xStart = xStart;
            this.yStart = yStart;
            this.potion = pair.first;
            this.count = pair.second;
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
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();

            content.add(new SelectableListContent(xStart + 1, yStart + 1, "Info") {
                               @Override
                               public void performAction(Model model, int x, int y) {
                                   setInnerMenu(new PotionInfoDialog(PotionsView.this, potion), model);
                               }
                           });
            content.add(
                    new SelectableListContent(xStart + 1, yStart + 2, "Use") {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new UsePotionMenu(PotionsView.this, potion, x+3, y-3), model);
                        }

                        @Override
                        public boolean isEnabled(Model model) {
                            return super.isEnabled(model) && count > 0;
                        }
                    });
            if (hasAlchemy()) {
                content.add(new SelectableListContent(xStart + 1, yStart + 3, "Brew") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new CastAlchemyMenu(PotionsView.this, potion, x+3, y-3, false), model);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return super.isEnabled(model) && AlchemySpell.calcCost(potion, hasRecipe(potion))
                                <= model.getParty().getInventory().getIngredients();
                    }
                });
                content.add(new SelectableListContent(xStart + 1, yStart + 4, "Distill") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new CastAlchemyMenu(PotionsView.this, potion, x+3, y-3, true), model);
                    }
                });
            }
            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }

        @Override
        public void transitionedFrom(Model model) {

        }
    }

    private class UsePotionMenu extends SelectableListMenu {

        private final String potionName;
        private final int xStart;
        private final int yStart;
        private Potion potion;

        public UsePotionMenu(PotionsView potionsView, Potion potion, int x, int y) {
            super(potionsView, 12, 9);
            this.potionName = potion.getName();
            this.xStart = x;
            this.yStart = y;
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
        public void transitionedTo(Model model) {
            super.transitionedTo(model);
            this.potion = MyLists.find(model.getParty().getInventory().getPotions(), p -> p.getName().equals(potionName));
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int row = 0;
            for (GameCharacter gc : MyLists.filter(model.getParty().getPartyMembers(), gc -> !model.getParty().getBench().contains(gc))) {
                content.add(new SelectableListContent(xStart + 1, yStart + 1 + row, gc.getFirstName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        if (potion.canBeUsedOn(model, gc)) {
                            String message = potion.useYourself(model, gc);
                            if (potion.removeAfterUse()) {
                                model.getParty().removeFromInventory(potion);
                            }
                            countPotions(model);
                            checkForSelectedRowReset(model);
                            if (!message.equals("")) {
                                PotionsView.this.setInnerMenu(new SimpleMessageView(UsePotionMenu.this, message), model);
                            }
                            setTimeToTransition(true);
                        } else {
                            PotionsView.this.setInnerMenu(new SimpleMessageView(UsePotionMenu.this, potion.getName() + " cannot be used on " + gc.getName() + "."), model);
                        }
                    }
                });
                row++;
            }
            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }

        @Override
        public void transitionedFrom(Model model) {

        }
    }

    private class CastAlchemyMenu extends SelectableListMenu {
        private final String potionName;
        private final int xStart;
        private final int yStart;
        private final boolean isDistill;
        private Potion potion;

        public CastAlchemyMenu(PotionsView potionsView, Potion potion, int x, int y, boolean isDistill) {
            super(potionsView, 12, 10);
            this.potionName = potion.getName();
            this.xStart = x;
            this.yStart = y;
            this.isDistill = isDistill;
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
        public void transitionedTo(Model model) {
            super.transitionedTo(model);
            this.potion = MyLists.find(model.getParty().getInventory().getPotions(), p -> p.getName().equals(potionName));
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new TextDecoration("By whom?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int row = 1;
            for (GameCharacter gc : MyLists.filter(model.getParty().getPartyMembers(), gc -> !model.getParty().getBench().contains(gc))) {
                content.add(new SelectableListContent(xStart + 1, yStart + 1 + row, gc.getFirstName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        ((AlchemySpell)alchemy).setPresetPotion(potion, isDistill);
                        setInnerMenu(new SimpleMessageView(CastAlchemyMenu.this, alchemy.castFromMenu(model, gc)), model);
                    }
                });
                row++;
            }
            return content;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }

        @Override
        public void transitionedFrom(Model model) {

        }
    }
}
