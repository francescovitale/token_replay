# TokenReplay
This is a Java implementation of the conformance checking algorithm "Token Replay".

To work, the algorithm requires three inputs:

- The Petri net description of the reference process

- A MySQL database compliant with the expected data model

- Events stored in the database for the application of the technique

A sample Petri net description and a MySQL database dump, which ships with a collection of events, are provided.
The main method of the Checker class allows executing the algorithm. 
Diagnostics (the output of the algorithm) are written to "Diagnostics/cf_diagnostics.txt" file. Diagnostics provide:

- The fitness of the replayed event log

- The activities that highlighted anomalies throughout the event log replay
