import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FormJuego extends JPanel {
    private JPanel Contenedor;
    private JPanel Oeste;
    private JPanel Centro;
    private JPanel Este;
    private JPanel Sur;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JLabel Cartas;
    private JLabel Deck;
    private JLabel Anfitrion;
    private JLabel Invitado;
    private JLabel StingMana;
    private JLabel StringManaInvitado;
    private JLabel StringVidaInvitado;
    private JPanel surCartas;
    private JPanel surDeck;
    private JLabel StringVida;
    private JLabel IntVida;
    private JLabel IntMana;
    private JLabel IntVidaInvitado;
    private JLabel IntManaInvitado;
    private JButton atrasButton;
    private JButton adelanteButton;
    private JLabel ImageCenter;
    private JButton Saltar;
    private JLabel valueAtaque;
    private JLabel valueMana;
    volatile private Nodo_2 nodo_carta;
    volatile private Baraja deck;
    volatile private lista_circular mano;
    volatile private Lista_enlazada_doble historial;
    volatile private Nodo_3 registro;
    volatile private lista_enlazada_simple CartasSupremas = new lista_enlazada_simple();
    volatile private boolean dragon = false;
    volatile private boolean secreto = false;
    volatile private int damageSecreto;

    public int getDamageSecreto() {
        return damageSecreto;
    }
    public void setDamageSecreto(int damageSecreto) {
        this.damageSecreto = damageSecreto;
    }
    public boolean isDragon() {
        return dragon;
    }
    public void setDragon(boolean dragon) {
        this.dragon = dragon;
    }
    public boolean isSecreto() {
        return secreto;
    }
    public void setSecreto(boolean secreto) {
        this.secreto = secreto;
    }

    public lista_enlazada_simple getCartasSupremas() {
        return CartasSupremas;
    }

    public FormJuego() {
        this.setSize(600,600);
        Path path = Paths.get("Proyecto-1-AED/src/main/java/otrasImagenes/atrasCarta.png");
        ImageIcon original = new ImageIcon(String.valueOf(path.toAbsolutePath()));
        Image originalImage = original.getImage();
        Image moded = originalImage.getScaledInstance(50,70,Image.SCALE_SMOOTH);
        Icon imageBuffer = new ImageIcon(moded);
        setButton4Icon(imageBuffer);
        this.add(Contenedor);
    }

    public void setNodo_carta(Nodo_2 nodo_carta) {
        this.nodo_carta = nodo_carta;
    }
    public Nodo_2 getNodo_carta() {
        return nodo_carta;
    }
    public void setDeck(Baraja deck) {
        this.deck = deck;
    }
    public Baraja getDeck() {
        return deck;
    }
    public lista_circular getMano() {
        return mano;
    }
    public Lista_enlazada_doble getHistorial(){return historial;}
    public void setMano(lista_circular mano) {
        this.mano = mano;
    }
    public Nodo_3 getRegistro(){return registro;}

    public void eliminarCartaMano(Carta carta){
        this.mano.eliminar(carta);
    }

    public void setValueAtaque(int ataque){this.valueAtaque.setText("Ataque:" + ataque);}
    public void setValueAtaqueString(String Ataque){this.valueAtaque.setText("Ataque:"+ Ataque);}
    public void setValueMana(int mana){this.valueMana.setText("Costo:" + mana + "mana");}

    public void insertarHistorial(String Jugador, Carta carta){
        if (historial == null){
            historial = new Lista_enlazada_doble();
        }
        Registro nuevoRegistro = new Registro(Jugador, carta);
        this.historial.insertar(nuevoRegistro);
    }

    public void mostarHistorial(Nodo_3 nodo){
        this.registro = nodo;
        Registro registro = (Registro) nodo.getDato();
        Carta carta = registro.getCarta();
        this.setImageCenterIcon(carta.getImage());
        int damage = carta.getDamage();
        if (damage != 0){
            this.setImageCenterText(registro.getJugador() + ": daño de " + damage);
        } else{
            this.setImageCenterText(registro.getJugador() + ": daño de " + carta.getTipo());
        }

    }

    public class Registro{
        private String jugador;
        private Carta carta;

        public Registro(String Jugador, Carta cartaUtilizada){
            this.carta = cartaUtilizada;
            this.jugador = Jugador;
        }

        public String getJugador() {
            return jugador;
        }

        public Carta getCarta() {
            return carta;
        }
    }

    public void setInvitado(String text){Invitado.setText(text);}
    public void setAnfitrion(String text){Anfitrion.setText(text);}
    public void setImageCenterText(String text){ImageCenter.setText(text);}

    public void setButton3Icon(Icon icon) {button3.setIcon(icon);}
    public void setButton4Icon(Icon icon) {button4.setIcon(icon);}
    public void setImageCenterIcon(Icon icon) {ImageCenter.setIcon(icon);}

    public void setIntVida(int intVida) {
        IntVida.setText(String.valueOf(intVida));
    }
    public void setIntMana(int intMana) {
        IntMana.setText(String.valueOf(intMana));
    }
    public void setIntVidaInvitado(int intVidaInvitado) { IntVidaInvitado.setText(String.valueOf(intVidaInvitado)); }
    public void setIntManaInvitado(int intManaInvitado) {
        IntManaInvitado.setText(String.valueOf(intManaInvitado));
    }

    public void setButton1Listener(ActionListener listener){this.button1.addActionListener(listener);}
    public void setButton2Listener(ActionListener listener){this.button2.addActionListener(listener);}
    public void setButton3Listener(ActionListener listener){this.button3.addActionListener(listener);}
    public void setButton4Listener(ActionListener listener){this.button4.addActionListener(listener);}
    public void setSaltarListener(ActionListener listener){this.Saltar.addActionListener(listener);}
    public void setAtrasButtonListener(ActionListener listener){this.atrasButton.addActionListener(listener);}
    public void setAdelanteButtonListener(ActionListener listener){this.adelanteButton.addActionListener(listener);}

    public void setinterfazCartas(){
        Nodo_2 nodoactual = this.getNodo_carta();
        Carta carta = nodoactual.getCarta_en_mano();
        if (carta.getDamage() != 0){
            this.setButton3Icon(carta.getImage());
            this.setValueAtaque(carta.getDamage());
            this.setValueMana(carta.getCosto());
        } else{
            this.setButton3Icon(carta.getImage());
            this.setValueAtaqueString(carta.getTipo());
            this.setValueMana(carta.getCosto());
        }
    }

    public void removeButton3Listener(){
        for(ActionListener listener : this.button3.getActionListeners()){
            this.button3.removeActionListener(listener);
        }
    }
    public void removeButton4Listener(){
        for(ActionListener listener : this.button4.getActionListeners()){
            this.button4.removeActionListener(listener);
        }
    }
    public void removeSaltarListener(){
        for(ActionListener listener : this.Saltar.getActionListeners()){
            this.Saltar.removeActionListener(listener);
        }
    }


}
