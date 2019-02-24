package jie.example.boutique;

import android.os.Bundle;

public class CommonFragmentActivity extends BasicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.hideActionBar();
		setContentView(R.layout.common_fragment_aty);
		initData();
		loadingData();
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void loadingData() {
		
	}

}
