import mysql.connector
import pandas as pd
import numpy as np
import math
import random
import subprocess
import sys


output_dir = "Output/"
input_dir = "Input/"
bpmn_dir = input_dir+"BPMN/"
petri_net_dir = output_dir+"PetriNets/"
traces_dir = input_dir+"Traces/"
modified_traces_dir = output_dir+"ModifiedTraces/"

petri_net_name = ""
schema_name = ""

def timestamp_builder(number):
	SSS = number
	ss = int(math.floor(SSS/1000))
	mm = int(math.floor(ss/60))
	hh = int(math.floor(mm/24))
	
	SSS = SSS % 1000
	ss = ss%60
	mm = mm%60
	hh = hh%24
	
	return "1970/01/01 "+str(hh)+":"+str(mm)+":"+str(ss)+"."+str(SSS)

def add_to_dictionary(dictionary, key, value):
    try:
        dictionary[key].append(value)
    except KeyError:
        dictionary[key] = [value]
    return dictionary
     


def get_bpmn():
    activities_file = open(bpmn_dir + "activities", "r")
    activities = {}
    read_activities = activities_file.readlines()
    start_activity = ""
    end_activity = ""
    internal_activities = []
    
    
    for read_activity in read_activities:
        tokens = read_activity.split(":")
        activity_class = tokens[0].replace('\n','')
        activity = tokens[1].replace('\n','')
        if(activity_class == "start"):
            start_activity = activity
        elif(activity_class == "ending"):
            end_activity = activity
        elif(activity_class == "internal"):
            internal_activities.append(activity)
    activities["start_activity"] = start_activity
    activities["end_activity"] = end_activity
    activities["internal_activities"] = internal_activities
    activities_file.close()
    
    
    xor_join_relations_file = open(bpmn_dir + "xor_join_relations", "r")
    xor_join_relations = {}
    read_join_relations = xor_join_relations_file.readlines()
    for read_join_relation in read_join_relations:
        xor_join_relation = {}
        precedent_activities = []
        successive_activity = ""
        tokens = read_join_relation.split(":")
        for precedent_activity in tokens[0].split(","):
            precedent_activities.append(precedent_activity.replace('\n',''))
        successive_activity = tokens[1].replace('\n','')
        xor_join_relation["precedent_activities"] = precedent_activities
        xor_join_relation["successive_activity"] = successive_activity
        xor_join_relations[read_join_relation.replace('\n','')] = xor_join_relation
    xor_join_relations_file.close()
    
    
    xor_split_relations_file = open(bpmn_dir + "xor_split_relations", "r")
    xor_split_relations = {}
    read_split_relations = xor_split_relations_file.readlines()
    for read_split_relation in read_split_relations:
        xor_split_relation = {}
        precedent_activity = ""
        successive_activities = []
        tokens = read_split_relation.split(":")
        for successive_activity in tokens[1].split(","):
            successive_activities.append(successive_activity.replace('\n',''))
        precedent_activity = tokens[0].replace('\n','')
        xor_split_relation["precedent_activity"] = precedent_activity
        xor_split_relation["successive_activities"] = successive_activities
        xor_split_relations[read_split_relation.replace('\n','')] = xor_split_relation
    xor_split_relations_file.close()
    
    precedence_relations_file = open(bpmn_dir + "precedence_relations", "r")
    precedence_relations = {}
    read_precedence_relations = precedence_relations_file.readlines()
    for read_precedence_relation in read_precedence_relations:
        precedence_relation = {}
        precedent_activity = ""
        successive_activity = ""
        tokens = read_precedence_relation.split(":")
        precedent_activity = tokens[0].replace('\n','')
        successive_activity = tokens[1].replace('\n','')
        precedence_relation["precedent_activity"] = precedent_activity
        precedence_relation["successive_activity"] = successive_activity
        precedence_relations[read_precedence_relation] = precedence_relation
    precedence_relations_file.close()
    
    return activities, xor_split_relations, xor_join_relations, precedence_relations
    

