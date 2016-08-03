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

#andrew ling add
class CsBandwidth:
    def __init__(self, dev):
        self.dev = dev

    def initDevForBandwidth(self):
        eth0 = "eth0"
        if not self.isInitDevForBandwidth(eth0):
            cmd = "tc qdisc add dev %s root handle 1: htb r2q 1" % (eth0)
            CsHelper.execute(cmd)
            logging.info("init the dev eth0 for bandwidth")
        ethNum = self.dev
        if not self.isInitDevForBandwidth(ethNum):
            cmd = "tc qdisc add dev %s root handle 1: htb r2q 1" % (ethNum)
            CsHelper.execute(cmd)
            logging.info("init the dev %s for bandwidth" % (self.dev))

    def isInitDevForBandwidth(self,dev):
        cmd = "tc qdisc show |grep %s |grep r2q" % (dev)
        for i in CsHelper.execute(cmd):
            if dev in i.strip():
                return True
        return False



