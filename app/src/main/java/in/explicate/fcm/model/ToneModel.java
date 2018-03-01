package in.explicate.fcm.model;

import android.net.Uri;

/**
 * Created by Mahesh on 09/12/17.
 */

public class ToneModel {

    private Uri url;

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

}
