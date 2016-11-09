package com.nodry.nodry.Presentacion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.nodry.nodry.Datos.Gasolinera;
import com.nodry.nodry.Negocio.GestionGasolineras;
import com.nodry.nodry.Negocio.IGestionGasolineras;

import java.util.List;

/**
 * Clase para la obtencion de datos de forma asincrona
 * en la aplicacion. Es necesaria ya que el hilo principal
 * no puede soportar cargas de mas de X segundos.
 * @author Alba Zubizarreta.
 * @version 1.0
 */
public class GetGasolinerasTask extends AsyncTask<Void, List<Gasolinera>, List<Gasolinera>> implements INotificable {

    // Contexto de la actividad origen
    private Context context;

    // String con la comunidad de las gasolineras a obtener
    private String CCAA;

    // Adapter que se encargara de presentar los datos una vez obtenidos
    private ArrayAdapter adapter;

    // Manejador para la supervision de tiempos
    private Handler handler;

    // Tarea que se encarga de que no se sobrepase el tiempo de peticion especificado
    private CancelerTask cancelerTask;

    // Mensajes de error
    private static final String MSG_NO_CONEXION = "No hay conexion a internet";
    private static final String MSG_NO_DATA = "No hay datos que mostrar,\nintentelo mas tarde de nuevo";

    // Tiempo de peticion especificado de un minuto
    private static final long RESPONSE_DELAY = 60000;

    // Tiempo para que se vea el dialogo de carga
    private static final long TEST_DELAY = 1000;

    private IGestionGasolineras gestionGasolineras;

    /**
     * Constructor
     * @param adapter con el adaptador que visualizara los datos
     * @param context con la actividad origen de la llamada
     */
    public GetGasolinerasTask (ArrayAdapter adapter, String CCAA, Context context){
        this.adapter = adapter;
        this.CCAA = CCAA;
        this.context = context;
        this.handler = new Handler();
        this.cancelerTask = new CancelerTask(this, this.context);

        // Si la conexion no funciona mostramos el mensaje y acabamos
        //if(!isNetworkAvailable(context)){
        //    showMessage(ERROR_MSG ,MSG_NO_CONEXION);
        //    ((ILoadable)context).conexionIncorrecta("No se han encontrado datos");
        //}
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((ILoadable)context).startLoading();
        handler.postDelayed(cancelerTask, RESPONSE_DELAY);
    }

    @Override
    protected List<Gasolinera> doInBackground(Void... param) {
        // Para forzar a ver si funciona el dialogo de carga
        try {
            Thread.sleep(TEST_DELAY);
        } catch (InterruptedException e) {
            Log.d("Error", e.toString());
        }

        gestionGasolineras = new GestionGasolineras();

        return gestionGasolineras.getGasolineras(CCAA, false);
    }

    @Override
    protected void onPostExecute(List<Gasolinera> listaGasolineras) {

        handler.removeCallbacks(cancelerTask);

        if(listaGasolineras == null || listaGasolineras.isEmpty()){
            showMessage(ERROR_MSG ,MSG_NO_DATA);
            ((ILoadable)context).conexionIncorrecta("No se han encontrado datos");
        }else {
            ((IUpdateable) adapter).update(listaGasolineras);
        }

        ((ILoadable) context).stopLoading();
    }

    @Override
    public void showMessage(String title, String msg) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}// GetGasolineras