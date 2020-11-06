
import com.fasterxml.jackson.databind.JsonNode;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Arrays;


public class Juego {
    /**
     * Crea la ventana de MonsterTECG
     */
    public static void main(String[] args) {
        FrameJuego nuevoJuego = new FrameJuego();
        nuevoJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class FrameJuego extends JFrame{
    /**
     * Ajusta el marco de la ventana de juego y crea el panel de juego
     */
    public FrameJuego(){
        super("Monster TecG");
        setBounds(200,50,600,600);
        setResizable(false);
        PanelJuego nuevoJuego = new PanelJuego();
        add(nuevoJuego);
        setVisible(true);
    }
}

class PanelJuego extends JPanel{

    private JButton button0;
    private JButton button1;
    private JPanel menu;
    private JPanel enviarDireccion;
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
    volatile private boolean enJuego = false;
    private String ipAjeno;
    private int puertoAjeno;
    private SwingWorker server;


    public PanelJuego(){
        runServer();
        setLayout(new FlowLayout(FlowLayout.CENTER,50,100));
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

    public class Anfitrion implements ActionListener {
        /**
         * Accion que ocurre al seleccionar ser Anfitrion
         *
         * @param e
         */
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
                datos.add(ipAddress);
                datos.add(puertoEnUso);
                add(datos);
                updateUI();
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    while (true) {
                        /*loop hace que se muestre el Ip y puerto del anfitrion
                        * hasta que se reciba un mensaje del invitado*/
                        boolean refrescar = getEnJuego();
                        if (refrescar) {
                            break;
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    empezarJuego();
                }
            };
            worker.execute();
        }
    }

    public class Invitado implements ActionListener{
        private JTextField campoIP;
        private JTextField campoPuerto;
        private JButton envioDireccion;

        /**
         * Accion que ocurre al seleccionar ser invitado
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            InetAddress localIP = null;
            try {
                enviarDireccion = new JPanel();
                enviarDireccion.setLayout(new GridLayout(3,2,10,30));
                localIP = InetAddress.getLocalHost();
                java.lang.String ipString = localIP.getHostAddress().toString();
                String jsonString = "{ \"ipAddress\":\"" + ipString + "\",\"port\":" + puertoInt + "}";
                remove(menu);
                JLabel ipAddress = new JLabel("Dirección IP:");
                enviarDireccion.add(ipAddress);
                campoIP = new JTextField(20);
                enviarDireccion.add(campoIP);
                JLabel puertoDestinatario = new JLabel("Puerto:");
                enviarDireccion.add(puertoDestinatario);
                campoPuerto = new JTextField(20);
                enviarDireccion.add(campoPuerto);
                envioDireccion = new JButton("Enviar a anfitrión");
                envioDireccion.addActionListener(new Enviar(jsonString));
                envioDireccion.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        empezarJuego();
                    }
                });
                enviarDireccion.add(envioDireccion);
                add(enviarDireccion);
                updateUI();
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
        }

        public java.lang.String getIpAddress(){
            return campoIP.getText();
        }

        public java.lang.String getPuerto(){
            return campoPuerto.getText();
        }
    }


    private class Enviar implements ActionListener{

        private String paquete ;

        /**
         * Agega funcionalidad a los botones para enviar paquetes a informacion.
         *
         * @param nuevoPaquete Json en forma de String
         */
        public Enviar(String nuevoPaquete){
            paquete = nuevoPaquete;
        }

        /**
         * Funcion de botones para enviar informaion al otro jugador
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            try {
                int port = Integer.parseInt(direccion.getPuerto());
                Socket newSocket = new Socket(direccion.getIpAddress(),port);
                DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
                output.writeUTF(paquete);
                output.close();
                System.out.println("enviado");
            } catch (IOException ioException) {
                System.out.println("Sending");
                System.out.println(ioException.getMessage());
            } catch (NumberFormatException numberFormatException){
                System.out.println("Port error");
                System.out.println(numberFormatException.getMessage());
            }
        }
    }

    /**
     * Crea un ServerSocket en el cual se aceptan mensajes enviados utilizando SwingWorker.
     */
    public void runServer(){
        server = new SwingWorker<Void, Void>(){
            @Override
            protected synchronized Void doInBackground() throws Exception {
                try {
                    while(true){
                        Socket inputSocket = newSS.accept();
                        DataInputStream input = new DataInputStream(inputSocket.getInputStream());
                        String stringRecibido = input.readUTF();
                        JsonNode jsonRecibido = Json.parse(stringRecibido);
                        System.out.println(stringRecibido);
                        System.out.println(jsonRecibido.has("ipAddress"));
                        if(jsonRecibido.has("ipAddress")){
                            ipAjeno = jsonRecibido.get("ipAddress").asText();
                            puertoAjeno = jsonRecibido.get("port").asInt();
                            enJuego = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } return null;
            }
        };
        server.execute();
    }

    public boolean getEnJuego(){return this.enJuego;}

    JFrame fdialogo;
    JDialog dialogo;
    JLabel nombre;
    JTextField campoNombre;
    JButton enter;
    String stringNombre;

    /**
     * Borra componentes en el Panel y agrega la interfaz de juego.
     */
    public void empezarJuego(){
        removeAll();
        fdialogo = new JFrame();
        dialogo = new JDialog(fdialogo, "Ingresar Nombre", true);
        dialogo.setLayout(new FlowLayout());
        nombre = new JLabel("Nombre:");
        campoNombre = new JTextField(10);
        enter = new JButton("Ingresar");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stringNombre = campoNombre.getText();
                dialogo.setVisible(false);
            }
        });
        dialogo.add(nombre);
        dialogo.add(campoNombre);
        dialogo.add(enter);
        dialogo.setSize(200,200);
        dialogo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
        setLayout(new BorderLayout(10,100));
        User newUser = new User(stringNombre);
        FormJuego setJuego = new FormJuego();
        JPanel juego = setJuego.getContenedor();
        add(juego);
        for(Component component : juego.getComponents()){
            System.out.println(component.getClass());
        }
        updateUI();
    }

}