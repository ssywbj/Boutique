package ying.jie.util;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import ying.jie.boutique.App;

/**
 * 声音播放类
 */
public class VoicePlayer {
    /**
     * @param voiceFileName 位于assets目录下的声音文件
     */
    public static void playVoice(String voiceFileName) {
        try {
            AssetFileDescriptor fd = App.getContext().getAssets().openFd(voiceFileName);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fd.getFileDescriptor(),
                    fd.getStartOffset(), fd.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.stop();
                    mp.release();
                    return true;
                }
            });
        } catch (Exception e) {
            LogUtil.globalLogError(e.toString());
        }
    }

}
