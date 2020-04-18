################################################################################################################################################
# FITR-303 : Real Time Systems
# Project Network AFDX
# Author : Xiao Niu
# Date : 27 March 2020
################################################################################################################################################
# To launch the program:
# In a terminal, type : 
#       python Projet_AFDX_FINAL_NIU.py {inputfile}
# Choices of inputfiles : 
#       EE.xml 
#       ESE.xml
#       ESSE.xml
#       ES2E_M.xml
#       ESE_F3.xml
#       3ESE.xml
#       STAR_3.xml
#       ISAE_TEST_1.xml
#       ISAE_TEST_2.xml
#       AFDX.xml 
################################################################################################################################################
# OUTPUT
# An XML file named (results_inputfile) is generated with the following computations :
#       1) End to end delays bounds for each flow
#       2) Loads of each link 
################################################################################################################################################

################################################################################################################################################
############################################################### BEGIN ##########################################################################
################################################################################################################################################
import xml.etree.ElementTree as ET
from xml.dom import minidom
import math as math
import numpy as np
import sys

################################################################################################################################################
# Step 1 : Interpretation of Input XML file
################################################################################################################################################
#  a) Parsing
InputDirectory = "samples/"
input = sys.argv[1]
inputfile = InputDirectory + str(input)

tree = ET.parse(inputfile)
root = tree.getroot()

# Network
for child in root.findall("network"):
    transmission_capacity = child.get("transmission-capacity")
    overhead = float(child.get("overhead"))
    if("Mbps" in transmission_capacity):
        transmission_capacity = transmission_capacity[0:-4]
        transmission_capacity_bps = float(transmission_capacity)*(10**6)
    else :
        transmission_capacity_bps = float(transmission_capacity)

# Stations
stations = []
for child in root.findall("station"):
    station = child.get("name")
    stations.append(station)

# Switches
switches = []
for child in root.findall("switch"):    
    switch = child.get("name")
    switches.append(switch)

# Links
links = []
for child in root.findall("link"):
    link = []
    end_from = child.get("from")
    end_to = child.get("to")
    name = child.get("name")
    link.append(end_from) 
    link.append(end_to)
    link.append(name)
    links.append(link)

# Flows (messages)
flows = []
for child in root.findall("flow"):
    flow = []
    name = child.get("name")
    max_payload_byte = float(child.get("max-payload"))
    payload = max_payload_byte
    period_s = float(child.get("period"))*0.001
    deadline_s = float(child.get("deadline"))*0.001
    jitter_s = float(child.get("jitter"))*0.001
    source = child.get("source")

    edges = []
    for target in child.findall("target"):
        target_name = target.get("name")
        edge = []
        edge.append(target_name)
        edge.append([0,source])
        for path_node in target.findall("path"):
            path_node_name = path_node.get("node")
            edge.append([edge[-1][1],path_node_name])
        edges.append(edge)
    for edge in edges :
        for pairs in edge :
            if (pairs[0]==0):
                edge.remove(pairs)
    flow.append(name) # flows[0]
    flow.append(payload) # flows[1]
    flow.append(period_s) # flows[2]
    flow.append(deadline_s) # flows[3]
    flow.append(jitter_s) # flows[4]
    flow.append(edges) # flows[5]
    flow.append(source) # flows[6]

    flows.append(flow)
################################################################################################################################################
# b) 
# # i) Load of each network link
# # #  Initialisation
path_travelled=[]
loads_direct = np.zeros(len(links))
loads_reverse = np.zeros(len(links))

# # #  Compute loads
for flow in flows:
    trame_size = 8*flow[1] + 8*overhead
    load = trame_size/flow[2]
    edges = flow[5]
    for i in range(len(links)):
        for j in range (len(edges)):
            for k in range(len(edges[j])-1):
                if ((links[i][0] == edges[j][k+1][0]) and (links[i][1] == edges[j][k+1][1])and([flow[0],edges[j][k+1]] not in path_travelled)):
                    loads_direct[i] = loads_direct[i]+load
                    path_travelled.append([flow[0],edges[j][k+1]])
                if ((links[i][1] == edges[j][k+1][0]) and (links[i][0] == edges[j][k+1][1])and([flow[0],edges[j][k+1]] not in path_travelled)):
                    loads_reverse[i] = loads_reverse[i]+load
                    path_travelled.append([flow[0],edges[j][k+1]])

# # ii) Stability condition
# # #  Initialisation
percentages_direct = []
percentages_reverse = []

# # #  Stability of the direct flows
for load_direct in loads_direct:
    percentage = load_direct/transmission_capacity_bps * 100 # %
    percentage_text = str(round(percentage,2))+"%"
    percentages_direct.append([percentage_text])

