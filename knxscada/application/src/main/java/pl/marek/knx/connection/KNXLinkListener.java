package pl.marek.knx.connection;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.Group;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.telegram.TelegramFlags;
import pl.marek.knx.utils.DataRepresentation;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.event.NetworkLinkListener;

public class KNXLinkListener implements NetworkLinkListener{
	
	private KNXTelegramListener listener;
	private DatabaseManager dbManager;
	
	private Datapoint tmpDatapoint;
	private boolean waitForReadResponse = false;
	private boolean waitForWriteResponse = false;
	
	public KNXLinkListener(KNXTelegramListener listener, DatabaseManager dbManager){
		this.listener = listener;
		this.dbManager = dbManager;
	}
	
	public void setTmpDatapoint(Datapoint datapoint){
		tmpDatapoint = datapoint;
	}
	
	public void setWaitForReadResponseFlag(){
		waitForReadResponse = true;
	}
	
	public void clearWaitForReadResponseFlag(){
		waitForReadResponse = false;
	}
	
	public void setWaitForWriteResponseFlag(){
		waitForWriteResponse = true;
	}
	
	public void clearWaitForWriteResponseFlag(){
		waitForWriteResponse = false;
	}

	public void indication(FrameEvent e) {
		listener.telegramReceived(createTelegram(e));
	}

	public void linkClosed(CloseEvent e) {
		Log.d(LogTags.KNX_CONNECTION, e.getReason());
	}

	public void confirmation(FrameEvent e) {
		listener.telegramReceived(createTelegram(e));
	}
	
    private Telegram createTelegram(FrameEvent e){
    	
        Date time = Calendar.getInstance().getTime();
        String priority = ((CEMILData)e.getFrame()).getPriority().toString();
        
        String sourceAddr = ((CEMILData)e.getFrame()).getSource().toString();
        String destinationAddr = ((CEMILData)e.getFrame()).getDestination().toString(); 
        byte hopcount = ((CEMILData)e.getFrame()).getHopCount();
        String msgCode = getMessageCode(e);
        byte [] rawdata = ((CEMILData)e.getFrame()).getPayload();

        String type = getTelegramType(e);     
        DPT dpt = getDPT(destinationAddr);
             
        String dptId = "";
        if (dpt != null){
        	dptId = dpt.getID();
        }
        String data = getData(rawdata, dpt);
        
        boolean ack = ((CEMILData)e.getFrame()).isAckRequested();
        boolean confirmation = ((CEMILData)e.getFrame()).isPositiveConfirmation();
        boolean repeated = ((CEMILData)e.getFrame()).isRepetition();
        byte[] rawframe = ((CEMILData)e.getFrame()).toByteArray();
        int framelength = ((CEMILData)e.getFrame()).getStructLength();
        
        TelegramFlags flags = new TelegramFlags(ack, confirmation, repeated);
        
        tmpDatapoint = null;
        
        int projectId = 0;
        //TODO Pobierać projectId na podstawie połączenia?
        
        return new Telegram(projectId,time, priority, sourceAddr, destinationAddr, hopcount, type, msgCode, rawdata, data, dptId, flags, rawframe, framelength);   
    }
    
    private String getMessageCode(FrameEvent e){
        short mCode = ((CEMILData)e.getFrame()).getMessageCode();
        if (mCode == CEMILData.MC_LDATA_CON)
        	return "L_Data.con";
        else if (mCode == CEMILData.MC_LDATA_IND)
        	return "L_Data.ind";
        else if (mCode == CEMILData.MC_LDATA_REQ)
        	return "L_Data.req";
        else 
        	return String.valueOf(mCode);
    }
    
    private String getTelegramType(FrameEvent e){
        short mCode = ((CEMILData)e.getFrame()).getMessageCode();
        
        String type = "";
        if (waitForReadResponse && !waitForWriteResponse && mCode == CEMILData.MC_LDATA_IND){
            type = "Response";
            clearWaitForReadResponseFlag();
        }
        else if (waitForReadResponse && !waitForWriteResponse && mCode == CEMILData.MC_LDATA_CON){
            type = "Read";
        }
        else if (waitForWriteResponse){
            type = "Write";
            clearWaitForWriteResponseFlag();
        }
        else{
            type = "Write";
        }
    	return type;
    }
    
    private String getData(byte [] rawdata, DPT dpt){
    	DPTXlator translator = null;
		try {
			if(dpt != null){
				translator = TranslatorTypes.createTranslator(dpt);
				translator.setData(rawdata, rawdata.length-1);
			} else{
				throw new KNXException("DPT is not declared");
			}
			
		} catch (KNXException e) {
			Log.d(LogTags.APPLICATION, e.getMessage());
			return DataRepresentation.byteArrayToHexString(rawdata);
		}
    	return translator.getValue();
    }
    
    private DPT getDPT(String destinationAddr){
    	DPT dpt = null;
    	try {
			GroupAddress addr = new GroupAddress(destinationAddr);
			Datapoint dp = getDatapoint(addr);
			if(dp != null){
				DPTXlator translator = TranslatorTypes.createTranslator(0,dp.getDPT());
				dpt = translator.getType();
			}
		} catch (KNXException e) {
			Log.d(LogTags.APPLICATION, e.getMessage());
		}
    	return dpt;
    }
    
    private Datapoint getDatapoint(GroupAddress addr){
    	Datapoint datapoint = null;
    	DatapointEntity dpEntity = dbManager.getDatapointEntityByAddress(addr.toString());
    	Group group = dbManager.getGroupByAddress(addr.toString());
    	if (dpEntity != null){
    		String name = "";
    		if(group != null){
    			name = group.getName();
    		}
    		datapoint = new StateDP(addr, name, 0, dpEntity.getDptId());
    	} else if (tmpDatapoint != null){
    		datapoint = tmpDatapoint;
    	}
    	
    	return datapoint;
    }
}
