###############################################################################################
# IMPORTS
###############################################################################################

# librairies requises
import matplotlib.pyplot as plt
import time
import numpy as np
from scipy import sparse

# Environnement Spark
from pyspark import SparkContext, SparkConf

# Version locale de python (python3 anaconda)
import os
os.environ['PYSPARK_PYTHON'] = '/usr/bin/python3.6'


###############################################################################################
# SPARK CONFIGURATION
###############################################################################################

conf = SparkConf()
conf.setMaster("local[*]")
# Name of application displayed on the cluster UI
conf.setAppName("Matrix Factorization")

# Set spark "app" with configuration (how to access a cluster)
sc = SparkContext(conf=conf)


###############################################################################################
# IMPORTING DATASET
###############################################################################################

def parseRating(line):
    """
    Parse les données par ligne.
    """
    fields = line.split('::')
    return int(fields[0]), int(fields[1]), float(fields[2])

# Chemin vers le fichier de données
# movieLensHomeDir="hdfs://master:54310/input/"

# Répertoire contenant le jeu de données
movieLensHomeDir = "data/"

# Nom du fichier
ratings_size = "ratings_tiny"

# ratings : RDD du type (userID, movieID, rating), create RDD
ratingsRDD = sc.textFile(
    movieLensHomeDir + ratings_size + ".dat").map(parseRating).setName("ratings").cache()


###############################################################################################
# OPTIMIZATION FUNCTIONS
###############################################################################################


def predictedRating(x, P, Q):
    """ 
    This function computes predicted rating
    Args:
        x: tuple (UserID, MovieID, Rating)
        P: user's features matrix (M by K)
        Q: item's features matrix (N by K)
    Returns:
        predicted rating: l 
    """
    # Predicted rating : rij = q_{j}.T * p_{i}
    predicted_rating = np.matmul(Q[x[1]-1, :].T, P[x[0]-1, :])

    return predicted_rating


def computeMSE(rdd, P, Q):
    """ 
    This function computes Mean Square Error (MSE)
    Args:
        rdd: RDD(UserID, MovieID, Rating)
        P: user's features matrix (M by K)
        Q: item's features matrix (N by K)
    Returns:
        mse: mean square error 
    """

    # Compute the R matrix
    R = np.dot(P, np.transpose(Q))

    # Create an RDD to sotre the squared error
    squared_error_rdd = rdd.map(lambda k: (k[2]-R[k[0]-1][k[1]-1])**2)

    # Add all the squared errors
    total_squared_error = squared_error_rdd.reduce(lambda x, y: x+y)

    # Calculate mean
    mse = total_squared_error/rdd.count()
    return mse

###############################################################################################
# GRADIENT DESCENT FUNCTION
###############################################################################################


def GD(trainRDD, K=10, MAXITER=50, GAMMA=0.001, LAMBDA=0.05):
    # Création des champs de la matrice
    row = []
    col = []
    data = []
    for part in trainRDD.collect():
        row.append(part[0]-1)
        col.append(part[1]-1)
        data.append(part[2])
    R = sparse.csr_matrix((data, (row, col)))

    # Initialisation aléatoire des matrices P et Q
    M, N = R.shape
    P = np.random.rand(M, K)
    Q = np.random.rand(N, K)

    nonzero = R.nonzero()
    I, J = nonzero[0], nonzero[1]
    for epoch in range(MAXITER):
        for i, j in zip(I, J):
            # Mise à jour de P[i,:] et Q[j,:] par descente de gradient à pas fixe (dérivé da la fonction de cout)
            P[i, :] = P[i, :] + GAMMA * (2*(R[i, j] - np.matmul(Q[j, :].T, P[i, :]))* Q[j, :] - LAMBDA*(2*P[i, :]))
            Q[j, :] = Q[j, :] + GAMMA * (2*(R[i, j] - np.matmul(Q[j, :].T, P[i, :]))* P[i, :] - LAMBDA*(2*Q[j, :]))

    return P, Q


###############################################################################################
# CROSS VALIDATION
###############################################################################################

def combinationTester(trainRDD, testRDD, lambda_pen, gamma_list, number):
    """
    For given combination lambda_pen and gamma_list, this function tests the combinations on 
    trainRDD by testing in testRDD
    """
    
    # initial list
    mse_for_comb = []

    for l in lambda_pen:
        for y in gamma_list:
            # For given combination, calculate parameters
            P, Q = GD(trainRDD, K=10, MAXITER=10, GAMMA=y, LAMBDA=l)
            
            # For above optimization, apply the test
            mse_test = computeMSE(testRDD, P, Q)
            mse_for_comb.append([l, y, mse_test])
            
            # print mean obtained for given lambda and gamma combination
            print("Using RDD number", number, "as test, the current train mse [lambda, gamma, MSE]:", [l, y, mse_test])

    optimal = mse_for_comb[0]
    for element in mse_for_comb:
        if(optimal[2] > element[2]):
            optimal = element

    print("For RDD number", number, ", the optimal combination [lambda, gamma, MSE] was:", optimal)

    return mse_for_comb



# Calcul de P, Q et de la mse
print("\nOptimizing...")

# Combinaison de valeurs de lambda et gamma qu'on veut tester
lambda_pen = np.arange(0.04, 0.08, 0.005)
gamma_list = np.arange(0.001, 0.008, 0.001)

# Create three random training sets and three random test sets
split_weight = 0.5

# Random
tempRDD1, tempRDD2 = ratingsRDD.randomSplit([split_weight, 1-split_weight])
rdd1, rdd2 = tempRDD1.randomSplit([split_weight, 1-split_weight])
rdd3, rdd4 = tempRDD2.randomSplit([split_weight, 1-split_weight])

# Use forth RDD as test
trainRDD = sc.union([rdd1, rdd2, rdd3])
list1 = combinationTester(trainRDD, rdd4, lambda_pen, gamma_list, 4)

# Use third RDD as test
trainRDD = sc.union([rdd1, rdd2, rdd4])
list2 = combinationTester(trainRDD, rdd3, lambda_pen, gamma_list, 3)

# Use second RDD as test
trainRDD = sc.union([rdd1, rdd3, rdd4])
list3 = combinationTester(trainRDD, rdd2, lambda_pen, gamma_list, 2)

# Use first RDD as test
trainRDD = sc.union([rdd2, rdd3, rdd4])
list4 = combinationTester(trainRDD, rdd1, lambda_pen, gamma_list, 1)

list_avg = []

for i in range(len(list1)):
    avg = (list1[i][2] + list2[i][2] + list3[i][2] + list4[i][2]) / (4)
    list_avg.append([list1[i][0], list1[i][1], avg])

optimal = list_avg[0]
for element in list_avg:
    if(optimal[2] > element[2]):
        optimal = element


print("==================================")
print("The lambda and gamma with the minimale error are:")
print("==================================")
print("Lambda:", optimal[0])
print("Gamma:", optimal[1])
print("Mean error:", optimal[2])
