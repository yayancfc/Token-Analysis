package tokenanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import layout.Utama;

public final class Emulator {

    public static void main(String[] args) throws FileNotFoundException {
        Utama utama = new Utama();
        utama.setVisible(true);
        utama.setLocationRelativeTo(null);
    }
}
