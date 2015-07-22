# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.


"""Creates a physical network"""
from baseCmd import *
from baseResponse import *
class createPhysicalNetworkCmd (baseCmd):
    def __init__(self):
        self.isAsync = "true"
        """the name of the physical network"""
        """Required"""
        self.name = None
        """the Zone ID for the physical network"""
        """Required"""
        self.zoneid = None
        """the broadcast domain range for the physical network[Pod or Zone]. In Acton release it can be Zone only in Advance zone, and Pod in Basic"""
        self.broadcastdomainrange = None
        """domain ID of the account owning a physical network"""
        self.domainid = None
        """the isolation method for the physical network[VLAN/L3/GRE]"""
        self.isolationmethods = []
        """the speed for the physical network[1G/10G]"""
        self.networkspeed = None
        """Tag the physical network"""
        self.tags = []
        """the VLAN for the physical network"""
        self.vlan = None
        self.required = ["name","zoneid",]

class createPhysicalNetworkResponse (baseResponse):
    def __init__(self):
        """the uuid of the physical network"""
        self.id = None
        """Broadcast domain range of the physical network"""
        self.broadcastdomainrange = None
        """the domain id of the physical network owner"""
        self.domainid = None
        """isolation methods"""
        self.isolationmethods = None
        """name of the physical network"""
        self.name = None
        """the speed of the physical network"""
        self.networkspeed = None
        """state of the physical network"""
        self.state = None
        """comma separated tag"""
        self.tags = None
        """the vlan of the physical network"""
        self.vlan = None
        """zone id of the physical network"""
        self.zoneid = None