# # #  Stability of the reverse flows
for load_reverse in loads_reverse:
    percentage = load_reverse/transmission_capacity_bps * 100 # %
    percentage_text = str(round(percentage,2))+"%"
    percentages_reverse.append([percentage_text])

################################################################################################################################################
# Step 2 : Compute the end-to-end delay bounds for simple cases
################################################################################################################################################
# Use case specifically for End station to End station (EE.xml)
if (len(links)==1):
# a) Arrival curve
    delays_end_to_end = []
    for i in range(len(flows)):
        for j in range(len(flows[i][5])):
            arrival_curve = []
            service_curve = []
            delay = 0

            message_size = 8 * flows[i][1] + 8 * overhead
            c = transmission_capacity_bps
            frequency = 1/flows[i][2]

            # Curves parameters
            b = message_size
            r = message_size*frequency

            # Departure arrival curve
            arrival_curve = [b, r]

################################################################################################################################################
# b) Service curve
            # Curves parameters
            R = c

            # Service curve
            service_curve = [R]

################################################################################################################################################
# c) End to end delay bound
            # Delay end to end 
            # Theorem 1 : D = T + b/R; with T = 0
            delay = b/R * (10 ** 6) # mu_s

            source_node = flows[i][5][j][1][0]   
            target_node = flows[i][5][j][0]
            delays_end_to_end.append([source_node, target_node, delay])

            # Exit arrival curve
            b = b + r * delay / (10 ** 6)
            r = r
            arrival_curve_out = [b, r]

################################################################################################################################################
# Step 3 : Compute the end-to-end delay bounds for general cases
################################################################################################################################################
# Use-cases with one flow : ESE.xml, ESSE.xml, ES2E_M.xml
if (len(links)>1 and len(flows)==1) :

    # Initialization & definition of the flow parameters    
    delays_end_to_end = []
    flow_name = flows[0][0]
    flow_payload = flows[0][1]
    flow_period = flows[0][2]
    flow_edges = flows[0][5]
    flow_source = flows[0][6]
    flow_targets_nb = len(flow_edges)

################################################################################################################################################
# ESE.xml & ESSE.xml
    if (flow_targets_nb == 1) :

    # Calculation of the source station 
        message_size = 8 * flow_payload + 8 * overhead
        c = transmission_capacity_bps

        # Curves parameters
        b = message_size
        r = message_size/flow_period
        R = c

        # Arrival curve (Depart)
        arrival_curve = [b, r]

        # Service curve
        service_curve = [R]

        # Station delay 
        # Theorem 1 : D = T + b/R; with T = 0
        delay = b/R * (10 ** 6) # mu_s

        # Arrival curve (Sortie)
        b = b + r * delay / (10 ** 6)
        r = r
        arrival_curve_out = [b, r]

    # iterative computation of next nodes in the path
        k = 1
        cont = True
        while (cont == True) :
            node = flow_edges[0][k][1]
            if (node in switches) :
                # Delay switch
                delay_new =  b/R * (10 ** 6) # mu_s

                # Update end to end delay
                delay = delay + delay_new

                # Theorem 2 : b* = b(t+D)
                b = b + r * delay_new / (10 ** 6) 

            if (flow_edges[0][k+1][1] in switches):
                k = k+1
            else :
                cont = False 

        source_node = flow_source  
        target_node = flow_edges[0][0]
        delays_end_to_end.append([source_node, target_node, delay])  

# ES2E_M.xml
    # Get all targets
    if (flow_targets_nb > 1) :   
        flow_targets = []
        for i in range(len(flow_edges)):
            flow_targets.append(flow_edges[i][0])

    # Calculation of the source station 
        message_size = 8 * flow_payload + 8 * overhead
        c = transmission_capacity_bps

        # Curves parameters
        b = message_size
        r = message_size/flow_period
        R = c

        # Arrival curve (Depart)
        arrival_curve = [b, r]

        # Service curve
        service_curve = [R]

        # Station delay
        # Theorem 1 : D = T + b/R; with T = 0
        delay = b/R * (10 ** 6) # mu_s
        delay_station = delay

        # Arrival curve (Sortie)
        b = b + r * delay / (10 ** 6)
        r = r
        arrival_curve_out = [b, r]

    # iterative computation of next nodes in the path
        for i in range(len(flow_edges)):
            delay = delay_station
            b = arrival_curve_out[0]
            r = arrival_curve_out[1]

            k = 1
            cont = True
            while (cont == True) :
                node = flow_edges[i][k][1]
                if (node in switches) :
                    # Delay switch
                    delay_new =  b/R * (10 ** 6) # mu_s

                    # Update end to end delay
                    delay = delay + delay_new

                    # Theorem 2 : b* = b(t+D)
                    b = b + r * delay_new / (10 ** 6) 

                if (flow_edges[0][k+1][1] in switches):
                    k = k+1
                else :
                    cont = False 

            source_node = flow_source  
            target_node = flow_edges[i][0]
            delays_end_to_end.append([source_node, target_node, delay])

