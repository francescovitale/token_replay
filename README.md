# TokenReplay
This is a Java implementation of the conformance checking algorithm Token Replay.

To work, the algorithm requires three inputs:

- The Petri net description of the reference process

- A MySQL database compliant with the expected data model

- Events stored in the database for the application of the technique

A sample Petri net description, MySQL database dump, which ships with a collection of events, is provided.
The main method of the Checker class allows executing the algorithm. Diagnostics are written to "Diagnostics/cf_diagnostics.txt" file.
