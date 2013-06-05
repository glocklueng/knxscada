package pl.marek.knx.interfaces;

import pl.marek.knx.telegram.Telegram;

public interface KNXWebTelegramListener {
	public static final String TELEGRAM_RECEIVED = "pl.marek.knx.telegram_received";
	public void telegramReceived(Telegram telegram);
	public String getClientIpAddress();
}