################################################################################################################################################
# Use cases with more flows : ESE_F3.xml, 3ESE.xml, STAR_3.xml, ISAE_TEST_1.xml, ISAE_TEST_2.xml, AFDX.xml
if (len(links)>1 and len(flows)>1) :

    # Initialization & definition of the flow parameters    
    delays_end_to_end = []
    flows_name = []
    flows_payload = []
    flows_period = []
    flows_edges = []
    flows_source = [] 
    for flow in flows :
        flows_name.append(flow[0])
        flows_payload.append(flow[1])
        flows_period.append(flow[2])
        flows_edges.append(flow[5])
        flows_source.append(flow[6])
    flows_targets = []
    for i in range(len(flows)):
        flow_targets = []
        for j in range(len(flows[i][5])) :
            flow_targets.append(flows[i][5][j][0])
        flows_targets.append(flow_targets)

    # Verify if the flows have different targets
    targets_all_same = True
    n = 1
    flows_target = flows_targets[0]
    while (targets_all_same == True) :
        if (flows_target == flows_targets[n]):
            flows_target = flows_targets[n]
            if (n<(len(flows_targets)-1)):
                n = n+1
            else :
                targets_all_same = True
                break
        else :
            targets_all_same = False
            break
    # Verify if the flows have different sources (in case of same target)
    if(targets_all_same == True):
        sources_all_same = True
        m = 1
        flow_source = flows_source[0]
        while (sources_all_same == True) :
            if (flow_source == flows_source[m]):
                flow_source = flows_source[m]
                if (n<(len(flows_source)-1)):
                    m = m+1
                else :
                    sources_all_same = True
                    break
            else :
                sources_all_same = False
                break

# Computation for use-case : 1 source, 1 target (ESE_F3.xml)            
        if(sources_all_same == True):
            for i in range(len(flows)) :
            # Calculation of the source station 
                message_size = 8 * flows_payload[i] + 8 * overhead
                c = transmission_capacity_bps

                # Curves parameters
                b = message_size
                r = message_size/flows_period[i]
                R = c

                # Arrival curve (Depart)
                arrival_curve = [b, r]

                # Service curve
                service_curve = [R]

                # Station delay
                # Theorem 1 : D = T + b/R; with T = 0
                delay = len(flows)  * b / R * (10 ** 6) # mu_s
                delay_station = delay

                # Arrival curve (Sortie)
                b = b + r * delay / (10 ** 6)
                r = r
                arrival_curve_out = [b, r]

            # iterative computation of next nodes in the path
                for j in range(len(flows[i][5])):
                    delay = delay_station
                    b = arrival_curve_out[0]
                    r = arrival_curve_out[1]

                    k = 1
                    cont = True
                    while (cont == True) :
                        node = flows[i][5][j][k][1]
                        if (node in switches) :
                            # Delay switch
                            delay_new =  len(flows)* b / R * (10 ** 6) # mu_s

                            # Update end to end delay
                            delay = delay + delay_new

                            # Theorem 2 : b* = b(t+D)
                            b = b + r * delay_new / (10 ** 6) 

                        if (flows[i][5][j][k+1][1] in switches):
                            k = k+1
                        else :
                            cont = False

                    source_node = flows[i][6] 
                    target_node = flows[i][5][j][0]
                    delays_end_to_end.append([source_node, target_node, delay])

# Computation for use-case : multiple sources, 1 target (3ESE.xml)       
        else:
            for i in range(len(flows)) :
            # Calculation of the source station 
                message_size = 8 * flows_payload[i] + 8 * overhead
                c = transmission_capacity_bps

                # Curves parameters
                b = message_size
                r = message_size/flows_period[i]
                R = c

                # Arrival curve (Depart)
                arrival_curve = [b, r]

                # Service curve
                service_curve = [R]

                # Station delay
                # Theorem 1 : D = T + b/R; with T = 0
                delay = b / R * (10 ** 6) # mu_s
                delay_station = delay

                # Arrival curve (Sortie)
                b = b + r * delay / (10 ** 6)
                r = r
                arrival_curve_out = [b, r]

            # iterative computation of next nodes in the path
                for j in range(len(flows[i][5])):
                    delay = delay_station
                    b = arrival_curve_out[0]
                    r = arrival_curve_out[1]

                    k = 1
                    cont = True
                    while (cont == True) :
                        node = flows[i][5][j][k][1]
                        if (node in switches) :
                            # Delay switch
                            delay_new =  len(flows)* b / R * (10 ** 6) # mu_s

                            # Update end to end delay
                            delay = delay + delay_new

                            # Theorem 2 : b* = b(t+D)
                            b = b + r * delay_new / (10 ** 6) 

                        if (flows[i][5][j][k+1][1] in switches):
                            k = k+1
                        else :
                            cont = False

                    source_node = flows[i][6] 
                    target_node = flows[i][5][j][0]
                    delays_end_to_end.append([source_node, target_node, delay])            

