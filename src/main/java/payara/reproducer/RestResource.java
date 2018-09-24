package payara.reproducer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("throwex")
public class RestResource {


    @GET
    public String throwsException() {
        throw new RuntimeException("my test exception");
    }

}
