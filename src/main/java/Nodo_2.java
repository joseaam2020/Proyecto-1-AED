public class Nodo_2 {
    public Carta carta_en_mano;
    Nodo_2 next;
    public Carta getCarta_en_mano() {
        return carta_en_mano;
    }
    public void setCarta_en_mano(Carta carta_en_mano) {
        this.carta_en_mano = carta_en_mano;
    }
    public Nodo_2(Carta carta_en_mano){
        Carta carta = carta_en_mano;
        next = this;
    }
}
