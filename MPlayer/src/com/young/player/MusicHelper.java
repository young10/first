package com.young.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class MusicHelper {

	public static final String LOG_TAG = "MusicHelper";
	private Context mContext;
	
	/* ���������� */
	public MediaPlayer mMediaPlayer;
	
	/* �����б� */
	public List<String> mMusicList;
	
	/* ��ǰ���ŵĸ������� */
	public int currentPlayingItem = 0;
	
	/* ��ǰ���ŵ�λ�� */
	public long currentPosition = 0;
	
	/* �Ƿ��Զ�������һ�� */
	public boolean isPlayingNext = false;
	
	/* �Ƿ���ͣ�� */
	public boolean isPauseed = false;
	
	public MusicHelper(Context context) {
		this.mContext = context;
		mMediaPlayer = new MediaPlayer();
		mMusicList = new ArrayList<String>();
		getAllMusicFiles(Constant.MEDIA_PATH);
	}
	
	/**
	 * ��ȡSD���е�����mp3����
	 * @param pathname the directory path
	 * @return List the array of all files
	 * */
	public List<String> getAllMusicFiles(String pathname) {
		if (pathname == null || pathname.length() < 1) {
			System.out.println("FileHelper.getAllMusicFiles, inavailable path when retrive a path");
			return null;
		}
		File mFile = new File(pathname);
		if (mFile.exists()) {
			if (mFile.isDirectory()) {
				File[] files = mFile.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							getAllMusicFiles(file.getPath()); // �ݹ�����
						} else {
							if (file.getAbsolutePath().endsWith(".mp3")) {
								mMusicList.add(file.getAbsolutePath());
							}
						}
					}
				} else {
					System.out.println("there are no any files under the path.");
				}
			} else {
				System.out.println("not a directory." );
			}
		} else {
			System.out.println("file not exists...");
		}
		return mMusicList;
	}
	
	/* ��ʼ�������� */
	public boolean playMusic() {
		Log.d(LOG_TAG, "playMusic()-->currentPlayingItem=" + currentPlayingItem);
		if (mMusicList == null || mMusicList.size() < 1) return false;
		try {
			if (!isPauseed) {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(mMusicList.get(currentPlayingItem));
				mMediaPlayer.prepare();
			}
			mMediaPlayer.start();
			
			//�Զ�������һ��
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					isPlayingNext = true;
					isPauseed = false;
					nextMusic();
					Intent intent = new Intent("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC");
					mContext.sendBroadcast(intent);//���͹㲥֪ͨ����UI����
				}
			});
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/* ��һ�� */
	public void prevMusic() {
		Log.d(LOG_TAG, "prevMusic()-->currentPlayingItem=" + currentPlayingItem);
		isPauseed = false;
		if (mMusicList == null || mMusicList.size() < 1) return;
		//�����ǰ����Ϊ��һ��,����һ��Ϊ���һ��
		if (currentPlayingItem-- <= 0) {
			currentPlayingItem = mMusicList.size() - 1;
		}
		//����ǲ���״̬,���Զ�������һ��
		if (mMediaPlayer.isPlaying()) {
			playMusic();
		} else if (isPlayingNext) {
			playMusic();
		} 
	}
	
	public void nextMusic() {
		Log.d(LOG_TAG, "nextMusic()-->currentPlayingItem=" + currentPlayingItem);
		isPauseed = false;
		if (mMusicList == null || mMusicList.size() < 1) return;
		if (currentPlayingItem++ >= mMusicList.size() - 1) {
			currentPlayingItem = 0;
		}
		if (mMediaPlayer.isPlaying()) {
			playMusic();
		} else if (isPlayingNext) {
			playMusic();
			isPlayingNext = false;
		} 
	}
	
	public boolean pause() {
		isPauseed = true;
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
		}else{
			mMediaPlayer.start();
		}
		return false;
	}
	
	public void stop() {
		mMediaPlayer.reset();
	}
	
	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
			
	}
	
	public boolean isPlaying() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}
	
	/* ��ȡ��ǰ���ŵ�λ��,�Ѳ��ŵĳ��� */
	public long getCurrentPosition() {
		if (mMediaPlayer != null) {
			Log.d(LOG_TAG, "currentPosition=" + mMediaPlayer.getCurrentPosition());
			currentPosition =  mMediaPlayer.getCurrentPosition();
			return currentPosition;
		}
		return -1;
	}
	
	/* ��λ������λ��,���ڿ������� */
	public void seekTo() {
		long position = getCurrentPosition();
		mMediaPlayer.seekTo((int) position);
	}
	
	public String getMusicName() {
		if (mMusicList == null || mMusicList.size() < 1) return "";
		String name = "";
		if (mMusicList != null) {
			name = mMusicList.get(currentPlayingItem);
			name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
		}
		return name;
	}

}
