package pl.marek.knx;

import java.util.HashSet;

import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.telegram.Telegram;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TelegramBroadcastReceiver extends BroadcastReceiver{

	private HashSet<KNXTelegramListener> telegramReceivers;

	public TelegramBroadcastReceiver(){
		telegramReceivers = new HashSet<KNXTelegramListener>();
	}
	
	public void addTelegramListener(KNXTelegramListener listener){
		telegramReceivers.add(listener);
	}
	
	public void removeTelegramListener(KNXTelegramListener listener){
		telegramReceivers.remove(listener);
	}
	
	public void removeAllListeners(){
		telegramReceivers.clear();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(KNXTelegramListener.TELEGRAM_RECEIVED)){
			Telegram telegram = intent.getParcelableExtra(Telegram.TELEGRAM);
			for(KNXTelegramListener telegramReceiver: telegramReceivers){
				if(telegramReceiver != null){
					telegramReceiver.telegramReceived(telegram);
				}
			}
		}
	}
}
