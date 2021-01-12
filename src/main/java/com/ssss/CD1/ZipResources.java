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
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	public Response getShares(@FormDataParam("image") InputStream fileInputStream, @PathParam("t") int t,
			@PathParam("n") int n) {

		try {
			System.out.println("You are now in Zip encryption service");
			byte[] secretByte = IOUtils.toByteArray(fileInputStream);

			MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

			byte[][] shares = makeSharePlus.constructPointsEX();

			Map<String, Object> result = new HashMap<String, Object>();
			for (int i = 0; i < n; i++) {
				result.put("share" + i, Base64.getEncoder().encodeToString(shares[i]));
			}
			Response reply = Response.ok(result).build();
			return reply;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/recovery/{t}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSecret(@PathParam("t") int t, Map<String, Object> map) {
		System.out.println("\n\nYou are now in Zip recovery service");
		String[] shares = new String[t];
		int numOfByte = Base64.getDecoder().decode((String) map.get("share" + 0)).length;
		for (int i = 0; i < t; i++) {
			shares[i] = "";
			byte[] temp = Base64.getDecoder().decode((String) map.get("share" + i));

			// below is necessary as the image may contain null so new String cannot use
			for (int k = 0; k < numOfByte; k++) {
				shares[i] += (char) temp[k];
			}
		}

		RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t, 8);

		byte[] response = recoverSecretPlus.getSecretEX();
//		System.out.println("Recovery Result: " + response);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("secret", response);
		Response reply = Response.ok(result).build();
		return reply;
	}

}