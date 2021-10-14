package com.pixeldreamland.tonguetwister;

import android.os.AsyncTask;
import java.net.MalformedURLException;
import java.net.URL;

class WebServerDataGetter extends AsyncTask<Void, Void, TongueTwister> {
    private final OnTongueTwisterReceivedListener listener;
    private final int idNumber;

    WebServerDataGetter(OnTongueTwisterReceivedListener listener, int idNumber) {
        this.idNumber = idNumber;
        this.listener = listener;
    }

    private TongueTwister getTongueTwisterAndAddItToTheList() {
        TongueTwister tongueTwister = null;
        URL url;

        try {
            url = new URL(Connect.WEB_SERVER_URL + "/rest/tongueTwister/" + idNumber);
            StringBuilder stringBuilder = Connect.getDataFromWebServer(url);
            tongueTwister = new TongueTwister();
            tongueTwister.setContent(stringBuilder.toString());
            tongueTwister.setId(idNumber);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return tongueTwister;
    }

    @Override
    protected TongueTwister doInBackground(Void... params) {
        return getTongueTwisterAndAddItToTheList();
    }

    @Override
    protected void onPostExecute(TongueTwister tongueTwister) {
        super.onPostExecute(tongueTwister);
        listener.onTongueTwisterReceived(tongueTwister);
    }

    interface OnTongueTwisterReceivedListener {
        void onTongueTwisterReceived(TongueTwister tongueTwister);
    }
}
