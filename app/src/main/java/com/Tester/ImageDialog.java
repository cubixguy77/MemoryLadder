package com.Tester;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class ImageDialog extends Dialog implements OnClickListener {
	
	Context context;
	private ImageView imageview;
	private Button button;
	private TextView textview;
	private int[] imageID;
	private String[] text;
	private int position;
	
	
	public ImageDialog(Context context, int []imageID, String []text, int position) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imagedialog);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		
		this.context = context;
		this.imageID = imageID;
		this.text = text;
		this.position = position;
	
		loadViews();
	}
	
	private void loadViews() {
		imageview = (ImageView) findViewById(R.id.image);
		textview = (TextView) findViewById(R.id.text);
		button = (Button) findViewById(R.id.button);
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