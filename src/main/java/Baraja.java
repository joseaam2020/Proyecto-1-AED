import java.util.Stack;

public class Baraja {
    Stack baraja = arma_pila();
    private Stack arma_pila() {
        Stack baraja = new Stack();
        int i = 0, cantidad = 30, rango = 30;
        int arreglo[] = new int[cantidad];

        for (i = 1; i < cantidad; i++) {
            arreglo[i] = (int) (Math.random() * rango);
            for (int j = 0; j < i; j++) {
                if (arreglo[i] == arreglo[j]) {
                    i--;
                }
            }
        }
        for (int k = 0; k < cantidad; k++) {
            baraja.push(arreglo[k]);
        }
        System.out.print(baraja.peek());
        return baraja;
    }
    public void getCarta_nueva() {
        int prueba = Integer.parseInt(baraja.pop().toString());

    }

}