package payara.reproducer;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;


@Provider
public class AppExceptionMapper implements ExceptionMapper<Throwable> {

    private final static Logger LOG = Logger.getLogger(AppExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOG.log(Level.SEVERE, "AppExceptionMapper was called", exception);

        return Response.status(500).entity("AppMapperCalled").build();
    }
}
