package com.app.test.rantesttikal.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Ran on 12/6/2017.
 */

public class NetManager {

    Context applicationContext;

    public NetManager(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager conManager = (ConnectivityManager)
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conManager.getActiveNetworkInfo() != null &&
                conManager.getActiveNetworkInfo().isConnected()?
                true : false;
    }
}
