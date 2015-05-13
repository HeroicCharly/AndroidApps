package carlosperez.okaneappv2;

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
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    static TableLayout logGastos;
    static TableRow trLogGastos;
    static Button bttnRegistrarGasto;
    static EditText txtbxProducto;
    static EditText txtbxPrecio;
    static double precio = 0;
    static String producto = "NP";
    static boolean cambioColorFondoTabla = false;

    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            LogGastos logGastos = new LogGastos();
            RegistrarGasto registrarGasto = new RegistrarGasto();


            fragmentTransaction.add(R.id.fragmentRegistrarGasto,registrarGasto);
            fragmentTransaction.add(R.id.fragmentLogGastos,logGastos);

            fragmentTransaction.commit();

            sContext = getApplicationContext();
        }
    }


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
        Context context = getsContext();
        CharSequence text = texto;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    public static Context getsContext(){
        return sContext;
    }

    public static class RegistrarGasto extends Fragment {

        public RegistrarGasto() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_registrar_gasto, container, false);

            //Asignamos el boton a una variable que podamos manejar.
            bttnRegistrarGasto = (Button)rootView.findViewById(R.id.bttnRegistrarGasto);

            //suscribimos el botón a un listener
            bttnRegistrarGasto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Asignamos los botones a una variable que podamos manejar
                    txtbxProducto = (EditText)rootView.findViewById(R.id.txtbxProducto);
                    txtbxPrecio = (EditText)rootView.findViewById(R.id.txtbxPrecio);

                    producto = txtbxProducto.getText().toString();

                    //nos aseguramos de que los valores no sean nullo o en vacio
                    if(producto.equals("") || producto.equals(null)){
                        //no hacer nada
                        crearToast("Sin producto");
                    }else
                    {
                        //agregar valores a log_gastos
                        crearToast(producto+"-"+txtbxPrecio.getText().toString());

                        //mandar a llamar función que agregará a log gastos
                        if(logGastos != null){
                            agregarALogGastos(producto,txtbxPrecio.getText().toString());
                        }
                    }
                }
            });

            return rootView;
        }
    }

    public static class LogGastos extends Fragment {

        public LogGastos() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_log_gastos, container, false);

            logGastos = (TableLayout)rootView.findViewById(R.id.tblLogGastos);
            trLogGastos = (TableRow)rootView.findViewById(R.id.trBase);

            return rootView;
        }
    }

    public static void agregarALogGastos(String producto , String precio) {

        //TableRow tableRow = (TableRow)logGastos.findViewById(R.id.trBase);
        TableRow tableRow = new TableRow(getsContext());

        /*
        EditText tdConcepto = new EditText(getsContext());
        EditText tdPrecio = new EditText(getsContext());

        tdConcepto.setText(producto);
        tdPrecio.setText(precio);

        trLogGastos.addView(tdConcepto);
        trLogGastos.addView(tdPrecio);

        logGastos.addView(trLogGastos);
        */



        EditText tdconcepto = new EditText(getsContext());
        EditText tdprecio = new EditText(getsContext());

        tdconcepto.setText(producto);
        tdprecio.setText(precio);

        //Intercalamos el color del fondo de la tabla
        if (cambioColorFondoTabla) {
            tdprecio.setBackgroundColor(Color.GRAY);
            tdconcepto.setBackgroundColor(Color.GRAY);
            cambioColorFondoTabla = false;
        } else {
            tdprecio.setBackgroundColor(Color.WHITE);
            tdconcepto.setBackgroundColor(Color.WHITE);
            cambioColorFondoTabla = true;
        }

        tableRow.addView(tdconcepto);
        tableRow.addView(tdprecio);

        logGastos.addView(tableRow);

    }
}
