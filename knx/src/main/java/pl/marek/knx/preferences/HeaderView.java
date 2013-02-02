package pl.marek.knx.preferences;

import pl.marek.knx.R;

public enum HeaderView {
	CATEGORY(R.layout.preference_category) {

		@Override
		public void createView(HeaderBuilder builder) {
			builder.createTitleView();
		}

	},
	ITEM(R.layout.preference_header_item) {

		@Override
		public void createView(HeaderBuilder builder) {
			builder.createTitleView();
			builder.createIconView();
			builder.createSummaryView();
		}

	},
	SWITCH(R.layout.preference_header_switch_item) {

		@Override
		public void createView(HeaderBuilder builder) {
			builder.createTitleView();
			builder.createIconView();
			builder.createSummaryView();
			builder.createSwitchView();
		}
	};
	
	private int resourceId;
	
	private HeaderView(int resourceId){
		this.resourceId = resourceId;
	}
	
	public int getResourceId(){
		return resourceId;
	}
	
	public abstract void createView(HeaderBuilder builder);
}
