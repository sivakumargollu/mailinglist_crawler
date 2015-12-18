
This can be executed in three ways.
<br/>
Method 1. With command line arguments, java -jar path/to/jar/xx.jar  arg1(year) arg2(month) 
<br/>
Year should be in yyyy format and months like(01,02,03.. etc) . Both arguments are optional.
<br/><br/>
Method 2 : a . create file "crawler.properties"  in the jar located directory. Copy and paste the following three properties.(Modify if required)<br/>
	   year=2014<br/>
	   month=01,02,03<br/>
	   //logpath = default to /home/username/MavneMailingList,Uncomment it and ovverride if required.<br/>
          b. run the command  java -jar path/to/jar/xx.jar<br/>
          <br/><br/>
Methods 3. a. Import jar into your project.<br/>
           b. Invoke "CrawlerProperties.getInstance()" method it will provide instance of "CrawlerPropertiess", change  configuration if required.<br/>
	   c. Invoke "downLoadMails()" method of  MailingListDownLoader by passing above instance as parameter.<br/>
