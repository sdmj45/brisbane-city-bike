# Brisbane-city-bike

brisbane-city-bike is a Big Data platform project based on Spark/Scala. It processes JSON data files as input to provide City Bike data clustering on the location of bike stations.

There are two parts in the project: ansible and scala. 

# Ansible

* playbook_install_platform.yml 

This playbook is used to install the tools as htop, java and spark, we create a spark standalone cluster with a master and two slaves. 

* playbook_pre_deploy.yml

This playbook is used to do the jobs of pre deploying, like coping spark configuration file( in our case, we copy run/spark.conf.properties to remote system, so if we want to change the spark job config, we have to just change this file, and it will take into account automatically), coping spark jar file and input data files

* playbook_run.yml

This playbook is used to run training-node firstly and then run scoring-node

* playbook_post_run.yml

This playbook is used to copy model and output files from remote to local system, in our case, in the data/output path



## Ansible version: 
latest version as 2.7.0

# Scala
We use sbt to create a multi-project which has 3 parts. We separate scoring-node and training-node in micro-service, they can run in mode independently, so if there is a problem in training-node, this will not impact scoring-node and vice-versa.

## training-node
Training-node is used to train the model, we take a part of data in Brisbane_CityBike.json and copy that to data/in/Brisbane_CityBike_Train.json as the training input, and save the model in {{ data_model_dir }} directory

## scoring-node
Scoring-node is used to predict the results, we load the model in {{ data_model_dir }} and predict the input data which exists in data/in/Brisbane_CityBike.json, and output the result in Json format in {{ data_output_dir }}

## common
Common part contains the classes and objects in common between training-node and scoring-node.

# Running

We have to just launch 
```
run/run.sh
```
it calls the different playbook files and launch different tasks listed in the job

It runs in the orders as below
1. create 3 virtual machines
2. install java, spark cluster
3. generate assembly jar of training-node and scoring-node
4. copy file config, input data file, spark jars in remote file system
5. run training-node application in spark cluster
6. run scoring-node application in spark cluster
7. copy model and output files from remote to local 

## To do
1. Add logical part for calculating cluster number optimized
2. Install hdfs and store the input, model, output in hdfs
3. After predicting input, we can move the input to another directory to avoid working with the same file again
4. Using Kubernetes for installing the cluster 
5. Using Streaming for the training and scoring, we can use Kafka for example.
6. Add Unit test for the different functions
7. Using a scheduler for training or scoring like Airflow, Oozie 


## Contributors
Jian MA