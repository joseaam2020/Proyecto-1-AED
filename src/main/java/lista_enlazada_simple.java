public class lista_enlazada_simple {
}
public class lista_enlazada_simple {
    private Nodo_1 primer_nodo;
    private int lista_size;
    public lista_enlazada_simple(){
        this.primer_nodo = null;
        this.lista_size = 0;
    }
    public void lista_vacia(){
        if(primer_nodo == null){
            System.out.print("Lista vacia");
        }else{
            System.out.print("La lista contiene elementos");
        }
    }
    public void agregar_nodo(Nodo_1 jugada){
        Nodo_1 jugada_instante = new Nodo_1(jugada, Carta.armar_carta(1,1,"magico",1));
        jugada.next_nodo = primer_nodo;
        primer_nodo = jugada;
        lista_size++;
    }
    public Nodo_1 getPrimer_nodo() {
        return primer_nodo;
    }
}
