package com.ssss.CD1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.imageio.ImageIO;
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

@Path("ImageApi")
@PermitAll
public class ImageResources {

	@POST
	@Path("/encryption/{n}/{t}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	public Response getShares(@FormDataParam("image") InputStream fileInputStream, @PathParam("t") int t,
			@PathParam("n") int n) {

		try {
			System.out.println("You are now in Image encryption service");
			byte[] secretByte = IOUtils.toByteArray(fileInputStream);

//			byte[] secretByte = Arrays.copyOfRange(temp, 0, temp.length - 1);
//			System.out.println("secret: "+Arrays.toString(secretByte));
			MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

//			String[] shares = makeSharePlus.constructPoints();

			byte[][] shares = makeSharePlus.constructPointsEX();
//			System.out.println("points(shares): " + Arrays.deepToString(shares) + "\n");

//			// this is temporary only, because I am lazy and want to test only, you should add another function is SSSS to do this, not here. But leave to later/but still O(n) anyway.   
//			int numOfByte = shares[0].split("-").length;
//			byte[][] secretEncrypt = new byte[n][numOfByte ];// 1st is not share
//			String[][] sharePerByte = new String[n][numOfByte];
//			for (int i = 0; i < n; i++) {
//				sharePerByte[i] = shares[i].split("-");
//				System.out.println( Arrays.deepToString(sharePerByte[i]));
//			}
//			
//			for (int k = 0; k < numOfByte; k++) {
//				for (int i = 0; i < n; i++) {
//					secretEncrypt[i][k] = (byte)(Integer.parseInt(sharePerByte[i][k]));
//				}
//			}

			Map<String, Object> result = new HashMap<String, Object>();
			for (int i = 0; i < n; i++) {
//				System.out.println(shares[i]);
//				byte[] tempReuslt = (shares[i]);
//				System.out.println("tempReuslt " + tempReuslt.length + ": ");
//				for (int pp = 0; pp < tempReuslt.length; pp++) {
//					System.out.print((int) tempReuslt[pp] + " ");
//				}

				result.put("share" + i, Base64.getEncoder().encodeToString(drawImage(shares[i])));
//				result.put("share" + i, shares[i]);
			}
//			System.out.print(result);
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
		System.out.println("\n\nYou are now in image recovery service");
//		System.out.println("t: " + t + "map content: " + map);
		String[] shares = new String[t];
//		int numOfByte = Base64.getDecoder().decode((String) map.get("share" + 0)).length;
		for (int i = 0; i < t; i++) {
			shares[i] = "";
//			byte[] temp = Base64.getDecoder().decode((String) map.get("share" + i));
//			System.out.println("temp"+temp.length);

//			System.out.println("Receive: ");
			// below is necessary as the image may contain null so new String cannot use
//			for (int k = 0; k < numOfByte; k++) {
//				shares[i] += (char) temp[k];
//
//				System.out.print((int) ((char) temp[k]) + " ");
//
//			}

			// reverse engineering
			ByteArrayInputStream income = new ByteArrayInputStream(
					Base64.getDecoder().decode((String) map.get("share" + i)));
			BufferedImage receivedImage;
			try {
				
				receivedImage = ImageIO.read(income);

				int height = receivedImage.getHeight();
				int width = receivedImage.getWidth();
				int length =height*width;
				char[] charTemp=new char [length];
				int k=0;

				shares[i]=String.valueOf(charTemp);
//
//				System.out.println("width: " + width + " height: " + height);
//				System.out.println();

//				byte[] reverseResult = new byte[(width - 1) * (height - 1) + 1];
//				int counter = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
//						if (x == 0 && y == 0) {
////							System.out.print(receivedImage.getRGB(0, 0)+" ");
//							shares[i] += (char)receivedImage.getRGB(0, 0);
//						} else if (x == 0 || y == 0) {
//							continue;
//						}
//						else {
////							System.out.print(receivedImage.getRGB(x, y)+" ");
//							shares[i] += (char) receivedImage.getRGB(x, y);
////							System.out.println((int)bufferedImage.getRGB(x, y)+ " ");
//						}
						// we can now use only one pixel to store x, no need above line anymore
						charTemp[k++]= (char) receivedImage.getRGB(x, y);
					}
				}
				shares[i]=String.valueOf(charTemp);
//				System.out.println(shares[i].length());
//				System.out.println(length);

//				System.out.println(shares[i]);

			} catch (IOException e) {

				e.printStackTrace();
			}

//			System.out.println();
//			System.out.println("shares[i]"+shares[i].length());

		}
//		byte[][] charBuffer = new byte[t][((String)map.get("share0" )).length()];

//		for (int i = 0; i < t; i++) {
////			shares[i] = (String) map.get("share" + i);
//			
//			
//			// this is temporary only, because I am lazy and want to test only, you should add another function is SSSS to do this, not here. But leave to later/but still O(n) anyway. 
//			charBuffer[i]=Base64.getDecoder().decode((String) map.get("share" + i));
//			shares[i]="";
//			for(int k=0;k<charBuffer[0].length;k++)
//			{
//				shares[i]+=(((int)charBuffer[i][k])&0xff)+"-";
//			}
//			System.out.print(shares[i]);
//	
//		}

		RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t, 8);

		byte[] response = recoverSecretPlus.getSecretEX();
//		System.out.println("Recovery Result: " + response);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("secret", response);
//		System.out.print(result);
		Response reply = Response.ok(result).build();
		return reply;
	}

	// part of the idea for image drawing from
	// https://dyclassroom.com/image-processing-project/how-to-create-a-random-pixel-image-in-java,
	// by Yusuf Shakeel
	// The size of the image we draw here is not the size of the original image,
	// because we are using bytes as the original image is stored in bytes
	// but the thing is that the original image doesn't store a pixel in a byte, but usually
	// much more,
	// this will result our image product much smaller than the original image
	//However, we know a standard png image usually up to 3 bytes per pixel, which means our product image can have more bytes in size even it looks smaller in width and height
	//new update here is that, we dont actually need a extra row and column to store the x,
	//just add one more pixel is enough and the worst case is still the same, which the share.length + 1 is a prime number,
	//then our product image is then in share.length +1 in width and 1 in height 
	byte[] drawImage(byte[] share) {
//		byte[] trier = { 5, 6, 7, 18 };
//		share = trier;
//		System.out.println("the first one: " + (int) share[0]);
//		System.out.println("the second one: " + (int) share[1]);
//		System.out.println("the third one: " + (int) share[2]);
		System.out.println("the length: " + share.length);
		int[] roots = new int[2];
		roots = closest_roots(share.length);
		int x = roots[0];
		int y = roots[1];
//		System.out.println("first root: " + x + " second root: " + y);
		int i = 0;
//		int x=30;
//		int y=30;
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB); // +1 to save x, I know it
																					// duplicated the space

		for (int height = 0; height < y; height++) {
			for (int width = 0; width < x; width++) {
//				System.out.println("width: "+width+" height: "+height);
//				if (width == 0 || height == 0) {
//					image.setRGB(width, height, (int) share[0]); // we waste the first column and row to store x, can be
//																	// improved latter, lazy now
//				} else {
//					image.setRGB(width, height, (int) share[++i]);
////					System.out.print("x: "+width+" y: "+height+ " value: "+ image.getRGB(width, height));
//				}

				// we can actually a single pixel to store x
				image.setRGB(width, height, (int) share[i++]);
			}
		}
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", result); // we stick to png as png is a lossless format although the size is
													// big, but is the most suitable for our project to store bytes
		} catch (IOException e) {

			e.printStackTrace();
		}
		return result.toByteArray();
	}

	// find two closest roots for a given image size
	// it cannot provide a perfect square always, as we need to find two value that
	// value 1 x value = the length, not always get a good solution, especially for
	// a prime value..
	int[] closest_roots(int length) {
		int[] results = new int[2];
		int first_num = (int) Math.ceil(Math.sqrt(length));
		while (true) {

			if (length % first_num == 0) {
				results[0] = first_num;
				results[1] = length / first_num;

				return results;
			} else {
				first_num++;
			}
		}

	}

}