def find_xor_split_relation(xor_split_relation, modified_trace):
    precedent_activity = xor_split_relation["precedent_activity"]
    successive_activities = xor_split_relation["successive_activities"]

    modified_trace_activities = modified_trace["activities"]
    modified_trace_lifecycle_stages = modified_trace["lifecycle_stages"]
    modified_trace_timestamps = modified_trace["timestamps"]

    for idx,activity in enumerate(modified_trace_activities):
        if activity == precedent_activity:
            if modified_trace_lifecycle_stages[idx] == "end":
                try:
                    successive_activity = successive_activities[successive_activities.index(modified_trace_activities[idx+1])]
                    if modified_trace_lifecycle_stages[idx+1] == "start":
                        artificial_activity_to_insert = "XOR_"+precedent_activity+"_"+successive_activity
                    
                    modified_trace_activities.insert(idx+1,artificial_activity_to_insert)
                    modified_trace_activities.insert(idx+1,artificial_activity_to_insert)
                    modified_trace_timestamps.insert(idx+1,modified_trace_timestamps[idx] + 0.2)
                    modified_trace_timestamps.insert(idx+1,modified_trace_timestamps[idx] + 0.1)
                    modified_trace_lifecycle_stages.insert(idx+1,"end")
                    modified_trace_lifecycle_stages.insert(idx+1,"start")
                    
                except Exception:
                    pass
            
    
    modified_trace["activities"] = modified_trace_activities
    modified_trace["lifecycle_stages"] = modified_trace_lifecycle_stages
    modified_trace["timestamps"] = modified_trace_timestamps

    return modified_trace

def find_xor_join_relation(xor_join_relation, modified_trace):
    precedent_activities = xor_join_relation["precedent_activities"]
    successive_activity = xor_join_relation["successive_activity"]
    
    modified_trace_activities = modified_trace["activities"]
    modified_trace_lifecycle_stages = modified_trace["lifecycle_stages"]
    modified_trace_timestamps = modified_trace["timestamps"]

    for idx,activity in enumerate(modified_trace_activities):
        if activity == successive_activity:
            if modified_trace_lifecycle_stages[idx] == "start":
                try:
                    precedent_activity = precedent_activities[precedent_activities.index(modified_trace_activities[idx-1])]
                    
                    if modified_trace_lifecycle_stages[idx-1] == "end":
                        
                        artificial_activity_to_insert = "XOR_"+precedent_activity+"_"+successive_activity
                    modified_trace_activities.insert(idx,artificial_activity_to_insert)
                    modified_trace_activities.insert(idx,artificial_activity_to_insert)
                    modified_trace_timestamps.insert(idx,modified_trace_timestamps[idx-1] + 0.2)
                    modified_trace_timestamps.insert(idx,modified_trace_timestamps[idx-1] + 0.1)
                    modified_trace_lifecycle_stages.insert(idx,"end")
                    modified_trace_lifecycle_stages.insert(idx,"start")
                except Exception:
                    pass

    modified_trace["activities"] = modified_trace_activities
    modified_trace["lifecycle_stages"] = modified_trace_lifecycle_stages
    modified_trace["timestamps"] = modified_trace_timestamps

    return modified_trace
def modify_trace(xor_split_relations, xor_join_relations, trace):
    modified_trace = trace

    for xor_split_relation_label in xor_split_relations:
        modified_trace = find_xor_split_relation(xor_split_relations[xor_split_relation_label], modified_trace)
    
    for xor_join_relation_label in xor_join_relations:
        modified_trace = find_xor_join_relation(xor_join_relations[xor_join_relation_label], modified_trace)

    return modified_trace

def read_traces(filename):
    traces = {}
    
    read_events = pd.read_csv(traces_dir + filename)
    
    case_ids = list(set(list(read_events["case"].values)))
    for case_id in case_ids:
        events = {}

        activities = []
        lifecycle_stages = []

        trace_mask = read_events["case"] == case_id
        raw_trace = read_events[trace_mask]
        activities = list(raw_trace["event"].values)
        for activity in activities:
            lifecycle_stages.append("start")
            lifecycle_stages.append("end")
        activities = list(np.repeat(raw_trace["event"].values,2))

        events["activities"] = activities
        events["lifecycle_stages"] = lifecycle_stages

        traces[case_id] = events

    return traces

def read_traces_mysql(schema_name):
    eventlog_db = mysql.connector.connect(host="127.0.0.1", user="root", password="root")
    dbcursor = eventlog_db.cursor()

    sql = ("SELECT * FROM " + schema_name +".event")
    traces = {}

    dbcursor.execute(sql)

    event_table = dbcursor.fetchall()

    sql = ("SELECT * FROM " + schema_name +".activityinstance")
    dbcursor.execute(sql)
    activity_instance_table = dbcursor.fetchall()
    
    case_ids = []

    for tuple in event_table:
        try:
            case_ids.index(tuple[-1])
        except ValueError:
            case_ids.append(tuple[-1])
    
    

    for case_id in case_ids:
        events = {}

        activities = []
        lifecycle_stages = []
        timestamps = []


        for event in event_table:
            tuple_case_id = event[-1] 
            timestamp = event[-4]
            lifecycle_stage = event[-3]
            if(tuple_case_id == case_id):
                act_inst_id = event[-2]
                for act_inst in activity_instance_table:
                    reference_activity = act_inst[2]
                    if act_inst[0] == act_inst_id:
                        activities.append(reference_activity)
                lifecycle_stages.append(lifecycle_stage)
                timestamps.append(round(float(timestamp),5))
                #timestamps.append(float(timestamp))
        events["timestamps"] = timestamps
        events["activities"] = activities
        events["lifecycle_stages"] = lifecycle_stages
        traces[case_id] = events


    dbcursor.close()
    eventlog_db.close()

    return traces

