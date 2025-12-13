package view;

import model.Model;

public class WideSubviewView extends MainGameView {

    public WideSubviewView() {
        setAllowLogChange(false);
    }

    @Override
    protected void drawFrame(Model model) {
        BorderFrame.drawWideFrame(model.getScreenHandler());
    }

    @Override
    protected void drawParty(Model model) {

    }
}
