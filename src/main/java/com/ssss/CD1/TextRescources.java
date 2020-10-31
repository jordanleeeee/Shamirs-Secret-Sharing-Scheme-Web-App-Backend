package com.ssss.CD1;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

import ssss.MakeSharePlus;
import ssss.RecoverSecretPlus;

@Path("TextApi")
@PermitAll
public class TextRescources {
	
	@POST
	@Path("/encryption")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShares ( Map<String, Object> map){
		System.out.println("You are now in text encryption service");
		System.out.println("map content: "+map);
		
		
		// I can input Chinese and special symbols now :)
//		String secret = "✌ ★ The Hong Kong University of Single and Toxic (香港單身毒男大學) ★ ✌";
//		int n = 8;
//		int t = 3;
		
		String secret=(String) map.get("secret");
		int n=(int) map.get("totalShare");
		int t=(int) map.get("threshold");
		
		System.out.println("Secret: " + secret+ "\n");
		byte[] secretByte = secret.getBytes();
		MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);
		
		String[] shares = makeSharePlus.constructPoints();
		System.out.println("points(shares): " + Arrays.deepToString(shares) + "\n");

		
		Map<String, Object> result = new HashMap<String,Object>();
		for(int i=0;i<n;i++)
		{
			result.put("share"+i,shares[i] );
		}
		System.out.print(result);
		Response reply = Response.ok(result).build();
		return reply;
	}
	
	
	
	@POST
	@Path("/recovery/{t}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSecret ( Map<String, Object> map,@PathParam("t") int t){
		System.out.println("You are now in text recovery service");
		System.out.println("t: "+t+"map content: "+map);
		String []shares=new String[t];
		
		for(int i=0;i<t;i++)
		{
			shares[i]=(String) map.get("share"+i);
			System.out.println(i+": "+shares[i]);
		}

		RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t,8);
		
		String response=new String(recoverSecretPlus.getSecret(), StandardCharsets.UTF_8);
		System.out.println("Recovery Result: " + response);
		
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("secret",response );
		System.out.print(result);
		Response reply = Response.ok(result).build();
		return reply;
	}

}
