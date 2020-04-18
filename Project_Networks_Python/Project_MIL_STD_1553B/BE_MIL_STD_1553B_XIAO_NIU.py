# BE MIL STD 1553B
# Auteur : Xiao Niu
# Date : 03 december 2019
################################################################################################################################################
#BEGIN
################################################################################################################################################
import xml.etree.ElementTree as ET
import math as math
################################################################################################################################################
# Etape 1
# Affichage des messages
tree = ET.parse("xmlB1-periodique.xml")
root = tree.getroot()
messageList = root.findall('message')
print(len(messageList))

for message in messageList :
    nom = message[0].text
    messagetype = message[1].text
    frequence = message[2].text
    taille_mes = message[3].text
    emetteur = message[4].text
    recepteur = message[5].text
    print(nom, messagetype,frequence,taille_mes,emetteur,recepteur)

################################################################################################################################################
# Etape 2
# Calul de la longueur totale de chaque message et d3
Table_C = []
for message in messageList :
    DT = ET.SubElement (message,'DT')
    if (message[4].text == "SXJJ" or message[5].text == "SXJJ" ):
        messagelength =  int(message[3].text)*int(20)+int(56)
    else :
        messagelength =  int(message[3].text)*int(20)+int(106)
    d3 = float(messagelength)/float(1e6)   
    DT.text = str(d3)    

################################################################################################################################################
# Etape 3 Véfification de la condition nécessaire

    C = float(messagelength)/1e6
    Table_C.append(C)

T_micro = float(1)/float(50)
Sufficient_cond = sum(Table_C)/T_micro
print("Sufficient condion is equal to %0.2f : " % Sufficient_cond)
if (Sufficient_cond > 1) :
    print("Sufficient condition verification test failed")
else :
    print("Sufficient condition verification test succeeded")

################################################################################################################################################
# Etape 4 Véfification de la condition nécessaire
error = float(0.000005)
delay_end_to_end = []
for i in range(len(messageList)):    
    # first iteration:
    C_j = []
    C_i = float(messageList[i][6].text)
    for j in range(len(messageList)):
        if (float(messageList[j][2].text)< float(messageList[i][2].text) and messageList[j][0].text != messageList[i][0].text) :  
            C_j.append(float(messageList[j][6].text))
        else :
            C_j.append(0)    
    C_j_max =  max(C_j)    
    W = C_i + max(C_j)   

    # new iterations :
    stop_cond = False
    n=1
    while(stop_cond == False) :
        C_k_term_sum = 0       
        for l in range(len(messageList)):
            if (float(messageList[l][2].text) >= float(messageList[i][2].text) and messageList[l][0].text != messageList[i][0].text) :
                C_k= float(messageList[l][6].text)
                W_old = W
                T_k= 1/float(messageList[l][2].text) 
                C_k_term = C_k*math.ceil(W_old/T_k)
                C_k_term_sum = C_k_term_sum + C_k_term 
        W_new = C_i + C_j_max + C_k_term_sum
        diff = abs(float( W_new - W_old )/float(W_old))
        if(diff<= error):
            stop_cond = True
            delay_end_to_end.append(W_new)
        else :
            n=n+1   
            W = W_new
            C_j.clear() 

    #Calcul de d2
delay_propagation = []
for i in range(len(messageList)):
    DMAC = ET.SubElement (messageList[i],'DMAC')
    DBEB = ET.SubElement (messageList[i],'DBEB')
    d2 = delay_end_to_end[i]-float(messageList[i][6].text)
    delay_propagation.append(d2)
    DMAC.text = str(d2)  
    DBEB.text = str(delay_end_to_end[i]) 

################################################################################################################################################
# Etape 5 test d'ordonnancabilite
for i in range(len(messageList)):
    Test = ET.SubElement (messageList[i],'Test')
    if(delay_end_to_end[i]<= (1/float(messageList[i][2].text))):
        test_ord = True
    else :
        test_ord = False   
    Test.text = str(test_ord)  
    
tree.write('OUTPUT-BE_MIL_STD_1553B_XIAO_NIU.xml')

################################################################################################################################################
#END
################################################################################################################################################