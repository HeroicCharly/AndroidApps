package carlosperez.okaneappv2;

/**
 * Created by Carlos Pérez on 14/05/2015.
 */
public class dbBalance {


    int id;
    String totalGastado;
    String saldoInicial;
    String saldoActual;

    public dbBalance(){}

    public dbBalance(int id,String totalGastado,String saldoInicial,String Actual){
        this.totalGastado = totalGastado;
        this.saldoInicial = saldoInicial;
        this.saldoActual = Actual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(String totalGastado) {
        this.totalGastado = totalGastado;
    }

    public String getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(String saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(String saldoActual) {
        this.saldoActual = saldoActual;
    }

}
