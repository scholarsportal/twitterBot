package info.scholarsportal.ejournals;
 
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator; 
import java.util.Map;
 

import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
 


public class TwitterBot {

	static String propertyFilePath = "";

	public TwitterBot() throws Exception
	{ 		 
		 
	}

	public static void main(String[] args) throws Exception 
	{
		if (args.length != 1) {
            System.out.println("Usage: ./TwitterBot $1");
            System.out.println("       Where $1 is the property file location eg: /User/abc/twitter4j.properties");
            return;
        }
		
		if (!args[0].endsWith("properties")) {
            System.out.println("Usage: ./TwitterBot $1");
            System.out.println("       Where $1 is the property file location eg: /User/abc/twitter4j.properties");
            return;
		}
		
		TwitterBot tb= new TwitterBot();
		propertyFilePath = args[0];
		String date = tb.getDate();
		tb.getArticleCount(date);
		
	}// end of main
	
	private static String getDate()
	{
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24 ;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String prevDate = dateFormat.format(date.getTime() - MILLIS_IN_DAY);
		String inputDate = prevDate.split(" ")[0];
		return inputDate;
	}
	
	private static void getArticleCount(String inputDate) throws IOException, ParseException {
		
		String get_URL =  " https://journals.scholarsportal.info/search.json?q=((COVID-19)%20OR%20(coronavirus)%20OR%20(2019-nCoV)%20OR%20(SARS-CoV-2))&search_in=anywhere&op=AND&q="+inputDate+"&search_in=LD "; 
		
		URL obj = new URL(get_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET"); 
		int responseCode = con.getResponseCode();
		
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			String numberofArticles =  parseJSON(response.toString());
			
			if( Integer.parseInt(numberofArticles) > 0 )
			{
				try{
					
					String tweetMessage = "NEW COVID RESEARCH AVAILABLE "+ inputDate + "\n"+
							"Read newly added articles related to COVID-19 on Scholars Portal Journals : https://journals.scholarsportal.info/search?q=((COVID-19)%20OR%20(coronavirus)%20OR%20(2019-nCoV)%20OR%20(SARS-CoV-2))&search_in=anywhere&op=AND&q="+inputDate+"&search_in=LD" +
							"\nRead all COVID-19 articles on Scholars Portal Journals: bit.ly/SPJCOVID19" +
							"\n #COVID19 #SARSCoV2";

					String tweetMessageFr = "NOUVELLE RECHERCHE SUR COVID "+ inputDate + "\n"+
							"Lire les articles sur COVID-19 récemment ajoutés à Scholars Portal Périodiques : https://journals.scholarsportal.info/search?q=((COVID-19)%20OR%20(coronavirus)%20OR%20(2019-nCoV)%20OR%20(SARS-CoV-2))&search_in=anywhere&op=AND&q="+inputDate+"&search_in=LD" +
							"\nLire tous les articles sur COVID-19 sur Scholars Portal Périodiques : bit.ly/SPJCOVID19" + 
							"\n #COVID19 #SARSCoV2";


					sendTweet(tweetMessage);
					sendTweet(tweetMessageFr);
					
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
			}
			
			
		} else {
			System.out.println("GET request not worked.  Response code : " + responseCode);
		}

	}
	

	private static void sendTweet(String text)  {
 
		try
		{
			Twitter twitter = getTwitter();
			twitter.updateStatus(text);
			System.out.println("Successfully updated the Tweet to =========["
					+ twitter.getScreenName().toString( )+ "].");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 

	} 
	
	private static Twitter getTwitter() throws IOException, URISyntaxException {
        TwitterCredentials creds = new TwitterCredentials(propertyFilePath);
        creds.configure();
        if (creds.getConsumerKey() == null) {
            throw new RuntimeException("Incorrect Twitter client configuration: Consumer key is null");
        }
        if (creds.getConsumerSecret() == null) {
            throw new RuntimeException("Incorrect Twitter client configuration: Consumer secret is null");
        }
        if (creds.getAccessSecret() == null) {
            throw new RuntimeException("Incorrect Twitter client configuration: Access secret is null");
        }
        if (creds.getAccessToken() == null) {
            throw new RuntimeException("Incorrect Twitter client configuration: Access token is null");
        }
        ConfigurationBuilder twitterConfig = new ConfigurationBuilder();
        twitterConfig.setOAuthConsumerKey(creds.getConsumerKey());
        twitterConfig.setOAuthConsumerSecret(creds.getConsumerSecret());
        twitterConfig.setOAuthAccessToken(creds.getAccessToken());
        twitterConfig.setOAuthAccessTokenSecret(creds.getAccessSecret());
        twitterConfig.setJSONStoreEnabled(true);
        return new TwitterFactory(twitterConfig.build()).getInstance();
    }

   
	
	private static String parseJSON(String inputJSON) throws FileNotFoundException, IOException, ParseException
	{

		String output = "";
		// parsing file  
		Object obj = new JSONParser().parse(inputJSON); 

		// typecasting obj to JSONObject 
		JSONObject jo = (JSONObject) obj; 

		// getting response 
		Map response = ((Map)jo.get("response")); 

		// iterating address Map 
		Iterator<Map.Entry> itr1 = response.entrySet().iterator(); 
		while (itr1.hasNext()) { 
			Map.Entry pair = itr1.next(); 
			if(pair.getKey().equals("total"))
			{
				output =   pair.getValue().toString();
			}
		} 

		return output;

	}


 
}