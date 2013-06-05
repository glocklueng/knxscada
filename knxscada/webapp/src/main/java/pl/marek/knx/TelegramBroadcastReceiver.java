package pl.marek.knx;

import java.util.ArrayList;
import java.util.HashSet;

import pl.marek.knx.interfaces.KNXWebTelegramListener;
import pl.marek.knx.telegram.Telegram;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TelegramBroadcastReceiver extends BroadcastReceiver{

	private HashSet<KNXWebTelegramListener> telegramReceivers;

	public TelegramBroadcastReceiver(){
		telegramReceivers = new HashSet<KNXWebTelegramListener>();
	}
	
	public void addTelegramListener(KNXWebTelegramListener listener){
		telegramReceivers.add(listener);
	}
	
	public void removeTelegramListener(KNXWebTelegramListener listener){
		telegramReceivers.remove(listener);
	}
	
	public void removeAllListeners(){
		telegramReceivers.clear();
	}
	
	public void removeListenersByIpAddress(String ip){
		ArrayList<KNXWebTelegramListener> listenersToRemove = new ArrayList<KNXWebTelegramListener>();
		for(KNXWebTelegramListener telegramReceiver: telegramReceivers){
			if(telegramReceiver.getClientIpAddress().equals(ip)){
				listenersToRemove.add(telegramReceiver);
			}
		}
		for(KNXWebTelegramListener telegramReceiver: listenersToRemove){
			telegramReceivers.remove(telegramReceiver);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(KNXWebTelegramListener.TELEGRAM_RECEIVED)){
			Telegram telegram = intent.getParcelableExtra(Telegram.TELEGRAM);
			for(KNXWebTelegramListener telegramReceiver: telegramReceivers){
				if(telegramReceiver != null){
					telegramReceiver.telegramReceived(telegram);
				}
			}
		}
	}
}
