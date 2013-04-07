package pl.marek.knx.receivers;

import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.telegram.Telegram;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TelegramBroadcastReceiver extends BroadcastReceiver{

	private KNXTelegramListener telegramReceiver;

	public TelegramBroadcastReceiver(KNXTelegramListener telegramReceiver){
		this.telegramReceiver = telegramReceiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(KNXTelegramListener.TELEGRAM_RECEIVED)){
			Telegram telegram = intent.getParcelableExtra(Telegram.TELEGRAM);
			telegramReceiver.telegramReceived(telegram);
		}
	}
}