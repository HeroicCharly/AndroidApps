package carlosperez.okaneappv2;

import android.app.Activity;
import android.app.ListFragment;

/**
 * Created by Carlos Pérez on 27/04/2015.
 */
public class Gasto extends ListFragment {
    OnRegistrarGasto mCallBack;

    public interface OnRegistrarGasto{
        public void onGuardarGasto(String concepto, double precio);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mCallBack = (OnRegistrarGasto) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                + "debe implementar OnRegistrarGasto");
        }
    }
}
