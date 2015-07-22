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


"""Updates a region"""
from baseCmd import *
from baseResponse import *
class updateRegionCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """Id of region to update"""
        """Required"""
        self.id = None
        """updates region with this end point"""
        self.endpoint = None
        """updates region with this name"""
        self.name = None
        self.required = ["id",]

class updateRegionResponse (baseResponse):
    def __init__(self):
        """the ID of the region"""
        self.id = None
        """the end point of the region"""
        self.endpoint = None
        """true if GSLB service is enabled in the region, false otherwise"""
        self.gslbserviceenabled = None
        """the name of the region"""
        self.name = None
        """true if security groups support is enabled, false otherwise"""
        self.portableipserviceenabled = None

