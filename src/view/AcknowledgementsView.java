package view;

import model.Model;

public class AcknowledgementsView extends CreditsView {

    private static final String[][] content = new String[][]{new String[]{
            "Win Music #1",
            "by remaxim",
            "licensed CC-BY-SA 3.0, GPL 3.0 or GPL 2.0",
            "https://opengameart.org/content/win-music-1"
    },
            new String[]{
                    "Fantasy Sound Effect Library",
                    "by Little Robot Sound Factory",
                    "licensed CC-BY 3.0",
                    "https://opengameart.org/content/fantasy-sound-effects-library"
            }
    };

    public AcknowledgementsView() {
        super("Acknowledgements", content);
    }

    @Override
    public GameView getNextView(Model model) {
        return new IntroGameView();
    }
}
