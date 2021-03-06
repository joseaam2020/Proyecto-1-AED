import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.JsonNode;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

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
    private String ipAjeno = null;
    private int puertoAjeno = -1;
    private SwingWorker server;
    private User newUser = null;
    private User stranger;
    private lista_enlazada_simple todasCartas = null;
    volatile private FormJuego setJuego = new FormJuego();
    volatile private boolean enTurno = false;
    private boolean esAnfitrion = false;

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
                esAnfitrion = true;
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
                    String stringNombre = preguntarNombre();
                    newUser = new User(stringNombre);
                    Enviar enviar = new Enviar(newUser.makeJsonString());
                    enviar.actionPerformed(new ActionEvent(new Object(), 0, "do"));
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
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            InetAddress localIP = null;
            try {
                enviarDireccion = new JPanel();
                enviarDireccion.setLayout(new GridLayout(3,2,10,30));
                localIP = InetAddress.getLocalHost();
                java.lang.String ipString = localIP.getHostAddress().toString();
                String jsonString = "{ \"ipAddress\":\"" + ipString
                                    + "\",\"port\":" + puertoInt
                                    + "}";
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
                envioDireccion.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String stringNombre = preguntarNombre();
                        newUser = new User(stringNombre);
                        Enviar enviar = new Enviar(newUser.makeJsonString());
                        enviar.actionPerformed(e);
                    }
                });
                envioDireccion.addActionListener(new Enviar(jsonString));
                /*
                envioDireccion.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        empezarJuego();
                    }
                });*/
                enviarDireccion.add(envioDireccion);
                add(enviarDireccion);
                updateUI();
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
                if(ipAjeno == null && puertoAjeno == -1){
                    puertoAjeno = Integer.parseInt(direccion.getPuerto());
                    ipAjeno = direccion.getIpAddress();
                }
                Socket newSocket = new Socket(ipAjeno,puertoAjeno);
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
                        if(jsonRecibido.has("ipAddress")){
                            ipAjeno = jsonRecibido.get("ipAddress").asText();
                            puertoAjeno = jsonRecibido.get("port").asInt();
                        }
                        if(jsonRecibido.has("carta")){
                            if(todasCartas != null && jsonRecibido.get("carta").asInt() != 31 && !jsonRecibido.has("robada")){
                                System.out.println(jsonRecibido.get("carta").asInt());
                                if (jsonRecibido.get("carta").asInt() == 12){
                                    Nodo_1 nodoActual = todasCartas.getPosicion(11);
                                    Carta cartaActual = (Carta) nodoActual.getDato();
                                    setJuego.insertarHistorial(stranger.getNombre(),cartaActual);
                                    setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                    Enviar enviar = new Enviar(Carta.armar_carta(
                                            31,
                                            0,
                                            "null",
                                            0,
                                            null).makeJsonCode());
                                    enviar.actionPerformed(new ActionEvent(new Object(), 0, "do"));
                                } else if(jsonRecibido.get("carta").asInt() == 16){
                                    Nodo_1 nodoActual = todasCartas.getPosicion(15);
                                    Carta cartaActual = (Carta) nodoActual.getDato();
                                    setJuego.insertarHistorial(stranger.getNombre(),cartaActual);
                                    setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                    Nodo_2 nodo = setJuego.getNodo_carta();
                                    Nodo_2 nuevonodo = nodo.next;
                                    setJuego.setNodo_carta(nuevonodo);
                                    setJuego.setinterfazCartas();
                                    Carta carta = nodo.getCarta_en_mano();
                                    String json = carta.makeJsonCodeRobada();
                                    setJuego.eliminarCartaMano(carta);
                                    Enviar enviar = new Enviar(json);
                                    enviar.actionPerformed(new ActionEvent(new Object(), 0, "do"));
                                } else if (jsonRecibido.get("carta").asInt() == 13){
                                    Nodo_1 nodoActual = todasCartas.getPosicion(12);
                                    Carta cartaActual = (Carta) nodoActual.getDato();
                                    setJuego.insertarHistorial(stranger.getNombre(),cartaActual);
                                    setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                    int mana = newUser.getMana();
                                    newUser.setMana((int) mana/2);
                                    setJuego.setIntMana(newUser.getMana());
                                    setEnTurno(true);
                                    updateUI();
                                } else if (jsonRecibido.get("carta").asInt() == 18){
                                    Nodo_1 nodoActual = todasCartas.getPosicion(17);
                                    Carta cartaActual = (Carta) nodoActual.getDato();
                                    setJuego.insertarHistorial(stranger.getNombre(),cartaActual);
                                    setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                    int vida = newUser.getVida();
                                    newUser.setVida((int) vida/3);
                                    setJuego.setIntVida(newUser.getVida());
                                    setEnTurno(true);
                                    updateUI();
                                } else {
                                    int size = todasCartas.getLista_size();
                                    for(int i = 0; i < size; i++){
                                        Nodo_1 nodoActual = todasCartas.getPosicion(i);
                                        Carta cartaActual = (Carta) nodoActual.getDato();
                                        int codigoCarta = cartaActual.getCodigo();
                                        int codigoRecibido = jsonRecibido.get("carta").asInt();
                                        if(codigoCarta == codigoRecibido && newUser != null && setJuego != null){
                                            int damage = cartaActual.getDamage();
                                            if (cartaActual.getTipo().equals("dragon") || cartaActual.getTipo().equals("stickman") || cartaActual.getTipo().equals("huevo") || cartaActual.getTipo().equals("monster energy")){
                                                damage = 0;
                                                System.out.println(cartaActual.getTipo());
                                            }
                                            if (jsonRecibido.has("secreta")){
                                                damage = jsonRecibido.get("secreta").asInt();
                                            }
                                            newUser.setVida(newUser.getVida() - damage);
                                            setJuego.setIntVida(newUser.getVida());
                                            setJuego.insertarHistorial(stranger.getNombre(),cartaActual);
                                            setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                            setEnTurno(true);
                                            updateUI();
                                        }
                                    }
                                }

                            } else if (jsonRecibido.has("robada")){
                                int size = todasCartas.getLista_size();
                                for(int i = 0; i < size; i++){
                                    Nodo_1 nodoActual = todasCartas.getPosicion(i);
                                    Carta cartaActual = (Carta) nodoActual.getDato();
                                    int codigoCarta = cartaActual.getCodigo();
                                    int codigoRecibido = jsonRecibido.get("carta").asInt();
                                    if(codigoCarta == codigoRecibido && newUser != null && setJuego != null){
                                        setJuego.getMano().insertar(cartaActual);
                                        Enviar enviar = new Enviar(Carta.armar_carta(
                                                31,
                                                0,
                                                "null",
                                                0,
                                                null).makeJsonCode());
                                        enviar.actionPerformed(new ActionEvent(new Object(), 0, "do"));
                                    }
                                }
                            }
                            else{
                                setEnTurno(true);
                            }
                        }
                        if(jsonRecibido.has("usuario")){
                            stranger = new User(jsonRecibido.get("usuario").asText());
                            stranger.setVida(jsonRecibido.get("vida").asInt());
                            stranger.setMana(jsonRecibido.get("mana").asInt());
                            setJuego.setInvitado(stranger.getNombre());
                            setJuego.setIntVidaInvitado(stranger.getVida());
                            setJuego.setIntManaInvitado(stranger.getMana());
                            if(!enJuego){
                                enJuego = true;
                                if(!esAnfitrion){
                                    System.out.println("Se hizo setEnTurno");
                                    setEnTurno(true);
                                }
                            }
                        }
                        if (jsonRecibido.has("ganaste")){
                            fdialogo = new JFrame();
                            dialogo = new JDialog(fdialogo, "Perdiste", true);
                            dialogo.setLayout(new FlowLayout());
                            nombre = new JLabel("Ganaste :)");
                            enter = new JButton("Cerrar");
                            enter.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    dialogo.setVisible(false);
                                }
                            });
                            dialogo.add(nombre);
                            dialogo.add(enter);
                            dialogo.setSize(200,200);
                            dialogo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            dialogo.setLocationRelativeTo(setJuego);
                            dialogo.setVisible(true);
                            setLayout(new BorderLayout(10,100));
                        } jsonRecibido = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } return null;
            }
        };
        server.execute();
    }

    public boolean getEnJuego(){return this.enJuego;}
    public void setEnTurno(boolean valor){this.enTurno = valor;}

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
        SwingWorker<Void, Void> turno = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                todasCartas = Carta.cargarImagenes();
                Baraja deck = new Baraja();
                removeAll();
                //setJuego = new FormJuego();
                int cont = 0;

                final lista_circular mano = new lista_circular();

                while (cont<4){
                    Nodo_1 nuevacarta = todasCartas.getPosicion(deck.getCarta_nueva());
                    Carta carta = (Carta) nuevacarta.getDato();
                    mano.insertar(carta);
                    cont++;
                }

                Nodo_2 pop = mano.getCarta_ult();
                Carta actual = pop.getCarta_en_mano();
                setJuego.setNodo_carta(pop);
                setJuego.setDeck(deck);
                setJuego.setMano(mano);

                setJuego.setinterfazCartas();
                setJuego.setAnfitrion(newUser.getNombre());
                setJuego.setIntVida(newUser.getVida());
                setJuego.setIntMana(newUser.getMana());

                setJuego.setButton2Listener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Nodo_2 nodoactual = setJuego.getNodo_carta();
                        nodoactual = nodoactual.next;
                        setJuego.setNodo_carta(nodoactual);
                        Carta carta = nodoactual.getCarta_en_mano();
                        setJuego.setinterfazCartas();
                        updateUI();
                    }
                });
                setJuego.setButton1Listener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Nodo_2 nodoactual = setJuego.getNodo_carta();
                        nodoactual = nodoactual.prev;
                        setJuego.setNodo_carta(nodoactual);
                        setJuego.setinterfazCartas();
                        updateUI();
                    }
                });
                setJuego.setAdelanteButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Nodo_3 registro = setJuego.getRegistro().getNext();
                        if (registro != null){
                            setJuego.mostarHistorial(registro);
                        }
                    }
                });
                setJuego.setAtrasButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Nodo_3 registro = setJuego.getRegistro().getPrev();
                        if (registro != null){
                            setJuego.mostarHistorial(registro);
                        }
                    }
                });
                add(setJuego);
                updateUI();
                boolean enTurnoActual = false;
                System.out.println(enTurnoActual);

                while(true) {
                    if (enTurnoActual != enTurno) {
                        enTurnoActual = enTurno;
                        if (enTurno) {
                            if(newUser.getVida() <= 0){
                                fdialogo = new JFrame();
                                dialogo = new JDialog(fdialogo, "Perdiste", true);
                                dialogo.setLayout(new FlowLayout());
                                nombre = new JLabel("Perdiste :(");
                                enter = new JButton("Cerrar");
                                enter.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        dialogo.setVisible(false);
                                    }
                                });
                                dialogo.add(nombre);
                                dialogo.add(enter);
                                dialogo.setSize(200,200);
                                dialogo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                                dialogo.setLocationRelativeTo(setJuego);
                                dialogo.setVisible(true);
                                setLayout(new BorderLayout(10,100));
                                Enviar enviar = new Enviar("{\"ganaste\":true}");
                                enviar.actionPerformed(new ActionEvent(new Object(), 0, "do"));
                            }
                            System.out.println("Empieza Turno");
                            setJuego.setButton3Listener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Carta carta = setJuego.getNodo_carta().getCarta_en_mano();
                                    int costo = carta.getCosto();
                                    int userMana = newUser.getMana();
                                    boolean secreta = false;
                                    int damageInicial = 0;
                                    if(costo <= userMana){
                                        if (setJuego.isDragon()){
                                            System.out.println("entro");
                                            if (carta.getTipo().equals("mago") || carta.getTipo().equals("bruja")){
                                                damageInicial = carta.getDamage();
                                                carta.setDamage(carta.getDamage() + setJuego.getDamageSecreto());
                                                setJuego.setDragon(false);
                                                secreta = true;
                                                System.out.println("sumo");
                                            }
                                        }
                                        if  (setJuego.isSecreto()){
                                            System.out.println("entro");
                                            if (!(carta.getTipo().equals("mago") || carta.getTipo().equals("bruja"))){
                                                damageInicial = carta.getDamage();
                                                carta.setDamage(carta.getDamage() + setJuego.getDamageSecreto());
                                                setJuego.setSecreto(false);
                                                secreta = true;
                                                System.out.println("sumo");
                                            }
                                        }
                                        if (carta.getCodigo() == 14){
                                            setJuego.removeButton3Listener();
                                            Nodo_2 nodo = setJuego.getNodo_carta();
                                            nodo = nodo.next;
                                            setJuego.setNodo_carta(nodo);
                                            setJuego.eliminarCartaMano(carta);
                                            setJuego.insertarHistorial(newUser.getNombre(),carta);
                                            setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                            setJuego.getCartasSupremas().agregar_nodo(carta);
                                            setJuego.setinterfazCartas();
                                            updateUI();
                                            setJuego.setButton3Listener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    Nodo_2 nodo = setJuego.getNodo_carta();
                                                    Carta cartaNueva = nodo.getCarta_en_mano();
                                                    nodo = nodo.next;
                                                    setJuego.setNodo_carta(nodo);
                                                    setJuego.getCartasSupremas().agregar_nodo(cartaNueva);
                                                    setJuego.eliminarCartaMano(cartaNueva);
                                                    setJuego.setinterfazCartas();
                                                    updateUI();
                                                    if (setJuego.getCartasSupremas().getLista_size() >= 4){
                                                        for(int i = 0; i < 4; i++){
                                                            Nodo_1 nodoSupremo = setJuego.getCartasSupremas().getPosicion(i);
                                                            Carta cartaSuprema = (Carta) nodoSupremo.getDato();
                                                            setJuego.insertarHistorial(newUser.getNombre(),cartaSuprema);
                                                            setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                                            Enviar enviar = new Enviar(cartaSuprema.makeJsonCode());
                                                            enviar.actionPerformed(e);
                                                        }
                                                        int deck = setJuego.getDeck().getCarta_nueva();
                                                        if (deck != 0){
                                                            int intCarta = deck;
                                                            Nodo_1 nodoCarta = todasCartas.getPosicion(intCarta);
                                                            Carta carta = (Carta) nodoCarta.getDato();
                                                            setJuego.getMano().insertar(carta);
                                                        }
                                                    }
                                                }
                                            });
                                        } else{
                                             if (carta.getTipo().equals("dragon")){
                                                 setJuego.setDragon(true);
                                                 setJuego.setDamageSecreto(carta.getDamage());
                                                 Enviar enviar = new Enviar(carta.makeJsonCode());
                                                 enviar.actionPerformed(e);
                                                 newUser.setMana(userMana - costo);
                                                 setJuego.insertarHistorial(newUser.getNombre(),carta);
                                                 setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                                 setJuego.eliminarCartaMano(carta);
                                                 Nodo_2 nodoactual = setJuego.getNodo_carta();
                                                 nodoactual = nodoactual.next;
                                                 setJuego.setNodo_carta(nodoactual);
                                                 setJuego.setinterfazCartas();
                                                 System.out.println("Acabo Turno");
                                                 float newMana = (float) (newUser.getMana() * 1.25);
                                                 newUser.setMana((int) newMana);
                                                 setJuego.setIntMana(newUser.getMana());
                                                 Enviar usuario = new Enviar(newUser.makeJsonString());
                                                 usuario.actionPerformed(e);
                                                 setEnTurno(false);
                                                 System.out.println("dragon");
                                            } else if (carta.getTipo().equals("stickman") || carta.getTipo().equals("monster energy") || carta.getTipo().equals("huevo")){
                                                 setJuego.setSecreto(true);
                                                 setJuego.setDamageSecreto(carta.getDamage());
                                                 Enviar enviar = new Enviar(carta.makeJsonCode());
                                                 enviar.actionPerformed(e);
                                                 newUser.setMana(userMana - costo);
                                                 setJuego.insertarHistorial(newUser.getNombre(),carta);
                                                 setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                                 setJuego.eliminarCartaMano(carta);
                                                 Nodo_2 nodoactual = setJuego.getNodo_carta();
                                                 nodoactual = nodoactual.next;
                                                 setJuego.setNodo_carta(nodoactual);
                                                 setJuego.setinterfazCartas();
                                                 System.out.println("Acabo Turno");
                                                 float newMana = (float) (newUser.getMana() * 1.25);
                                                 newUser.setMana((int) newMana);
                                                 setJuego.setIntMana(newUser.getMana());
                                                 Enviar usuario = new Enviar(newUser.makeJsonString());
                                                 usuario.actionPerformed(e);
                                                 setEnTurno(false);
                                                 System.out.println("secreto");
                                            } else {
                                                 if (carta.getCodigo() == 17){
                                                     int sumaVida = (int) (Math.random() * 500);
                                                     newUser.setVida(newUser.getVida() + sumaVida);
                                                     setJuego.setIntVida(newUser.getVida());
                                                     updateUI();
                                                 } else if (carta.getCodigo() == 15) {
                                                     newUser.setMana(newUser.getMana() * 4);
                                                     setJuego.setIntMana(newUser.getMana());
                                                     userMana = newUser.getMana();
                                                     updateUI();
                                                 }
                                                     if (secreta){
                                                         Enviar enviar = new Enviar(carta.makeJsonCodeSecreta());
                                                         enviar.actionPerformed(e);
                                                         carta.setDamage(damageInicial);
                                                     }else{
                                                         Enviar enviar = new Enviar(carta.makeJsonCode());
                                                         enviar.actionPerformed(e);
                                                     }
                                                     newUser.setMana(userMana - costo);
                                                     setJuego.insertarHistorial(newUser.getNombre(),carta);
                                                     setJuego.mostarHistorial(setJuego.getHistorial().getTail());
                                                     setJuego.eliminarCartaMano(carta);
                                                     Nodo_2 nodoactual = setJuego.getNodo_carta();
                                                     nodoactual = nodoactual.next;
                                                     setJuego.setNodo_carta(nodoactual);
                                                     setJuego.setinterfazCartas();
                                                     System.out.println("Acabo Turno");
                                                     float newMana = (float) (newUser.getMana() * 1.25);
                                                     newUser.setMana((int) newMana);
                                                     setJuego.setIntMana(newUser.getMana());
                                                     Enviar usuario = new Enviar(newUser.makeJsonString());
                                                     usuario.actionPerformed(e);
                                                     setEnTurno(false);
                                             }
                                        }
                                    }
                                }
                            });
                            setJuego.setButton4Listener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    int deck = setJuego.getDeck().getCarta_nueva();
                                    if (deck != 0){
                                        int intCarta = deck;
                                        Nodo_1 nodoCarta = todasCartas.getPosicion(intCarta);
                                        Carta carta = (Carta) nodoCarta.getDato();
                                        setJuego.getMano().insertar(carta);
                                        setJuego.removeButton4Listener();
                                    }
                                }
                            });
                            setJuego.setSaltarListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Enviar enviar = new Enviar(Carta.armar_carta(31,0,"null",0,null).makeJsonCode());
                                    enviar.actionPerformed(e);
                                    float newMana = (float) (newUser.getMana() * 1.25);
                                    newUser.setMana((int) newMana);
                                    setJuego.setIntMana(newUser.getMana());
                                    Enviar usuario = new Enviar(newUser.makeJsonString());
                                    usuario.actionPerformed(e);
                                    setEnTurno(false);
                                }
                            });

                        } else {
                            System.out.println("Se quita funcionalidad");
                            setJuego.removeButton3Listener();
                            setJuego.removeButton4Listener();
                            setJuego.removeSaltarListener();
                        }
                        System.out.println(enTurno);
                        System.out.println(enTurnoActual);
                    }
                }
            }
        }; turno.execute();

    }

    public String preguntarNombre(){
        fdialogo = new JFrame();
        dialogo = new JDialog(fdialogo, "Ingresar Nombre", true);
        dialogo.setLayout(new FlowLayout());
        nombre = new JLabel("Nombre:");
        campoNombre = new JTextField(13);
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
        return stringNombre;
    }
}
