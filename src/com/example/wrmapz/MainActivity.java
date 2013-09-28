package com.example.wrmapz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.wrmapz.R;
import com.example.wrmapz.view.MenuForm;

public class MainActivity extends Activity implements OnClickListener{
	
	
	
	private MenuForm menuForm;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMenuForm(new MenuForm(this));
		menuForm.getButtonPeta().setOnClickListener((android.view.View.OnClickListener) this);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.info).setVisible(false);
		menu.findItem(R.id.screenshot).setVisible(false);
		menu.findItem(R.id.menu_position).setVisible(false);
		menu.findItem(R.id.menu_mapfile).setVisible(false);	
		menu.findItem(R.id.action_settings).setVisible(false);
		menu.findItem(R.id.menu_render_theme).setVisible(false);
		menu.findItem(R.id.menu_preferences).setVisible(false);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if( view== menuForm.getButtonPeta()){			
			try{			
//			Toast.makeText(this, "anda memilih Peta: ", Toast.LENGTH_LONG).show();
			intent =  new Intent(this, PetaActivity.class);
			startActivity(intent);
			}
			catch(Exception errorKarena){
				Toast.makeText(this, "Maaf Error Karena "+errorKarena, Toast.LENGTH_LONG).show();
			}
		}
		
	}

	public MenuForm getMenuForm() {
		return menuForm;
	}

	public void setMenuForm(MenuForm menuForm) {
		this.menuForm = menuForm;
	}

}
