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
import view.widget.ArmorClassWidget;

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
            addDisabledListContent(content, x, y++, "Leader");
        } else if (model.getParty().getBench().contains(gc)) {
            addDisabledListContent(content, x, y++, "Absent from party");
        } else {
            content.add(new SelectableListContent(x, y++, "Not Leader") {
                @Override
                public void performAction(Model model, int x, int y) {
                    if (model.isInQuest()) {
                        PartyView.super.setInnerMenu(new SimpleMessageView(PartyView.this,
                                "You cannot change the party leader while on a Quest."), model);
                    } else if (model.isInCombat()) {
                        PartyView.super.setInnerMenu(new SimpleMessageView(PartyView.this,
                                "You cannot change the party leader during combat."), model);
                    } else if (model.isInDungeon()) {
                        PartyView.super.setInnerMenu(new SimpleMessageView(PartyView.this,
                                "You cannot change the party leader while in a Dungeon."), model);
                    } else if (model.getParty().getLeaderLockedUntil() > model.getDay()) {
                        PartyView.super.setInnerMenu(new SimpleMessageView(PartyView.this,
                                "You cannot change the party leader right now."), model);
                    } else {
                        PartyView.super.setInnerMenu(new SetLeaderMenu(PartyView.this, gc, x, y), model);
                    }
                }

                @Override
                public boolean isEnabled(Model model) {
                    return true;
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

        y += 4;
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
        String weaponLabel = (w instanceof UnarmedCombatWeapon ? "NO WEAPON" : w.getName());
        content.add(new OpenWeaponMenuListContent(x, y++, gc, weaponLabel));
        y+=4;

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
        y += 4;

        Accessory accessory = gc.getEquipment().getAccessory();
        if (accessory != null) {
            content.add(new OpenAccessoryMenuListContent(x, y++, gc, accessory.getName()));
            y += 3;
        } else {
            content.add(new OpenAccessoryMenuListContent(x, y++, gc, "NO ACCESSORY"));
        }

        return content;
    }

    private String getSPString(Accessory accessory) {
        if (accessory.getSPBonus() != 0) {
            return "Stamina " + MyStrings.withPlus(accessory.getSPBonus());
        }
        return "";
    }

    private String getHealthString(Accessory accessory) {
        if (accessory.getHealthBonus() != 0) {
            return "Health " + MyStrings.withPlus(accessory.getHealthBonus());
        }
        return "";
    }

    private String getSpeedString(Accessory accessory) {
        if (accessory.getSpeedModifier() != 0) {
            return "Speed " + MyStrings.withPlus(accessory.getSpeedModifier());
        }
        return "";
    }

    private void addDisabledListContent(List<ListContent> content, int x, int y, String text) {
        content.add(new SelectableListContent(x, y, text) {
            @Override
            public void performAction(Model model, int x, int y) { }

            @Override
            public boolean isEnabled(Model model) {
                return false;
            }
        });
    }

    private String getAttackString(Weapon w) {
        StringBuilder bldr = new StringBuilder();
        if (w.isRangedAttack()) {
            bldr.append("Range");
        } else {
            bldr.append("Melee");
        }
        if (w.getNumberOfAttacks() > 1) {
            bldr.append(" x").append(w.getNumberOfAttacks());
        }
        if (w.getInitBonus() != 0) {
            bldr.append(" Init ");
            if (w.getInitBonus() > 0) {
                bldr.append("+");
            }
            bldr.append(w.getInitBonus());
        }
        if (w.getSpeedModifier() != 0) {
            bldr.append(" Speed ");
            if (w.getSpeedModifier() > 0) {
                bldr.append("+");
            }
            bldr.append(w.getSpeedModifier());
        }
        return bldr.toString();
    }

    private String getArmorString(ArmorItem armor, boolean showZeroArmor) {
        String extra = "";
        if (armor.getMP() != 0) {
            extra = armor.getMP() + " MP ";
            if (armor.getAP() == 0) {
                return extra;
            }
        }
        if (armor.getAP() != 0 || showZeroArmor) {
            return armor.getAP() + " AP " + extra + (armor.isHeavy() ? "Heavy" : "Light");
        }
        return "";
    }

    private String getBonusesAsString(Item w) {
        StringBuilder bldr = new StringBuilder();
        for (MyPair<Skill, Integer> bonus : w.getSkillBonuses()) {
            bldr.append(bonus.first.getName() + " " + MyStrings.withPlus(bonus.second) + " ");
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

                int newY = y + 2;
                int rightOfPortraitX = xStart + 9;
                print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Health %9s", gc.getHP() + "/" + gc.getMaxHP()));
                print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Stamina %8s", gc.getSP() + "/" + gc.getMaxSP()));
                int ap = gc.getAP();
                int mp = gc.getMP();
                if (ap > 0 && mp > 0) {
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Armor %3dMP %2dAP", mp, ap));
                } else if (ap > 0) {
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Armor %7d AP", ap));
                } else if (mp > 0) {
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Armor %7d MP", mp));
                } else {
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Armor %10s", "None"));
                }
                print(model.getScreenHandler(), rightOfPortraitX, newY++, String.format("Speed %10d", gc.getSpeed()));

                newY += 3;
                Weapon w = gc.getEquipment().getWeapon();
                rightOfPortraitX -= 3;
                int capSize = rightColumnX - xStart - 7;
                if (!(w instanceof UnarmedCombatWeapon)) {
                    newY++;
                    // ROW 1: Skill and Damage Table
                    String skillName = w.getSkill().getName().replace(" Weapons","").replace(" Combat", "");
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, cap(capSize, skillName + " " + w.getDamageTableAsString()));
                    // ROW 2: Type of attack, speed and init bonus
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, cap(capSize, getAttackString(w)));
                    // ROW 3 and 4: Skill Bonuses and Extra
                    String bonuses = getBonusesAsString(w);
                    if (!bonuses.isEmpty()) {
                        bonuses += " ";
                    }
                    String restContent = bonuses + w.getExtraText().replace(", ", " ");
                    String[] parts = MyStrings.partition(restContent, capSize);
                    for (int i = 0; i < 2; ++i) {
                        if (parts.length > i) {
                            print(model.getScreenHandler(), rightOfPortraitX, newY++, parts[i]);
                        } else {
                            newY++;
                        }
                    }
                } else {
                    newY++;
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, cap(capSize, w.getExtraText()));
                    newY+=3;
                }

                newY++;
                // ROW 1: Armor bonuses
                print(model.getScreenHandler(), rightOfPortraitX, newY++, cap(capSize, getArmorString(gc.getEquipment().getClothing(), true)));
                // ROW 2, 3, 4
                {
                    String[] parts = MyStrings.partition(getBonusesAsString(gc.getEquipment().getClothing()), capSize);
                    for (int i = 0; i < 3; ++i) {
                        if (parts.length > i) {
                            print(model.getScreenHandler(), rightOfPortraitX, newY++, parts[i]);
                        } else {
                            newY++;
                        }
                    }
                }

                Accessory accessory = gc.getEquipment().getAccessory();
                if (accessory != null) {
                    newY++;
                    String row1 = getArmorString(accessory, false);
                    if (!row1.isEmpty()) {
                        row1 += " ";
                    }
                    print(model.getScreenHandler(), rightOfPortraitX, newY++, cap(capSize, row1 + getSpeedString(accessory) + getHealthString(accessory) + getSPString(accessory)));
                    String bonuses = getBonusesAsString(accessory);
                    if (!bonuses.isEmpty()) {
                        bonuses += " ";
                    }
                    String restContent = bonuses + accessory.getExtraText().replace(", ", " ");
                    String[] parts = MyStrings.partition(restContent, capSize);
                    for (int i = 0; i < 2; ++i) {
                        if (parts.length > i) {
                            print(model.getScreenHandler(), rightOfPortraitX, newY++, parts[i]);
                        } else {
                            newY++;
                        }
                    }
                }

                drawEquipment(model, gc, x+1, y+9);
                int skillsEndRow = printSkills(model.getScreenHandler(), gc, y);

                ArmorClassWidget.drawYourself(model.getScreenHandler(), rightColumnX,
                        ++skillsEndRow, gc.getCharClass().canUseHeavyArmor());

                ++skillsEndRow;
                print(model.getScreenHandler(), rightColumnX, skillsEndRow,
                        String.format("Avg Damage %1.1f", gc.calcAverageDamage()));
                ++skillsEndRow;
            }
        });
    }

    private String cap(int length, String s) {
        if (s.length() < length) {
            return s;
        }
        return s.substring(0, length);
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

        @Override
        public boolean isEnabled(Model model) {
            return !model.getParty().getBench().contains(gc) && super.isEnabled(model);
        }
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
