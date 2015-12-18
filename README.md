
Mailing list

Default Configuration  : Year - 2014
                         Month - 00 means all months
                         Logpath(to write .txt files) : users.home/MavneMailingList

This can be executed in three ways.



Method 1. With command line arguments, java -jar path/to/jar/xx.jar  arg1(year) arg2(month) //Year should be in yyyy format and months like(01,02,03.. etc) . Both arguments are optional.

Method 2 : a . create file "crawler.properties"  at jars home folder. 
	   #year(yyyy format)
	   year=2014
		#month(mm format in comma seperated,00 for entire year)
	   month=03,04
		#logpath,should be writbable 
		//logpath = default to /home/username/MavneMailingList,Uncomment it and ovverride if required.
          b. run the command

Methods 3. a. Import jar into your project.
           b. Invoke "ConfigProperties.getInstance()" method it will provide instance of "ConfigProperties", change configuration if required.
	   c. Invoke "downLoadMails()" method of  MailingListDownLoader by passing above instance as parameter.


