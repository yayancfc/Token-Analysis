package tokenanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public final class TokenScanner {

    private static String nilaiToken = "";
    private static int baris = 0;
    private static int kolom = 0;
    private static boolean bacaString = false;
    private static boolean bacaNomor = false;
    private static boolean nilaiReal = false;
    private static boolean rangeMatematik = false;
    private static boolean bacaTitikDua = false;
    private static boolean bacaBoolean = false;
    private static boolean bacaTitik = false;

    public static ArrayList<Token> tokenArrayList = new ArrayList<>();


    //Enum yang membedakan suatu karakter dengan karatker lainnya
    enum ENUM_KARAKTER {
        HURUF, DIGIT, SPASI, SIMBOL, QUOTE
    }

    private static final HashMap<String, ENUM_KARAKTER> TIPE_KARAKTER;

    //Buat hashmap yang memetakan simbol dan tipe tokennya
    private static final HashMap<String, String> TOKEN_SIMBOL;

    static {
        TOKEN_SIMBOL = new HashMap<>();
        TOKEN_SIMBOL.put("(", "TK_KURUNG_BUKA");
        TOKEN_SIMBOL.put(")", "TK_KURUNG_TUTUP");
        TOKEN_SIMBOL.put("[", "TK_KURUNG_SIKU_BUKA");
        TOKEN_SIMBOL.put("]", "TK_KURUNG_SIKU_TUTUP");
        TOKEN_SIMBOL.put(".", "TK_TITK");
        TOKEN_SIMBOL.put("..", "TK_RENTANG");
        TOKEN_SIMBOL.put(":", "TK_TITIK_DUA");
        TOKEN_SIMBOL.put(";", "TK_TITIK_KOMA");
        TOKEN_SIMBOL.put("+", "TK_PLUS");
        TOKEN_SIMBOL.put("-", "TK_MINUS");
        TOKEN_SIMBOL.put("*", "TK_KALI");
        TOKEN_SIMBOL.put("/", "TK_BAGI");
        TOKEN_SIMBOL.put("<", "TK_KURANG_DARI");
        TOKEN_SIMBOL.put("<=", "TK_KURANG_DARI_SAMA_DENGAN");
        TOKEN_SIMBOL.put(">", "TK_LEBIH_DARI");
        TOKEN_SIMBOL.put(">=", "TK_LEBIH_DARI_SAMA_DENGAN");
        TOKEN_SIMBOL.put(":=", "TK_ASSIGNMENT");
        TOKEN_SIMBOL.put(",", "TK_KOMA");
        TOKEN_SIMBOL.put("=", "TK_SAMA_DENGAN");
        TOKEN_SIMBOL.put("<>", "TK_TIDAK_SAMA_DENGAN");
    }

    //Buat hashmap yang memetakan operator dan tipe tokennya
    private static final HashMap<String, String> TOKEN_OPERATOR;

    static {
        TOKEN_OPERATOR = new HashMap<>();
        TOKEN_OPERATOR.put("+", "TK_PLUS");
        TOKEN_OPERATOR.put("-", "TK_MINUS");
        TOKEN_OPERATOR.put("*", "TK_KALI");
        TOKEN_OPERATOR.put("/", "TK_BAGI");
        TOKEN_OPERATOR.put("<", "TK_KURANG_DARI");
        TOKEN_OPERATOR.put("<=", "TK_KURANG_DARI_SAMA_DENGAN");
        TOKEN_OPERATOR.put(">", "TK_LEBIH_DARI");
        TOKEN_OPERATOR.put(">=", "TK_LEBIH_DARI_SAMA_DENGAN");
        TOKEN_OPERATOR.put(":=", "TK_ASSIGNMENT");
        TOKEN_OPERATOR.put("=", "TK_SAMA_DENGAN");
        TOKEN_OPERATOR.put("<>", "TK_TIDAK_SAMA_DENGAN");
    }


    //Buat hashmap yang memetakan keyword pada tipe tokennya
    private static final HashMap<String, String> TOKEN_KEYWORDS;

    static {
        TOKEN_KEYWORDS = new HashMap<>();
        String keyword;

        try {
            //Baca keywords.txt
            Scanner sc = new Scanner(new File(System.getProperty("user.dir")+"\\src\\tokenanalysis\\keywords.txt"));
            while (sc.hasNext()) {
                keyword = sc.next();

                //Masukan tiap kata pada keyword.txt pada hashmap TOKEN_KEYWORDS
                TOKEN_KEYWORDS.put(keyword, String.format("TK_%s", keyword.toUpperCase()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Buat hashmap yang memetakan karakter pada tipe enum karakternya
    static {
        TIPE_KARAKTER = new HashMap<>();

        //Kode ASCII pada tabel ASCII 65 - 90 atau huruf A - Z
        for (int i = 65; i < 91; i++) {
            String currentChar = String.valueOf(Character.toChars(i)[0]);

            //Masukkan huruf A - Z pada hashmap
            TIPE_KARAKTER.put(currentChar, ENUM_KARAKTER.HURUF);

            //Masukkan huruf a - z (lowercase) pada hashmap
            TIPE_KARAKTER.put(currentChar.toLowerCase(), ENUM_KARAKTER.HURUF);
        }

        //Kode ASCII pada tabel ASCII 48 - 57 atau angka 0 - 9
        for (int i = 48; i < 58; i++) {
            String currentChar = String.valueOf(Character.toChars(i)[0]);
            TIPE_KARAKTER.put(currentChar, ENUM_KARAKTER.DIGIT);
        }

        //Kode ASCII pada tabel ASCII 1 - 32 (spasi, tab, dsb)
        for (int i = 1; i < 33; i++) {

            String currentChar = String.valueOf(Character.toChars(i)[0]);
            TIPE_KARAKTER.put(currentChar, ENUM_KARAKTER.SPASI);
        }

        //Masukan tiap key pada hashmap simbol manjadi key pada hashmap tipe karakter dengan value enum simbol
        for (String key : TOKEN_SIMBOL.keySet()) {
            TIPE_KARAKTER.put(key, ENUM_KARAKTER.SIMBOL);
        }

        //Masukkan karakter ' sebagai tipe karakter dengan value enum quote
        TIPE_KARAKTER.put(String.valueOf(Character.toChars(39)[0]), ENUM_KARAKTER.QUOTE);
    }


    //Modul utama yang me-scan suatu file menjadi kelompok-kelompok token dalam bentuk ArrayList
    public static ArrayList<Token> scan(File file) throws FileNotFoundException {

        //Pisahkan kata berdasarkan delimiter kosong untuk membaca tiap karakter
        Scanner scanner = new Scanner(file).useDelimiter("");

        //System.out.print("Source kode : \n\n");
        while (scanner.hasNext()) {
            char karakter = scanner.next().toLowerCase().charAt(0);
            //System.out.print(karakter);
            //Cek setiap karakter dan bentuk token-tokennya
            cekKarakterDanBentukToken(karakter);
        }
        ArrayList<Token> temp = tokenArrayList;
        tokenArrayList = new ArrayList<>();
        return temp;
    }

    private static void cekKarakterDanBentukToken(char karakterSekarang) {
        //System.out.println(karakterSekarang);
        //Ambil nilai enumerasi karakter dari hashmap TIPE_KARAKTER
        
        switch (TIPE_KARAKTER.get(String.valueOf(karakterSekarang))) {

            case HURUF:
                
                if (!bacaNomor) {
                    nilaiToken += karakterSekarang;
                } 

                //Penanganan kasus range matematis seperti E12
                if (karakterSekarang == 'E' && bacaNomor) {
                    nilaiToken += karakterSekarang;
                    rangeMatematik = true;
                }
                
               
                break;

            case DIGIT:
                
                
                if (nilaiToken.isEmpty()) {
                    bacaNomor = true;
                }

                nilaiToken += karakterSekarang;

                break;

            case SPASI:
                       
                if (bacaString) {
                    //Masukkan spasi ini pada string
                    nilaiToken += karakterSekarang;
                } else if (bacaTitikDua) {
                    //Terdapat spasi setelah membaca karakter :
                    bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                    bacaTitikDua = false;

                } else if (bacaBoolean) {
                    //Terdapat spasi setelah membaca boolean berupa < atau > atau =
                    bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                    bacaBoolean = false;
                } else if (bacaNomor) {
                    deretNomor();
                } else {

                    //Akhir dari suatu kata
                    //System.out.println(satuKata());
                    nilaiToken = satuKata();

                    //Kode ASCII untuk baris baru
                    if (karakterSekarang == Character.toChars(10)[0]) {
                        baris++;
                        kolom = 0;
                        //Kode ASCII untuk tab
                    } else if (karakterSekarang == Character.toChars(9)[0]) {
                        kolom += 4;
                        //Kode ASCII untuk spasi
                    } else if (karakterSekarang == Character.toChars(32)[0]) {
                        //Spasi
                        kolom++;
                    }

                } 

                break;
            case SIMBOL:
                //Bila sedang baca string masukkan simbol ini pada nilai token string sekarang
                
                if (bacaString) {
                    nilaiToken += karakterSekarang;
                } else if (bacaNomor) {
                    if (karakterSekarang == '.') {
                        if (!nilaiReal) {
                            //Jika ditemukan titik dan status nilai real false, berarti nilai real ditemukan.
                            //contoh 3.1
                            nilaiReal = true;
                            nilaiToken += karakterSekarang;
                        } else {
                            //Bila telah ditemukan titik dan nilai real telah true, berarti nilai token range ditemukan.
                            //contoh 3..4
                            nilaiReal = false;
                            nilaiToken = nilaiToken.substring(0, nilaiToken.length() - 1);
                            deretNomor();
                            nilaiToken = "..";
                            bangkitkanToken("TK_RANGE");
                            nilaiToken = "";
                        }
                        //Bila terdeteksi ekspresi matematik maka tambahkan nomor ke nilai token
                        //contoh 1.5E-45 atau -2E64+1
                    } else if (rangeMatematik && ((karakterSekarang == '+' || karakterSekarang == '-'))) {
                        nilaiToken += karakterSekarang;
                        //Bila menemukan delimiter penutup maka jadikan nomor ini sebagai sebuah token
                    } else if (karakterSekarang == ';' || karakterSekarang == ']' || karakterSekarang == ')') {
                        deretNomor();
                        nilaiToken += karakterSekarang;
                        bangkitkanToken(TOKEN_SIMBOL.get(String.valueOf(karakterSekarang)));
                    }        

                    //Bila nilai token sebelumnya titik dua dan karakter sekarang = maka nilai assignment ditemukan
                    //contoh :=
                } else if (bacaTitikDua && karakterSekarang == '=') {
                    nilaiToken += karakterSekarang;
                    bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                    bacaTitikDua = false;


                } else if (bacaBoolean) {
                    //Token yang terbentuk antara <= atau <>
                    if (nilaiToken.equals("<") && ((karakterSekarang == '=') || (karakterSekarang == '>'))) {
                        nilaiToken += karakterSekarang;
                        bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                        //Token yang terbentuk >=
                    } else if (nilaiToken.equals(">") && (karakterSekarang == '=')) {
                        nilaiToken += karakterSekarang;
                        bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                    }
                    bacaBoolean = false;
                } else {
                    //Bila yang terdeteksi identifier
                    if (karakterSekarang == ';') {
                        // Bila ditemukan titik koma maka masukan sebagai satu token
                        nilaiToken = satuKata();
                        nilaiToken = ";";
                        bangkitkanToken(TOKEN_SIMBOL.get(String.valueOf(karakterSekarang)));
                    } else if (karakterSekarang == ':') {
                        // Bila ditemukan titik dua maka kata sebelumnya selesai dan masukan kata sebelumnya
                        // sebagai satu token
                        nilaiToken = satuKata();
                        bacaTitikDua = true;
                        nilaiToken += karakterSekarang;
                    } else if (karakterSekarang == '<' || karakterSekarang == '>') {
                        // Bila ditemukan < atau > maka kata sebelumnya selesai dan masukan kata sebelumnya
                        // sebagai satu token
                        nilaiToken = satuKata();
                        bacaBoolean = true;
                        nilaiToken += karakterSekarang;
                    } else if (karakterSekarang == '.') {
                        //Bila ditemukan . setelah kata end maka program berhenti
                        if (nilaiToken.equals("end")) {
                            nilaiToken += karakterSekarang;
                            bangkitkanToken("TK_END");
                        } else if (nilaiToken.equals(".") && bacaTitik) {
                            //Ditemukan titik setelah titik maka jadilah token range
                            nilaiToken = "..";
                            //Token ..
                            bangkitkanToken("TK_RANGE");
                            bacaTitik = false;
                        } else {
                            nilaiToken += karakterSekarang;
                            bacaTitik = true;
                        }
                    } else if (TOKEN_SIMBOL.containsKey(String.valueOf(karakterSekarang))) {
                        //Bila ditemukan simbol lainnya, maka kata sebelumnya dianggap sebagai satu token
                        //kemudian jadikan simbol tersebut sebagai sebuah token
                        nilaiToken = satuKata();
                        nilaiToken = String.valueOf(karakterSekarang);
                        bangkitkanToken(TOKEN_SIMBOL.get(nilaiToken));
                    }

                }

                break;
            case QUOTE:
                //Rubah status baca string
                bacaString = !bacaString;
                nilaiToken += karakterSekarang;

                if (!bacaString) {
                    //Akhir dari string
                    if (nilaiToken.length() == 1) {
                        bangkitkanToken("TK_KONSTANTA_CHAR");
                    } else if (nilaiToken.length() > 1) {
                        bangkitkanToken("TK_KONSTANTA_STRING");
                    }
                }

                break;
            default:
                
                throw new Error("Unhandled element scanned");
        }
    }

    //Memanggil bangkitkan token untuk satu kata berupa identifier atau boolean (true atau false)
    private static String satuKata() {
        //Akhir dari sebuah kata yang merupakan keywords
        if (TOKEN_KEYWORDS.containsKey(nilaiToken)) {
            bangkitkanToken(TOKEN_KEYWORDS.get(nilaiToken));
        } else {
            //Cek apakah nilai token sudah lebih dari satu karakter atau tidak
            if (nilaiToken.length() > 0) {
                if (nilaiToken.equals("true") || nilaiToken.equals("false")) {
                    bangkitkanToken("TK_KONSTANTA_BOOLEAN");
                } else {
                    bangkitkanToken("TK_IDENTIFIER");
                }
            }
        }

        //Kembalikan semua status menjadi False
        kembalikanStatus();

        return nilaiToken;
    }

    //Mengembalikan semua status menjadi false
    private static void kembalikanStatus() {
        bacaString = false;
        bacaNomor = false;
        nilaiReal = false;
        rangeMatematik = false;
        bacaTitikDua = false;
        bacaBoolean = false;
    }

    //Membangkitkan sebuah token (membuat instansi objek dari kelas token)
    private static void bangkitkanToken(String tipe) {
        Token token = new Token(tipe, nilaiToken, tentukanJenis(tipe), kolom, baris);
        tokenArrayList.add(token);
        kolom += nilaiToken.length();
        nilaiToken = "";
    }

    //Memanggil bangkitkan token untuk sebuah nomor baik real atau integer
    private static void deretNomor() {
        bacaNomor = false;
        if (nilaiReal) {
            bangkitkanToken("TK_KONSTANTA_REAL");
            nilaiReal = false;
        } else {
            bangkitkanToken("TK_KONSTANTA_INT");
        }
    }

    //Menentukan jenis dari sebuah token
    private static String tentukanJenis(String tipe) {
        String jenis ;
        if (TOKEN_KEYWORDS.containsValue(tipe))
            jenis = "Identifier / Keyword";
        else if (TOKEN_OPERATOR.containsValue(tipe))
            jenis = "Operator";
        else if (tipe.equals("TK_BOOLEAN") || tipe.equals("TK_REAL")
                || tipe.equals("TK_KONSTANTA_INT") || tipe.equals("TK_KONSTANTA_CHAR")
                || tipe.equals("TK_KONSTANTA_STRING"))
            jenis = "Konstanta";
        else if (tipe.equals("TK_IDENTIFIER"))
            jenis = "Identifier / Variabel";
        else
            jenis = "Delimiter";

        return jenis;
    }

}
