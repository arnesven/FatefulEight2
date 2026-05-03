package model;

import view.CreditsView;
import view.GameView;
import view.AcknowledgementsView;

public class StartingCreditsView extends CreditsView {

    private static final String[][] contents = new String[][]{
            new String[]{
                    "Game Design and Programming",
                    "Erik Nilsson",
            },
            new String[]{
                    "Music",
                    "Per Soderberg",
            },
            new String[]{
                    "Play Testers",
                    "Nathalie Bjallerhag     Peter Komaromy",
                    "Pontus Haglund     Robert Hallberg",
                    "Carl Johansson     Magnus Thalen",
                    "Simon Hessling Oscarsson"
            }
    };

    public StartingCreditsView() {
        super("Credits", contents);
    }

    @Override
    public GameView getNextView(Model model) {
        return new AcknowledgementsView();
    }
}
