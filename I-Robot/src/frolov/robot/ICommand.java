package frolov.robot;


public interface ICommand{
   
   public String getName();
   
   public Response run() throws Exception;

}
