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


"""Lists SSL certificates"""
from baseCmd import *
from baseResponse import *
class listSslCertsCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """Account Id"""
        self.accountid = None
        """Id of SSL certificate"""
        self.certid = None
        """Loadbalancer Rule Id"""
        self.lbruleid = None
        self.required = []

class listSslCertsResponse (baseResponse):
    def __init__(self):
        """SSL certificate ID"""
        self.id = None
        """account for the certificate"""
        self.account = None
        """certificate chain"""
        self.certchain = None
        """certificate"""
        self.certificate = None
        """certificate fingerprint"""
        self.fingerprint = None
        """List of loabalancers this certificate is bound to"""
        self.loadbalancerrulelist = None
        """private key"""
        self.privatekey = None

