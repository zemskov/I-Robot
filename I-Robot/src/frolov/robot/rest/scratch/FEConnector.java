package frolov.robot.rest.scratch;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apache.commons.logging.*;
import frolov.robot.*;



@Path("/")
public class FEConnector{
   private static Log log = LogFactory.getLog(FEConnector.class);
   private static final String LOG = "[Connector] ";
   
   
   @Context ServletContext context;   
   
   public FEConnector(@Context HttpServletRequest hsr){
      log.info(LOG + "\n------------------------------------\n" + hsr.getRequestURI());
      
   }
   
   
   @GET
   @Path("/crossdomain.xml")
   @Produces(MediaType.APPLICATION_XML)
   public String crossdomain() throws Exception{
      return "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" /></cross-domain-policy>";
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
