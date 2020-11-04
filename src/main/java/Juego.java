
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Juego {
    public static void main(String[] args) {
        FrameJuego nuevoJuego = new FrameJuego();
        nuevoJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class FrameJuego extends JFrame{
    public FrameJuego(){
        super("Monster TecG");
        setBounds(200,50,800,800);
        setResizable(false);
        PanelJuego nuevoJuego = new PanelJuego();
        add(nuevoJuego);
        setVisible(true);
    }
}

class PanelJuego extends JPanel implements Runnable{

    private JButton button0;
    private JButton button1;
    private JPanel menu;
    private Invitado direccion;
    private ServerSocket newSS;
    {
        try {
            newSS = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int puertoInt = newSS.getLocalPort();
    private String puertoString = String.valueOf(puertoInt);


    public PanelJuego(){
        setLayout(new FlowLayout(FlowLayout.CENTER,10,200));
        menu = new JPanel();
        JLabel seleccion = new JLabel("Desea ser:");
        menu.add(seleccion);
        button0 = new JButton("Anfitrión");
        button0.addActionListener(new Anfitrion());
        menu.add(button0);
        button1 = new JButton("Invitado");
        direccion = new Invitado();
        button1.addActionListener(direccion);
        menu.add(button1);
        add(menu);
    }

    public class Anfitrion implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                System.out.println(puertoString);
                remove(menu);
                JPanel datos = new JPanel();
                InetAddress localIP = InetAddress.getLocalHost();
                java.lang.String ipString = localIP.getHostAddress().toString();
                JLabel ipAddress = new JLabel("Dirección IP:" + ipString);
                JLabel puertoEnUso = new JLabel("Utilizando puerto:" + puertoString);
                add(ipAddress);
                add(puertoEnUso);
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
            updateUI();
        }
    }

    public class Invitado implements ActionListener{
        private JTextField campoIP;
        private JTextField campoPuerto;
        private JButton envioDireccion;

        @Override
        public void actionPerformed(ActionEvent e) {
            remove(menu);
            JLabel ipAddress = new JLabel("Dirección IP:");
            add(ipAddress);
            campoIP = new JTextField(20);
            add(campoIP);
            JLabel puertoDestinatario = new JLabel("Puerto:");
            add(puertoDestinatario);
            campoPuerto = new JTextField(20);
            add(campoPuerto);
            envioDireccion = new JButton("Enviar a anfitrión");
            envioDireccion.addActionListener(new Enviar());
            add(envioDireccion);
            updateUI();
        }

        public java.lang.String getIpAddress(){
            return campoIP.getText();
        }

        public java.lang.String getPuerto(){
            return campoPuerto.getText();
        }
    }

    private class Enviar implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            try {
                int port = Integer.parseInt(direccion.getPuerto());
                Socket newSocket = new Socket(direccion.getIpAddress(),port);
                ObjectOutputStream output = new ObjectOutputStream(newSocket.getOutputStream());
                output.close();
            } catch (IOException ioException) {
                System.out.println("Sending");
                System.out.println(ioException.getMessage());
            } catch (NumberFormatException numberFormatException){
                System.out.println("Port error");
                System.out.println(numberFormatException.getMessage());
            }
        }
    }


    @Override
    public void run() {
        try {
            while(true){
                Socket inputSocket = newSS.accept();
                DataInputStream input = new DataInputStream(inputSocket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}