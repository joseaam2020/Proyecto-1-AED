public class User{
    private int mana;
    private int vida;
    private String nombre;

    public User(String Nombre) {
        this.mana = 200;
        this.vida = 1000;
        this.nombre = Nombre;
    }

    public int getMana() {
        return mana;
    }

    public int getVida() {
        return vida;
    }

    public void setMana(int Mana) {
        mana = Mana;
    }

    public void setVida(int Vida) {
        vida = Vida;
    }


}