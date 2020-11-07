import java.io.ObjectInputStream;

public class Nodo_1 {
    private Object dato;
    private Nodo_1 next;

    public Nodo_1(Object Dato){
        this.dato = Dato;
    }

    public Object getDato() {
        return dato;
    }

    public Nodo_1 getNext() {
        return next;
    }

    public void setNext(Nodo_1 next) {
        this.next = next;
    }


}
