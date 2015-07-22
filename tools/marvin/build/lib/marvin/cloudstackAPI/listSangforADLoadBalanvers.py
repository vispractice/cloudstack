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


"""lists SangforAD load balancer devices"""
from baseCmd import *
from baseResponse import *
class listSangforADLoadBalanversCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """List by keyword"""
        self.keyword = None
        """SangforAD load balancer device ID"""
        self.lbdeviceid = None
        """"""
        self.page = None
        """"""
        self.pagesize = None
        """the Physical Network ID"""
        self.physicalnetworkid = None
        self.required = []

class listSangforADLoadBalanversResponse (baseResponse):
    def __init__(self):
        """the management IP address of the external load balancer"""
        self.ipaddress = None
        """device capacity"""
        self.lbdevicecapacity = None
        """true if device is dedicated for an account"""
        self.lbdevicededicated = None
        """device id of the SangforAD load balancer"""
        self.lbdeviceid = None
        """device name"""
        self.lbdevicename = None
        """device state"""
        self.lbdevicestate = None
        """the physical network to which this SangforAD load balancer belongs to"""
        self.physicalnetworkid = None
        """the private interface of the load balancer"""
        self.privateinterface = None
        """name of the provider"""
        self.provider = None
        """the public interface of the load balancer"""
        self.publicinterface = None

