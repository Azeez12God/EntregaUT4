package Entrega;

public class Cromo {
    private Long id;
    private String nombre;
    private String equipo;
    private int posicionAlbum;

    public Cromo() {
    }

    public Cromo(Long id, String nombre, String equipo, int posicionAlbum) {
        this.id = id;
        this.nombre = nombre;
        this.equipo = equipo;
        this.posicionAlbum = posicionAlbum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public int getPosicionAlbum() {
        return posicionAlbum;
    }

    public void setPosicionAlbum(int posicionAlbum) {
        this.posicionAlbum = posicionAlbum;
    }

    @Override
    public String toString() {
        return "Cromo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", equipo='" + equipo + '\'' +
                ", posicionAlbum=" + posicionAlbum +
                '}';
    }
}
