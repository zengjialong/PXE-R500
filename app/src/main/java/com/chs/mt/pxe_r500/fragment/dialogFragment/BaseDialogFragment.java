package com.chs.mt.pxe_r500.fragment.dialogFragment;

import android.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
//        if(MacCfg.BOOL_DialogHideBG){
//            Window window = getDialog().getWindow();
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.dimAmount = 0.0f;
//            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            window.setAttributes(windowParams);
//        }

//        Window window = getDialog().getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(Color.RED));
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
//        window.setAttributes(windowParams);
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.72), LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
    }
}
