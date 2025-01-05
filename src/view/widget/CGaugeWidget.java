package view.widget;

import model.states.duel.gauges.CTypePowerGauge;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

public class CGaugeWidget extends BGaugeWidget {

    private static final Sprite GAUGE_TYPE = new Sprite16x16("gaugetype", "gauge.png", 0x26, MyColors.WHITE,
            MyColors.BLUE, MyColors.GRAY, MyColors.GRAY_RED);

    public CGaugeWidget(CTypePowerGauge cTypePowerGauge) {
        super(cTypePowerGauge);
    }

    @Override
    public void drawGaugeLogo(ScreenHandler screenHandler, int xStart, int yStart) {
        screenHandler.put(xStart-3, yStart-3, GAUGE_TYPE);
        screenHandler.put(xStart-1, yStart-3, GAUGE_LEFT);
        screenHandler.put(xStart+1, yStart-3, GAUGE_RIGHT);
    }
}
