public class Carta {
    int codigo;
    int damage;
    String tipo;
    int nivel;
    public static Carta armar_carta(int codigo, int damage, String tipo, int nivel){
        Carta carta = new Carta();
        carta.nivel = nivel;
        carta.codigo = codigo;
        carta.damage = damage;
        carta.tipo = tipo;
        return carta;
    }
    public int getNivel() {
        return nivel;
    }
    public int getDamage(){
        System.out.print("damage: "+damage);
        return damage;
    }
    public int getCodigo(){
        System.out.print("código: "+codigo);
        return codigo;
    }
    public String getTipo(){
        System.out.print("código: "+tipo);
        return tipo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}