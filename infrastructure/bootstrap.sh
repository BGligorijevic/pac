#!/usr/bin/env bash

sudo apt-get update

# install mysql
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
sudo apt-get install -y vim curl python-software-properties
sudo apt-get update
sudo apt-get -y install mysql-server

# allow external access
sed -i "s/^bind-address/#bind-address/" /etc/mysql/my.cnf

# root is the god
mysql -u root -proot -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION; FLUSH PRIVILEGES;"

# create app user and table
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS VOTES; 
GRANT USAGE ON *.* TO 'voteapp'@'%' IDENTIFIED BY 'voteapp';
GRANT SELECT, INSERT, UPDATE, DELETE ON VOTES.* TO 'voteapp'@'%';
FLUSH PRIVILEGES;"

sudo /etc/init.d/mysql restart