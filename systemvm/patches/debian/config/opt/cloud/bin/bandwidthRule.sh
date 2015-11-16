#!/usr/bin/env bash
#andrew ling add for deleting the bandwidth filter rules.

while getopts 'ADc:r:p:' OPTION
do
  case $OPTION in
  A)    op="-A"
        ;;
  D)    op="-D"
        ;;
  c)    dev="$OPTARG"
        ;;
  r)    classid="$OPTARG"
        ;;
  p)    prio="$OPTARG"
        ;;
  ?)   logger -t cloud "It is the wrong paramter."
        ;;
  esac
done
if [ "$op" == "-D" ]
then
handles=`tc filter list dev $dev |grep "flowid 1:$classid"|awk '{print $10}'`

for handle in $handles
do
tc filter delete dev $dev parent 1: protocol ip prio $prio handle ${handle} u32
done
fi
