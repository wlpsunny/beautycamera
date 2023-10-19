package com.eagleapps.beautycam.act;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.eagleapps.beautycam.R;

public class BaseAct extends AppCompatActivity {
    private AlertDialog mAlertDialog;

    public void onStop() {
        super.onStop();
        AlertDialog alertDialog = mAlertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void requestPermission(final String str, String str2, int i) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
            showAlertDialog(getString(R.string.permission_title_rationale), str2, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(BaseAct.this, new String[]{str}, i);
                }
            }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{str}, i);
    }

    public void showAlertDialog(String str, String str2, OnClickListener onClickListener, String str3, OnClickListener onClickListener2, String str4) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) str);
        builder.setMessage((CharSequence) str2);
        builder.setPositiveButton((CharSequence) str3, onClickListener);
        builder.setNegativeButton((CharSequence) str4, onClickListener2);
        mAlertDialog = builder.show();
    }
}
