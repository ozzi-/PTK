# PTK
Phishing Toolkit

```USAGE: java -jar ptk.jar {command}
Commands:
   'simsend'      - simulates the send command by ouputting the recipients and email bodies
   'send'         - sends the emails
   'evaluate'     - evaluates which recipients clicked the link
   'evaluatefull' - evaluates which recipients clicked the link and prints information
```

## Examples

### simsend

```
Sending Mail to oz@ozzi-mail.com>Ozzi with body:
<html>
Dear Ozzi
Please find your invoice here <a href="http://192.168.200.36/phish/log.php?id=b3pAb3otd2ViLmNvbQ%3D%3D">Ozzi_invoice.pdf</a><br>
Phishing for Phish Inc.
<img src="http://192.168.200.36/phish/log.php?pxl=&id=b3pAb3otd2ViLmNvbQ%3D%3D" style="height:1px !important; width:1px !important; border: 0 !important; margin: 0 !important; padding: 0 !important" width="1" height="1" border="0">
</html>

----
Sending Mail to ceo@bigcompany.ch>Mr. Hansjoerg Federli with body:
<html>
Dear Mr. Hansjoerg Federli
please go here <a href="http://192.168.200.36/phish/log.php?id=b3pAb3otd2ViLmNvbQ%3D%3D">IMPORTANT LINK</a>

. . . 

```

### send
```
Preparing to send to 10 recipients
Successfully sent to oz@ozzi-mail.com
Successfully sent to ceo@bigcompany.ch
. . . 
```

### evaluatefull

```
Getting Log from http://192.168.200.36/phish/log.json
3 of 5 read the mail / clicked the link.

                        Target                  Datetime                   IP      Pixel     Clicks
===================================================================================================
             ceo@bigcompany.ch       2018-03-06 15:28:01        192.168.200.1          2          1 

            hans@bigcompany.ch       2018-03-06 15:33:01        192.168.200.12         1          0 

             max@bigcompany.ch       2018-03-06 15:33:01        192.168.200.16         1          0 

```

## Setup
0. Upload log.php to a webserver.
1. Include log.php in your actual phishing page
2. Edit config.properties
3. Create the body file
4. Create the recipient file

### Configuration
The following settings must be configured initially:
```
smtpHost = 127.0.0.1
smtpPort = 25
smtpUser = test1
smtpPassword = lol
from = test1@local.ch
subject = Test
bodyFile = /home/ozzi/body
recipientFile = /home/ozzi/recipient
logURL = http://192.168.200.36/phish/log.json
trackingPixelURL = http://192.168.200.36/phish/log.php
```

### Body File
This files define the template message sent to the recipients.
Note: %name%, %mailaddress% and %trackingpixel% will be replaced when sending.
You can use sendsim to check the results before sending. Those variables are not required.
```
<html>
Dear %name%
Please find your invoice here <a href="http://192.168.200.36/phish/log.php?id=%mailaddress%">%name%_invoice.pdf</a><br>
With kind regards<br>
Phishing for Phish Inc.
%trackingpixel%
</html>
```

### Recipients File
Recipients are stored in a simple fashion. One recipient per line. In order to set the %name% variable, use a ">" character after the email like illustrated below
```
ozzi@ozzimail.ch>Ozzi
ceo@bigcompany.ch>Mr. Hansjoerg Federli
noname@random.com
```
