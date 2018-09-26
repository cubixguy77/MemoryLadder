package com.memoryladder.shapeschallenges;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class ImageDialog extends Dialog implements OnClickListener {

	private ImageView imageview;
	private TextView textview;
	private int[] imageID;
	private String[] text;
	private int position;

	ImageDialog(Context context, int[] imageID, String[] text, int position) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imagedialog);
		setCancelable(true);
		setCanceledOnTouchOutside(true);

		this.imageID = imageID;
		this.text = text;
		this.position = position;
	
		loadViews();
	}
	
	private void loadViews() {
		imageview = findViewById(R.id.image);
		textview = findViewById(R.id.text);
		Button button = findViewById(R.id.button);
		button.setOnClickListener(this);
	
		refresh();
	}
	
	private void refresh() {
		imageview.setImageResource(imageID[position]);
		textview.setText(text[position]);
	}

	@Override
	public void onClick(View v) {
		if (position+1 < imageID.length) {
			position++;
			refresh();
		}
	}
	
}