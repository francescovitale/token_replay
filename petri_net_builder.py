import mysql.connector
import pandas as pd
import numpy as np
import math
import random

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
    

def bpmn_to_petri_net(activities, xor_split_relations, xor_join_relations, precedence_relations):
    nominal_transitions = []
    artificial_transitions = []
    places = []
    PT = {}
    TP = {}
    final_transition = activities["end_activity"]
    initial_marking = ["start"]
    petri_net = {}
    
    # 1st step: create all the nominal transitions
    
    nominal_transitions.append(activities["start_activity"])
    nominal_transitions.append(activities["end_activity"])
    for activity in activities["internal_activities"]:
        nominal_transitions.append(activity)
    
    # 2nd step: create the start and end places, and connect them to the start and end activities
    
    places.append("start")
    places.append("end")
    add_to_dictionary(PT,"start",activities["start_activity"])
    add_to_dictionary(TP,activities["end_activity"],"end")
    
    # 3rd step: for each of the internal activities, create a precedent place, and connect it to the correspondent activity
    
    for nominal_transition in activities["internal_activities"]:
        place_to_append = "prec_" + nominal_transition
        places.append(place_to_append)
        add_to_dictionary(PT,place_to_append, nominal_transition)
    place_to_append = "prec_" + activities["end_activity"]
    places.append(place_to_append)
    add_to_dictionary(PT,place_to_append, activities["end_activity"])
        
    # 4th step: for each precedence relation, connect the transition to the corresponding place of the relation
    
    
    for precedence_relation_label in precedence_relations:
        precedent_activity = precedence_relations[precedence_relation_label]["precedent_activity"]
        successive_activity = precedence_relations[precedence_relation_label]["successive_activity"]
        place_to_connect = "prec_" + successive_activity
        add_to_dictionary(TP,precedent_activity,place_to_connect)
    
    # 5th step: for each XOR split, create a place for the precedent activity (with the corresponding TP connection), then two transitions linked to the created place (with the cooresponding PT connections), and connect these two transitions 
    # to the correspondent places of the successive transitions
    
    
    for xor_split_relation_label in xor_split_relations:
        precedent_activity = xor_split_relations[xor_split_relation_label]["precedent_activity"]
        successive_activities = xor_split_relations[xor_split_relation_label]["successive_activities"]
        
        place_to_append = "split_" + precedent_activity
        for successive_activity in successive_activities:
            place_to_append = place_to_append + "_" + successive_activity
        places.append(place_to_append)
        add_to_dictionary(TP,precedent_activity,place_to_append)
        
        for successive_activity in successive_activities:
            artificial_activity = "XOR_"+precedent_activity+"_"+successive_activity
            artificial_transitions.append(artificial_activity)
            add_to_dictionary(PT,place_to_append,artificial_activity)
            if(successive_activity != activities["start_activity"]):
                place_to_connect = "prec_"+successive_activity
            else:
                place_to_connect = "start"
            add_to_dictionary(TP,artificial_activity,place_to_connect)
    
    # 6th step: for each XOR join, create a place for each precedent transition (with the corresponding TP connection), then a transition for each of the created place (with the corresponding PT connections), and connect these transitions with the place of the successive activity
    
    for xor_join_relation_label in xor_join_relations:
        successive_activity = xor_join_relations[xor_join_relation_label]["successive_activity"]
        precedent_activities = xor_join_relations[xor_join_relation_label]["precedent_activities"]
        
        for precedent_activity in precedent_activities:
            place_to_append = "join_"+precedent_activity+"_"+successive_activity
            places.append(place_to_append)
            add_to_dictionary(TP,precedent_activity,place_to_append)
            artificial_activity = "XOR_"+precedent_activity+"_"+successive_activity
            artificial_transitions.append(artificial_activity)
            add_to_dictionary(PT,place_to_append,artificial_activity)
            place_to_connect = "prec_"+successive_activity
            add_to_dictionary(TP,artificial_activity,place_to_connect)
     
    '''
    print(places)
    print(nominal_transitions)
    print(artificial_transitions)
    print(PT)
    print(TP)
    '''
    

    petri_net["nominal_transitions"] = nominal_transitions
    petri_net["artificial_transitions"] = artificial_transitions
    petri_net["places"] = places
    petri_net["PT"] = PT
    petri_net["TP"] = TP
    petri_net["start_transition"] = activities["start_activity"]
    petri_net["final_transition"] = final_transition
    petri_net["initial_marking"] = initial_marking
    
    return petri_net
