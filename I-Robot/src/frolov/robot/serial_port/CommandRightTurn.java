package frolov.robot.serial_port;

import java.util.*;
import frolov.robot.*;
import frolov.robot.serial_port.type1.*;

public class CommandRightTurn extends AbstractType1 implements ICommand{
   
   public CommandRightTurn(String sParameters) throws Exception{
      super(sParameters);
   }
   

   @Override
   public Map<String, Object> run() throws Exception{
      super.go();
      
      // TODO Auto-generated method stub
      return null;
   }
}
