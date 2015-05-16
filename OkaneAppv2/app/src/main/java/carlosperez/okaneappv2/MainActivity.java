package carlosperez.okaneappv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static boolean cambioColorFondoTabla = false;
    private static Context mainActivityContext;
    private static DataBase DB;
    private static TextView totalGastado;
    private static LogGastos logGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            logGastos = new LogGastos();
            RegistrarGasto registrarGasto = new RegistrarGasto();


            fragmentTransaction.add(R.id.fragmentRegistrarGasto,registrarGasto);
            fragmentTransaction.add(R.id.fragmentLogGastos, logGastos);

            fragmentTransaction.commit();

            mainActivityContext = getBaseContext();
            DB = new DataBase(getApplicationContext());

            totalGastado = (TextView)findViewById(R.id.txtViewTotal);
            updateTotalGastado();
        }
    }

    //UPDATES
    public static void updateTotalGastado(){
        totalGastado.setText("Total:$"+Integer.toString(DB.getTotalGastado()));
    }

    //FIN UPDATES

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void crearToast(String texto){
        Context context = mainActivityContext ;
        CharSequence text = texto;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    public void addGastoToFragment(){

    }

    public static class RegistrarGasto extends Fragment implements registrar_gasto.OnFragmentInteractionListener {

        public RegistrarGasto() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_registrar_gasto, container, false);

            Button btnRegistrarGasto = (Button)rootView.findViewById(R.id.bttnRegistrarGasto);

            btnRegistrarGasto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String txtProducto = ((EditText)rootView.findViewById(R.id.txtbxProducto)).getText().toString();
                    String txtPrecio = ((EditText)rootView.findViewById(R.id.txtbxPrecio)).getText().toString();

                    //Mandamos el mensaje al Activity que esta implementando el Fragment
                    registrarGasto(txtProducto, txtPrecio);
                }
            });

            return rootView;
        }

        @Override
        public void registrarGasto(String concepto, String precio) {
            crearToast(concepto + "-" + precio);

            dbGasto nuevoGasto = new dbGasto(1,concepto,precio);
            DB.createDBGasto(nuevoGasto);

            updateTotalGastado();

            //Limpiamos el texto a registrar
            EditText dummy = (EditText)getActivity().findViewById(R.id.txtbxProducto);
            dummy.setText("");
            dummy = (EditText)getActivity().findViewById(R.id.txtbxPrecio);
            dummy.setText("");
        }

    }

    public static class LogGastos extends Fragment {

        public LogGastos() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_log_gastos, container, false);

            //Declaramos los elementos que iran en la tabla
            TableLayout logGastos = (TableLayout)rootView.findViewById(R.id.tblLogGastos);

            //obtenemos todos los gastos registrados
            List<dbGasto> listaDeGastos =  DB.getAllGastos();

            //Hacemos un loop para obtener todos los datos de la BD
            for(dbGasto dummyGasto : listaDeGastos){

                TableRow tableRow = new TableRow(rootView.getContext());
                EditText tdConcepto = new EditText(rootView.getContext());
                EditText tdPrecio = new EditText(rootView.getContext());

                tdConcepto.setText(dummyGasto.getConcepto());
                tdPrecio.setText(dummyGasto.getPrecio());

                //Intercalamos el color de fondo de los renglones

                if(cambioColorFondoTabla){
                    cambioColorFondoTabla = false;
                    tdPrecio.setBackgroundColor(Color.GRAY);
                    tdConcepto.setBackgroundColor(Color.GRAY);
                }else {
                    cambioColorFondoTabla = true;
                    tdPrecio.setBackgroundColor(Color.WHITE);
                    tdConcepto.setBackgroundColor(Color.WHITE);
                }

                //Agregamos al renglon los elementos
                tableRow.addView(tdConcepto);
                tableRow.addView(tdPrecio);

                logGastos.addView(tableRow);
            }

            return rootView;
        }

    }

}
