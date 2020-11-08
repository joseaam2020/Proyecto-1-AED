public class User{
    private int mana;
    private int vida;
    private String nombre;

    public User(String Nombre) {
        this.mana = 200;
        this.vida = 1000;
        this.nombre = Nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMana() {
        return this.mana;
    }

    public int getVida() {
        return this.vida;
    }

    public void setMana(int Mana) {
        this.mana = Mana;
    }

    public void setVida(int Vida) {
        this.vida = Vida;
    }


}