
public class lista_enlazada_simple {
    private Nodo_1 primer_nodo;
    private int lista_size;

    public lista_enlazada_simple(){
        this.primer_nodo = null;
        this.lista_size = 0;
    }

    public int getLista_size() {
        return lista_size;
    }
    /**
     * Este método revisa si la lista está vacía
     * @return boolean
     */
    public boolean lista_vacia(){
        if(primer_nodo == null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Método para agregar nodos a la lista simple
     * @param dato Object
     */
    public void agregar_nodo(Object dato){
        if(lista_vacia()){
            Nodo_1 newNodo = new Nodo_1(dato);
            primer_nodo = newNodo;
            lista_size++;
        } else{
            Nodo_1 nodoActual = primer_nodo;
            while(nodoActual.getNext() != null){
                nodoActual = nodoActual.getNext();
            };
            nodoActual.setNext(new Nodo_1(dato));
            lista_size++;
        }
    }

    /**
     * Método usado para revisión de funcionamiento
     */
    public void print(){
        if(lista_vacia()){
            System.out.println("[]");
        } else {
            StringBuilder lista = new StringBuilder();
            lista.append("[" + primer_nodo.getDato());
            Nodo_1 nodoActual = primer_nodo.getNext();
            while(nodoActual != null){
                lista.append(nodoActual.getDato() + ",");
                nodoActual = nodoActual.getNext();
            } lista.append("]");
            System.out.println(String.valueOf(lista));
        }
    }

    /**
     * Encontrar la posición de una carta en la mano del jugador
     * @param posicion int
     * @return Nodoactual
     */
    public Nodo_1 getPosicion(int posicion){
        if(posicion > this.lista_size-1){
            return null;
        }
        int indice = 0;
        Nodo_1 nodoActual = this.primer_nodo;
        while(indice != posicion){
            nodoActual = nodoActual.getNext();
            indice++;
        } return nodoActual;
    }

}
