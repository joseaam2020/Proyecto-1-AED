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

    public FormJuego() {
        this.setSize(600,600);
        Path path = Paths.get("Proyecto-1-AED/src/main/java/otrasImagenes/atrasCarta.png");
        ImageIcon original = new ImageIcon(String.valueOf(path.toAbsolutePath()));
        System.out.println(original.toString());
        Image originalImage = original.getImage();
        Image moded = originalImage.getScaledInstance(50,70,Image.SCALE_SMOOTH);
        Icon imageBuffer = new ImageIcon(moded);
        setButton4Icon(imageBuffer);
        this.add(Contenedor);
    }

    public void setInvitado(String text){Invitado.setText(text);}
    public void setAnfitrion(String text){Anfitrion.setText(text);}

    public void setButton3Icon(Icon icon) {button3.setIcon(icon);}
    public void setButton4Icon(Icon icon) {button4.setIcon(icon);}

    public void setButton1Listener(ActionListener listener){this.button1.addActionListener(listener);}
    public void setButton2Listener(ActionListener listener){this.button2.addActionListener(listener);}
    public void setButton3Listener(ActionListener listener){this.button3.addActionListener(listener);}
    public void setButton4Listener(ActionListener listener){this.button4.addActionListener(listener);}


}
