import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Request format: PROTOCOL_VERSION##ACTION##[PARAMETERS]
 *
 */
public class StfmpRequest {
	
	private String protocolVersion;
	private String action;
	private List<String> params;
	
	public StfmpRequest(String protocolVersion, String action, List<String> params) {
		super();
		this.protocolVersion = protocolVersion;
		this.action = action;
		this.params = params;
	}
	public String getProtocolVersion() {
		return protocolVersion;
	}
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	public String toRawString() {
		String paramsString = "";
		if(params != null) {
			//for(String param : params) {
			//	paramsString += param + "#";
			//}
			for(int i=0; i<params.size();i++) {
				if(i == (params.size()-1)) {
					paramsString += params.get(i);
				}else {
					paramsString += params.get(i) + "#";
				}
			}
		}
		return protocolVersion + "##" + action + "##" + paramsString + "\r\n";
		
	}
	
	
	
	public static StfmpRequest fromRawString(String rawString) {
		String[] parts = rawString.split("##");
		String protocolVersion = parts[0];
		String action = parts[1];
		List<String> params = null;
		if(parts.length == 3) {
			String paramsString = parts[2];
			if(!paramsString.isEmpty()) {
				params = new ArrayList<>();
				if(paramsString.contains("#")) {
					String[] paramParts = paramsString.split("#");
					for(String paramPart : paramParts) {
						String readyParam = paramPart.replace("#", "").replace("\r\n", "");
						params.add(readyParam);
					}
				}else {
					String para = paramsString.replace("\r\n", "");
					params.add(para);
				}
				
				
				
			}
		}
		return new StfmpRequest(protocolVersion, action, params);
	}
	
}