def write_petri_net(name,petri_net):
    transitions_filename = "transitions"
    places_filename = "places"
    TP_filename = "TP.csv"
    PT_filename = "PT.csv"
    final_transition_filename = "final_transition"
    initial_marking_filename = "default_marking"
    
    # write the places
    
    f = open(petri_net_dir+places_filename, "w")
    for idx,place in enumerate(petri_net["places"]):
        if(idx < len(petri_net["places"])-1):
            f.write(place + "\n")
        else:
            f.write(place)
    f.close()
    
    f = open(petri_net_dir+transitions_filename,"w")
    for transition in petri_net["nominal_transitions"]:
        f.write(transition + "\n")
    for idx,transition in enumerate(petri_net["artificial_transitions"]):
        if(idx < len(petri_net["artificial_transitions"])-1):
            f.write(transition + "\n")
        else:
            f.write(transition)
    f.close()
    # write the final transition

    f = open(petri_net_dir + final_transition_filename, "w")
    
    final_transition = petri_net["final_transition"]
    f.write(final_transition)
    
    f.close()
      
    # write the PT structure

    f = open(petri_net_dir + PT_filename, "w")
    
    PT = {}
    
    
    for place in petri_net["places"]:
        PT[place] = {}
        for transition in petri_net["nominal_transitions"]:
            PT[place][transition] = 0
        for transition in petri_net["artificial_transitions"]:
            PT[place][transition] = 0
    

    for place in petri_net["places"]:
        try:
            place_to_transition_list = petri_net["PT"][place]
            for transition in place_to_transition_list:
                PT[place][transition] = 1
        except KeyError:
            for transition in place_to_transition_list:
                PT[place][transition] = 0
            pass

    f.write(";")
    for idx,transition in enumerate(PT[place]):
        if(idx<len(PT[place])-1):
            f.write(transition + ";")
        else:
            f.write(transition)
    f.write("\n")
    for place in PT:
        f.write(place + ";")
        for transition in PT[place]:
            f.write(str(PT[place][transition]) + ";")
        f.write("\n")
        
    f.close()

    
    
    # write the TP structure
    
    f = open(petri_net_dir + TP_filename, "w")
    
    TP = {}
    
    for place in petri_net["places"]:
        TP[place] = {}
        for transition in petri_net["nominal_transitions"]:
            TP[place][transition] = 0
        for transition in petri_net["artificial_transitions"]:
            TP[place][transition] = 0
    
    for transition in petri_net["nominal_transitions"]:
        try:
            transition_to_place_list = petri_net["TP"][transition]
            for place in transition_to_place_list:
                TP[place][transition] = 1
        except KeyError:
            for place in transition_to_place_list:
                TP[place][transition] = 0
            pass
        

    for transition in petri_net["artificial_transitions"]:
        try:
            transition_to_place_list = petri_net["TP"][transition]
        except KeyError:
            pass
        for place in transition_to_place_list:
            TP[place][transition] = 1
    
    f.write(";")
    for idx,transition in enumerate(TP[place]):
        if(idx<len(TP[place])-1):
            f.write(transition + ";")
        else:
            f.write(transition)
    f.write("\n")
    for place in TP:
        f.write(place + ";")
        for transition in TP[place]:
            f.write(str(TP[place][transition]) + ";")
        f.write("\n")
    
    f.close()
    
    # write the initial marking
    
    f = open(petri_net_dir + initial_marking_filename, "w")
    
    for idx,place in enumerate(petri_net["initial_marking"]):
        if idx<len(petri_net["initial_marking"]):
            f.write(place)
        else:
            f.write(place + "\n")
    f.close()
      
    return None
    
