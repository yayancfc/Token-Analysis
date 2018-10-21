package tokenanalysis;

//Kelas POJO (Plain Old Java Object) untuk menampung setiap token yang di scan
public final class Token {

    //Nilai token
    private String mNilai;

    //Jenis token
    private String mJenis;

    //Tipe token
    private String mTipe;

    //Kolom tempat token berada
    private int mKolom;

    //Baris tempat token berada
    private int mBaris;


    //Konstruktor Token
    public Token(String mTipe, String mNilai, String mJenis, int mKolom, int mBaris) {
        this.mTipe = mTipe;
        this.mNilai = mNilai;
        this.mJenis = mJenis;
        this.mKolom = mKolom;
        this.mBaris = mBaris;
    }

    //Getter dan setter
    public String getmNilai() {
        return mNilai;
    }

    public void setmNilai(String mNilai) {
        this.mNilai = mNilai;
    }

    public String getmJenis() {
        return mJenis;
    }

    public void setmJenis(String mJenis) {
        this.mJenis = mJenis;
    }

    public String getmTipe() {
        return mTipe;
    }

    public void setmTipe(String mTipe) {
        this.mTipe = mTipe;
    }

    public int getmKolom() {
        return mKolom;
    }

    public void setmKolom(int mKolom) {
        this.mKolom = mKolom;
    }

    public int getmBaris() {
        return mBaris;
    }

    public void setmBaris(int mBaris) {
        this.mBaris = mBaris;
    }

    //Method toString yang di-override
    @Override
    public String toString() {
        return "\n" + mNilai + " : " + mJenis;
    }


}
