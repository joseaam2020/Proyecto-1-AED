
public class lista_enlazada_simple {
    private Nodo_1 primer_nodo;
    private int lista_size;

    public lista_enlazada_simple(){
        this.primer_nodo = null;
        this.lista_size = 0;
    }
    public boolean lista_vacia(){
        if(primer_nodo == null){
            return true;
        }else{
            return false;
        }
    }
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

}
