# TokenReplayExtended
This is a Java implementation of the conformance checking algorithm "Token Replay", extended to work with basic lifecycle stages linked to events.

To work, the algorithm requires three inputs:

- The Petri net description of the reference process (each description is made of a collection of files, each of which are to be placed in a dedicated directory, whose name will be the one the process will be recognized with by the algorithm, under the path "PetriNets/")

- A MySQL database with a schema named "eventlog_rtis", whose data model must be compliant with the one engineered for the algorithm to work (made of the tables "process", "activity", "process_instance", "activity_instance", "event", refer to the database dump contained under the path "dbdumps/" for more information)

- Events stored in the database - in the "event" table - for the application of the technique

A sample Petri net description, and a MySQL database dump, which ships with a collection of events, are provided.
The main method of the Checker class allows executing the algorithm. 
Diagnostics (the output of the algorithm) are written to the two "Diagnostics/cf_diagnostics.txt" and "Diagnostics/perf_diagnostics.txt" files. "cf_diagnostics.txt" provide:

- The fitness of the replayed event log

- The activities that highlighted anomalies throughout the event log replay

"perf_diagnostics.txt" provide:

- Trace-level timing statistics of the replayed event log
  - ACET (Average-Case Execution Time)
  - BCET (Best-Case Execution Time)
  - WCET (Worst-Case Execution Time)
- Transition-level timing statistics of the replayed event log
  - ACET
  - BCET
  - WCET


