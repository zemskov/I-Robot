package frolov.robot.rest.scratch;

import javax.servlet.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import frolov.robot.*;



@Path("/")
public class FEConnector{
   @Context ServletContext context;   
   
   public FEConnector(){
   }

   @GET
   @Path("/forward")
   @Produces(MediaType.TEXT_PLAIN)
   public String forward() throws Exception{
      
      ((RoboConfig) context.getAttribute("roboConfig")).mapCommands.get("Ехать вперед").run();
      
      return "Test";
   }
   
   
   
   @GET
   @Path("/backward")
   @Produces(MediaType.TEXT_PLAIN)
   public String backward() throws Exception{
      
      ((RoboConfig) context.getAttribute("roboConfig")).mapCommands.get("Ехать назад").run();
      
      return "Test";
   }
   
   
   
   
   @GET
   @Path("/stop")
   @Produces(MediaType.TEXT_PLAIN)
   public String stop() throws Exception{
      ((RoboConfig) context.getAttribute("roboConfig")).mapCommands.get("Стоп!").run();
      return "Test";
   }
   
}
