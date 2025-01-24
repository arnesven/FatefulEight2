package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.items.ArmorItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import util.MyPair;
import util.MyStrings;
import view.*;
import view.help.RaceAndClassHelpDialog;
import view.sprites.ArrowSprites;
import view.subviews.SubView;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PartyView extends SelectableListMenu {
    private static final int WIDTH = 48;
    private static final int HEIGHT = 26;
    private int rightColumnX;
    private int selectedCharacter;

    public PartyView(GameView previous) {
        super(previous, WIDTH, HEIGHT);
        this.selectedCharacter = 0;
        rightColumnX = DrawingArea.WINDOW_COLUMNS / 2 + 6;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected boolean isValid(Model model) {
        return model.getParty().size() > 0;
    }

    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        GameCharacter gc = model.getParty().getPartyMember(selectedCharacter);
        content.add(new ListContent(xStart+1, yStart+1, String.format("%-39sExp %4d", gc.getFullName(), gc.getXP())));
        String secondRow = gc.getRace().getName() + " " + gc.getCharClass().getFullName() + " (" + gc.getCharClass().getShortName() + ") lvl " + gc.getLevel();
        if (gc.getRace().getName().equals(gc.getCharClass().getFullName())) {
            secondRow = gc.getCharClass().getFullName() + " (" + gc.getCharClass().getShortName() + ") lvl " + gc.getLevel();
        }
        content.add(new SelectableListContent(xStart + 1, yStart + 2, String.format("%-38sNext %4d", secondRow, GameCharacter.getXPForNextLevel(gc.getLevel()))) {
            @Override
            public void performAction(Model model, int x, int y) {
                setInnerMenu(new RaceAndClassHelpDialog(model, gc), model);
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });

        int x = xStart+9;
        int y = yStart+3;

        if (model.getParty().getLeader() == gc) {
            addListContent(content, x, y++, "Leader");
        } else if (model.getParty().getBench().contains(gc)) {
            content.add(new SelectableListContent(x, y++, "Absent from party") {
                @Override
                public void performAction(Model model, int x, int y) { }

                @Override
                public boolean isEnabled(Model model) {
                    return false;
                }
            });
        } else {
            content.add(new SelectableListContent(x, y++, "Not Leader") {
                @Override
                public void performAction(Model model, int x, int y) {
                    PartyView.super.setInnerMenu(new SetLeaderMenu(PartyView.this, gc, x, y), model);
                }

                @Override
                public boolean isEnabled(Model model) {
                    return !model.isInQuest() && !model.isInCombat() && !model.isInDungeon();
                }
            });
        }

        if (model.getParty().getBench().contains(gc)) {
            y++;
        } else {
            content.add(new SelectableListContent(x, y++, "Formation " +
                    (model.getParty().getFrontRow().contains(gc) ? "Front" : "Back")) {
                @Override
                public void performAction(Model model, int x, int y) {
                    model.getParty().toggleFormationFor(gc);
                }
            });
        }

        addListContent(content, x, y++, String.format("Health %9s", gc.getHP() + "/" + gc.getMaxHP()));
        addListContent(content, x, y++, String.format("Stamina %8s", gc.getSP() + "/" + gc.getMaxSP()));
        addListContent(content, x, y++, String.format("Armor %10d", gc.getAP()));
        addListContent(content, x, y++, String.format("Speed %10d", gc.getSpeed()));
        String status = gc.getStatus();
        if (status.length() >= 16) {
            status = status.substring(0, 7) + " ...";
        }
        content.add(new SelectableListContent(x, y++, String.format("Status %-9s", status)) {
            @Override
            public void performAction(Model model, int x, int y) {
                PartyView.super.setInnerMenu(new ConditionsDetailMenu(PartyView.this, gc, x, y), model);
            }

            @Override
            public boolean isEnabled(Model model) {
                return gc.getConditions().size() > 0;
            }
        });
        AbilitiesDetailMenu abilitiesDetails = new AbilitiesDetailMenu(model,PartyView.this, gc, x, y);
        String abilityText = "Abilities...";
        if (abilitiesDetails.getNoOfAbilities() == 0) {
            abilityText = "No Abilities";
        }
        content.add(new SelectableListContent(x, y++, abilityText) {
            @Override
            public void performAction(Model model, int x, int y) {
                PartyView.super.setInnerMenu(abilitiesDetails, model);
            }

            @Override
            public boolean isEnabled(Model model) {
                return abilitiesDetails.getNoOfAbilities() > 0;
            }
        });


        y+=1;
        Weapon w = gc.getEquipment().getWeapon();
        x -= 3;
        if (!(w instanceof UnarmedCombatWeapon)) {
            content.add(new OpenWeaponMenuListContent(x, y++, gc, w.getName()));
            addListContent(content, x, y++, w.getSkill().getName().replace(" Weapons","") + " " + w.getDamageTableAsString());
            addListContent(content, x, y++, getAttackString(w));
            addListContent(content, x, y++, getBonusesAsString(w));
            String extra = w.getExtraText();
            if (extra.length() > rightColumnX - SubView.X_OFFSET) {
                extra = extra.substring(0, rightColumnX - SubView.X_OFFSET);
            }
            addListContent(content, x, y++, extra);
        } else {
            content.add(new OpenWeaponMenuListContent(x, y++, gc, "NO WEAPON"));
            addListContent(content, x, y++, w.getExtraText());
            y+=3;
        }

        content.add(new OpenEquipMenuListContent(x, y++, gc, gc.getEquipment().getClothing().getName()) {
            @Override
            protected SelectableListMenu getSubMenu(Model model, int x, int y, GameCharacter gc) {
                return new SetClothingMenu(PartyView.this, gc, x, y);
            }

            @Override
            public boolean isEnabled(Model model) {
                return super.isEnabled(model) && gc.canChangeClothing();
            }
        });
        addListContent(content, x, y++, getArmorString(gc.getEquipment().getClothing(), true));
        addListContent(content, x, y++, getBonusesAsString(gc.getEquipment().getClothing()));
        y += 2;

        Accessory accessory = gc.getEquipment().getAccessory();
        if (accessory != null) {
            content.add(new OpenAccessoryMenuListContent(x, y++, gc, accessory.getName()));
            addListContent(content, x, y++, getArmorString(accessory, false) + " " +  getSpeedString(accessory) + getHealthString(accessory) + getSPString(accessory));
            addListContent(content, x, y++, getBonusesAsString(accessory));
            addListContent(content, x, y++, accessory.getExtraText());
        } else {
            content.add(new OpenAccessoryMenuListContent(x, y++, gc, "NO ACCESSORY"));
        }

        return content;
    }

    private String getSPString(Accessory accessory) {
        if (accessory.getSPBonus() != 0) {
            return "Stamina " + (accessory.getSPBonus()>0?"+":"") + accessory.getSPBonus();
        }
        return "";
    }

    private String getHealthString(Accessory accessory) {
        if (accessory.getHealthBonus() != 0) {
            return "Health " + (accessory.getHealthBonus()>0?"+":"") + accessory.getHealthBonus();
        }
        return "";
    }

    private String getSpeedString(Accessory accessory) {
        if (accessory.getSpeedModifier() == 0) {
            return "";
        } else if (accessory.getSpeedModifier() > 0) {
            return "Speed +" + accessory.getSpeedModifier();
        }
        return "Speed " + accessory.getSpeedModifier();
    }

    private void addListContent(List<ListContent> content, int x, int y, String text) {
        if (!text.equals("")) {
            content.add(new ListContent(x, y, text));
        }
    }

    private String getAttackString(Weapon w) {
        StringBuilder bldr = new StringBuilder();
        if (w.isRangedAttack()) {
            bldr.append("Ranged ");
        } else {
            bldr.append("Melee ");
        }
        bldr.append("Atk");
        if (w.getNumberOfAttacks() > 1) {
            bldr.append(" x").append(w.getNumberOfAttacks());
        }

        if (w.getSpeedModifier() != 0) {
            bldr.append(", Speed ");
            if (w.getSpeedModifier() > 0) {
                bldr.append("+");
            }
            bldr.append(w.getSpeedModifier());
        }
        return bldr.toString();
    }

    private String getArmorString(ArmorItem armor, boolean showZeroArmor) {
        if (armor.getAP() != 0 || showZeroArmor) {
            return "Armor " + armor.getAP() + " " + (armor.isHeavy() ? "Heavy" : "Light");
        }
        return "";
    }

    private String getBonusesAsString(Item w) {
        StringBuilder bldr = new StringBuilder();
        for (MyPair<Skill, Integer> bonus : w.getSkillBonuses()) {
            bldr.append(bonus.first.getName() + " " + bonus.second + " ");
        }
        return bldr.toString();
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart, yStart+3) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                GameCharacter gc = model.getParty().getPartyMember(selectedCharacter);
                gc.drawAppearance(model.getScreenHandler(), x+1, y);
                model.getScreenHandler().put(x+1, y+7, ArrowSprites.LEFT);
                model.getScreenHandler().put(x+7, y+7, ArrowSprites.RIGHT);

                drawEquipment(model, gc, x+1, y+9);
                int skillsEndRow = printSkills(model.getScreenHandler(), gc, y);

                print(model.getScreenHandler(), rightColumnX, ++skillsEndRow,
                        "Armor Class ");
                printArmorClass(model.getScreenHandler(), rightColumnX+12, skillsEndRow,
                        gc.getCharClass().canUseHeavyArmor());

                ++skillsEndRow;
                print(model.getScreenHandler(), rightColumnX, skillsEndRow,
                            String.format("Avg Damage %1.1f", gc.calcAverageDamage()));
                ++skillsEndRow;
                print(model.getScreenHandler(), rightColumnX, ++skillsEndRow, "Other Classes");
                print(model.getScreenHandler(), rightColumnX, ++skillsEndRow, gc.getOtherClasses());
            }
        });
    }

    private void printArmorClass(ScreenHandler screenHandler, int x, int y, boolean canUseHeavyArmor) {
        String text = canUseHeavyArmor ? "HEAVY" : "LIGHT";
        MyColors bgColor = canUseHeavyArmor ? MyColors.BLACK : MyColors.BLUE;
        BorderFrame.drawString(screenHandler, text, x, y, MyColors.WHITE, bgColor);
    }

    private void drawEquipment(Model model, GameCharacter gc, int x, int y) {
        gc.getEquipment().drawWeapon(model.getScreenHandler(), x, y);
        y+=5;
        gc.getEquipment().drawClothing(model.getScreenHandler(), x, y);
        y+=5;
        gc.getEquipment().drawAccessory(model.getScreenHandler(), x, y);
    }

    private int printSkills(ScreenHandler screenHandler, GameCharacter gc, int row) {
        int midX = rightColumnX;
        row++;
        for (Skill s : gc.getSkillSet()) {
            int rank = gc.getRankForSkill(s);
            int base = gc.getUnmodifiedRankForSkill(s);
            int extra = rank - base;
            String extraString = extra > 0 ? "+"+extra : (extra < 0 ? ""+extra : "");
            if (base != 0 || extra != 0) {
                print(screenHandler, midX, row, String.format("%-14s%2d", s.getName(), base));
                if (extra != 0) {
                    print(screenHandler, midX+16, row, extraString,
                            extra > 0 ? MyColors.LIGHT_GREEN : MyColors.LIGHT_RED);
                }
                row++;
            }
        }
        return row;
    }

    @Override
    public void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedCharacter = selectedCharacter - 1;
            if (selectedCharacter < 0) {
                selectedCharacter = model.getParty().size() - 1;
            }
            checkForSelectedRowReset(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedCharacter = (selectedCharacter + 1) % model.getParty().size();
            checkForSelectedRowReset(model);
            madeChanges();
        }
    }

    public void setSelectedCharacter(int i) {
        this.selectedCharacter = i;
    }

    private abstract class OpenEquipMenuListContent extends SelectableListContent {
        private final GameCharacter gc;

        public OpenEquipMenuListContent(int x, int y, GameCharacter gc, String title) {
            super(x, y, title);
            this.gc = gc;
        }

        @Override
        public void performAction(Model model, int x, int y) {
            setInnerMenu(getSubMenu(model, x, y, gc), model);
        }

        protected abstract SelectableListMenu getSubMenu(Model model, int x, int y, GameCharacter gc);
    }

    private class OpenWeaponMenuListContent extends OpenEquipMenuListContent {
        public OpenWeaponMenuListContent(int x, int y, GameCharacter gc, String text) {
            super(x, y, gc, text);
        }

        @Override
        protected SelectableListMenu getSubMenu(Model model, int x, int y, GameCharacter gc) {
            return new SetWeaponMenu(PartyView.this, gc, x, y);
        }
    }

    private class OpenAccessoryMenuListContent extends OpenEquipMenuListContent {
        private final GameCharacter character;

        public OpenAccessoryMenuListContent(int x, int y, GameCharacter gc, String text) {
            super(x, y, gc, text);
            this.character = gc;
        }

        @Override
        protected SelectableListMenu getSubMenu(Model model, int x, int y, GameCharacter gc) {
            return new SetAccessoryMenu(PartyView.this, gc, x, y);
        }

        @Override
        public boolean isEnabled(Model model) {
            return super.isEnabled(model) && character.canChangeAccessory();
        }
    }

    private class ChangeClassMenu extends FixedPositionSelectableListMenu {
        private final GameCharacter gameCharacter;

        public ChangeClassMenu(PartyView partyView, GameCharacter gc, int x, int y) {
            super(partyView, 20, 9, x, y);
            this.gameCharacter = gc;
        }

        @Override
        public void transitionedFrom(Model model) {

        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return List.of(new TextDecoration("Change Class?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> content = new ArrayList<>();
            int i = 2;
            for (CharacterClass cl : CharacterClass.getSelectableClasses()) {
                content.add(new SelectableListContent(xStart+1, yStart+i, cl.getFullName()) {
                    final CharacterClass newClass = cl;
                    @Override
                    public void performAction(Model model, int x, int y) {
                        gameCharacter.setClass(newClass);
                        setTimeToTransition(true);
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
