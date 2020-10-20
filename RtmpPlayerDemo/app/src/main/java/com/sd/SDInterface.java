/**
 * 客户端底层接口
 */
package com.sd;
import com.sd.Constant;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SDInterface {

	static {
		try {
			System.loadLibrary("TerminalSdk");
		} catch (Exception e) {
			Log.e("SDMedia", "Can not load library libTerminalSdk.so");
			e.printStackTrace();
		}
	}

	private static final String TAG = "SDMedia";
	private Handler eventHandler = null;

	public SDInterface(Handler eventHandler) {
		super();
		this.eventHandler = eventHandler;
	}


	//***************************JNI方式主动调用的接口************************************//

	//初始化资源，整个系统中只需调用一次即可，成功返回0，失败返回负数
	public native int SDsysinit(String strLogFileDir, byte byLogFileLevel);

	//资源的释放
	public native void SDsysexit();

	//连接服务器接口，成功返回0，失败返回负数
	public native int SDConnectRtmp(String strRtmpUrl, int nJitterBuffDelay);

	//断开连接接口
	public native void SDDisconnectRtmp();
	

	//***************************JNI方式被动接口************************************//
	// 【注意事项】
	//	1、通知型回调函数中应尽快的退出，不进行耗时操作，不调用主动API接口。
	
	//被动接口：来自底层的状态变更反馈（比如启动重连、重连成功、账号被顶下去等）
	public void onSysStatus(int nUid, int nType) {

		Log.i(TAG, "onSysStatus() with type=" + nType);
		if (eventHandler != null) {
			Message statusMsg = eventHandler.obtainMessage();
			statusMsg.what = nType;
			statusMsg.arg1 = nUid;
			eventHandler.sendMessage(statusMsg);
		}
	}

}
