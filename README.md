
Mailing list

Default Configuration  : Year - 2014 , <br/>
                         Month - 00,<br/>
                         Logpath (to write .txt files) : users.home/MavneMailingList
</br>
This can be executed in three ways.
<br/>
Method 1. With command line arguments, java -jar path/to/jar/xx.jar  arg1(year) arg2(month) 
//Year should be in yyyy format and months like(01,02,03.. etc) . Both arguments are optional.
Method 2 : a . create file "crawler.properties"  in the jar located directory. Copy and page following three properties.
	   year=2014
	   month=03,04
	   //logpath = default to /home/username/MavneMailingList,Uncomment it and ovverride if required.
          b. run the command  java -jar path/to/jar/xx.jar
Methods 3. a. Import jar into your project.
           b. Invoke "CrawlerProperties.getInstance()" method it will provide instance of "CrawlerPropertiess", change  configuration if required.
	   c. Invoke "downLoadMails()" method of  MailingListDownLoader by passing above instance as parameter.


