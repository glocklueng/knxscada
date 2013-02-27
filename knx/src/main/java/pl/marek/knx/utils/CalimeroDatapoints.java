package pl.marek.knx.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator4ByteUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorDate;
import tuwien.auto.calimero.dptxlator.DPTXlatorDateTime;
import tuwien.auto.calimero.dptxlator.DPTXlatorString;
import tuwien.auto.calimero.dptxlator.DPTXlatorTime;

public class CalimeroDatapoints {
	
	public static HashMap<Integer,Field[]> DPTs = new HashMap<Integer, Field[]>();
    static{
    	DPTs.put(0, DPTXlatorBoolean.class.getDeclaredFields());
    	DPTs.put(1, DPTXlator3BitControlled.class.getDeclaredFields());
    	DPTs.put(2, DPTXlator8BitUnsigned.class.getDeclaredFields());
    	DPTs.put(3, DPTXlator2ByteUnsigned.class.getDeclaredFields());
    	DPTs.put(4, DPTXlator4ByteUnsigned.class.getDeclaredFields());
    	DPTs.put(5, DPTXlator2ByteFloat.class.getDeclaredFields());
    	DPTs.put(6, DPTXlatorDate.class.getDeclaredFields());
    	DPTs.put(7, DPTXlatorTime.class.getDeclaredFields());
    	DPTs.put(8, DPTXlatorDateTime.class.getDeclaredFields());
    	DPTs.put(9, DPTXlatorString.class.getDeclaredFields());
    }
	public CalimeroDatapoints(){
	
	}
	
	public static ArrayList<DPT> getFunctions(int key){
		ArrayList<DPT> functions = new ArrayList<DPT>();
		
		Field[] fields = DPTs.get(key);
        for (Field field: fields){
            if(field.getType().getSimpleName().equals("DPT")){
                try {
                    functions.add((DPT)field.get(null));
                } 
                catch (IllegalArgumentException ex) {} 
                catch (IllegalAccessException ex) {}
            }
        }
		return functions;
	}

}
