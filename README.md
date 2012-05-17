# rxlist-forge

JBoss Forge plugin for collect key properties list from source code with regular expressions

## Utility

I find this plugin useful when I'm writing multilingual applications. Normally, I'm writing java or xhtml code and I insert
multilingual code and I don't complete properties files with multilingual keys. It's very tedious write in 2 files 
at both time.

And then, and I must to review all code to get all multilingual keys. Sometimes, I write an application in spanish and 
english, and these file contains different keys or not contains all multilingual keys.

### Use case

I have 2 incomplete multilingual files

messages_en.properties:

    say.hello=Hi!
    say.goodbye=Good bye!
    
messages_es.properties

    say.hello=Hola!
    say.goodbye=Hasta luego!
    say.goodmorning=Buenos dias!
    
Imagine that I'm writing my application with 20 java classes and 30 xhtml files with several multilingual code. I'd like
to recopile all multilingual keys and insert into properties file.

In xhtml files I use this format to insert multilingual code:

    <h:outputText value="#{messages['person.preffix']}, #{user.lastname}"/>
    
And in java files:

    facesmessages.info(messages.getMessage("command.success.message"))
    
Normally, when I write java or xhtml code I don't remember which multilingual keys I used before. Then I can use this
plugin to search and complete multilingual properties files. At this example it's easy to review, but it's more
complicated with big source codes.

### Commands name and parameters

There are only one command in this plugin
	
	collect-keys
	
### Parameters

	--property-files : regular expression to match property files (was --rxProperties)
	
	--source-files : regular expression to match source files to search for message keys (was --rxSearchFiles)
	
	--key-pattern : regular expression for matching a key in a source file (was --regexp)

### Example commands

Update all messages_*.properties files with all found keys in *.java. The last regular expression describe where is
multilingual keys -> messages.getMessage\\(\"(.*?)\"\\). This only get string key

    collect-keys --property-files messages_.*.properties --source-files .*.java --key-pattern messages.getMessage\\(''(.*?)''\\);
    
With example above, this will insert

    command.success.message=>>command.success.message
    
in messages_en.properties and messages_es.properties

We can use this with xhtml file with:

    collect-keys --property-files messages_.*.properties --source-files .*.xhtml --key-pattern messages.getMessage\\('(.*?)'\\)")
    
And this will insert:

    person.preffix=>>person.preffix
    
in messages_en.properties and messages_es.properties

The final properties file, will be:

    say.hello=Hola!
    say.goodbye=Hasta luego!
    say.goodmorning=Buenos dias!
    command.success.message=>>command.success.message
    person.preffix=>>person.preffix
    
## Install

Execute on forge environment:

    forge git-plugin git://github.com/ppalazon/rxlist-forge.git
