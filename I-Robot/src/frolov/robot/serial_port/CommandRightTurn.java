package frolov.robot.serial_port;

import frolov.robot.*;
import frolov.robot.serial_port.type1.*;

public class CommandRightTurn extends AbstractType1 implements ICommand{
   
   public CommandRightTurn(String sParameters) throws Exception{
      super(sParameters);
   }
}