# Computations for use-cases : multiple sources, multiple targets (STAR_3.xml, ISAE_TEST_1.xml, ISAE_TEST_2.xml, AFDX.xml)
    else :
        for i in range(len(flows)) :
            direct_flows = []
            for link in links :
                direct_flow = [link[0],link[1]]
                direct_flows.append(direct_flow)

    # Calculation of the source station 
            message_size = 8 * flows_payload[i] + 8 * overhead
            c = transmission_capacity_bps

            # Curves parameters
            b = message_size
            r = message_size/flows_period[i]
            R = c

            # Arrival curve (Depart)
            arrival_curve = [b, r]

            # Service curve
            service_curve = [R]

            # Station delay
            # Theorem 1 : D = T + b/R; with T = 0
            delay = b / R * (10 ** 6) # mu_s
            delay_station = delay

            # Arrival curve (Sortie)
            b = b + r * delay / (10 ** 6)
            r = r
            arrival_curve_out = [b, r]

    # iterative computation of next nodes in the path
            for j in range(len(flows[i][5])):
                delay = delay_station
                b = arrival_curve_out[0]
                r = arrival_curve_out[1]

                k = 1
                cont = True
                while (cont == True) :
                    previous = flows[i][5][j][k][0]
                    node = flows[i][5][j][k][1]
                    if (node in switches) :
                        switch_entrance_flows = 0
                        switch_exit_flows = 0

                        # Find flows entering a swith node
                        for l in range(len(links)) :
                            if ((node == links[l][1]) and (loads_direct[l]!=0)) :
                                nb_flows = float(loads_direct[l])/float(8*flows[i][1] + 8*overhead)*float(flows[i][2])
                                switch_entrance_flows = switch_entrance_flows + math.ceil(nb_flows)
                            if ((node == links[l][0]) and (loads_reverse[l]!=0)) :
                                switch_exit_flows = switch_exit_flows + 1
                        switch_arriving_flows = switch_entrance_flows

                        # Delay switch
                        delay_new =  switch_arriving_flows * b / R * (10 ** 6) # mu_s

                        # Update end to end delay
                        delay = delay + delay_new

                        # Theorem 2 : b* = b(t+D)
                        b = b + r * delay_new / (10 ** 6) 

                    if (flows[i][5][j][k+1][1] in switches):
                        k = k+1
                    else :
                        cont = False 

                source_node = flows[i][6] 
                target_node = flows[i][5][j][0]
                delays_end_to_end.append([source_node, target_node, delay])      

################################################################################################################################################
# Step 4 : Write xml file as output
################################################################################################################################################
# create the file structure
results = ET.Element('results')
delays = ET.SubElement(results, 'delays')
for i in range(len(flows)) :
    flow = ET.SubElement(delays, 'flow')
    flow.set('name', flows[i][0])
    for j in range(len(flows[i][5])):
        target = ET.SubElement(flow,'target')
        target.set('name', flows[i][5][j][0])
        delay_flow = 0
        for k in range(len(delays_end_to_end)):
            if (delays_end_to_end[k][0]==flows[i][6] and delays_end_to_end[k][1] == flows[i][5][j][0]) :
                delay_flow = str(round(delays_end_to_end[k][2],2))
        target.set('value', delay_flow)
jitters = ET.SubElement(results, 'jitters')
backlogs = ET.SubElement(results, 'backlogs')
load = ET.SubElement(results, 'load')
for i in range(len(links)) :
    edge = ET.SubElement(load, 'edge')
    edge.set('name', links[i][2])
    usage1 = ET.SubElement(edge, 'usage')
    usage1.set('percent',percentages_direct[i][0])
    usage1.set('type',"direct")
    usage1.set('value',str(round(loads_direct[i],2)))
    usage2 = ET.SubElement(edge, 'usage') 
    usage2.set('percent',percentages_reverse[i][0])
    usage2.set('type',"reverse")
    usage2.set('value',str(round(loads_reverse[i],2)))
edge = ET.SubElement(load, 'edge')

# Write Output file
xmlstr = minidom.parseString(ET.tostring(results)).toprettyxml(indent="   ", encoding=None)
with open("results_" + str(input), "w") as f:
    f.write(xmlstr)

################################################################################################################################################
############################################################### END ############################################################################
################################################################################################################################################