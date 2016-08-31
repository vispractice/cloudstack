#!/usr/bin/env bash
#Andrew ling update the mutiline label route table 

routeTableName=$1
vmInstanceIp=$2
oldRouteTableName=$(sudo ip rule show |grep "from $vmInstanceIp lookup"|awk -F' ' '{print $5}')
if [ -n "$oldRouteTableName" ]
then
      sudo ip rule del from $vmInstanceIp table $oldRouteTableName pref 4000
      sudo ip rule add from $vmInstanceIp table $routeTableName pref 4000
else
      sudo ip rule add from $vmInstanceIp table $routeTableName pref 4000
fi
