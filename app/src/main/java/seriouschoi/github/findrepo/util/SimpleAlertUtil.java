package seriouschoi.github.findrepo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleAlertUtil {
    public static void showAlert(Context con, String msg) {
        SimpleAlertUtil.showAlert(con, msg, null);
    }

    public static void showAlert(Context con, String msg, String okBtnText, final OnAlertButtonListener listener) {
        msg = msg == null ? "unknown alert." : msg;
        AlertDialog.Builder alert = new AlertDialog.Builder(con);
        alert.setPositiveButton(okBtnText == null ? "" : okBtnText.trim(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                if (listener != null) {
                    listener.onOkClick();
                }
            }
        });
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.show();
    }

    public static void showAlert(Context con, String msg, final OnAlertButtonListener listener) {
        msg = msg == null ? "unknown alert." : msg;
        AlertDialog.Builder alert = new AlertDialog.Builder(con);
        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                if (listener != null) {
                    listener.onOkClick();
                }
            }
        });
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.show();
    }

    public static void showConfirm(Context con, String msg, final OnConfirmButtonListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(con);
        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                if (listener != null) {
                    listener.onOkClick();
                }
            }
        });
        alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onCancelClick();
                }
            }
        });
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.show();
    }

    public static interface OnAlertButtonListener {
        public void onOkClick();
    }

    public static interface OnConfirmButtonListener {
        public void onOkClick();

        public void onCancelClick();
    }
}
