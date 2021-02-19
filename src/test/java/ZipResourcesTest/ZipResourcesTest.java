package ZipResourcesTest;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.ssss.CD1.ZipResources;

public class ZipResourcesTest {

	int t = 2;
	int n = 3;
	
	//upload the test zip file
	@Test
	public void uploadtheimagefile_getShares() throws IOException{
		InputStream fileInputStream = new FileInputStream("src/test/java/ZipResourcesTest/test_file.zip");
		ZipResources zipresource = new ZipResources();
		Response testresponse = null;
		testresponse = zipresource.getShares(fileInputStream, t, n);
		assert testresponse != null;
		
	}
	
	//upload fail test
	@Test(expected = NullPointerException.class)
	public void uploadfail_getShares(){
		ZipResources zipresource = new ZipResources();
		zipresource.getShares(null, t, n);
		
	}
	
	//test the response
	@Test
	public void checkreturnresponsenumber_getShares() throws IOException {
		FileInputStream fileInputStream = new FileInputStream("src/test/java/ZipResourcesTest/test_file.zip");
		ZipResources zipresource = new ZipResources();
		Response testresponse = null;
		testresponse = zipresource.getShares(fileInputStream, t, n);
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
//		FileInputStream fileInputStream = new FileInputStream("src/test/java/ZipResourcesTest/test_file.zip");
//		byte[] secretByte = IOUtils.toByteArray(fileInputStream);
		ZipResources zipresource = new ZipResources();
//		Response testresponse_encryption = null;
//		testresponse_encryption = zipresource.getShares(fileInputStream, 2, 2);
//		Map<String, Object> messageEntity = (Map<String, Object>) testresponse_encryption.getEntity();
		Map<String, Object > map=  new HashMap<String, Object>();;
	//	share0=AlbLQOKF6w==, share1=AY/Dh19t1Q==}
		
		
		//IF the recovery process is correct, the below shares will be recovered as "123456", we just want to test recovery here
		map.put("share0","AlbLQOKF6w==");
		map.put("share1","AY/Dh19t1Q==");
		
		Response testresponse_recovery = null;
		testresponse_recovery = zipresource.getSecret(2, map);
		String recoveredSecret=new String(((Map<String, byte[]>)testresponse_recovery.getEntity()).get("secret"),StandardCharsets.UTF_8);
		System.out.println("The recovered Zip Secret "+ recoveredSecret);
		
//		Map<String, Object> returnresponse = (Map<String, Object>) testresponse_recovery.getEntity();
//		System.out.println("the size of original file: " + secretByte.toString().length());
//		System.out.println("the return size: " + returnresponse.get("secret").toString().length());
		assertEquals(recoveredSecret,"123456");
		
	}
	
}
