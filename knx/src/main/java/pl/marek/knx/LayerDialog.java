package pl.marek.knx;

import pl.marek.knx.IconPickerDialog.OnIconPickListener;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.utils.DrawableUtils;
import pl.marek.knx.utils.MessageDialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LayerDialog<T extends Layer> extends BaseDialog implements
		View.OnClickListener, OnIconPickListener {

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ICON = "icon";
	public static final String CLASS_NAME = "class_name";

	private TextView titleView;
	private TextView nameView;
	private TextView descView;
	private TextView nameLabelView;
	private TextView descLabelView;
	private ImageButton iconViewButton;
	private Button applyButton;
	private Button cancelButton;

	private int iconRes = -1;

	private T layer;
	private Class<T> clazz;
	private OnLayerDialogApproveListener listener;

	public LayerDialog(Context context, Class<T> clazz) {
		super(context, R.style.dialogTheme);
		this.clazz = clazz;
	}

	public LayerDialog(Context context, Class<T> clazz, T layer) {
		this(context, clazz);
		this.layer = layer;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layer_dialog);

		setDialogSize();

		titleView = (TextView) findViewById(R.id.dialog_title_text);
		nameView = (TextView) findViewById(R.id.dialog_new_layer_name);
		descView = (TextView) findViewById(R.id.dialog_new_layer_description);
		iconViewButton = (ImageButton) findViewById(R.id.dialog_new_layer_icon);
		nameLabelView = (TextView) findViewById(R.id.dialog_new_layer_name_label);
		descLabelView = (TextView) findViewById(R.id.dialog_new_layer_description_label);

		applyButton = (Button) findViewById(R.id.dialog_layer_add_button);
		cancelButton = (Button) findViewById(R.id.dialog_new_layer_cancel_button);

		applyButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		iconViewButton.setOnClickListener(this);

		setInitialValues();
	}

	private void setInitialValues() {
		if (layer != null) {
			applyButton.setText(getContext().getString(
					R.string.dialog_layer_edit_button));
			nameView.setText(layer.getName());
			descView.setText(layer.getDescription());
			setIcon(layer.getIcon());
		} else {
			if (clazz.equals(SubLayer.class)) {
				applyButton.setText(getContext().getString(
						R.string.dialog_sublayer_add_button));
			}
		}
		if (clazz.equals(SubLayer.class)) {
			nameView.setHint(getContext().getString(
					R.string.dialog_new_sublayer_name_hint));
			descView.setHint(getContext().getString(
					R.string.dialog_new_sublayer_description_hint));
			nameLabelView.setText(getContext().getString(
					R.string.dialog_new_sublayer_name));
			descLabelView.setText(getContext().getString(
					R.string.dialog_new_sublayer_description));
		}
	}

	public void setOnLayerDialogApproveListener(
			OnLayerDialogApproveListener listener) {
		this.listener = listener;
	}

	public String getName() {
		return nameView.getText().toString();
	}

	public void setName(String name) {
		nameView.setText(name);
	}

	private boolean validateName() {
		if (getName().isEmpty()) {
			return false;
		}
		return true;
	}

	public String getDescription() {
		return descView.getText().toString();
	}

	public void setDescription(String description) {
		descView.setText(description);
	}

	public String getIconName() {
		String name = "";
		try {
			if (iconRes > 0)
				name = getContext().getResources()
						.getResourceEntryName(iconRes);
		} catch (NotFoundException ex) {
			Log.d(getContext().getString(R.string.application_name),
					ex.getMessage());
		}
		return name;
	}

	public void setIcon(String name) {
		iconRes = DrawableUtils.getResourceIdByName(getContext(), name);
		if (iconRes > 0)
			iconViewButton.setImageResource(iconRes);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setApplyButtonText(String text) {
		applyButton.setText(text);
	}

	public void setLayer(T layer) {
		this.layer = layer;
	}

	public T getLayer() {
		return layer;
	};

	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(applyButton)) {
			if (validateName()) {
				if (layer == null) {
					Layer layer = new Layer();
					if(clazz.getName().equals(SubLayer.class.getName())){
						layer = new SubLayer();
					}
					layer.setName(getName());
					layer.setDescription(getDescription());
					layer.setIcon(getIconName());
					listener.onLayerDialogAddAction(layer, clazz);
					
				} else {
					layer.setName(getName());
					layer.setDescription(getDescription());
					layer.setIcon(getIconName());
					listener.onLayerDialogEditAction(layer, clazz);
					
				}
				dismiss();
			} else {
				MessageDialog msgDialog = new MessageDialog(getContext());
				if (clazz.equals(SubLayer.class)) {
					msgDialog.showDialog(
							getContext().getString(R.string.sublayer_name_empty_title),
							getContext().getString(R.string.sublayer_name_empty_text),
							getContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert));
				} else {
					msgDialog.showDialog(
							getContext().getString(R.string.layer_name_empty_title),
							getContext().getString(R.string.layer_name_empty_text),
							getContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert));
				}
			}
		} else if (v.equals(cancelButton)) {
			cancel();
		} else if (v.equals(iconViewButton)) {
			IconPickerDialog picker = new IconPickerDialog(getContext());
			if (clazz.equals(SubLayer.class)) {
				picker.setIcons(DrawableUtils.getSubLayersIconsResources());
			} else {
				picker.setIcons(DrawableUtils.getLayersIconsResources());
			}
			picker.setOnIconPickListener(this);
			picker.show();
		}
	}

	@Override
	public void onIconPick(int iconRes) {
		iconViewButton.setImageResource(iconRes);
		this.iconRes = iconRes;
	}

	public interface OnLayerDialogApproveListener {
		public void onLayerDialogAddAction(Layer layer, Class<?> clazz);
		public void onLayerDialogEditAction(Layer layer, Class<?> clazz);
	}
}
