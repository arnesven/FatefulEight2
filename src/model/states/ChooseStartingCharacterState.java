package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.horses.HorseItemAdapter;
import model.items.InventoryDummyItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import util.MyRandom;
import view.*;
import view.help.TutorialStartDialog;
import view.party.CharacterCreationView;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class ChooseStartingCharacterState extends GameState {

    public ChooseStartingCharacterState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        print("How would you like to select your starting character? ");
        model.getLog().waitForAnimationToFinish();
        GameCharacter gc;
        Item selectedStartingItem = null;
        while (true) {
            int choice = multipleOptionArrowMenu(model, 30, 16, List.of("Choose Preset",
                    "Random Preset",
                    "Generate",
                    "Create Custom"));

            if (choice == 3) {
                gc = characterCreation(model);
                if (gc != null) {
                    break;
                }
            } else if (choice == 0) {
                gc = selectPreset(model);
                if (gc != null) {
                    model.getAllCharacters().remove(gc);
                    break;
                }
            } else if (choice == 1) {
                gc = randomPreset(model);
                if (gc != null) {
                    model.getAllCharacters().remove(gc);
                    break;
                }
            } else { // generate
                gc = generateCharacter(model);
                if (gc != null) {
                    break;
                }
            }
        }
        println("");

        print(".You have selected your starting character: ");
        addSelectedItem(model, gc, selectedStartingItem);
        model.getParty().add(gc);
        println(gc.getFullName() + " the " + gc.getRace().getName() + " " + gc.getCharClass().getFullName() + ".");
        return model.getCurrentHex().getDailyActionState(model);
    }

    private GameCharacter generateCharacter(Model model) {
        return nonCustomStartingCharacter(model, new GenerateStartingCharacterView(model));
    }

    private GameCharacter randomPreset(Model model) {
        return nonCustomStartingCharacter(model, new RandomPresetStartingCharacterView(model));
    }

    private GameCharacter selectPreset(Model model) {
        return nonCustomStartingCharacter(model, new SelectStaringCharacterView(model));
    }

    private GameCharacter nonCustomStartingCharacter(Model model, StartingCharacterView view) {
        while (true) {
            model.transitionToDialog(view);
            print(" ");
            model.getLog().waitForAnimationToFinish();
            GameCharacter gc = view.getFinalCharacter();
            if (gc == null) {
                return null;
            }
            if (chooseStartingItem(model, gc) != null) {
                return gc;
            }
        }
    }

    private GameCharacter characterCreation(Model model) {
        CharacterCreationView charCreation = new CharacterCreationView(model.getView());
        while (true) {
            model.transitionToDialog(charCreation);
            print(" ");
            model.getLog().waitForAnimationToFinish();
            GameCharacter gc = charCreation.getFinishedCharacter();
            if (gc == null) {
                return null;
            }
            if (chooseStartingItem(model, gc) != null) {
                return gc;
            }
        }
    }

    private GameCharacter chooseStartingItem(Model model, GameCharacter gc) {
        ChooseStartingItemView startingItemView = new ChooseStartingItemView(model, gc);
        print(" ");
        model.transitionToDialog(startingItemView);
        model.getLog().waitForAnimationToFinish();
        if (!startingItemView.didCancel()) {
            addSelectedItem(model, gc, startingItemView.getSelectedItem());
            return gc;
        }
        return null;
    }

    private void addSelectedItem(Model model, GameCharacter gc, Item startingItem) {
        if (startingItem instanceof Weapon) {
            gc.getEquipment().setWeapon((Weapon) startingItem);
        } else if (startingItem instanceof Clothing) {
            gc.getEquipment().setClothing((Clothing) startingItem);
        } else if (startingItem instanceof Accessory) {
            gc.getEquipment().setAccessory((Accessory) startingItem);
        } else if (startingItem instanceof HorseItemAdapter) {
            model.getParty().getHorseHandler().addHorse(((HorseItemAdapter) startingItem).getHorse());
        } else {
            startingItem.addYourself(model.getParty().getInventory());
        }
    }
}
