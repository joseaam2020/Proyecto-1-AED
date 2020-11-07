
public class Nodo_1 {
    Nodo_1 jugada;
    Carta carta_usada;
    Nodo_1 next_nodo;
    public Nodo_1(Nodo_1 jugada,Carta carta_usada){
        this.jugada = jugada;
        this.next_nodo= jugada.next_nodo;
    }
    public Nodo_1 getNext_nodo() {
        return next_nodo;
    }
}
