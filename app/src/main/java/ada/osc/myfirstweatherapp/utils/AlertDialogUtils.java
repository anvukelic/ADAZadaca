package ada.osc.myfirstweatherapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ada.osc.myfirstweatherapp.view.locations.LocationsContract;

/**
 * Created by avukelic on 24-May-18.
 */
public class AlertDialogUtils {

    public interface OnAlertDialogButtonClickListener {
        void onAlertDialogButtonClick(String location);
    }


    public static void askForDeleteAlertDialog(Context context, final String location, final OnAlertDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete location");
        builder.setMessage("Do you want to delete location " + location);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onAlertDialogButtonClick(location);
                }
                dialog.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
