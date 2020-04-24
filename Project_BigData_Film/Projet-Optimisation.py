###############################################################################################
# IMPORTS
###############################################################################################
# Librairies requises
import matplotlib.pyplot as plt
import time
import numpy as np
from scipy import sparse

# Environnement Spark
from pyspark import SparkContext, SparkConf

# Environment python à utiliser (python3 anaconda)
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

# Start time for performance recordance
start_time = time.time()


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

# Nom du fichier à utiliser
ratings_size = "ratings_tiny"

# ratings : RDD du type (userID, movieID, rating), créer un RDD à partir des données
ratingsRDD = sc.textFile(
    movieLensHomeDir + ratings_size + ".dat").map(parseRating).setName("ratings").cache()

# Obtenir paramétres des données
numRatings = ratingsRDD.count()
numUsers = ratingsRDD.map(lambda r: r[0]).distinct().count()
numMovies = ratingsRDD.map(lambda r: r[1]).distinct().count()

# Message pour debugger
print("\nWe have %d ratings from %d users on %d movies.\n" %
      (numRatings, numUsers, numMovies))

M = ratingsRDD.map(lambda r: r[0]).max()
N = ratingsRDD.map(lambda r: r[1]).max()
matrixSparsity = float(numRatings)/float(M*N)

# Comprendre la nature des données
print("We have %d users, %d movies and the rating matrix has %f percent of non-zero value.\n" %
      (M, N, 100*matrixSparsity))


###############################################################################################
# OPTIMIZATION FUNCTIONS
###############################################################################################


# Calcul du rating prédit
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

# Calcul de l'erreur MSE
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


# Algorithem de descente de gradient pour la factorisation de matrices
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

    # Print the initial mse
    print("The initial means squared error is", computeMSE(trainRDD, P, Q), "\n")

    # Calcul de l'erreur MSE initiale
    mse_train = []

    nonzero = R.nonzero()
    I, J = nonzero[0], nonzero[1]
    for epoch in range(MAXITER):
        for i, j in zip(I, J):
            # Mise à jour de P[i,:] et Q[j,:] par descente de gradient à pas fixe (dérivé da la fonction de cout)
            P[i, :] = P[i, :] + GAMMA * (2*(R[i, j] - np.matmul(Q[j, :].T, P[i, :]))* Q[j, :] - LAMBDA*(2*P[i, :]))
            Q[j, :] = Q[j, :] + GAMMA * (2*(R[i, j] - np.matmul(Q[j, :].T, P[i, :]))* P[i, :] - LAMBDA*(2*Q[j, :]))

        # Calcul de l'erreur MSE courante, et sauvegarde dans le tableau mse
        mse_train.append([epoch, computeMSE(trainRDD, P, Q)])

    return P, Q, mse_train


###############################################################################################
# SETUP
###############################################################################################

# Séparation du jeu de données en un jeu d'apprentissage et un jeu de test, taille du jeu d'apprentissage (en %70)
learningWeight = 0.7

# Création des RDD "apprentissage" et "test" depuis la fonction randomsplit
trainRDD, testRDD = ratingsRDD.randomSplit([learningWeight, 1-learningWeight])


###############################################################################################
# OPTIMIZATION
###############################################################################################

# Calcul de P, Q et de le mse
print("\nOptimizing...")

start_opt_time = time.time()  # 0.001 # 0.05
# 1 optimal y=0.004, l = -0.07
P, Q, mse = GD(trainRDD, K=10, MAXITER=20, GAMMA=0.0015, LAMBDA=0.075)
end_opt_time = time.time()

# Affichage des résultats
for element in mse:
    print("epoch: ", element[0], " - MSE: ", element[1])

print("\nFinished")


###############################################################################################
# RESULT SECTION
###############################################################################################

# Prédiction des ratings
mse_test = computeMSE(testRDD, P, Q)

print("The error obtained from the test was:", mse_test)

end_time = time.time()

print("==================================")
print("Total execution time : %s seconds" % (end_time - start_time))
print("==================================")

print("==================================")
print("Optimization execution time : %s seconds" %
      (end_opt_time - start_opt_time))
print("==================================")


###############################################################################################
# RESULT VISUALISATION
###############################################################################################

error = []

for element in mse:
    error.append(element[1])

# Affichage de l'erreur MSE
f1 = plt.figure()
plt.plot(error)
plt.xlabel("Iteration")
plt.xlabel("MSE")
plt.title("MSE evolution with " + ratings_size)
f1.savefig("MSE_evolution_" + ratings_size + ".png", bbox_inches='tight')
