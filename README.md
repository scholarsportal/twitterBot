To add tweets automatically based on Journals Covid Search results to your twitter account

Steps to be followed:

  Apply for a developer account from your twitter account and create a Twitter app(https://learn.g2.com/how-to-make-a-twitter-bot).

  Install JAVA

  Download the twitterBot project from git

  Unpack the zip file - this will create the directory 'twitterBot'

  Add Twitter app’s API keys and access tokens to twitter4j.properties

  Run the program to add tweet:
  java -cp ./:lib/twitter4j-core-4.0.7.jar:lib/json-simple-1.1.jar info.scholarsportal.ejournals.TwitterBot twitter4j.properties
