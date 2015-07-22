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


"""List resource detail(s)"""
from baseCmd import *
from baseResponse import *
class listResourceDetailsCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """list by resource id"""
        """Required"""
        self.resourceid = None
        """list by resource type"""
        """Required"""
        self.resourcetype = None
        """list resources by account. Must be used with the domainId parameter."""
        self.account = None
        """list only resources belonging to the domain specified"""
        self.domainid = None
        """if set to true, only details marked with display=true, are returned. Always false is the call is made by the regular user"""
        self.fordisplay = None
        """defaults to false, but if true, lists all resources from the parent specified by the domainId till leaves."""
        self.isrecursive = None
        """list by key"""
        self.key = None
        """List by keyword"""
        self.keyword = None
        """If set to false, list only resources belonging to the command's caller; if set to true - list resources that the caller is authorized to see. Default value is false"""
        self.listall = None
        """"""
        self.page = None
        """"""
        self.pagesize = None
        """list objects by project"""
        self.projectid = None
        self.required = ["resourceid","resourcetype",]

class listResourceDetailsResponse (baseResponse):
    def __init__(self):
        """the account associated with the tag"""
        self.account = None
        """customer associated with the tag"""
        self.customer = None
        """the domain associated with the tag"""
        self.domain = None
        """the ID of the domain associated with the tag"""
        self.domainid = None
        """tag key name"""
        self.key = None
        """the project name where tag belongs to"""
        self.project = None
        """the project id the tag belongs to"""
        self.projectid = None
        """id of the resource"""
        self.resourceid = None
        """resource type"""
        self.resourcetype = None
        """tag value"""
        self.value = None

