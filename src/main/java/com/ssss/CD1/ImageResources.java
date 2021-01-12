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

			MakeSharePlus makeSharePlus = new MakeSharePlus(secretByte, t, n, 8);

			byte[][] shares = makeSharePlus.constructPointsEX();

			Map<String, Object> result = new HashMap<String, Object>();
			for (int i = 0; i < n; i++) {
				result.put("share" + i, Base64.getEncoder().encodeToString(drawImage(shares[i])));
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
		System.out.println("\n\nYou are now in image recovery service");
		String[] shares = new String[t];
		for (int i = 0; i < t; i++) {
			shares[i] = "";
			// reverse engineering
			ByteArrayInputStream income = new ByteArrayInputStream(
					Base64.getDecoder().decode((String) map.get("share" + i)));
			BufferedImage receivedImage;
			try {
				receivedImage = ImageIO.read(income);
				int height = receivedImage.getHeight();
				int width = receivedImage.getWidth();

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						shares[i] += (char) receivedImage.getRGB(x, y);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		RecoverSecretPlus recoverSecretPlus = new RecoverSecretPlus(shares, t, 8);

		byte[] response = recoverSecretPlus.getSecretEX();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("secret", response);
		Response reply = Response.ok(result).build();
		return reply;
	}

	// part of the idea for image drawing from
	// https://dyclassroom.com/image-processing-project/how-to-create-a-random-pixel-image-in-java,
	// by Yusuf Shakeel
	// The size of the image we draw here is not the size of the original image,
	// because we are using bytes as the original image is stored in bytes
	// but the thing is that the original image doesn't store a pixel in a byte, but
	// usually
	// much more,
	// this will result our image product much smaller than the original image
	// However, we know a standard png image usually up to 3 bytes per pixel, which
	// means our product image can have more bytes in size even it looks smaller in
	// width and height
	// new update here is that, we dont actually need a extra row and column to
	// store the x,
	// just add one more pixel is enough and the worst case is still the same, which
	// the share.length + 1 is a prime number,
	// then our product image is then in share.length +1 in width and 1 in height
	byte[] drawImage(byte[] share) {
		System.out.println("the length: " + share.length);
		int[] roots = new int[2];
		roots = closest_roots(share.length);
		int x = roots[0];
		int y = roots[1];
		int i = 0;
		BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB); // +1 to save x, I know it
																				   // duplicated the space

		for (int height = 0; height < y; height++) {
			for (int width = 0; width < x; width++) {
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
		int first_num = (int) Math.ceil(Math.sqrt(length));
		while (length % first_num != 0) {
			first_num++;
		}
		return new int[] {first_num, length / first_num};
	}

}
