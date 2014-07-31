package com.young.player;

import com.young.mplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerActivity extends Activity{

	protected static final String LOG_TAG = "MusicMan";
	private MusicHelper mMusicHelper;
    private ImageButton preBtn;
    private ImageButton playBtn;
    private ImageButton nextBtn;
    private TextView musicInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mMusicHelper = new MusicHelper(PlayerActivity.this);
    	setContentView(R.layout.activity_main);
    		musicInfo = (TextView) findViewById(R.id.music_info);
    		musicInfo.setText(mMusicHelper.getMusicName());
	
    		preBtn = (ImageButton) findViewById(R.id.previousBtn);
    		playBtn = (ImageButton) findViewById(R.id.playBtn);
    		if (mMusicHelper.isPlaying()) {
    			playBtn.setImageDrawable(getResources().getDrawable(R.drawable.stop));
    		} else {
    			playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play));
    		}
				nextBtn = (ImageButton) findViewById(R.id.nextBtn);
				preBtn.setOnClickListener(musicListener);
				playBtn.setOnClickListener(musicListener);
				nextBtn.setOnClickListener(musicListener);
}

    private OnClickListener musicListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.previousBtn :
			mMusicHelper.prevMusic();
			musicInfo.setText(mMusicHelper.getMusicName());
			Log.d(LOG_TAG, "PREVIOUS BUTTON PRESSED...");
			break;
		case R.id.playBtn :
			if (mMusicHelper.isPlaying()) {
				playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play));
				mMusicHelper.pause();
			} else {
				playBtn.setImageDrawable(getResources().getDrawable(R.drawable.stop));
				mMusicHelper.playMusic();
			}
			Log.d(LOG_TAG, "play button pressed...");
			break;
		case R.id.nextBtn :
			mMusicHelper.nextMusic();
			musicInfo.setText(mMusicHelper.getMusicName());
			Log.d(LOG_TAG, "next button pressed...");
		default :
			break;
		}
		
	}

};

}
