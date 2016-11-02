package com.genie.service;

import com.genie.model.RaspRC522;
import com.genie.util.Convert;

/**
 * Created by Liang on 2016/3/13.
 */




public class WiringPiSPIExample
{
    public static void main(String args[]) throws InterruptedException
    {
        RaspRC522 rc522=new RaspRC522();
        int back_length [] = new int[1];	//返回的数据
        byte tagid [] = new byte[5];	
        int status ;
        String strUID ;		//卡号
        byte sector = 15 ;	//扇区 
        byte block = 3 ;	//块
        
        //寻卡，获得序列号
        if( rc522.Request(RaspRC522.PICC_REQIDL, back_length) == RaspRC522.MI_OK ) {
        	System.out.println("Detecte Card =" + back_length[0] );
        }
        //防冲撞
        if(	rc522.AntiColl(tagid) != RaspRC522.MI_OK )
        {
            System.out.println("Anticoll error !");
            return;
        }

        //显示序列号
        strUID= Convert.bytesToHex( tagid );
        System.out.println( "Card Read UID ="  + strUID );
        System.out.println("Card Read UID =" + strUID.substring(0,2) + "," +
                strUID.substring(2,4) + "," +
                strUID.substring(4,6) + "," +
                strUID.substring(6,8));
        
        //Select the scanned tag , 选中指定序列号的卡
        int size= rc522.Select_Tag( tagid );
        System.out.println(" Select Tag , Size =" + size );
        
        //默认密钥
        //byte[] defaultkey = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        
        byte[] defaultkey = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
        
        //Authenticate,A密钥验证卡,
        status = rc522.Auth_Card( RaspRC522.PICC_AUTHENT1B , sector , block , defaultkey , tagid );
        if( status != RaspRC522.MI_OK ){
            System.out.println("Authenticate error !");
            return;
        } else {
        	System.out.println("Authenticate SUCCESS ! defaultKey =" + Convert.bytesToHex( defaultkey ));
        }
        
        /**
         * 
         * 卡各扇区初始控制字FF078069
         * 
         * ---------------------
         * 
         * 15扇区存储控制改为08778F69
         * 
         * A密钥改为330123
         * 
         * B密钥改为
         * 
         * 
         */
//        byte [] data = new byte[16] ;
//        
//        /* 密码A (6字节) , Data 位置 ( 0 ~ 5 字节 ) */
//        byte[] keyA = new byte[]{(byte)0x03,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03};
//        System.arraycopy( keyA , 0 , data , 0 , 6 );
//        /* 存取控制 (4字节) , Data 位置 ( 6 ~ 9 字节 ) */
//        byte controlbytes [] = new byte[]{(byte)0x08,(byte)0x77,(byte)0x8f,(byte) 0x69};
//        System.arraycopy( controlbytes , 0 , data , 6 , 4 );
//        /* 密码B (6字节) , Data 位置 ( 10 ~ 15 字节 ) */
//        byte[] keyB = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
//        System.arraycopy( keyB , 0 , data , 10 , 6 );
//        status = rc522.Write( sector , block , data );
//        
//        if( status == RaspRC522.MI_OK ) {
//            System.out.println( "Write Key finished !" );
//        	System.out.println( "Basic Info =" + data );
//        } else {
//            System.out.println( "Write Key error , status =" + status );
//            return;
//        }
        
        byte [] writeValues = new byte[]{
        		(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,
        		(byte)0x07,(byte)0x08,(byte)0x09,(byte)0x10,(byte)0x11,(byte)0x12,
        		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff
        };
        
        status = rc522.Write( sector , (byte)2 , writeValues );
        
        if( status == RaspRC522.MI_OK ) {
            System.out.println( "Write Data finished !" );
        	System.out.println( "Data Info =" + Convert.bytesToHex( writeValues ) );
        } else {
            System.out.println( "Write data error , status =" + status );
            return;
        }
        
    }

}
