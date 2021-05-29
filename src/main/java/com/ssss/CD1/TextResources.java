package com.ssss.CD1;

import java.nio.charset.StandardCharsets;
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

import ssss.MakeSharePlus;
import ssss.RecoverSecretPlus;

@Path("TextApi")
@PermitAll
public class TextResources {

    @POST
    @Path("/encryption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShares(Map<String, Object> map) {
        System.out.println("You are now in text encryption service XD");
        String secret = (String) map.get("secret");
        int n = (int) map.get("totalShare");
        int t = (int) map.get("threshold");

        byte[] secretByte = secret.getBytes();
        MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

        byte[][] shares = makeSharePlus.constructPointsEX();
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put("share" + i, Base64.getEncoder().encodeToString(shares[i]));
        }
        return Response.ok(result).build();
    }

    @POST
    @Path("/recovery/{t}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSecret(Map<String, Object> map, @PathParam("t") int t) {
        System.out.println("You are now in text recovery service XD");
        String[] shares = new String[t];
        for (int i = 0; i < t; i++) {
            shares[i] = "";
            byte[] temp = Base64.getDecoder().decode((String) map.get("share" + i));

            //below is necessary as the image may contain null so new String cannot use
            for (byte b : temp) {
                shares[i] += (char) b;
            }
        }

        RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t);
        String response = new String(recoverSecretPlus.getSecretEX(), StandardCharsets.UTF_8);

        Map<String, Object> result = new HashMap<>();
        result.put("secret", response);
        return Response.ok(result).build();
    }

}
