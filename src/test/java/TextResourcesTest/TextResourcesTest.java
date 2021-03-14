package TextResourcesTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ssss.CD1.TextRescources;


public class TextResourcesTest {
	
	int n = 3;
	int t = 2;
	
	@Test
	public void testuploadtextfile_getShares() {
		Map<String, Object> inputobject = new HashMap<String, Object>();
		inputobject.put("secret", "testing");
		inputobject.put("totalShare",n);
		inputobject.put("threshold" ,t);
		
		TextRescources test = new TextRescources();
		Response returnresponse = null;
		returnresponse = test.getShares(inputobject);
		assert returnresponse != null;
	}
	
	@Test(expected = NullPointerException.class)
	public void testuploadtextfilefail_getShares() {
		Map<String, Object> inputobject = null;
		TextRescources test = new TextRescources();
		Response returnResponse = null;
		returnResponse = test.getShares(inputobject);
		
	}
	
	@Test
	public void checkreturnresponsenumber_getShares() throws IOException {
		Map<String, Object> inputobject = new HashMap<String, Object>();
		inputobject.put("secret", "testing");
		inputobject.put("totalShare",n);
		inputobject.put("threshold" ,t);
		TextRescources test = new TextRescources();
		Response returnresponse = null;
		returnresponse = test.getShares(inputobject);
		int number_of_share = 0;
		
		
		Map<String, Object> messageEntity = (Map<String, Object>) returnresponse.getEntity();
		Iterator<?> it = messageEntity.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
	        it.remove();
	        number_of_share++;
	    }

		assertEquals(number_of_share,n);
	}
	
	//test the successful upload
		@Test
		public void checkresutnresponsesize_recovery() throws IOException {
			Map<String, Object> inputobject = new HashMap<String, Object>();
			String secretByte = "testing";
			inputobject.put("secret", secretByte);
			inputobject.put("totalShare",t);
			inputobject.put("threshold" ,t);
			TextRescources test = new TextRescources();
			Response returnresponse_encryption = null;
			returnresponse_encryption = test.getShares(inputobject);
			Map<String, Object> messageEntity = (Map<String, Object>) returnresponse_encryption.getEntity();
			
			Response testresponse_recovery = null;
			testresponse_recovery = test.getSecret(messageEntity,t);
			Map<String, Object> returnresponse = (Map<String, Object>) testresponse_recovery.getEntity();
			System.out.println("the size of original file: " + secretByte.toString().length());
			System.out.println("the return size: " + returnresponse.get("secret").toString().length());
			assertEquals(secretByte.toString(),returnresponse.get("secret").toString());
			
		}
}
