package frolov.robot;

import jssc.*;
import org.apache.commons.logging.*;



public class Test{
   private static Log log = LogFactory.getLog(Test.class);
   private static final String LOG = "[Detector] ";

   
   public static void main(String[] args) throws SerialPortException {
      String[] arrPorts = SerialPortList.getPortNames();
      
      SerialPort serialPort = new SerialPort(arrPorts[0]);      
      
      // Let's open
      serialPort.openPort();
      log.debug(LOG + " opened.");
      
      // Listens for incoming data
      //serialPort.addEventListener(new PortReader(this), SerialPort.MASK_RXCHAR);

      // Something standart
      serialPort.setParams(SerialPort.BAUDRATE_9600,
                           SerialPort.DATABITS_8,
                           SerialPort.STOPBITS_1,
                           SerialPort.PARITY_NONE);

      // Hardware Overflow
      serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
      //serialPort.purgePort(255);
      
      try{
         Thread.sleep(5000);
      }
      catch (InterruptedException e){
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      
      
      serialPort.writeByte((byte) 160);
      
      try{
         Thread.sleep(5000);
      }
      catch (InterruptedException e){
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      

      serialPort.writeByte((byte) 0);
   }
}
