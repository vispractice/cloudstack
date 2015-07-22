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


"""Accepts or declines project invitation"""
from baseCmd import *
from baseResponse import *
class updateProjectInvitationCmd (baseCmd):
    def __init__(self):
        self.isAsync = "true"
        """id of the project to join"""
        """Required"""
        self.projectid = None
        """if true, accept the invitation, decline if false. True by default"""
        self.accept = None
        """account that is joining the project"""
        self.account = None
        """list invitations for specified account; this parameter has to be specified with domainId"""
        self.token = None
        self.required = ["projectid",]

class updateProjectInvitationResponse (baseResponse):
    def __init__(self):
        """any text associated with the success or failure"""
        self.displaytext = None
        """true if operation is executed successfully"""
        self.success = None

