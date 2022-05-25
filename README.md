# Python toolchain for automated token replay-based conformance checking with timing analysis

This repository groups python scripts for:

- The translation of a BPMN model (specified using a group of files for capturing all dependencies among activities) to a trace-equivalent Petri net 
- The translation of BPMN traces to the corresponding trace-equivalent Petri net
- Recording the extended traces to a MySQL database
- Apply the token replay technique (based on the open-source implementation TokenReplayExtended contained within this repository)

The generation of a trace-equivalent Petri net to a BPMN model is done through the "petri_net_builder.py" script, which works with the "Input" and "Output" directories contained in this branch. Please note the generated Petri net will be put into the "Output/PetriNets" directory. This sub-directory must be put into the "PetriNets" folder for correct traces handling (i.e., the next step this toolchain handles). Please, also note that this script connects to the MySQL database for recording the process name and all of the related activities (BPMN and non-BPMN).
Translation of BPMN traces, recording of the extended traces to a MySQL database, and application of the token replay technique is done through the "traces_handler_extended.py" script, which also works with the "Input" and "Output" directories contained in this branch, and requires an input argument, which is the name of the network (for this release, use "start_of_mission_carmine"). Please note this script will run two jar executables, namely LogExtractor.jar and TokenReplayExtended.jar. This last executable requires the two "Diagnostics" and "PetriNets" contained in this branch. Please, also note that BPMN traces are collected 
from the MySQL database itself (whose schema name is "eventlog_rtis",) which is supposed to have recorded BPMN traces until this script is run.

The BPMN model is contained within the "Input/BPMN" directory. Please, refer to the existing files for correct BPMN model specification.
The extended traces will be recorded within the "Output/ModifiedTraces" directory.


