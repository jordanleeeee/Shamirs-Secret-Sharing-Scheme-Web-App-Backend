package com.ssss.CD1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.apache.commons.io.IOUtils;

import ssss.MakeSharePlus;
import ssss.RecoverSecretPlus;

@Path("ZipApi")
@PermitAll
public class ZipResources {

    @POST
    @Path("/encryption/{n}/{t}")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response getShares(@FormDataParam("image") InputStream fileInputStream, @PathParam("t") int t,
                              @PathParam("n") int n) {
        try {
            System.out.println("You are now in Zip encryption service");
            byte[] secretByte = IOUtils.toByteArray(fileInputStream);
            MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

            byte[][] shares = makeSharePlus.constructPointsEX();
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < n; i++) {
                result.put("share" + i, Base64.getEncoder().encodeToString(shares[i]));
            }
            return Response.ok(result).build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @POST
    @Path("/recovery/{t}")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSecret(@PathParam("t") int t, Map<String, Object> map) {
        System.out.println("\n\nYou are now in Zip recovery service");
        String[] shares = new String[t];
        int numOfByte = Base64.getDecoder().decode((String) map.get("share" + 0)).length;
        for (int i = 0; i < t; i++) {
            shares[i] = "";
            byte[] temp = Base64.getDecoder().decode((String) map.get("share" + i));
            char[] charTemp = new char[temp.length];
            for (int k = 0; k < numOfByte; k++) {
                charTemp[k] = (char) temp[k];
            }
            shares[i] = String.valueOf(charTemp);
        }

        RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t);
        byte[] response = recoverSecretPlus.getSecretEX();

        Map<String, Object> result = new HashMap<>();
        result.put("secret", response);
        return Response.ok(result).build();
    }

}