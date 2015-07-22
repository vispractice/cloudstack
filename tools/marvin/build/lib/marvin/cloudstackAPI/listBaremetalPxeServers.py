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


"""list baremetal pxe server"""
from baseCmd import *
from baseResponse import *
class listBaremetalPxeServersCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """Pxe server device ID"""
        self.id = None
        """List by keyword"""
        self.keyword = None
        """"""
        self.page = None
        """"""
        self.pagesize = None
        self.required = []

class listBaremetalPxeServersResponse (baseResponse):
    def __init__(self):
        """device id of"""
        self.id = None
        """the physical network to which this external dhcp device belongs to"""
        self.physicalnetworkid = None
        """name of the provider"""
        self.provider = None
        """url"""
        self.url = None

