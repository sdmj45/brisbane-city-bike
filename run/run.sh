#!/usr/bin/env bash
#description     :This script will launch application.
#author		     :Jian MA
#date            :20190327
#version         :0.1

# install Virtual Machine
cd ansible/platforms/vagrant && vagrant up &&
# install platform
cd ../../ && ansible-playbook -i hosts ./playbook_install_platform.yml &&
# generate spark assembly jar
cd ../ && sbt clean assembly &&
# copy spark conf, spark jar and data input
cd ansible && ansible-playbook -i hosts ./playbook_pre_deploy.yml &&
# run training-node and scoring-node
ansible-playbook -i hosts ./playbook_run.yml &&
# copy results to local directory
ansible-playbook -i hosts ./playbook_post_run.yml