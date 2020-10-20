package com.mediapro.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private EditText mEditRtmpUrl;
    private EditText mEditJitterBuffDelay;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //* Init UI
        mEditRtmpUrl = (EditText) findViewById(R.id.id_rtmp_url);
        mEditJitterBuffDelay = (EditText)findViewById(R.id.id_jitter_buff);

        sharedPreferences = getSharedPreferences("UserInfo", 0);
        String rtmpUrl = sharedPreferences.getString("rtmpUrl", "");
        int jittBuff = sharedPreferences.getInt("jittBuff", 500);
        if (rtmpUrl.length() != 0)
        {
            mEditRtmpUrl.setText(rtmpUrl);
            mEditJitterBuffDelay.setText(Integer.toString(jittBuff));
        }
    }

    public void OnBtnClicked(View view) {
        //RTMP URL
        String strRtmpUrl = mEditRtmpUrl.getEditableText().toString();
        if (IsRtmpUrl(strRtmpUrl) == false)
        {
            Toast.makeText(MainActivity.this, "RTMP地址非法", Toast.LENGTH_SHORT).show();
            return;
        }
        //缓存时间
        String strJitterDelay = mEditJitterBuffDelay.getEditableText().toString().trim();
        if(strJitterDelay.length() == 0) {
            Toast.makeText(this, "请输入缓存时间", Toast.LENGTH_SHORT).show();
            return;
        }

        int nJitterDelay = 200;
        try {
            nJitterDelay = Integer.parseInt(strJitterDelay);
        } catch (Exception e) {
            Toast.makeText(this, "请输入合法的缓存时间", Toast.LENGTH_SHORT).show();
            return;
        }

        sharedPreferences = getSharedPreferences("UserInfo", 0);
        sharedPreferences.edit()
                .putString("rtmpUrl", strRtmpUrl)
                .putInt("jittBuff", nJitterDelay)
                .commit();

        switch(view.getId()){
            case R.id.btn_watch_live:
                Intent it = new Intent(this, PlayerActivity.class);
                Bundle bd = new Bundle();
                bd.putString("rtmp_url", strRtmpUrl);
                bd.putInt("jitter_buff", nJitterDelay);
                it.putExtras(bd);
                startActivity(it);
                break;
        }

    }

    public static boolean IsRtmpUrl(String rtmp_url)
    {
        if(rtmp_url==null || rtmp_url.length()==0){
            return false;//字符串为空或者空串
        }
        
        if(rtmp_url.startsWith("rtmp://") == false)
        {
            return false;
        }
        return true;
    }
}
