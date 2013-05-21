package pl.marek.knx.components.controllers;


public enum ElementGroupAddressType{
	MAIN {
		@Override
		public String getLabel() {
			return "group-address-default-label";
		}
	},
	STATUS {
		@Override
		public String getLabel() {
			return "group-address-state-label";
		}
	},
	OTHER {
		@Override
		public String getLabel() {
			return "group-address-other-label";
		}
	};
	
	public abstract String getLabel();
}
