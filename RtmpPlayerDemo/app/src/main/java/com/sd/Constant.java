package com.sd;
//和C层一致的常量定义
public class Constant {

	
	// 底层状况通知
	public interface SystemStatusType {	
		final byte SYS_NOTIFY_CONNECT_START = 0;		// 初次开始连接
		final byte SYS_NOTIFY_CONNECT_SUCCESS = 1;		// 连接成功
		final byte SYS_NOTIFY_RECONNECT_START = 2;		// 底层开始重新连接
	}	
	
	// 输出到日志文件的级别
	public interface LogLevel {
		final byte LOG_LEVEL_DEBUG = 1;
		final byte LOG_LEVEL_INFO = 2;
		final byte LOG_LEVEL_WARN = 3;
		final byte LOG_LEVEL_ERROR = 4;
		final byte LOG_LEVEL_ALARM = 5;
		final byte LOG_LEVEL_FATAL = 6;
		final byte LOG_LEVEL_NONE = 7;
	}
}
