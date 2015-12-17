
Mailing list

Default Configuration  : Year - 2014
                         Month - 00 means all months
                         Logpath(to write .txt files) : users.home/MavneMailingList

This can be executed in two ways.

Method 1. With command line arguments, java -jar path/to/jar/xx.jar  arg1(year) arg2(month) //Year should be in yyyy format and months like(01,02,03.. etc) . Both arguments are optional.

Methods 2. a. Import jar into your project.
           b. Invoke "ConfigProperties.getInstance()" method it will provide instance of "ConfigProperties", change configuration if required.
	   c. Invoke "downLoadMails()" method of  MailingListDownLoader by passing above instance as parameter.


