package com.neu360.x_lab.laboratorio.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import com.neu360.x_lab.laboratorio.Controllers.LoginActivity;

public class MensajesDelSistema {

    public static void mensajeError(Context context, String strTitulo, String strMensaje){

        new AlertDialog.Builder(context)
                .setTitle(strTitulo)
                .setMessage(strMensaje)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


    }

    public static void mensajeErrorLooper(Context context, String strTitulo, String strMensaje){
        Looper.prepare();
        new AlertDialog.Builder(context)
                .setTitle(strTitulo)
                .setMessage(strMensaje)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

        Looper.loop();
    }

}
