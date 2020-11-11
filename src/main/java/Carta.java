import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Carta {
    int codigo;
    int damage;
    String tipo;
    int nivel;
    Icon image;
    int costo;

    public static Carta armar_carta(int codigo, int damage, String tipo, int nivel, Icon icon ){
        Carta carta = new Carta();
        carta.nivel = nivel;
        carta.codigo = codigo;
        carta.damage = damage * nivel;
        carta.tipo = tipo;
        carta.image = icon;
        carta.costo = carta.damage / 2;
        return carta;
    }

    public int getCosto(){return costo;}
    public Icon getImage() {
        return image;
    }
    public int getNivel() {
        return nivel;
    }
    public int getDamage(){
        //System.out.print("damage: "+damage);
        return damage;
    }
    public int getCodigo(){
        //System.out.print("código: "+codigo);
        return codigo;
    }
    public String getTipo(){
        //System.out.print("código: "+tipo);
        return tipo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    /**
     * Carga toda las imagenes de images en Cartas, dandoles sus atributos respectivos
     *
     * @return una lista simple con todas las cartas con archivos de images
     */
    public static lista_enlazada_simple cargarImagenes() {
        Path path = Paths.get("Proyecto-1-AED/src/main/java/images");
        //C:\Users\AndresP\IdeaProjects\Proyecto-1-AED-main\proyecto1\Proyecto-1-AED\src\main\java\images
        File images = new File(String.valueOf(path.toAbsolutePath()));//String.valueOf(path.toAbsolutePath()));
        System.out.println(String.valueOf(path.toAbsolutePath()));
        lista_enlazada_simple todasCartas = new lista_enlazada_simple();
        int i = 1;
        for (File image : images.listFiles()) {
            String nombre = image.getName();
            String[] propiedades = nombre.split("\\.");
            int damage;
            int nivel = 0;
            switch (propiedades[0]) {
                case "hechizo sanación":
                    damage = 0;
                    break;
                case "huevo":
                    damage = 0;
                    break;
                case "stickman":
                    damage = 60;
                    break;
                case "pinguino":
                    damage = 80;
                    break;
                case "mago":
                    damage = 100;
                    break;
                case "dragon":
                    damage = 150;
                    break;
                case "conejo":
                    damage = 70;
                    break;
                case "bruja":
                    damage = 100;
                    break;
                case "bandido":
                    damage = 50;
                    break;
                default:
                    damage = 0;
            }
            if (!Objects.equals(propiedades[1], "png")) {
                nivel = Integer.parseInt(propiedades[1]);
            }
            ImageIcon original = new ImageIcon(image.getAbsolutePath());
            Image originalImage = original.getImage();
            Image moded = originalImage.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
            Icon imageBuffer = new ImageIcon(moded);
            Carta nuevaCarta = Carta.armar_carta(i, damage, propiedades[0], nivel, imageBuffer);
            todasCartas.agregar_nodo(nuevaCarta);
            i++;
        }
        return todasCartas;
    }

    public String makeJsonCode(){
        String jsonCode = "{ \"carta\" :" + this.codigo + "}";
        return jsonCode;
    }
}