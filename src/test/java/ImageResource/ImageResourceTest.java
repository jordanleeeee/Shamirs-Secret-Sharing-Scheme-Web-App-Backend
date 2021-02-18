package ImageResource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ssss.CD1.ImageResources;

import ssss.MakeSharePlus;

public class ImageResourceTest {
	
	int t = 2;
	int n = 3;
	
	//upload the test image file
	@Test
	public void uploadtheimagefile_getShares() throws IOException{
		InputStream fileInputStream = new FileInputStream("src/test/java/ImageResource/image.png");
		ImageResources imageresource = new ImageResources();
		Response testresponse = null;
		testresponse = imageresource.getShares(fileInputStream, t, n);
		assert testresponse != null;
		
	}
	
	//upload fail test
	@Test(expected = NullPointerException.class)
	public void uploadfail_getShares(){
		ImageResources imageresource = new ImageResources();
		imageresource.getShares(null, t, n);
		
	}
	
	//test the response
	@Test
	public void checkreturnresponsenumber_getShares() throws IOException {
		FileInputStream fileInputStream = new FileInputStream("src/test/java/ImageResource/image.png");
		ImageResources imageresource = new ImageResources();
		Response testresponse = null;
		testresponse = imageresource.getShares(fileInputStream, t, n);
		int number_of_share = 0;
		
		
		Map<String, Object> messageEntity = (Map<String, Object>) testresponse.getEntity();
		Iterator<?> it = messageEntity.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
	        it.remove();
	        number_of_share++;
	    }

		assertEquals(number_of_share,n);
	}
	
	//test the sucessful upload
	@Test
	public void checkresutnresponsesize_recovery() throws IOException {
		FileInputStream fileInputStream = new FileInputStream("src/test/java/ImageResource/image.png");
		byte[] secretByte = IOUtils.toByteArray(fileInputStream);
		ImageResources imageresource = new ImageResources();
		Response testresponse_encryption = null;
		testresponse_encryption = imageresource.getShares(fileInputStream, 2, 2);
		Map<String, Object> messageEntity = (Map<String, Object>) testresponse_encryption.getEntity();
		
		Response testresponse_recovery = null;
		testresponse_recovery = imageresource.getSecret(2, messageEntity);
		Map<String, Object> returnresponse = (Map<String, Object>) testresponse_recovery.getEntity();
		System.out.println("the size of original file: " + secretByte.toString().length());
		System.out.println("the return size: " + returnresponse.get("secret").toString().length());
		assertEquals(secretByte.toString().length(),returnresponse.get("secret").toString().length());
		
	}
	
}
