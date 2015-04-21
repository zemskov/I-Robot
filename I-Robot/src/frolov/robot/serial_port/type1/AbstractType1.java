package frolov.robot.serial_port.type1;

import java.io.*;
import javax.xml.bind.*;



public abstract class AbstractType1{
   public AbstractType1(String sFileParameters) throws Exception{
      JAXBContext jc = JAXBContext.newInstance(XCommand.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      InputStream in = this.getClass().getClassLoader().getResourceAsStream(sFileParameters);      
      XCommand xCommand = (XCommand) JAXBIntrospector.getValue(unmarshaller.unmarshal(in));
      System.out.println(xCommand);
   }
}
