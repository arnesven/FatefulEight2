package view;

import model.Model;
import model.characters.GameCharacter;
import model.items.Equipment;
import model.items.Item;
import model.items.ItemDeck;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import util.Arithmetics;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.party.SetEquipmentMenu;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static view.party.CharacterCreationView.NOT_OK_SPRITE;

public class FullPartySelectView extends SelectableListMenu {
    private static final int ROWS_PER_CHAR = 6;
    private final int maxCharacters;
    private final Model model;
    private int[] selectedCharacters;
    private boolean canceled = false;
    private final int[] levels;
    private static final int MAX_LEVEL = 12;
    private final Equipment[] equipments;

    public FullPartySelectView(Model model) {
        super(model.getView(), DrawingArea.WINDOW_COLUMNS-1, DrawingArea.WINDOW_ROWS-1);
        this.model = model;
        this.maxCharacters = model.getAllCharacters().size();
        this.selectedCharacters = new int[]{maxCharacters, maxCharacters, maxCharacters, maxCharacters,
                        maxCharacters, maxCharacters, maxCharacters, maxCharacters};
        this.levels = new int[]{1,1,1,1,1,1,1,1};
        this.equipments = new Equipment[]{new Equipment(), new Equipment(), new Equipment(), new Equipment(),
                                          new Equipment(), new Equipment(), new Equipment(), new Equipment()};
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
                        BorderFrame.drawString(model.getScreenHandler(), String.format("%02d HP, SPEED %d", gc.getMaxHP(), gc.getSpeed()),
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
                drawChecksNotOk(model, x);
            }
        });
    }

    private GameCharacter getSelectedCharacter(int i) {
        return model.getAllCharacters().get(selectedCharacters[i-1]);
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
        content.add(new SelectableListContent(xStart + DrawingArea.WINDOW_COLUMNS/2-2, yStart+DrawingArea.WINDOW_ROWS-3, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }

            @Override
            public boolean isEnabled(Model model) {
                return characterSelectionOk();
            }
        });
        content.add(new SelectableListContent(xStart + DrawingArea.WINDOW_COLUMNS/2-4, yStart+DrawingArea.WINDOW_ROWS-2, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                FullPartySelectView.this.canceled = true;
                setTimeToTransition(true);
            }
        });
        return content;
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
        return model.getAllCharacters().get(selectedCharacter).getName();
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        List<ListContent> content = buildContent(model, 0, 0);
        if (content.get(getSelectedRow()) instanceof CarouselListContent) {
            CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                carousel.turnLeft(model);
                madeChanges();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                carousel.turnRight(model);
                madeChanges();
            }
        }
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
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this, equipment) {
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
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this, equipment) {
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
                            setInnerMenu(new SelectItemList(SetAllEquipmentMenu.this, equipment) {
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

        private static abstract class SelectItemList extends SelectableListMenu {
            private final Equipment equipment;

            public SelectItemList(GameView previous, Equipment equipment) {
                super(previous, 18, 20);
                this.equipment = equipment;
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
                            selectItem(w);
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
}
