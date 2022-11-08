WireMock - a web service test double for all occasions
======================================================

Key Features
------------
	
-	HTTP response stubbing, matchable on URL, header and body content patterns
-	Request verification
-	Runs in unit tests, as a standalone process or as a WAR app
-	Configurable via a fluent Java API, JSON files and JSON over HTTP
-	Record/playback of stubs
-	Fault injection
-	Per-request conditional proxying
-	Browser proxying for request inspection and replacement
-	Stateful behaviour simulation
-	Configurable response delays

Full documentation can be found at [wiremock.org](http://wiremock.org/ "wiremock.org")

Additional feature of this fork
------------
 
 -	Support for multiple instances with stub and scenario synchronization
 -	Synced commands
 	-	Stub creation
	-	Stub updated
	-	Stub deletion
	-	Stub state
	-	Stub reset

Performance degradation of this fork
------------

-	Syncrhonization has a performance hit when scernario based stubs are being served making this unsuitable for performance testing
-	Syncrhonization has a performance hit when adding, updating, and deleting stubs via HTTP. Please add stubs via [BPG Wiremock](https://github.com/cwt-dev/bpg-wiremock) repository instead

Questions and Issues
--------------------
If you have a question about WireMock, or are experiencing a problem you're not sure is a bug please post a message to the 
[WireMock mailing list](https://groups.google.com/forum/#!forum/wiremock-user).

On the other hand if you're pretty certain you've found a bug please open an issue.

Contributing
------------
We welcome bug fixes and new features in the form of pull requests. If you'd like to contribute, please be mindful of the
following guidelines:
* All changes should include suitable tests, whether to demonstrate the bug or exercise and document the new feature.
* Please make one change per pull request.
* If the new feature is significantly large/complex/breaks existing behaviour, please first post a summary of your idea
on the mailing list to generate a discussion. This will avoid significant amounts of coding time spent on changes that ultimately get rejected.
* Try to avoid reformats of files that change the indentation, tabs to spaces etc., as this makes reviewing diffs much
more difficult.
* Abide by [the Architecture Rules](https://github.com/wiremock/wiremock/tree/master/src/test/java/com/github/tomakehurst/wiremock/archunit) enforced by ArchUnit.

Building WireMock locally
-------------------------
To run all of WireMock's tests:
```bash
./gradlew clean test
```

To build both JARs (thin and standalone):
```bash
./gradlew jar shadowJar 
```

The built JAR will be placed under ``build/libs``.

To publish both JARs to Nexus:

```bash
./gradlew publish
```

