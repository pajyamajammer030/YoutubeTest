package com.example.youtubetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_list);

        PlaylistItemReceiver receiver = new PlaylistItemReceiver();
        receiver.execute();
    }

    private class ListItemClickListner implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String radioId = item.get("id");
            String radioTitle = item.get("title");
            Intent intent = new Intent(RadioListActivity.this, RadioPlayerActivity.class);
            intent.putExtra("radioId", radioId);
            intent.putExtra("radioTitle", radioTitle);
            startActivity(intent);
        }
    }

    private class PlaylistItemReceiver extends AsyncTask<String, String, String> {
        List<Map<String,String>> radioList = null;
        public PlaylistItemReceiver() {

        }

        @Override
        protected String doInBackground(String... string) {
            String APIKEY = "AIzaSyDEfFT1M0tKb8dgyDyT54KRMWG5Lb_Xh0Q";
            String PLAYLISTID = "PLDCx5WcWNkqpmyT4EGdKe4xTZxiB9m_oU";

            //ニューラジオ再生リスト
            //PLDCx5WcWNkqpmyT4EGdKe4xTZxiB9m_oU
            String urlStr = "https://www.googleapis.com/youtube/v3/playlistItems?part=id,snippet,contentDetails&playlistId=";
            urlStr += PLAYLISTID;
            urlStr += "&maxResults=5&key=";
            urlStr += APIKEY;

            String result = "";
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = is2String(is);

            } catch(MalformedURLException ex) {

            } catch(IOException ex) {

            } finally {
                if (con != null) {
                    con.disconnect();
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch(IOException ex) {

                    }
                }
            }
            return result;
        }

        @Override
        public void onPostExecute(String result) {
            ListView lvRadio = findViewById(R.id.lvRadio);
            List<Map<String,String>> radioList = createRadioList(result);

            String[] from = {"id", "title"};
            int[] to = {android.R.id.text1, android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(RadioListActivity.this, radioList, android.R.layout.simple_list_item_2, from, to);
            lvRadio.setAdapter(adapter);

            lvRadio.setOnItemClickListener(new ListItemClickListner());
        }

        private List<Map<String, String>> createRadioList(String result) {
            String[][] playlist;
            playlist = getPlaylistItem(result);
            List<Map<String,String>> radioList = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                addRadioList(radioList, playlist[i][0], playlist[i][1]);
            }

            return radioList;
        }

        private void addRadioList(List<Map<String,String>> radioList, String id, String title) {
            Map<String, String> radio = new HashMap<>();
            radio.put("id", id);
            radio.put("title", title);
            radioList.add(radio);
        }

        public String[][] getPlaylistItem(String result) {
            String playlistItems[][] = new String[5][2];
            for(int itemNum = 0; itemNum < 5; itemNum++) {
                editResult(result, itemNum, playlistItems);
            }

            return playlistItems;
        }

        public void editResult(String result, int itemNum, String[][] playlistItems) {
            String videoId = "";
            String title = "";

            try {
                JSONObject rootJSON = new JSONObject(result);
                JSONArray items = rootJSON.getJSONArray("items");
                JSONObject itemsNow = items.getJSONObject(itemNum);

                JSONObject snippetJSON = itemsNow.getJSONObject("snippet");

                title = snippetJSON.getString("title");

                JSONObject contentDetailsJSON = itemsNow.getJSONObject("contentDetails");

                videoId = contentDetailsJSON.getString("videoId");


            } catch (JSONException ex) {

            }

            playlistItems[itemNum][0] = videoId;
            playlistItems[itemNum][1] = title;
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

}