package org.isatools.resources;


import org.isatools.magetoisatab.io.MAGETabObtain;
import org.isatools.utils.ZipUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.logging.Logger;

@Path("/")
public class MAGE2ISA {

    private static Logger log = Logger.getLogger(MAGE2ISA.class.getName());

    @Context
    private UriInfo context;

    // Creates a new instance of MICheckout

    public MAGE2ISA() {
    }

    /**
     * Retrieves representation of an instance of MICheckout
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("convert")
    @Produces("application/zip")
    public Response getISAConfiguration(@QueryParam("accession") String accession) {

        if (accession != null && !accession.equals("")) {
            try {
                MAGETabObtain obtain = new MAGETabObtain();
                File contentDirectory = obtain.doConversion(accession);

                if (contentDirectory != null) {

                    log.info("Content directory is " + contentDirectory.getAbsolutePath());

                    ZipUtil util = new ZipUtil();

                    File zippedDir = util.zipDirectoryContents(accession, System.getProperty("java.io.tmpdir"), contentDirectory.listFiles());

                    Response zippedResponse = Response.ok(zippedDir).type("application/zip")
                            .header("Content-Disposition", "attachment; filename=" + zippedDir.getName()).build();
                    return zippedResponse;
                } else {
                    Response errorResponse = Response.ok("We encountered a problem whilst processing your request. Be aware that some MAGE-Tab files simply won't convert to " +
                            "ISAtab due to poor annotation and file structure -- please try again.").type("text/plain").build();
                    return errorResponse;
                }

            } catch (Exception e) {
                log.info(e.toString());
                Response errorResponse = Response.ok("We encountered a problem whilst processing your request. Be aware that some MAGE-Tab files simply won't convert to " +
                        "ISAtab due to poor annotation and file structure -- please try again.").type("text/plain").build();
                return errorResponse;
            }
        } else {
            Response errorResponse = Response.ok("Please provide a valid accession.").type("text/plain").build();
            return errorResponse;
        }

    }

    /**
     * Retrieves representation of an instance of MICheckout
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("test")
    @Produces("text/plain")
    public String getTestChecklist(@QueryParam("name") String name) {

        return "This is a test output " + name + "! :D The service is working...";
    }


}