package com.ssss.CD1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.apache.commons.io.IOUtils;

import ssss.MakeSharePlus;
import ssss.RecoverSecretPlus;

@Path("ImageApi")
@PermitAll
public class ImageResources {

	@POST
	@Path("/encryption/{n}/{t}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	public Response getShares(@FormDataParam("image") InputStream fileInputStream,@PathParam("t") int t,@PathParam("n") int n) {

		try {
			System.out.println("You are now in Image encryption service");
			byte[] temp = IOUtils.toByteArray(fileInputStream);

			byte[] secretByte = Arrays.copyOfRange(temp, 0, temp.length - 1);
//			System.out.println("secret: "+Arrays.toString(secretByte));
			MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

			String[] shares = makeSharePlus.constructPoints();
			System.out.println("points(shares): " + Arrays.deepToString(shares) + "\n");

			// I still cannot open the image even I do this>.<
//			int numOfByte = shares[0].split("-").length;
//			byte[][] secretEncrypt = new byte[n][numOfByte - 1];// 1st is not share
//			String[][] sharePerByte = new String[n][numOfByte];
//			for (int i = 0; i < n; i++) {
//				sharePerByte[i] = shares[i].split("-");
//				System.out.println( Arrays.deepToString(sharePerByte[i]));
//			}
//			
//			for (int k = 1; k < numOfByte; k++) {
//				for (int i = 0; i < n; i++) {
//					secretEncrypt[i][k-1] = (byte)(Integer.parseInt(sharePerByte[i][k]));
//				}
//			}

			Map<String, Object> result = new HashMap<String, Object>();
			for (int i = 0; i < n; i++) {
//				result.put("share"+i,Base64.getEncoder().encodeToString(secretEncrypt[i]) );
				result.put("share" + i, shares[i]);
			}
			System.out.print(result);
			Response reply = Response.ok(result).build();
			return reply;

		} catch (IOException e) {

			e.printStackTrace();
		}

		return null;
	}

	@POST
	@Path("/recovery/{t}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSecret(@PathParam("t") int t, Map<String, Object> map) {
		System.out.println("You are now in text recovery service");
		System.out.println("t: " + t + "map content: " + map);
		String[] shares = new String[t];

		for (int i = 0; i < t; i++) {
			shares[i] = (String) map.get("share" + i);
		}

		RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t, 8);

		byte[] response = recoverSecretPlus.getSecret();
		System.out.println("Recovery Result: " + response);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("secret", response);
		System.out.print(result);
		Response reply = Response.ok(result).build();
		return reply;
	}

}
