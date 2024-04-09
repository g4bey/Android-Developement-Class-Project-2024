package gabey.space.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/*
    This class was generated w/ chatGPT as this "higher-level" boilerplate.
    I wanted to be transparent even though it's the only class made w/ AI.

    Prompt:
        Context: Android development.
        Goal: Write a helper class with a static method displaying an dialog
              Make sure the method takes a string as well as a callback.
 */
public class ErrorDialogHelper {
    public interface ErrorDialogCallback {
        void onDismiss();
    }

    public static void showErrorDialog(Context context, String message, final ErrorDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("A critical error has occured.")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onDismiss();
                        }
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
