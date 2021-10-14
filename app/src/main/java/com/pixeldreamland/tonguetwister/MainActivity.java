package com.pixeldreamland.tonguetwister;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView text;
    private TextToSpeech tts;
    private String textToString;
    private boolean pause;


    public MainActivity() {
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (tts.isSpeaking()) {
            tts.stop();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tts.isSpeaking()) {
            tts.stop();

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            // do something when the button is pressed

            public void onClick(DialogInterface arg0, int arg1) {

                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is pressed
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).show();

        if (tts.isSpeaking()) {
            tts.stop();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            TongueTwisterDataManager tongueTwisterDataManager = TongueTwisterDataManager.getInstance();
            tongueTwisterDataManager.destroy();
        }
        tts.shutdown();
    }


    @Override
    protected void onResume() {
        super.onResume();
        TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
        if (!(tongueTwisterDataManagerHandle.getTongueTwisterList().isEmpty())) {
            text.setText(tongueTwisterDataManagerHandle.getTongueTwisterList().get(tongueTwisterDataManagerHandle.getCurrentIndex()).getContent());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);

        if (!Locale.getDefault().getLanguage().equals("en")) {
            tts.setLanguage(Locale.ENGLISH);
        }

        text = findViewById(R.id.textView1);
        final TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
        if (tongueTwisterDataManagerHandle.getTongueTwisterList().isEmpty()) {
            new MyTask(this).execute();
        }


        final ImageButton prev = findViewById(R.id.prev123);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause = false;
                if (tongueTwisterDataManagerHandle.getCurrentIndex() > 0) {
                    if (tts.isSpeaking()) {
                        tts.stop();
                    }
                    text.setText(tongueTwisterDataManagerHandle.getTongueTwisterList().get(tongueTwisterDataManagerHandle.getCurrentIndex() - 1).getContent());
                    tongueTwisterDataManagerHandle.decrementCurrentIndex();
                } // Code here executes on main thread after user presses button
            }
        });

        final ImageButton next = findViewById(R.id.imageNextButton);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause = false;
                if (tongueTwisterDataManagerHandle.isGetMoreTongueTwistersIsFinished() && ((tongueTwisterDataManagerHandle.getTongueTwisterList().size()) - (tongueTwisterDataManagerHandle.getCurrentIndex() + 1)) <= 8) {
                    tongueTwisterDataManagerHandle.setGetMoreTongueTwistersIsFinished(false);
                    getMoreTongueTwisters();
                }

                if (tongueTwisterDataManagerHandle.getCurrentIndex() < tongueTwisterDataManagerHandle.getTongueTwisterList().size() - 1) {
                    if (tts.isSpeaking()) {
                        tts.stop();
                    }

                    text.setText(tongueTwisterDataManagerHandle.getTongueTwisterList().get(tongueTwisterDataManagerHandle.getCurrentIndex() + 1).getContent());
                    if (tongueTwisterDataManagerHandle.getCurrentIndex() == 10) {
                        tongueTwisterDataManagerHandle.getTongueTwisterList().remove(0);
                    } else tongueTwisterDataManagerHandle.incrementCurrentIndex();
                }
                // Code here executes on main thread after user presses button
            }
        });


        final ImageButton textToSpeech = findViewById(R.id.textToSpeechButton);
        pause = false;

        // set tts progress listener here. no need to reset it on every click
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                        pause = false;
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onStart(String utteranceId) {
            }
        });

        textToSpeech.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pause) {
                    pause = false;
                    textToSpeech.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                    if (tts.isSpeaking()) {
                        tts.stop();
                    }
                } else {
                    textToString = text.getText().toString();

                    HashMap<String, String> map = new HashMap<>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(textToString, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(map));
                    } else {
                        tts.speak(textToString, TextToSpeech.QUEUE_FLUSH, map);
                    }

                    pause = true;
                    textToSpeech.setImageResource(R.drawable.ic_pause_black_36dp);
                }
            }
        });
    }

    @Override
    public void onInit(int status) {

    }

    public int getRandomTongueTwisterId() {
        int random = 1;
        TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
        if (tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size() > 1) {
            random = 1 + (int) (Math.random() * ((tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size() - 1) + 1));
        }
        int randomId = tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().get(random - 1);

        if (tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size() > 0) {
            tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().remove(random - 1);
        }

        return randomId;
    }

    public void getMoreTongueTwisters() {
        final TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
        int numberOfTongueTwistersInDatabase = tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInDatabase();
        if (tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size() == 0) {
            while (numberOfTongueTwistersInDatabase > 0) {
                tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().add(numberOfTongueTwistersInDatabase);
                numberOfTongueTwistersInDatabase--;
            }

            for (int i = 0; i < tongueTwisterDataManagerHandle.getTongueTwisterList().size(); i++) {
                tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().remove(tongueTwisterDataManagerHandle.getTongueTwisterList().get(i).getId());

            }
        }

        final int numberOfUniqueTongueTwistersToGet;
        if (tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size() >= 15) {
            numberOfUniqueTongueTwistersToGet = 15;
        } else {
            numberOfUniqueTongueTwistersToGet = tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().size();
        }

        for (int i = 0; i < numberOfUniqueTongueTwistersToGet; i++) {
            final int finalI = i;
            WebServerDataGetter webServerDataGetter1 = new WebServerDataGetter(new WebServerDataGetter.OnTongueTwisterReceivedListener() {
                @Override
                public void onTongueTwisterReceived(TongueTwister tongueTwister) {
                    tongueTwisterDataManagerHandle.getTongueTwisterList().add(tongueTwister);
                    if (tongueTwisterDataManagerHandle.getTongueTwisterList().size() == 1) {
                        text.setText(tongueTwister.getContent());
                    }
                    if ((numberOfUniqueTongueTwistersToGet - finalI) == 1) {
                        tongueTwisterDataManagerHandle.setGetMoreTongueTwistersIsFinished(true);
                    }

                }
            }, getRandomTongueTwisterId());

            webServerDataGetter1.execute();
        }
    }

    private static class MyTask extends AsyncTask<Void, Void, Integer> {

        private final WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        MyTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int tongueTwisterSize = 0;
            StringBuilder stringBuilder;
            URL url;
            if (!isCancelled())
                try {
                    url = new URL(Connect.WEB_SERVER_URL + "/rest/tongueTwister/countOfAllIds");
                    stringBuilder = Connect.getDataFromWebServer(url);
                    tongueTwisterSize = Integer.valueOf(stringBuilder.toString().trim());
                    final TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
                    tongueTwisterDataManagerHandle.setNumberOfTongueTwistersInDatabase(tongueTwisterSize);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                }

            return tongueTwisterSize;
        }


        @Override
        protected void onPostExecute(Integer tongueTwisterSize) {
            super.onPostExecute(tongueTwisterSize);
            TongueTwisterDataManager tongueTwisterDataManagerHandle = TongueTwisterDataManager.getInstance();
            int curr = tongueTwisterSize;
            while (curr > 0) {
                tongueTwisterDataManagerHandle.getNumberOfTongueTwistersInTheDatabase().add(curr);
                curr--;
            }
            activityReference.get().getMoreTongueTwisters();
        }
    }
}