def write_traces(traces, output_filename):
    timestamps = []
    activities = []
    lifecycle_stages = []
    case_id_replicas = []
    for case_id in traces:
        case_id_to_insert = case_id
        counter = 0
        for activity in traces[case_id_to_insert]["activities"]:
            activities.append(activity)
            case_id_replicas.append(case_id)
        for timestamp in traces[case_id_to_insert]["timestamps"]:
            timestamps.append(str(timestamp))
        for lifecycle_stage in traces[case_id_to_insert]["lifecycle_stages"]:
            lifecycle_stages.append(lifecycle_stage)
    csv_to_write = pd.DataFrame(columns=["case","event","timestamp","lifecyclestage"])
    csv_to_write["case"] = case_id_replicas
    csv_to_write["event"] = activities
    csv_to_write["timestamp"] = timestamps
    csv_to_write["lifecyclestage"] = lifecycle_stages

    csv_to_write.to_csv(modified_traces_dir+output_filename, index=False, header=False)
        

def get_last_transition():
    f = open(petri_net_dir+"/final_transition","r")
    
    last_transition = f.readlines()


    f.close()

    return last_transition.pop()

def clear_database():
    eventlog_db = mysql.connector.connect(host="127.0.0.1", user="root", password="root")
    dbcursor = eventlog_db.cursor()

    sql = ("DELETE FROM " + schema_name +".event")

    dbcursor.execute(sql)

    sql = ("DELETE FROM " + schema_name +".activityinstance")

    dbcursor.execute(sql)

    sql = ("DELETE FROM " + schema_name +".processinstance")
   
    dbcursor.execute(sql)
    # Commit your changes in the database
    eventlog_db.commit()

    dbcursor.close()
    eventlog_db.close()

def sort_traces(traces_to_sort):
    for trace in traces_to_sort:
        trace_timestamps = traces_to_sort[trace]["timestamps"]
        trace_activities = traces_to_sort[trace]["activities"]
        trace_lifecycle_stages = traces_to_sort[trace]["lifecycle_stages"]
        for i in range(1, len(trace_timestamps)):
  
            timestamp_key = trace_timestamps[i]
            activity_key = trace_activities[i]
            lifecycle_key = trace_lifecycle_stages[i]
      
            j = i-1
            while j >=0 and timestamp_key < trace_timestamps[j] :
                    trace_timestamps[j+1] = trace_timestamps[j]
                    trace_activities[j+1] = trace_activities[j]
                    trace_lifecycle_stages[j+1] = trace_lifecycle_stages[j]
                    j -= 1
            trace_timestamps[j+1] = timestamp_key
            trace_activities[j+1] = activity_key
            trace_lifecycle_stages[j+1] = lifecycle_key

        traces_to_sort[trace]["timestamps"] = trace_timestamps
        traces_to_sort[trace]["activities"] = trace_activities
        traces_to_sort[trace]["lifecycle_stages"] = trace_lifecycle_stages

    return traces_to_sort


try:
    petri_net_name = sys.argv[1]
except IndexError:
    print("Not enough input arguments provided. Please, insert the Petri net name as the first parameter.")
    sys.exit()

schema_name = "eventlog_rtis"
traces_filename = "modified_traces"
activities, xor_split_relations, xor_join_relations, precedence_relations = get_bpmn()

traces = read_traces_mysql(schema_name)
traces = sort_traces(traces)

last_transition = get_last_transition()

modified_traces = {}

for case_id in traces:
    modified_traces[case_id] = modify_trace(xor_split_relations, xor_join_relations, traces[case_id])


output_filename = traces_filename

print(modified_traces)

write_traces(modified_traces, output_filename)


clear_database()

subprocess.call(['C:/Program Files/Java/jdk-17.0.2/bin/java.exe', '-jar', 'LogExtractor.jar', modified_traces_dir+output_filename, petri_net_name])
subprocess.call(['C:/Program Files/Java/jdk-17.0.2/bin/java.exe', '-jar', 'TokenReplayExtended.jar'])

clear_database()
