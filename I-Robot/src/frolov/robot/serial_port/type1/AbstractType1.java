package frolov.robot.serial_port.type1;

import java.io.*;
import java.nio.*;
import java.util.*;
import javax.xml.bind.*;
import jssc.*;
import org.apache.commons.logging.*;
import frolov.robot.*;



public abstract class AbstractType1{
   private static Log log = LogFactory.getLog(Main.class);
   private static final String LOG = "[Main] ";
   
   
   private final XCommand xCommand;
   protected final Map<String, Object> mapResponse = new HashMap<String, Object>();
   
   public AbstractType1(String sFileParameters) throws Exception{
      JAXBContext jc = JAXBContext.newInstance(XCommand.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      InputStream in = this.getClass().getClassLoader().getResourceAsStream(sFileParameters);      
      xCommand = (XCommand) JAXBIntrospector.getValue(unmarshaller.unmarshal(in));
      
      log.info(LOG + xCommand);
   }
   


   
   protected void go() throws Exception{
      // Listens for incoming data
      try{
         Main.serialPort.removeEventListener();
      }
      catch (Exception e){
      }
      Main.serialPort.purgePort(255);
      Main.serialPort.addEventListener(new PortReader(this), SerialPort.MASK_RXCHAR);
      
      Main.serialPort.writeBytes(DatatypeConverter.parseHexBinary(xCommand.code));
      
      synchronized(AbstractType1.this){
         this.wait(1000);
      }
   }
   
   
   
   private class PortReader implements SerialPortEventListener{
      private AbstractType1 abstractCommand;
      private ByteBuffer bbuf;

      public PortReader(AbstractType1 abstractCommand){
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
               System.out.println(arrbyteRead.length);
               bbuf.put(arrbyteRead);
               
               if(bbuf.hasRemaining()){
               }
               else{
                  log.trace(LOG + "Buffer is full.");
                  
                  bbuf.flip();
                  
                  for(XCommand.XResponseValue responseValue : abstractCommand.xCommand.response.listValues){
                     if("int".endsWith(responseValue.type)){
                        if(responseValue.length == 1) {
                           byte byte1 = bbuf.get();
                           
                           mapResponse.put(responseValue.name, (int) byte1); 
                        }
                        else if(responseValue.length == 2) {
                           byte byte1 = bbuf.get();
                           byte byte2 = bbuf.get();
                           
                           mapResponse.put(responseValue.name, (int) byte1 + byte2 * 256); 
                        }
                     }
                  }
                  
                  log.trace(LOG + mapResponse);
                  
                  synchronized(AbstractType1.this){
                     AbstractType1.this.notifyAll();
                  }
               }
            }
            catch(SerialPortException e){
            }
         }
      }
   }
   
}
