public class lista_circular {
    Nodo_2 carta_ult;
    Nodo_2 carta_next;

    public lista_circular(){
        carta_ult = null;
    }

    //método para saber si la lista está vacía
    public boolean lista_vacia(){
        return carta_ult == null;
    }

    //método para ingresar carta

    /**
     * Este método ingresa el objeto tipo carta a la mano del jugador para ser usada in-game
     * @param carta_a_ingresar Carta
     */
    public void insertar(Carta carta_a_ingresar){
        Nodo_2 nuevo = new Nodo_2(carta_a_ingresar);
        if(carta_ult!=null){
             nuevo.next = carta_ult.next;
             nuevo.prev = carta_ult;
             carta_ult.next=nuevo;
             nuevo.next.prev = nuevo;
        }
        carta_ult=nuevo;
    }

    public Nodo_2 getCarta_ult() {
        return carta_ult.next;
    }

    public void setCarta_ult(Nodo_2 carta_ult) {
        this.carta_ult = carta_ult;
    }

    public Nodo_2 getCarta_next() {
        carta_next = carta_ult.next;
        return carta_next;
    }

    /**
     * Método para eliminar las cartas de la mano al ser usadas o robadas
     * @param carta_a_eliminar Carta
     * @return encontrado
     */
    public boolean eliminar(Carta carta_a_eliminar){
        Nodo_2 actual;
        boolean encontrado = false;
        actual = carta_ult;
        while (actual.next!=carta_ult && !encontrado){
            encontrado = (actual.next.carta_en_mano==carta_a_eliminar);
            if (!encontrado){
                actual=actual.next;
            }
        }
        encontrado=(actual.next.carta_en_mano==carta_a_eliminar);
        if (encontrado){
            Nodo_2 aux = actual.next;
            if (carta_ult==carta_ult.next){
                carta_ult=null;
            }else{
                if (aux==carta_ult){
                    carta_ult=actual;
                }
                aux.next.prev = actual;
                actual.next=aux.next;
            }
            aux=null;
        }
        return encontrado;
    }
}