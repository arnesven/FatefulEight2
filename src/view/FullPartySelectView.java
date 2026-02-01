package view;

import model.headquarters.Headquarters;
import model.items.special.MagicMirror;
import model.journal.*;
import model.mainstory.MainStoryStep;
import model.Model;
import model.characters.GameCharacter;
import model.characters.special.*;
import model.horses.Pony;
import model.horses.Regal;
import model.items.Equipment;
import model.items.HorseStartingItem;
import model.items.Item;
import model.items.ItemDeck;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import model.map.UrbanLocation;
import util.Arithmetics;
import util.MyLists;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static view.party.CharacterCreationView.NOT_OK_SPRITE;

public class FullPartySelectView extends SelectableListMenu {
    private static final int ROWS_PER_CHAR = 6;
    private static final int COLUMN_SKIP = 36;
    private static final int MAX_GOLD = 100000;
    private static final int INVENTORY_TAB = 9;
    private static final int INVENTORY_VSKIP = 13;
    private static final int MAX_NOTORIETY = 1000;
    private final int maxCharacters;
    private final Model model;
    private final int[] selectedCharacters;
    private final List<GameCharacter> selectableCharacters;
    private boolean canceled = false;
    private final int[] levels;
    private static final int MAX_LEVEL = 12;
    private final Equipment[] equipments;
    private int startingGold = 20;
    private int startingObols = 0;
    private int startingFood = 10;
    private int startingMaterials = 0;
    private int startingIngredients = 0;
    private int startingLockpicks = 0;
    private int startingNotoriety = 0;
    private int expandDirection = 0;
    private int startingDay = 1;
    private int selectedHeadquarters = 0;
    private final List<Item> otherItems = new ArrayList<>();
    private final List<MainStorySpawnLocation> mainStorySpawnLocations = new ArrayList<>(List.of(
            new MainStorySpawnEast(), new MainStorySpawnNorth(), new MainStorySpawnWest(), new MainStorySpawnSouth()));
    private int selectedMainSpawn = 0;
    private MainStoryStep mainStoryProgression = MainStoryStep.NOT_STARTED;
    private final List<String> headquartersLocations;

