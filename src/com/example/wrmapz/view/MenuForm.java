package com.example.wrmapz.view;



import android.app.Activity;
import android.widget.Button;

import com.example.wrmapz.R;

public class MenuForm {
	private Activity activity;
	private Button buttonPeta;
	

	public MenuForm(Activity activity) {
		super();
		this.setActivity(activity);
		setButtonPeta((Button) activity.findViewById(R.id.buttonPeta));		
	}

	
	public Button getButtonPeta() {
		return buttonPeta;
	}


	public void setButtonPeta(Button buttonPeta) {
		this.buttonPeta = buttonPeta;
	}


	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	

}
