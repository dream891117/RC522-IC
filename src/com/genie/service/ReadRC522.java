package com.genie.service;


import com.genie.model.RaspRC522;
import com.genie.util.Convert;
import com.pi4j.util.Console;


/**
 * 
 * 
 * 使用RC522模块读取IC卡内容
 * 
 * - 包含卡号和卡内信息
 * 
 * @author Genie
 * 
 * @version 1.0 (2016.11.17)
 *
 */
public class ReadRC522 {

	final Console console = new Console();
	
	public void readCard(){
		
		console.title("<-- This RC522  -->", "Read Information");
		
		// display connection details
        console.box(" Connecting to: We are reading information every 1 second .");
		
        //Ctrl - C , exit
        console.promptForExit();
        
        RaspRC522 rc522 = new RaspRC522();
        
        int status ;
        
        byte sector = 15 ;	//扇区选择15
        byte block = 2 ;	//块选择2
        
		while ( true ) {
			
			try {
				Thread.sleep( 3000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String strUID ;	//卡号
			
			byte [] tagid  = new byte[5] ;
			
			rc522.Select_Tag(tagid);
	        rc522.Select_MirareOne( tagid );
	        strUID = Convert.bytesToHex(tagid);
	        
	        if ( strUID.equals("0000000000") ) {
	        	console.println("Dont find card !");
	        	continue ;
	        }
	        
	        console.println( "-----" + strUID );
	    		
	        byte [] keyA = new byte[]{(byte)0x03,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03};
	        
	        //Authenticate , A密钥验证卡 , 可以读数据块2
	        byte data[] = new byte[16] ;
	        status = rc522.Auth_Card( RaspRC522.PICC_AUTHENT1A , sector , block , keyA , tagid );
	        
	        if( status != RaspRC522.MI_OK ){
	        	console.println("Authenticate A error");
	        	continue ;
	        } else {
	        	console.println("Authenticate keyA SUCCESS !");
	        }
	        
	        status = rc522.Read( sector , block , data );
	        
	        rc522.Stop_Crypto();
	        
	        console.println(" Successfully Authenticated , sector = " + sector + " , block =" + block  );
	        console.println(" Read data =" + Convert.bytesToHex(data) ) ;
	        
	        console.println("");
	        console.println("");
	        
	    }
		
	}
	
	public static void main(String[] args) {
		
		ReadRC522 readRC522 = new ReadRC522();
		readRC522.readCard();
		
	}
	
	
}