    public FullPartySelectView(Model model) {
        super(model.getView(), 58, DrawingArea.WINDOW_ROWS-1);
        this.model = model;
        this.selectableCharacters = new ArrayList<>(model.getAllCharacters());
        selectableCharacters.add(new EnchantressCharacter());
        selectableCharacters.add(new RedKnightCharacter());
        selectableCharacters.add(new WitchKingCharacter());
        selectableCharacters.add(new GoblinCharacter());
        selectableCharacters.add(new CaidCharacter());
        selectableCharacters.add(new WillisCharacter());
        this.maxCharacters = selectableCharacters.size();
        this.selectedCharacters = new int[]{maxCharacters, maxCharacters, maxCharacters, maxCharacters,
                        maxCharacters, maxCharacters, maxCharacters, maxCharacters};
        this.levels = new int[]{1,1,1,1,1,1,1,1};
        this.equipments = new Equipment[]{new Equipment(), new Equipment(), new Equipment(), new Equipment(),
                                          new Equipment(), new Equipment(), new Equipment(), new Equipment()};
        mainStorySpawnLocations.add(0, null);
        headquartersLocations = new ArrayList<>();
        headquartersLocations.add("None");
        headquartersLocations.addAll(MyLists.transform(model.getWorld().getLordLocations(),
                UrbanLocation::getPlaceName));
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                y++;
                for (int i = 1; i <= 8; ++i) {
                    BorderFrame.drawString(model.getScreenHandler(), "Character " + i + ":", x + 1, y, MyColors.WHITE, MyColors.BLUE);
                    if (!isVacant(i)) {
                        GameCharacter gc = getSelectedCharacter(i);
                        Sprite spr = gc.getAvatarSprite();
                        model.getScreenHandler().register(spr.getName(), new Point(x+1, y+1), spr);
                        BorderFrame.drawString(model.getScreenHandler(), String.format("%s, %02d HP, SPEED %d", gc.getRace().getName(), gc.getMaxHP(), gc.getSpeed()),
                                x+11, y+1, MyColors.LIGHT_GRAY, MyColors.BLUE);
                        Equipment eq = equipments[i-1];
                        BorderFrame.drawString(model.getScreenHandler(), eq.getWeapon().getName(), x+11, y+2, MyColors.LIGHT_GRAY, MyColors.BLUE);
                        BorderFrame.drawString(model.getScreenHandler(), eq.getClothing().getName(), x+11, y+3, MyColors.LIGHT_GRAY, MyColors.BLUE);
                        if (eq.getAccessory() != null) {
                            BorderFrame.drawString(model.getScreenHandler(), eq.getAccessory().getName(), x + 11, y+4, MyColors.LIGHT_GRAY, MyColors.BLUE);
                        }
                    }
                    y += ROWS_PER_CHAR;
                }

                y = yStart + 2;
                BorderFrame.drawString(model.getScreenHandler(), "Notoriety: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                y++;
                BorderFrame.drawString(model.getScreenHandler(), "World ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Day: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Expanded: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Main Spawn: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Main Story: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);

                y = yStart+INVENTORY_VSKIP;
                BorderFrame.drawString(model.getScreenHandler(), "Inventory ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Gold: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Obols: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Food: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Ingrs: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Mtrls: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Picks: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), "Headquarters: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);
                y++;
                BorderFrame.drawString(model.getScreenHandler(), "Other: ", x + COLUMN_SKIP, y++, MyColors.WHITE, MyColors.BLUE);

                for (Item it : otherItems) {
                    BorderFrame.drawString(model.getScreenHandler(), it.getName(), x + COLUMN_SKIP, y++, MyColors.LIGHT_GRAY, MyColors.BLUE);
                }

                drawChecksNotOk(model, x);
            }
        });
    }

    private GameCharacter getSelectedCharacter(int i) {
        return selectableCharacters.get(selectedCharacters[i-1]);
    }

    private boolean isVacant(int i) {
        return selectedCharacters[i-1] == maxCharacters;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            int finalI = i;
            content.add(new CarouselListContent(xStart + 16, yStart + 2 + finalI * ROWS_PER_CHAR, nameOrVacant(selectedCharacters[finalI])) {
                @Override
                public void turnLeft(Model model) {
                    selectedCharacters[finalI] = Arithmetics.decrementWithWrap(selectedCharacters[finalI], maxCharacters+1);
                }

                @Override
                public void turnRight(Model model) {
                    selectedCharacters[finalI] = Arithmetics.incrementWithWrap(selectedCharacters[finalI], maxCharacters+1);
                }
            });
            content.add(new CarouselListContent(xStart + 7, yStart + 3 + finalI * ROWS_PER_CHAR, classOrNA(finalI+1)) {
                @Override
                public void turnLeft(Model model) {
                    if (isVacant(finalI+1)) {
                        return;
                    }
                    GameCharacter gc = getSelectedCharacter(finalI+1);
                    for (int i = 0; i < gc.getClasses().length; ++i) {
                        if (gc.getClasses()[i] == gc.getCharClass()) {
                            int index = Arithmetics.decrementWithWrap(i, gc.getClasses().length);
                            gc.setClass(gc.getClasses()[index]);
                            break;
                        }
                    }
                }

                @Override
                public void turnRight(Model model) {
                    if (isVacant(finalI+1)) {
                        return;
                    }
                    GameCharacter gc = getSelectedCharacter(finalI+1);
                    for (int i = 0; i < gc.getClasses().length; ++i) {
                        if (gc.getClasses()[i] == gc.getCharClass()) {
                            int index = Arithmetics.incrementWithWrap(i, gc.getClasses().length);
                            gc.setClass(gc.getClasses()[index]);
                            break;
                        }
                    }
                }
            });
            content.add(new CarouselListContent(xStart + 7, yStart + 4  + finalI * ROWS_PER_CHAR, levelOrNA(finalI+1)) {
                @Override
                public void turnLeft(Model model) {
                    if (isVacant(finalI+1)) {
                        return;
                    }
                    levels[finalI] = Arithmetics.decrementWithWrap(levels[finalI]-1, MAX_LEVEL) + 1;
                }

                @Override
                public void turnRight(Model model) {
                    if (isVacant(finalI+1)) {
                        return;
                    }
                    levels[finalI] = Arithmetics.incrementWithWrap(levels[finalI]-1, MAX_LEVEL) + 1;
                }
            });
            content.add(new SelectableListContent(xStart + 6, yStart + 5 + finalI * ROWS_PER_CHAR, "Equip") {
                @Override
                public void performAction(Model model, int x, int y) {
                    setInnerMenu(new SetAllEquipmentMenu(FullPartySelectView.this, equipments[finalI]), model);
                }

                @Override
                public boolean isEnabled(Model model) {
                    return !isVacant(finalI+1);
                }
            });
        }
        int partyRow = yStart + 2;
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB + 4, partyRow++, String.format("%3d", startingNotoriety)) {
            @Override
            public void turnLeft(Model model) {
                startingNotoriety = Arithmetics.decrementWithWrap(startingNotoriety, MAX_NOTORIETY);
            }

            @Override
            public void turnRight(Model model) {
                startingNotoriety = Arithmetics.incrementWithWrap(startingNotoriety, MAX_NOTORIETY);
            }
        });
        partyRow += 2;
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB + 3, partyRow++, String.format("%3d", startingDay) ) {
            @Override
            public void turnLeft(Model model) {
                startingDay = Arithmetics.decrementWithWrap(startingDay, 101);
            }

            @Override
            public void turnRight(Model model) {
                startingDay = Arithmetics.incrementWithWrap(startingDay, 101);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB + 3, partyRow++, worldStateToString(expandDirection) ) {
            @Override
            public void turnLeft(Model model) {
                expandDirection = Arithmetics.decrementWithWrap(expandDirection, 0x10);
            }

            @Override
            public void turnRight(Model model) {
                expandDirection = Arithmetics.incrementWithWrap(expandDirection, 0x10);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB + 5, partyRow++, makeSpawnString()) {
            @Override
            public void turnLeft(Model model) {
                selectedMainSpawn = Arithmetics.decrementWithWrap(selectedMainSpawn, mainStorySpawnLocations.size());
            }

            @Override
            public void turnRight(Model model) {
                selectedMainSpawn = Arithmetics.incrementWithWrap(selectedMainSpawn, mainStorySpawnLocations.size());
            }
        });
        partyRow += 1;

        content.add(new CarouselListContent(xStart + COLUMN_SKIP + 2, partyRow, mainStoryProgression.makeNiceString() ) {
            @Override
            public void turnLeft(Model model) {
                mainStoryProgression = MainStoryStep.values()[Arithmetics.decrementWithWrap(mainStoryProgression.ordinal(), MainStoryStep.values().length)];
            }

            @Override
            public void turnRight(Model model) {
                mainStoryProgression = MainStoryStep.values()[Arithmetics.incrementWithWrap(mainStoryProgression.ordinal(), MainStoryStep.values().length)];
            }
        });
        int inventoryRow = yStart + INVENTORY_VSKIP + 1;
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingGold)) {
            @Override
            public void turnLeft(Model model) {
                startingGold = Arithmetics.decrementWithWrap(startingGold, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingGold = Arithmetics.incrementWithWrap(startingGold, MAX_GOLD);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingObols)) {
            @Override
            public void turnLeft(Model model) {
                startingObols = Arithmetics.decrementWithWrap(startingObols, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingObols = Arithmetics.incrementWithWrap(startingObols, MAX_GOLD);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingFood)) {
            @Override
            public void turnLeft(Model model) {
                startingFood = Arithmetics.decrementWithWrap(startingFood, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingFood = Arithmetics.incrementWithWrap(startingFood, MAX_GOLD);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingIngredients)) {
            @Override
            public void turnLeft(Model model) {
                startingIngredients = Arithmetics.decrementWithWrap(startingIngredients, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingIngredients = Arithmetics.incrementWithWrap(startingIngredients, MAX_GOLD);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingMaterials)) {
            @Override
            public void turnLeft(Model model) {
                startingMaterials = Arithmetics.decrementWithWrap(startingMaterials, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingMaterials = Arithmetics.incrementWithWrap(startingMaterials, MAX_GOLD);
            }
        });
        content.add(new CarouselListContent(xStart + COLUMN_SKIP + INVENTORY_TAB, inventoryRow++, String.format("%5d", startingLockpicks)) {
            @Override
            public void turnLeft(Model model) {
                startingLockpicks = Arithmetics.decrementWithWrap(startingLockpicks, MAX_GOLD);
            }

            @Override
            public void turnRight(Model model) {
                startingLockpicks = Arithmetics.incrementWithWrap(startingLockpicks, MAX_GOLD);
            }
        });
        inventoryRow++;
        content.add(new CarouselListContent(xStart + COLUMN_SKIP, inventoryRow++, headquartersLocations.get(selectedHeadquarters)) {
            @Override
            public void turnLeft(Model model) {
                selectedHeadquarters = Arithmetics.decrementWithWrap(selectedHeadquarters, headquartersLocations.size());
            }

            @Override
            public void turnRight(Model model) {
                selectedHeadquarters = Arithmetics.incrementWithWrap(selectedHeadquarters, headquartersLocations.size());
            }
        });
        content.add(new SelectableListContent(xStart + COLUMN_SKIP + INVENTORY_TAB-1, inventoryRow, "ADD") {
            @Override
            public void performAction(Model model, int x, int y) {
                setInnerMenu(new SelectItemList(FullPartySelectView.this) {
                    @Override
                    protected List<? extends Item> getItems() {
                        List<Item> result = new ArrayList<>();
                        result.addAll(ItemDeck.allSpells());
                        result.addAll(ItemDeck.allPotions());
                        result.add(new HorseStartingItem(new Regal()));
                        result.add(new HorseStartingItem(new Pony()));
                        result.add(new MagicMirror());
                        return result;
                    }

                    @Override
                    protected void selectItem(Item w) {
                        otherItems.add(w);
                    }
                }, model);
            }
        });
        content.add(new SelectableListContent(xStart + COLUMN_SKIP + INVENTORY_TAB + 3, inventoryRow++, "REMOVE") {
            @Override
            public void performAction(Model model, int x, int y) {
                otherItems.remove(otherItems.size()-1);
            }

            @Override
            public boolean isEnabled(Model model) {
                return !otherItems.isEmpty();
            }
        });
        content.add(new SelectableListContent(xStart + COLUMN_SKIP + 10 - 2, yStart+DrawingArea.WINDOW_ROWS-3, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }

            @Override
            public boolean isEnabled(Model model) {
                return characterSelectionOk();
            }
        });
        content.add(new SelectableListContent(xStart + COLUMN_SKIP + 10 - 4, yStart+DrawingArea.WINDOW_ROWS-2, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                FullPartySelectView.this.canceled = true;
                setTimeToTransition(true);
            }
        });
        return content;
    }

    private String makeSpawnString() {
        String result;
        MainStorySpawnLocation loc = mainStorySpawnLocations.get(selectedMainSpawn);
        if (loc == null) {
            result = "Random";
        } else {
            result = loc.getClass().getSimpleName().replace("MainStorySpawn", "");
        }
        return String.format("%-6s", result);
    }

    private String worldStateToString(int expandDirections) {
        String[] dirStrings = {
                "----", "---E", "--S-", "--SE",
                "-W--", "-W-E", "-WS-", "-WSE",
                "N---", "N--E", "N-S-", "N-SE",
                "NW--", "NW-E", "NWS-", "NWSE"
        };
        if (expandDirections >= dirStrings.length) {
            return "????";
        }
        return dirStrings[expandDirections];
    }

    private String levelOrNA(int i) {
        if (isVacant(i)) {
            return "N/A";
        }
        return String.format("%3d", levels[i-1]);
    }

    private String classOrNA(int selectedCharacter) {
        if (isVacant(selectedCharacter)) {
            return "N/A";
        }
        return String.format("%3s", getSelectedCharacter(selectedCharacter).getCharClass().getShortName());
    }

    private boolean characterSelectionOk() {
        if (isVacant(1)) {
            return false;
        }
        for (int i = 1; i < 8; ++i) {
            if (!isVacant(i+1)) {
                for (int j = i-1; j >= 0; --j) {
                    if (selectedCharacters[i] == selectedCharacters[j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void drawChecksNotOk(Model model, int x) {
        if (isVacant(1)) {
            model.getScreenHandler().put(x + 13, 2, NOT_OK_SPRITE);
        }
        for (int i = 1; i < 8; ++i) {
            if (!isVacant(i+1)) {
                for (int j = i-1; j >= 0; --j) {
                    if (selectedCharacters[i] == selectedCharacters[j]) {
                        model.getScreenHandler().put(x + 13, i*ROWS_PER_CHAR+2, NOT_OK_SPRITE);
                        break;
                    }
                }
            }
        }
    }


    private String nameOrVacant(int selectedCharacter) {
        if (selectedCharacter == maxCharacters) {
            return "VACANT";
        }
        return selectableCharacters.get(selectedCharacter).getName();
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        super.handleCarousels(keyEvent, model);
    }

    public List<GameCharacter> getCharacters() {
        List<GameCharacter> result = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            if (!isVacant(i+1)) {
                GameCharacter gc = getSelectedCharacter(i+1);
                gc.setLevel(levels[i]);
                gc.setEquipment(equipments[i]);
                result.add(gc);
            }
        }
        return result;
    }

    public boolean didCancel() {
        return canceled;
    }

    public void miscSetup(Model model) {
        model.getParty().goldTransaction(startingGold - model.getParty().getGold());
        model.getParty().addToObols(startingObols - model.getParty().getObols());
        model.getParty().addToFood(startingFood - model.getParty().getFood());
        model.getParty().getInventory().addToIngredients(startingIngredients - model.getParty().getInventory().getIngredients());
        model.getParty().getInventory().addToMaterials(startingMaterials - model.getParty().getInventory().getMaterials());
        model.getParty().getInventory().addToLockpicks(startingLockpicks - model.getParty().getInventory().getLockpicks());
        for (Item it : otherItems) {
            if (it instanceof HorseStartingItem) {
                model.getParty().getHorseHandler().addHorse(((HorseStartingItem) it).getHorse());
            } else {
                it.addYourself(model.getParty().getInventory());
            }
        }
        model.getParty().addToNotoriety(startingNotoriety - model.getParty().getNotoriety());
        model.setWorldState(expandDirection);
        model.setDay(startingDay);
        model.progressMainStoryForTesting(mainStoryProgression, mainStorySpawnLocations.get(selectedMainSpawn));
        if (selectedHeadquarters != 0) {
            UrbanLocation loc = model.getWorld().getUrbanLocationByPlaceName(headquartersLocations.get(selectedHeadquarters));
            model.getParty().setHeadquarters(model, new Headquarters(loc, Headquarters.MEDIUM_SIZE));
        }
    }

    private static class SetAllEquipmentMenu extends SelectableListMenu {
        private final Equipment equipment;

        public SetAllEquipmentMenu(GameView previous, Equipment equipment) {
            super(previous, 18, 7);
            this.equipment = equipment;
        }

        @Override
        public void transitionedFrom(Model model) {

        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new DrawableObject(xStart, yStart+1) {
                @Override
                public void drawYourself(Model model, int x, int y) {
                    BorderFrame.drawCentered(model.getScreenHandler(), "Equipment", y, MyColors.WHITE, MyColors.BLUE);
                }
            });
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            return List.of(
                    new SelectableListContent(xStart + 2, yStart+2, equipment.getWeapon().getName()) {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this) {
                                @Override
                                protected List<? extends Item> getItems() {
                                    return ItemDeck.allWeapons();
                                }

                                @Override
                                protected void selectItem(Item w) {
                                    equipment.setWeapon((Weapon) w);
                                }
                            }, model);
                        }
                    },
                    new SelectableListContent(xStart + 2, yStart+3, equipment.getClothing().getName()) {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this) {
                                @Override
                                protected List<? extends Item> getItems() {
                                    return ItemDeck.allApparel();
                                }

                                @Override
                                protected void selectItem(Item w) {
                                    equipment.setClothing((Clothing) w);
                                }
                            }, model);
                        }
                    },
                    new SelectableListContent(xStart + 2, yStart+4, equipment.getAccessory()==null?"NO ACCESSORY":equipment.getAccessory().getName()) {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this) {
                                @Override
                                protected List<? extends Item> getItems() {
                                    List<Accessory> result = new ArrayList<>();
                                    result.addAll(ItemDeck.allGloves());
                                    result.addAll(ItemDeck.allJewelry());
                                    result.addAll(ItemDeck.allShoes());
                                    result.addAll(ItemDeck.allShields());
                                    result.addAll(ItemDeck.allHeadGear());
                                    return result;
                                }

                                @Override
                                protected void selectItem(Item w) {
                                    equipment.setAccessory((Accessory) w);
                                }
                            }, model);
                        }
                    },
                    new SelectableListContent(xStart + 6, yStart+6, "DONE") {
                        @Override
                        public void performAction(Model model, int x, int y) {
                            setTimeToTransition(true);
                        }
                    });
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }
    }

    private static abstract class SelectItemList extends SelectableListMenu {

        public SelectItemList(GameView previous) {
            super(previous, 24, 20);
        }

        @Override
        public void transitionedFrom(Model model) {

        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            int row=yStart+1;
            List<Item> items = new ArrayList<>(getItems());
            Collections.sort(items, Comparator.comparing(Item::getName));
            for (Item w : items) {
                result.add(new SelectableListContent(xStart + 1, row, w.getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        selectItem(w.copy());
                        setTimeToTransition(true);
                    }
                });
                row++;
            }
            return result;
        }

        protected abstract List<? extends Item> getItems();

        protected abstract void selectItem(Item w);

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

        }
    }

}
