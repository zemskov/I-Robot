package frolov.robot.serial_port;

import frolov.robot.*;
import frolov.robot.serial_port.type1.*;

public class CommandStop extends AbstractType1 implements ICommand{
   
   public CommandStop(String sParameters) throws Exception{
      super(sParameters);
   }
}
