# rxlist-forge


JBoss Forge plugin for recopilate key properties list from source code with regular expressions

## Utilite

I find this useful when I'm writing multilingual applications. Normally, I'm writing java or xhtml code and I insert
multilingual code and I don't complete properties files with multilingual keys. It's very tedious write in 2 files 
at both time.

And then, when the application is running I see multilingual keys, and I must to review all code to get all multilingual
keys. Sometimes, I write an application in spanish and english, and these file contains different keys or not contains all
multilingual keys.

### Use case

I have 2 multilingual files (messages_en.properties and messages_es.properties)

messages_en.properties:
    say.hello=Hi!
    say.goodbye=Good bye!