def write_petri_net_mysql(petri_net_name, petri_net, schema_name):
    eventlog_db = mysql.connector.connect(host="127.0.0.1", user="root", password="root")
    
    # insert the process model entry
    
    
    dbcursor = eventlog_db.cursor()

    sql = "INSERT INTO "+schema_name+".process (Name) VALUES ('"+petri_net_name+"')"
    try:
        dbcursor.execute(sql)
    except mysql.connector.Error:
        pass

    eventlog_db.commit()
    
    # insert the activities
    
    for transition in petri_net["nominal_transitions"]:
        sql = "INSERT INTO "+schema_name+".activity (Name,ProcessModel) VALUES ('"+transition+"','"+petri_net_name+"')"
        try:
            dbcursor.execute(sql)
        except mysql.connector.Error:
            pass
        eventlog_db.commit()
    
    for transition in petri_net["artificial_transitions"]:
        sql = "INSERT INTO "+schema_name+".activity (Name,ProcessModel) VALUES ('"+transition+"','"+petri_net_name+"')"
        try:
            dbcursor.execute(sql)
        except mysql.connector.Error:
            pass
        eventlog_db.commit()

def find_xor_split_relation(xor_split_relation, modified_trace):
    precedent_activity = xor_split_relation["precedent_activity"]
    successive_activities = xor_split_relation["successive_activities"]

    for idx,event in enumerate(modified_trace):
        if event == precedent_activity:
            try:
                successive_activity = successive_activities[successive_activities.index(modified_trace[idx+1])]
                artificial_activity_to_insert = "XOR_"+precedent_activity+"_"+successive_activity
                modified_trace.insert(idx+1,artificial_activity_to_insert)
            except ValueError:
                pass
    
    return modified_trace

def find_xor_join_relation(xor_join_relation, modified_trace):
    precedent_activities = xor_join_relation["precedent_activities"]
    successive_activity = xor_join_relation["successive_activity"]
    
    for idx,event in enumerate(modified_trace):
        if event == successive_activity:
            try:
                precedent_activity = precedent_activities[precedent_activities.index(modified_trace[idx-1])]
                artificial_activity_to_insert = "XOR_"+precedent_activity+"_"+successive_activity
                modified_trace.insert(idx,artificial_activity_to_insert)
            except ValueError:
                pass

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
        activities = []

        trace_mask = read_events["case"] == case_id
        raw_trace = read_events[trace_mask]
        activities = list(raw_trace["event"].values)
        traces[case_id] = activities

    return traces

def write_traces(traces, output_filename):
    timestamps = []
    activities = []
    case_id_replicas = []
    for case_id in traces:
        case_id_to_insert = case_id
        counter = 0
        for activity in traces[case_id_to_insert]:
            timestamps.append(timestamp_builder(counter))
            activities.append(activity)
            case_id_replicas.append(case_id)
            counter = counter + 1
    
    csv_to_write = pd.DataFrame(columns=["case","event","startTime","completeTime"])
    csv_to_write["case"] = case_id_replicas
    csv_to_write["event"] = activities
    csv_to_write["startTime"] = timestamps
    csv_to_write["completeTime"] = timestamps

    csv_to_write.to_csv(modified_traces_dir+output_filename, index=False)
        
def inject_anomalies(trace, n_missed_activities, n_exchanges, final_transition):
    trace.pop()

    

    for i in range(n_missed_activities):
        activity_to_remove = random.choice(trace)
        #print(activity_to_remove)
        trace.remove(activity_to_remove)

    if(len(trace) > 1):
        for j in range(n_exchanges):
            success = 0
            while(success != 1):
                first_activity_index = random.choice(range(len(trace)))
                first_activity = trace[first_activity_index]
                try:
                    trace[first_activity_index]=trace[first_activity_index+1]
                    trace[first_activity_index+1] = first_activity
                    success = 1
                except IndexError:
                    pass

    trace.append(final_transition)



petri_net_name = "start_of_mission_carmine"
schema_name = "eventlog_rtis"
activities, xor_split_relations, xor_join_relations, precedence_relations = get_bpmn()
petri_net = bpmn_to_petri_net(activities, xor_split_relations, xor_join_relations, precedence_relations)

write_petri_net(petri_net_name,petri_net)
write_petri_net_mysql(petri_net_name, petri_net, schema_name)





