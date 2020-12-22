package com.mediapro.demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.webrtc.SurfaceViewRenderer;

import com.sd.Constant;
import com.sd.Constant.LogLevel;
import com.sd.SDInterfacePlayer;
import com.sd.SDInterface;

import static com.sd.Constant.SystemStatusType.SYS_NOTIFY_CONNECT_START;
import static com.sd.Constant.SystemStatusType.SYS_NOTIFY_CONNECT_SUCCESS;
import static com.sd.Constant.SystemStatusType.SYS_NOTIFY_RECONNECT_START;



public class PlayerActivity extends Activity {
    private RelativeLayout mLayout = null;

    //播放窗口
    private SurfaceViewRenderer mSurfaceView = null;
    
    //播放API
    private SDInterfacePlayer mPlayer = null;
    
    //基础API
    private SDInterface mInterface = null;

    //待播放的RTMP URL
    String mRtmpUrl = null;

    //播放缓存大小，单位ms
    int mJitterBuff = 200;
    
    //是否启动播放
    boolean mbPlayStart = false;

    //日志文件存放路径
    private String mLogfileDir = "/sdcard/mediapro/";



    //来自底层消息的处理
    private final Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case SYS_NOTIFY_RECONNECT_START:
                    Toast.makeText(PlayerActivity.this, "网络超时，开始重连服务器...", Toast.LENGTH_SHORT).show();
                    break;
                case SYS_NOTIFY_CONNECT_SUCCESS:
                    Toast.makeText(PlayerActivity.this, "连服务器成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private static final String TAG = "SDMedia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLayout = (RelativeLayout) getLayoutInflater().inflate(
                R.layout.activity_player, null);
        setContentView(mLayout);

        //播放窗口
        mSurfaceView = (SurfaceViewRenderer) findViewById(R.id.suface_view);

        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);

        //初始化基础API、播放API
        initAvResource();

        //连接服务器
        int nRet = startConnect();
        if (nRet == 0)
        {
            //开始播放渲染
            //获得渲染窗口宽高比，以便对底层渲染进行宽高比指导。
            //注意：底层只关心宽高比值，而非精确宽高像素值
            mSurfaceView.post(new Runnable()
                              {
                                  @Override
                                  public void run() {
                                        int rendHeight = mSurfaceView.getMeasuredHeight();
                                        int rendWidth = mSurfaceView.getMeasuredWidth();
                                        Log.i(TAG, "get render width:" + rendWidth + "  render height:" + rendHeight);
                                        
                                        //开始播放
                                        startPlay(rendWidth, rendHeight);
                                  }
                              }
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        //停止播放
        stopPlay();
        
        //下线
        stopConnect();
        
        //资源回收
        uninitAvResource();
    }

    /**
     * the button click event listener
     *
     * @param btn
     */
    public void OnBtnClicked(View btn) {
        if (btn.getId() == R.id.btn_close) {
            stopPlay();
            finish();
        }
    }



    //初始化基础API、播放API
    private void initAvResource()
    {
        mInterface = new SDInterface(mHandler);
        mPlayer = new SDInterfacePlayer();    
    
        // 初始化系统，指定服务器IP地址、本地客户端输出日志文件级别和存放路径
        int ret = mInterface.SDsysinit(mLogfileDir, LogLevel.LOG_LEVEL_INFO);
        if(0 != ret)
        {
            Toast.makeText(this, "初始化资源返回错误编码:" + ret, Toast.LENGTH_LONG).show();
            Log.e(TAG, "SDsysinit failed return:" + ret);
        }
		
		mPlayer.SDplayinit(this, mSurfaceView, false, false);
    }
    
    //反初始化基础API、播放API
    private void uninitAvResource()
    {
        // 相关资源回收
        mPlayer.SDplayexit();
        mInterface.SDsysexit();
    }
    
    
    //连接媒体服务器
    private int startConnect()
    {
        //rtmp url
        mRtmpUrl = getIntent().getExtras().getString("rtmp_url");
        //jitter buff ms
        mJitterBuff = getIntent().getExtras().getInt("jitter_buff");
        
        int ret = mInterface.SDConnectRtmp(mRtmpUrl, mJitterBuff);
        if (ret != 0)
        {
            Log.e(TAG, "startConnect failed");
            return ret;
        }

        return 0;
    }

    //下线媒体服务器
    private void stopConnect()
    {
        mInterface.SDDisconnectRtmp();
    }
    
    //开始播放
    private void startPlay(int rendWidth, int rendHeight)
    {
        mPlayer.SDStartPlay(rendWidth, rendHeight);
        mPlayer.SDSetVolume((float)2.0);
        mbPlayStart = true;
    }
    
    //停止播放
    private void stopPlay()
    {
        if (mbPlayStart == true)
        {
            mPlayer.SDStopPlay();
            mbPlayStart = false;
        }
    }
}
