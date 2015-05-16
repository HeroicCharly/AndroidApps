package carlosperez.okaneappv2;

/**
 * Created by Carlos Pérez on 14/05/2015.
 */
public class dbGasto {

    int id;
    String concepto;
    String precio;

    public dbGasto(){}

    public dbGasto(int id, String concepto, String precio){
        this.id = id;
        this.concepto = concepto;
        this.precio = precio;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getConcepto() {
        return concepto;
    }

    public String getPrecio() {
        return precio;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
