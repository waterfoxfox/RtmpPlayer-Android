/**
 * 客户端底层接口（播放相关接口）
 */
package com.sd;
import android.content.Context;
import android.util.Log;

import android.app.Activity;
import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;


public class SDInterfacePlayer {
    /**
     * 加载api所需要的动态库
     */
    static {
        System.loadLibrary("TerminalSdk");
    }

    private EglBase eglBase;
    private static final String TAG = "SDMedia";

    //构造访问jni底层库的对象，请勿修改本变量名，jni层有使用
    private long fNativeAppId = 0;

    private VideoRenderer mRenderer = null;
    private boolean mPlayAudioOnly = false;
    private boolean mPlayVideoOnly = false;
    private boolean mInited = false;

    public SDInterfacePlayer() {

    }

    public void SDplayinit(final Activity act, final SurfaceViewRenderer surfaceView, final boolean playAudioOnly, final boolean playVideoOnly) {

        eglBase = EglBase.create();

        mPlayAudioOnly = playAudioOnly;
        mPlayVideoOnly = playVideoOnly;
        if (playAudioOnly == false) {
            surfaceView.init(eglBase.getEglBaseContext(), null);
            mRenderer = new VideoRenderer(surfaceView);
        }
        else {
            mRenderer = null;
        }

        nativeInitCtx(act.getApplicationContext());
        fNativeAppId = nativeCreate(playAudioOnly, playVideoOnly);
        mInited = true;
    }

    public void SDplayexit() {

        if (mInited)
        {
            nativeStopRtmpPlay();
            nativeDestroy();
            mInited = false;
        }
    }


    public void SDStartPlay(final int renderWidth, final int renderHeight) {
        if (mInited) 
		{
            if (mPlayAudioOnly == false) 
			{
                if (mPlayVideoOnly == false) 
				{
                    Log.i(TAG, "start play video audio with render w:" + renderWidth + " h:" + renderHeight);
                } 
				else 
				{
                    Log.i(TAG, "start play video only with render w:" + renderWidth + " h:" + renderHeight);
                }
                nativeStartRtmpPlay(mRenderer.GetRenderPointer(), renderWidth, renderHeight);
            }
            else 
			{
                Log.i(TAG, "start play audio only");
                nativeStartRtmpPlay(0, 0, 0);
            }
        }
        else 
		{
            Log.e(TAG, "start play failed should Init first.");
        }
    }

    public void SDStopPlay() {

        if (mInited)
        {
            Log.i(TAG, "stop play");
            nativeStopRtmpPlay();
        }
        else {
            Log.e(TAG, "StopPlay failed should Init first");
        }
    }

    public void SDSetVolume(float volume) {
        if (mInited)
        {
            Log.i(TAG, "SDSetVolume set volume to:" + volume);
            nativeSetVolume(volume);
        }
        else {
            Log.e(TAG, "SDSetVolume failed should Init first");
        }
    }
    
    /**
     * Jni interface
     */
    private static native void nativeInitCtx(Context ctx);
    private native long nativeCreate(boolean playAudioOnly, boolean playVideoOnly);
    private native void nativeStartRtmpPlay(final long renderPointer, final int renderWidth, final int renderHeight);
    private native void nativeStopRtmpPlay();
    private native void nativeDestroy();
    private native void nativeSetVolume(float volume);
}
