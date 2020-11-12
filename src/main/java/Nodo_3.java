public class Nodo_3 {
    private Object dato;
    private Nodo_3 next;
    private Nodo_3 prev;

    public Nodo_3(Object Dato){
        this.dato = Dato;
        this.next = null;
        this.prev = null;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Nodo_3 getNext() {
        return next;
    }

    public void setNext(Nodo_3 next) {
        this.next = next;
    }

    public Nodo_3 getPrev() {
        return prev;
    }

    public void setPrev(Nodo_3 prev) {
        this.prev = prev;
    }
}
