package frolov.robot.serial_port;

import frolov.robot.*;
import frolov.robot.serial_port.type1.*;

public class CommandMoveForward extends Command implements ICommand{
   
   public CommandMoveForward(String sParameters) throws Exception{
      super(sParameters);
   }
}
