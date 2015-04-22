package frolov.robot.rest.scratch;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;
import frolov.robot.*;

public class ScratchRest implements IRest{
   
   public ScratchRest(final RoboConfig roboConfig){
   }
   
   
   
   public void start() throws Exception{
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");

      Server jettyServer = new Server(8080);
      jettyServer.setHandler(context);

      ServletHolder jerseyServlet = context.addServlet(com.sun.jersey.spi.container.servlet.ServletContainer.class, "/*");
      jerseyServlet.setInitOrder(0);

      // Tells the Jersey Servlet which REST service/class to load.
      jerseyServlet.setInitParameter(
         "com.sun.jersey.config.property.packages",
         "frolov.robot.rest");

      try{
         jettyServer.start();
         jettyServer.join();
      }
      finally{
         jettyServer.destroy();
      }
   }
}
