#!/usr/bin/env bash
#Andrew ling delete the default gateway in the route rules.
route del default gw $(route -n|grep ^0.0.0.0|grep UG|awk -F' ' '{print $2}')