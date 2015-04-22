package frolov.robot.serial_port.type1;

import java.io.*;
import java.nio.*;
import javax.xml.bind.*;
import jssc.*;
import org.apache.commons.logging.*;
import frolov.robot.*;



public abstract class AbstractType1{
   private static Log log = LogFactory.getLog(Main.class);
   private static final String LOG = "[Main] ";
   
   
   private final XCommand xCommand; 
   
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
   }
   
   
   
   private static class PortReader implements SerialPortEventListener{
      private AbstractType1 abstractCommand;
      private ByteBuffer bbuf;

      public PortReader(AbstractType1 abstractCommand){
         this.abstractCommand = abstractCommand;
         
         bbuf = ByteBuffer.allocate(100);         
      }


      public void serialEvent(SerialPortEvent event){
         if(event.isRXCHAR() && event.getEventValue() > 0){
            
            
            try{
               byte[] arrbyteRead = Main.serialPort.readBytes();
               System.out.println(arrbyteRead.length);
               bbuf.put(arrbyteRead);
            }
            catch(SerialPortException e){
            }
         }
      }
   }
   
}
