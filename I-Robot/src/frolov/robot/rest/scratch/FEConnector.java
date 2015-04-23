package frolov.robot.rest.scratch;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.context.*;
import org.springframework.context.support.*;
import frolov.robot.*;



@Path("/")
public class FEConnector{
   
   final RoboConfig roboConfig;
   
   public FEConnector(){
      ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
      roboConfig = ctx.getBean("robo_config", RoboConfig.class);       
   }

   @GET
   @Path("/forward")
   @Produces(MediaType.TEXT_PLAIN)
   public String forward() throws Exception{
      roboConfig.mapCommands.get("Ехать вперед").run();
      
      return "Test";
   }
   
   
   
   @GET
   @Path("/stop")
   @Produces(MediaType.TEXT_PLAIN)
   public String stop() throws Exception{
      roboConfig.mapCommands.get("Стоп!").run();
      
      return "Test";
   }
   
}
