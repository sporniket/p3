# P3, the Programmable Properties Processor

> [WARNING] Please read carefully this note before using this project. It contains important facts for using this project.

Content

1. What is **P3, the Programmable Properties Processor**, and when to use it ?
2. What should you know before using **P3, the Programmable Properties Processor** ?
3. How to use **P3, the Programmable Properties Processor** ?
4. Known issues
5. Miscellanous


##1. What is **P3, the Programmable Properties Processor**, and when to use it ?
**P3, the Programmable Properties Processor** listen for specific event related with properties file parsing. When recognising a specific property name, it parse the 
value to create other processors to call, and learn which processor to call when a new property event is received.


###Licence
 **P3, the Programmable Properties Processor** is free software: you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 option) any later version.

 **P3, the Programmable Properties Processor** is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 more details.
 
 You should have received a copy of the GNU General Public License along with **P3, the Programmable Properties Processor**.
 If not, see http://www.gnu.org/licenses/ .


##2. What should you know before using **P3, the Programmable Properties Processor** ?
**P3, the Programmable Properties Processor** relies on :

* the `PropertiesParsingListener` class defined in the [sporniket-core](https://github.com/sporniket/core) project
* the description language SSL [Sporny Script Language, Pun Obviously Intended](https://github.com/sporniket/sslpoi).

> Do not use **P3, the Programmable Properties Processor** if this project or one of it's dependencies is not suitable for your project

##3. How to use **P3, the Programmable Properties Processor** ?

###From source
To get the latest available code, one must clone the git repository, build and install to the maven local repository.

	git clone https://github.com/sporniket/p3.git
	cd p3
	mvn install


###Maven
Add the following dependencies to your project.

	<dependency>
	    <groupId>com.sporniket.p3</groupId>
	    <artifactId>p3</artifactId>
	    <version><!-- the version to use --></version>
	</dependency>


##4. Known issues
...


##5. Miscellanous

### Report issues
Use the issue reporting system at the [project page](https://github.com/sporniket/p3)