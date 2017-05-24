package easyfilepickerdialog.kingfisher.com.library.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import easyfilepickerdialog.kingfisher.com.library.R;


/**
 * Created by kingfisher_goong on 12/26/16.
 */

public abstract class BaseNoFrameDialogFragment extends DialogFragment {

    abstract void setupDialogPosition();

    /**
     * Background của cái dialog này có transparent hay ko?
     *
     * @return
     */
    protected boolean isBackgroundTransparent() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // show dialog without border
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (isBackgroundTransparent()) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.bg_dim)));
        }
        setupDialogPosition();
    }


}
