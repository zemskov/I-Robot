package frolov.robot.serial_port.type1;

import java.io.*;
import java.nio.*;
import java.util.*;
import javax.xml.bind.*;
import jssc.*;
import org.apache.commons.logging.*;
import frolov.robot.*;
import frolov.robot.ui.*;



public class Command implements ICommand{
   private static Log log = LogFactory.getLog(Main.class);
   private static final String LOG = "[Main] ";
   
   
   private static final int COMMAND_TIMEOUT = 2000;
   
   
   private final XCommand xCommand;
   protected final Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
   private ByteBuffer bbuf;
   
   
   private final String sName;
   private final String sIconName;
   
   
   public Command(String sName, String sIcon, String sFileParameters) throws Exception{
      this.sName = sName;
      this.sIconName = sIcon;
      
      JAXBContext jc = JAXBContext.newInstance(XCommand.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      InputStream in = this.getClass().getClassLoader().getResourceAsStream(sFileParameters);      
      xCommand = (XCommand) JAXBIntrospector.getValue(unmarshaller.unmarshal(in));
      
      log.info(LOG + xCommand);
   }
   


   
   public Response run() throws Exception{
      // Listens for incoming data
      try{
         Main.serialPort.removeEventListener();
      }
      catch (Exception e){
      }
      
      
      
      //Let's clean incoming buffer
      Main.serialPort.purgePort(255);
      Main.serialPort.readBytes(Main.serialPort.getInputBufferBytesCount());
      
      Main.serialPort.addEventListener(new PortReader(this), SerialPort.MASK_RXCHAR);
      
      Main.serialPort.writeBytes(DatatypeConverter.parseHexBinary(xCommand.code));
      
      
      long lCommandStart = System.currentTimeMillis();
      synchronized(Command.this){
         //Let's wait for data
         this.wait(COMMAND_TIMEOUT);
      }
      if(System.currentTimeMillis() - lCommandStart > COMMAND_TIMEOUT) {
         //Time is out!
         //Seems the Robot died.
         //RIP
         Main.serialPort.closePort();
         Main.serialPort = null;
         Main.robotLost();
      }

      
      Response response = new Response();
      response.rawData = new byte[bbuf.array().length];
      response.parsedValues = new LinkedHashMap<String, Object>(mapResponse);
      System.arraycopy(bbuf.array(), 0, response.rawData, 0, bbuf.array().length);
      return response;
   }
   
   
   
   
   
   
   
   
   private class PortReader implements SerialPortEventListener{
      private Command abstractCommand;

      public PortReader(Command abstractCommand){
         this.abstractCommand = abstractCommand;

         //ok, let's find out how much space we need
         int iLength = 0;
         for(XCommand.XResponseValue responseValue : abstractCommand.xCommand.response.listValues){
            iLength += responseValue.length;
         }
         
         log.trace(LOG + "We need " + iLength + " bytes.");
         
         bbuf = ByteBuffer.allocate(iLength);         
      }


      public void serialEvent(SerialPortEvent event){
         if(event.isRXCHAR() && event.getEventValue() > 0){
            
            
            try{
               byte[] arrbyteRead = Main.serialPort.readBytes();
               
               //System.out.println(arrbyteRead.length);
               
               bbuf.put(arrbyteRead);
               
               if(bbuf.hasRemaining()){
               }
               else{
                  log.trace(LOG + "Buffer is full.");
                  
                  bbuf.flip();
                  
                  StringBuilder sb = new StringBuilder();
                  
                  while(bbuf.hasRemaining()){
                     int incomingByteFirst = bbuf.get() & 0xff;
                     sb.append(String.format("%8s", Integer.toBinaryString(incomingByteFirst) + ",").replaceAll(" ", "0"));
                     
                     if(incomingByteFirst >> 7 == 1){
                        //ok, it's the first byte
                        
                        int iMeterNumber = (incomingByteFirst & 127) >> 3;
                        System.out.println("Channel=" + iMeterNumber);

                        int incomingByteSecond = bbuf.get() & 0xff;
                        
                        int iValue = ((incomingByteFirst & 1) << 7) | incomingByteSecond;
                        System.out.println(iValue);
                        
                        try{
                           mapResponse.put(abstractCommand.xCommand.response.listValues.get(iMeterNumber).name, iValue);
                        }
                        catch (Throwable e){
                           //we can reach out of parameter array
                           //so let's skip such things then
                        }
                     }
                  }
                  
                  log.info(LOG + sb);
                  
                  
//                  for(XCommand.XResponseValue responseValue : abstractCommand.xCommand.response.listValues){
//                     if("int".endsWith(responseValue.type)){
//                        if(responseValue.length == 2) {
//                           int byte1 = bbuf.get() & 0xff;
//                           int byte2 = bbuf.get() & 0xff;
//                           
//                           mapResponse.put(responseValue.name, (int) byte1 + byte2 * 256); 
//                        }
//                     }
//                  }
                  
                  //log.trace(LOG + mapResponse);
                  //log.trace(LOG + bbuf.array());
                  
                  synchronized(Command.this){
                     Command.this.notifyAll();
                  }
                  
                  Main.setIconImage(sIconName);
               }
            }
            catch(SerialPortException e){
            }
         }
      }
   }



   public String getName(){
      return sName;
   }
   
}
