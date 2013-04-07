package pl.marek.knx;

import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.telegram.TelegramFlags;
import pl.marek.knx.utils.DataRepresentation;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TelegramDetailActivity extends Activity{
	
	private Telegram telegram;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telegram_detail);
		
		telegram = getIntent().getParcelableExtra(Telegram.TELEGRAM);
		
		TextView idView = (TextView)findViewById(R.id.telegram_detail_id);
		TextView timeView = (TextView)findViewById(R.id.telegram_detail_time);
		TextView priorityView = (TextView)findViewById(R.id.telegram_detail_priority);
		TextView srcAddrView = (TextView)findViewById(R.id.telegram_detail_source_address);
		TextView dstAddrView = (TextView)findViewById(R.id.telegram_detail_destination_address);
		TextView hopCountView = (TextView)findViewById(R.id.telegram_detail_hopcount);
		TextView typeView = (TextView)findViewById(R.id.telegram_detail_type);
		TextView msgCodeView = (TextView)findViewById(R.id.telegram_detail_msgcode);
		TextView rawdataView = (TextView)findViewById(R.id.telegram_detail_rawdata);
		TextView dataView = (TextView)findViewById(R.id.telegram_detail_data);
		TextView rawframeView = (TextView)findViewById(R.id.telegram_detail_rawframe);
		TextView framelengthView = (TextView)findViewById(R.id.telegram_detail_framelength);
		TextView dptIdView = (TextView)findViewById(R.id.telegram_detail_dptid);
		TextView ackView = (TextView)findViewById(R.id.telegram_detail_ack);
		TextView confView = (TextView)findViewById(R.id.telegram_detail_confirmation);
		TextView repView = (TextView)findViewById(R.id.telegram_detail_repeated);
		
		
		idView.setText(String.valueOf(telegram.getId()));
		timeView.setText(telegram.getTime().toString());
		priorityView.setText(telegram.getPriority());
		srcAddrView.setText(telegram.getSourceAddress());
		dstAddrView.setText(telegram.getDestinationAddress());
		hopCountView.setText(String.valueOf(telegram.getHopcount()));		
		typeView.setText(telegram.getType());
		msgCodeView.setText(telegram.getMsgCode());
		rawdataView.setText(DataRepresentation.byteArrayToHexString(telegram.getRawdata()));
		dataView.setText(telegram.getData());
		rawframeView.setText(DataRepresentation.byteArrayToHexString(telegram.getRawframe()));
		framelengthView.setText(String.valueOf(telegram.getFrameLength()));
		dptIdView.setText(telegram.getDptId());
		
		TelegramFlags flags = telegram.getFlags();
		ackView.setText(flags.isAck() ? getString(R.string.yes) : getString(R.string.no));
		confView.setText(flags.isConfirmation() ? getString(R.string.yes) : getString(R.string.no));
		repView.setText(flags.isRepeated() ? getString(R.string.yes) : getString(R.string.no));
		
		getActionBar().setTitle(getString(R.string.telegrams_activity_details_title));
	}

}
