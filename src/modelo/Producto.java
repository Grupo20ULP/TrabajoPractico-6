package modelo;

import java.util.Objects;

/**
 *
 * @author Nehuen
 */
public class Producto implements Comparable<Producto> {

    private int codigo;
    private String descripcion;
    private double precio;
    private int stock;
    private Rubro rubro;

    public Producto () {
    }

    public Producto (int codigo, String descripcion, double precio, int stock, Rubro rubro) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.rubro = rubro;
    }

    public int getCodigo () {
        return codigo;
    }

    public void setCodigo (int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion (String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio () {
        return precio;
    }

    public void setPrecio (double precio) {
        this.precio = precio;
    }

    public int getStock () {
        return stock;
    }

    public void setStock (int stock) {
        this.stock = stock;
    }

    public Rubro getRubro () {
        return rubro;
    }

    public void setRubro (Rubro rubro) {
        this.rubro = rubro;
    }

    // ----- ORDEN NATURAL POR CÓDIGO -----
    @Override
    public int compareTo (Producto o) {
        return Integer.compare(this.codigo, o.codigo);
    }

    // ----- IDENTIDAD POR CÓDIGO -----
    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if ( ! (o instanceof Producto)) {
            return false;
        }
        Producto that = (Producto) o;
        return this.codigo == that.codigo;
    }

    @Override
    public int hashCode () {
        return Objects.hash(codigo);
    }

    @Override
    public String toString () {
        return codigo + " - " + descripcion;
    }
}
