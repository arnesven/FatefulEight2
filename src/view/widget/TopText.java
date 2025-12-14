package view.widget;

import model.Model;
import model.states.DailyEventState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class TopText {
    public static final String TIME_OF_DAY_SETTINGS_FLAG = "showTimeOfDayInTopBar";
    public static final String GOLD_SETTINGS_FLAG = "showGoldInTopBar";
    public static final String OBOLS_SETTINGS_FLAG = "showObolsInTopBar";
    public static final String FOOD_SETTINGS_FLAG = "showFoodInTopBar";
    public static final String WEIGHT_SETTINGS_FLAG = "showWeightInTopBar";
    public static final String CARRYING_CAPACITY_SETTINGS_FLAG = "showCarryingCapacityInTopBar";
    public static final String HORSE_SETTINGS_FLAG = "showHorsesInTopBar";
    public static final String ALIGNMENT_SETTINGS_FLAG = "showAlignmentInTopBar";
    public static final String NOTORIETY_SETTINGS_FLAG = "showNotorietyInTopBar";
    public static final String MATERIALS_SETTINGS_FLAG = "showMaterialsInTopBar";
    public static final String LOCKPICKS_SETTINGS_FLAG = "showLockpicksInTopBar";
    public static final String INGREDIENTS_SETTINGS_FLAG = "showIngredientsInTopBar";
    public static final String KEY_REMINDERS_SETTINGS_FLAG = "keyRemindersInTopBar";

    public static final CharSprite FOOD_ICON_SPRITE = CharSprite.make(2, MyColors.PEACH, MyColors.WHITE, MyColors.BLACK);
    public static final CharSprite GOLD_ICON_SPRITE = CharSprite.make(0, MyColors.TAN, MyColors.LIGHT_YELLOW, MyColors.BLACK);
    public static final CharSprite OBOL_ICON_SPRITE = CharSprite.make(0, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.BLACK);
    public static final CharSprite REP_ICON_SPRITE = CharSprite.make(3, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.BLACK);
    public static final CharSprite INGREDIENTS_ICON_SPRITE = CharSprite.make(0x12, MyColors.WHITE, MyColors.LIGHT_GREEN, MyColors.BLACK);
    public static final CharSprite MATERIALS_ICON_SPRITE = CharSprite.make(0x13, MyColors.LIGHT_GRAY, MyColors.WHITE, MyColors.BLACK);
    private static final CharSprite LOCKPICKS_ICON_SPRITE = CharSprite.make(0x18, MyColors.LIGHT_GRAY, MyColors.WHITE, MyColors.BLACK);
    public static final CharSprite ALIGNMENT_ICON_SPRITE = makeAlignmentSprite();
    public static final CharSprite HORSES_ICON_SPRITE = CharSprite.make(0x15, MyColors.BEIGE, MyColors.BLACK, MyColors.BLACK);
    public static final CharSprite NOTORIETY_SPRITE = CharSprite.make(0x16, MyColors.RED, MyColors.BLACK, MyColors.BLACK);
    public static final CharSprite WEIGHT_ICON_SPRITE = CharSprite.make(0x17, MyColors.GRAY, MyColors.BLACK, MyColors.BLACK);
    public static final CharSprite MORNING_ICON = makeTimeOfDaySprite(0x19, MyColors.YELLOW, MyColors.GREEN, MyColors.YELLOW);
    public static final CharSprite MIDDAY_ICON = makeTimeOfDaySprite(0x1A, MyColors.YELLOW, MyColors.YELLOW, MyColors.YELLOW);
    public static final CharSprite EVENING_ICON = makeTimeOfDaySprite(0x19, MyColors.ORANGE, MyColors.GREEN, MyColors.GREEN);
    public static final CharSprite NIGHT_ICON = makeTimeOfDaySprite(0x1A, MyColors.BLACK, MyColors.WHITE, MyColors.BLACK);

    public void drawYourself(Model model) {
        int col = 0;
        col = addDay(model, col);
        if (isFlagSet(model, TIME_OF_DAY_SETTINGS_FLAG)) {
            col = addTimeOfDay(model, col);
        }
        if (isFlagSet(model, GOLD_SETTINGS_FLAG)) {
            col = addGold(model, col);
        }
        if (isFlagSet(model, OBOLS_SETTINGS_FLAG)) {
            col = addObols(model, col);
        }
        if (isFlagSet(model, FOOD_SETTINGS_FLAG)) {
            col = addFood(model, col);
        }
        if (isFlagSet(model, WEIGHT_SETTINGS_FLAG)) {
            col = addWeight(model, col);
        }
        if (isFlagSet(model, HORSE_SETTINGS_FLAG)) {
            col = addHorses(model, col);
        }
        if (isFlagSet(model, ALIGNMENT_SETTINGS_FLAG)) {
            col = addAlighment(model, col);
        }
        if (isFlagSet(model, NOTORIETY_SETTINGS_FLAG)) {
            col = addNotoriety(model, col);
        }
        if (isFlagSet(model, INGREDIENTS_SETTINGS_FLAG)) {
            col = addIngredients(model, col);
        }
        if (isFlagSet(model, MATERIALS_SETTINGS_FLAG)) {
            col = addMaterials(model, col);
        }
        if (isFlagSet(model, LOCKPICKS_SETTINGS_FLAG)) {
            col = addLockpicks(model, col);
        }
        if (isFlagSet(model, KEY_REMINDERS_SETTINGS_FLAG)) {
            drawKeyTexts(model);
        }
    }

    private int addTimeOfDay(Model model, int col) {
        Sprite icon = null;
        switch (model.getTimeOfDay()) {
            case MORNING:
                icon = MORNING_ICON;
                break;
            case MIDDAY:
                icon = MIDDAY_ICON;
                break;
            case EVENING:
                icon = EVENING_ICON;
                break;
            default:
                icon = NIGHT_ICON;
        }
        model.getScreenHandler().put(col-1, 0, icon);
        return col+1;
    }

    private int addLockpicks(Model model, int col) {
        return printWithIcon(model, col, String.format("%2d",
                model.getParty().getInventory().getLockpicks()), LOCKPICKS_ICON_SPRITE, MyColors.WHITE);
    }

    private boolean isFlagSet(Model model, String key) {
        Boolean val = model.getSettings().getMiscFlags().get(key);
        if (val == null) {
            return false;
        }
        return val;
    }

    private int addWeight(Model model, int col) {
        int weight = (int)Math.ceil(model.getParty().getEncumbrance() / 1000.0);
        MyColors color = MyColors.WHITE;
        if (model.getParty().getEncumbrance() > model.getParty().getCarryingCapacity()) {
            color = MyColors.LIGHT_RED;
        }
        int width;
        String str;
        if (isFlagSet(model, CARRYING_CAPACITY_SETTINGS_FLAG)) {
            int cap = model.getParty().getCarryingCapacity() / 1000;
            str = String.format("%4d/%d", weight, cap);
        } else {
            str = String.format("%4d", weight);
        }
        BorderFrame.drawString(model.getScreenHandler(), str, col, 0, color);
        width = str.length() + 2;
        model.getScreenHandler().put(col + width - 2, 0, WEIGHT_ICON_SPRITE);
        return col + width;
    }

    private int addObols(Model model, int col) {
        return printWithIcon(model, col, String.format("%4d", model.getParty().getObols()), OBOL_ICON_SPRITE, MyColors.LIGHT_GRAY);
    }

    private int addNotoriety(Model model, int col) {
        return printWithIcon(model,  col, String.format("%3d", model.getParty().getNotoriety()), NOTORIETY_SPRITE, MyColors.RED);
    }

    private int addMaterials(Model model, int col) {
        return printWithIcon(model, col, String.format("%3d", model.getParty().getInventory().getMaterials()),
                MATERIALS_ICON_SPRITE, MyColors.LIGHT_GRAY);
    }

    private int addIngredients(Model model, int col) {
        return printWithIcon(model, col, String.format("%3d", model.getParty().getInventory().getIngredients()),
                INGREDIENTS_ICON_SPRITE, MyColors.LIGHT_GREEN);
    }

    private int printWithIcon(Model model, int col, String format, CharSprite icon, MyColors textColor) {
        model.getScreenHandler().put(col+format.length(), 0, icon);
        BorderFrame.drawString(model.getScreenHandler(), format, col, 0, textColor);
        return col + format.length() + 2;
    }

    private int addHorses(Model model, int col) {
        MyColors horsesColor = MyColors.LIGHT_RED;
        if (model.getParty().getHorseHandler().canRide(model.getParty().getPartyMembers())){
            horsesColor = MyColors.LIGHT_GREEN;
        }
        return printWithIcon(model, col, String.format("%2d/%1d",  model.getParty().getHorseHandler().getFullBloods(),
                model.getParty().getHorseHandler().getPonies()), HORSES_ICON_SPRITE, horsesColor);
    }

    private int addAlighment(Model model, int col) {
        return printWithIcon(model, col, String.format("%3d", DailyEventState.getPartyAlignment(model)),
                ALIGNMENT_ICON_SPRITE, MyColors.WHITE);
    }

    private int addFood(Model model, int col) {
        return printWithIcon(model, col, String.format("%3d", model.getParty().getFood()), FOOD_ICON_SPRITE, MyColors.PEACH);
    }

    private int addGold(Model model, int col) {
        return printWithIcon(model, col, String.format("%4d", model.getParty().getGold()), GOLD_ICON_SPRITE, MyColors.LIGHT_YELLOW);
    }

    private int addDay(Model model, int col) {
        String dayAsString = String.format("DAY %d", model.getDay());
        BorderFrame.drawString(model.getScreenHandler(), dayAsString, 0, 0, MyColors.CYAN);
        return col + dayAsString.length() + 1;
    }

    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC=MENU F1=HELP F2=LOG INS=PAUSE", 47, 0, MyColors.WHITE);
    }

    private static CharSprite makeAlignmentSprite() {
        CharSprite spr = CharSprite.make(0x14, MyColors.GRAY, MyColors.WHITE, MyColors.BLACK);
        spr.setColor4(MyColors.GREEN);
        return spr;
    }

    private static CharSprite makeTimeOfDaySprite(int num, MyColors color1, MyColors colors2, MyColors color4) {
        CharSprite spr = CharSprite.make(num, color1, colors2, MyColors.BLACK);
        spr.setColor4(color4);
        return spr;
    }
}
