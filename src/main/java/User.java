public class User{
    private int mana;
    private int vida;
    private String nombre;

    public User(String Nombre) {
        this.mana = 200;
        this.vida = 1000;
        this.nombre = Nombre;
    }

    /**
     * Convierte toma los datos de usuario para generar archivo json
     * @return json
     */
    public String makeJsonString(){
        String json = ("{\"usuario\":\"" + this.nombre +"\","
                + "\"vida\":" + this.vida + ","
                + "\"mana\":" + this.mana +""
                +"}");
        return json;
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