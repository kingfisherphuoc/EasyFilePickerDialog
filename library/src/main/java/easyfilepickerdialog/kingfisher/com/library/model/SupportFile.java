package easyfilepickerdialog.kingfisher.com.library.model;

import android.support.annotation.NonNull;

/**
 * Created by kingfisher_goong on 5/24/17.
 */

public class SupportFile {
    final private String ext;
    final private int resId;

    public SupportFile(@NonNull String ext, int resId) {
        this.ext = ext;
        this.resId = resId;
    }

    public String getExt() {
        return ext;
    }

    public int getResId() {
        return resId;
    }
}
