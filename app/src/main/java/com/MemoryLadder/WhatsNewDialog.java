package com.MemoryLadder;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.mastersofmemory.memoryladder.R;

public class WhatsNewDialog extends Dialog {

	public WhatsNewDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_whats_new);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
}