package info.scholarsportal.ejournals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException; 
import java.net.URISyntaxException;
import java.util.Properties;


public class TwitterCredentials {

	static final String CONSUMERKEY = "CONSUMERKEY";
	static final String CONSUMERSECRET = "CONSUMERSECRET";
	static final String ACCESSTOKEN = "ACCESSTOKEN";
	static final String ACCESSSECRET = "ACCESSSECRET"; 


	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessSecret;

	protected Properties defaultProps = new Properties();
	protected Properties properties;

	public TwitterCredentials(String propertyFilePath) throws IOException
	{
		if (new File(propertyFilePath).exists())
		{
			defaultProps.load(new FileInputStream(propertyFilePath));
			properties = new Properties(defaultProps);
		}
		else
		{
			System.out.println(" Property File doesnot exists " + new File(propertyFilePath).getAbsolutePath());
		}
	}

	public void configure() throws URISyntaxException 
	{
		consumerKey = properties.getProperty(CONSUMERKEY).trim();
		consumerSecret = properties.getProperty(CONSUMERSECRET).trim();
		accessToken = properties.getProperty(ACCESSTOKEN).trim();
		accessSecret = properties.getProperty(ACCESSSECRET).trim(); 
	}


	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String key) {
		this.consumerKey = key;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String secret) {
		this.consumerSecret = secret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
}