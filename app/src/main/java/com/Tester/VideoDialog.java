package com.Tester;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;
//import com.MemoryLadderFull.R;

public class VideoDialog extends Dialog implements OnClickListener {
	
	Context context;
	VideoView video;
	String[] urls = new String[] {
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp", 
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",
			"rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp",		
	};
	
	public VideoDialog(Context context, int gameType) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video);
		
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		
		initButtons();
		initVideo(gameType);

	}
	
	public void initButtons() {
	}
	
	public void initVideo(int gameType) {
		video = (VideoView)findViewById(R.id.video);
		video.setVideoURI(Uri.parse(urls[gameType]));
		video.setMediaController(new MediaController(context)); // to increase using of the videoView
		video.requestFocus();
		video.start();
	}
	
	
	@Override
	public void onClick(View v) {
	}	
}