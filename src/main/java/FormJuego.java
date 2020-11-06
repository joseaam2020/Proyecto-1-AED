import javax.swing.*;
import java.net.URL;

public class FormJuego extends JFrame {
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

    public FormJuego() {
        this.setSize(600,600);
        this.add(Contenedor);
        this.pack();
    }

    public JPanel getContenedor(){
        return this.Contenedor;
    }


}
