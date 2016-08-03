# -- coding: utf-8 --
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
import CsHelper
import logging
from CsDatabag import CsDataBag
from CsRedundant import *


#andrew ling add
class CsMultilineRule(CsDataBag):

    def process(self):
        for public_ip in self.dbag:
            if public_ip == "id":
                continue
            for rule in self.dbag[public_ip]:
                if rule["type"] == "staticnat":
                    self.processMultilineRule(rule)

    def processMultilineRule(self, rule):
        vmInstanceIp = rule['internal_ip']
        routeTableName = rule['route_table_name']
        cmd = "ip rule show |grep \"from %s lookup\"|awk -F' ' '{print $5}'" % (vmInstanceIp)
        oldRouteTableName = CsHelper.execute(cmd)
        if not (oldRouteTableName is None):
            cmd = "ip rule del from %s table %s pref 4000" % (vmInstanceIp,oldRouteTableName)
            CsHelper.execute(cmd)
            logging.info("Delete old multiline ip rule for %s in the table " % (vmInstanceIp, routeTableName))
            if routeTableName != "none":
                cmd = "ip rule add from %s table %s pref 4000" % (vmInstanceIp, routeTableName)
                CsHelper.execute(cmd)
                logging.info("Added multiline ip rule for %s in the table " % (vmInstanceIp, routeTableName))
        else:
            cmd = "ip rule add from %s table %s pref 4000" % (vmInstanceIp, routeTableName)
            CsHelper.execute(cmd)
            logging.info("Added multiline ip rule for %s in the table " % (vmInstanceIp, routeTableName))






