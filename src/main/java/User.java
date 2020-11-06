 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Genera una ventana de Chat para un usuario, con su respectivo ServerSocket
 */
public class User {
    public static void main(String[] args) {
        UserFrame newFrame = new UserFrame("Chat");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * Genera el marco de la aplicacion. Extiende a JFrame.
 *
 */
class UserFrame extends JFrame{
    /**
     * Constructor de UserFrame.
     *
     * Añade el titulo y un icono al marco del Usuario
     *
     * @param titulo titulo del marco
     */
    public UserFrame(String titulo){
        super(titulo);
        URL iconUrl = getClass().getResource("chat-bubble.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        this.setIconImage(icon.getImage());
        setBounds(600,300,280,350);
        setResizable(false);
        UserPanel newUser = new UserPanel();
        add(newUser);
        setVisible(true);
    }
}

/**
 * Genera el panel de la aplicacion. En este se encuentran opciones para ingresar el nombre del usuario,
 * la ip del destinatario y su respectivo puerto. Ademas, proporciona una ventana para ver y eviar mensajes
 * y el numero del puerto el cual esta utilizando el usuario.
 */
class UserPanel extends JPanel implements Runnable{

    private JTextField campoTexto;
    private JTextField campoNombre;
    private JTextField ipAddress;
    private JTextField campoOut;
    private JButton boton1;
    private JTextArea campoMensajes;
    private JLabel userPort;
    private JScrollPane marcoMensajes;

    public UserPanel(){
        Color panelBackground = Color.decode("#292F36");
        Color labelColor = Color.decode("#FFFFFF");
        Color portColor = Color.decode("#FF6B6B");
        Color botonColor = Color.decode("#4ECDC4");
        this.setBackground(panelBackground);
        JLabel nombre = new JLabel("Nombre:");
        nombre.setForeground(labelColor);
        add(nombre);
        campoNombre = new JTextField(20);
        add(campoNombre);
        JLabel ip = new JLabel("Ip del destinatario:");
        ip.setForeground(labelColor);
        add(ip);
        ipAddress = new JTextField(10);
        add(ipAddress);
        JLabel out = new JLabel("Puerto del destinatario:");
        out.setForeground(labelColor);
        add(out);
        campoOut = new JTextField(10);
        campoOut.setText("0");
        add(campoOut);
        campoMensajes = new JTextArea(10,26);
        campoMensajes.setEditable(false);
        marcoMensajes = new JScrollPane();
        marcoMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        marcoMensajes.getViewport().add(campoMensajes);
        add(marcoMensajes);
        campoTexto = new JTextField(20);
        add(campoTexto);
        boton1 = new JButton("Enviar");
        TextoBoton newTextoBoton = new TextoBoton();
        boton1.addActionListener(newTextoBoton);
        boton1.setBackground(botonColor);
        boton1.setForeground(labelColor);
        add(boton1);
        Thread userThread = new Thread(this);
        userThread.start();
        userPort = new JLabel("");
        userPort.setForeground(portColor);
        add(userPort);
        campoOut.setText("");
    }

    /**
     * Acciones que ocurren al oprimir el boton de enviar. Implementa ActionListener.
     *
     * Crea un String con el nombre del usuario y el mensaje ingresado. Despues, genera una
     * conexion por medio de un Socket con el IP y el puerto indicado. A traves de este,
     * envia el texto por un DataInputStream. Puede lanzar excepciones de IoException y
     * NumberFormatException
     */
    private class TextoBoton implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            StringBuilder texto = new StringBuilder(campoNombre.getText() + ": " + campoTexto.getText());
            campoMensajes.append(String.valueOf(texto)+"\n");
            campoTexto.setText("");
            try {
                int port = Integer.parseInt(campoOut.getText());
                Socket newSocket = new Socket(ipAddress.getText(),port);
                DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
                output.writeUTF(String.valueOf(texto));
                output.close();
            } catch (IOException ioException) {
                System.out.println("Sending");
                System.out.println(ioException.getMessage());;
            } catch (NumberFormatException numberFormatException){
                System.out.println("Port error");
                System.out.println(numberFormatException.getMessage());
            }
        }
    }

    /**
     * Metodo generado al implementar Runnable. Se le ha modificado para que, por me dio de Threads,
     * cree un ServerSocket que esta constantemente a la escucha de mensaje enviados al puerto indicado.
     * Al recibir uno de estos mensajes, se imprime en la ventana de Chat.
     *
     * @throws IOException
      */
    public void run() {
        try {
            ServerSocket newSS = new ServerSocket(0);
            StringBuilder port = new StringBuilder();
            port.append(String.valueOf(newSS.getLocalPort()));
            userPort.setText("Utilizando puerto:" + String.valueOf(port));
            while(true){
                StringBuilder texto = new StringBuilder();
                Socket inputSocket = newSS.accept();
                DataInputStream input = new DataInputStream(inputSocket.getInputStream());
                String inputString = input.readUTF();
                texto.append(inputString);
                campoMensajes.append(String.valueOf(texto)+"\n");
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}