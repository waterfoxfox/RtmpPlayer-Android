# Low delay android rtmp player with simple api 


## RtmpPlaySdk Android版简介
一款低延时的极简接口RTMP播放器（Windows版和Android版）。市面上的RTMP播放器较多，有开源的ijkplayer及其衍生品，也有收费的功能繁多的播放器，适合自己的才是最好的，本播放器主要关注低延时、可靠性、易用性，其中Android版播放器的特性如下：

* 1、支持Rtmp掉线自动重连。
* 2、支持非阻塞Rtmp连接，外层可随时中断。
* 3、支持任意AAC采样率、声道数，内部自动resample。
* 4、支持H264+AAC组合Rtmp流。
* 5、支持渲染时保证画面宽高比而自适应加黑边。
* 6、支持外层可设置的Jitter Buff延时，设置为0时为极速模式，配合低延时推送端最小延时仅500ms。
* 7、仅六个接口，调用简洁，用户只需传入播放器窗口surface即可。
* 8、整个系统仅由一个so组成，占用空间小，性能强劲。

#### Windows版本：https://github.com/waterfoxfox/RtmpPlayer

## RtmpPlaySdk  JAVA API

### 
* 环境初始化，系统只需调用一次<br>
@param: outputPath：日志文件输出的目录，若目录不存在将自动创建<br>
@param: outputLevel：日志输出的级别，只有等于或者高于该级别的日志输出到文件<br>
@return: 成功返回0，失败返回负数<br>
int  `SDsysinit`(String outputPath,  byte outputLevel);

### 
* 环境反初始化，系统只需调用一次<br>
void  `SDsysexit`();

### 
* 初始化播放器<br>
@param: act：播放器所在的Activity<br>
@param: surfaceView：播放器渲染窗口<br>
@param: playAudioOnly：是否仅播放音频<br>
@param: playVideoOnly：是否仅播放视频<br>
void  `SDplayinit`(Activity act, SurfaceViewRenderer surfaceView, boolean playAudioOnly, boolean playVideoOnly);

### 
* 反初始化播放器<br>
void  `SDplayexit`();

### 
* 建立与远端媒体服务器之间的RTMP连接<br>
@param strRtmpUrl: 待播放的RTMP地址<br>
@param nJitterBuffDelay: 播放器内部JitterBuff缓存时间，单位毫秒<br>
@return: 成功返回0，失败返回负数<br>
int  `SDConnectRtmp`(String strRtmpUrl, int nJitterBuffDelay);

### 
* 断开与远端媒体服务器之间的RTMP连接<br>
void  `SDDisconnectRtmp`();


### 
* 开始播放<br>
@param renderWidth: 播放器渲染窗口宽度<br>
@param renderHeight: 播放器渲染窗口高度<br>
void  `SDStartPlay`(int renderWidth, int renderHeight);

### 
* 停止播放<br>
void  `SDStopPlay`();

###
调用顺序：<br>
SDsysinit<br>
SDplayinit<br>
SDConnectRtmp<br>
SDStartPlay<br>
<br>
SDStopPlay<br>
SDDisconnectRtmp<br>
SDplayexit<br>
SDsysexit<br>


### 跟多文档、代码资源见：https://mediapro.apifox.cn
### 本库加入了使用限制，仅做演示用途，若需要定制服务与技术支持请联系 www.mediapro.cc
