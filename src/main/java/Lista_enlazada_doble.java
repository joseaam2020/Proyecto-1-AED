public class Lista_enlazada_doble {
    private Nodo_3 head;
    private Nodo_3 tail;

    public Lista_enlazada_doble(){
        this.head = null;
        this.tail = null;
    }

    public Nodo_3 getTail() {
        return tail;
    }

    public boolean estaVacia(){
        return this.head == null;
    }

    /**
     * Este método ingresa un objeto Dato a la lista enlazada doble para hacer un Nodo
     * @param Dato Object
     */
    public void insertar(Object Dato){
        Nodo_3 newNodo = new Nodo_3(Dato);
        if(this.estaVacia()){
            this.head = newNodo;
            this.tail = newNodo;
        } else{
            this.tail.setNext(newNodo);
            newNodo.setPrev(this.tail);
            this.tail = newNodo;
        }
    }
}
