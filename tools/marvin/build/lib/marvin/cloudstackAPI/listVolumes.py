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


"""Lists all volumes."""
from baseCmd import *
from baseResponse import *
class listVolumesCmd (baseCmd):
    def __init__(self):
        self.isAsync = "false"
        """list resources by account. Must be used with the domainId parameter."""
        self.account = None
        """list only resources belonging to the domain specified"""
        self.domainid = None
        """list volumes on specified host"""
        self.hostid = None
        """the ID of the disk volume"""
        self.id = None
        """defaults to false, but if true, lists all resources from the parent specified by the domainId till leaves."""
        self.isrecursive = None
        """List by keyword"""
        self.keyword = None
        """If set to false, list only resources belonging to the command's caller; if set to true - list resources that the caller is authorized to see. Default value is false"""
        self.listall = None
        """the name of the disk volume"""
        self.name = None
        """"""
        self.page = None
        """"""
        self.pagesize = None
        """the pod id the disk volume belongs to"""
        self.podid = None
        """list objects by project"""
        self.projectid = None
        """the ID of the storage pool, available to ROOT admin only"""
        self.storageid = None
        """List resources by tags (key/value pairs)"""
        self.tags = []
        """the type of disk volume"""
        self.type = None
        """the ID of the virtual machine"""
        self.virtualmachineid = None
        """the ID of the availability zone"""
        self.zoneid = None
        self.required = []

class listVolumesResponse (baseResponse):
    def __init__(self):
        """ID of the disk volume"""
        self.id = None
        """the account associated with the disk volume"""
        self.account = None
        """the date the volume was attached to a VM instance"""
        self.attached = None
        """the date the disk volume was created"""
        self.created = None
        """the boolean state of whether the volume is destroyed or not"""
        self.destroyed = None
        """the ID of the device on user vm the volume is attahed to. This tag is not returned when the volume is detached."""
        self.deviceid = None
        """bytes read rate of the disk volume"""
        self.diskBytesReadRate = None
        """bytes write rate of the disk volume"""
        self.diskBytesWriteRate = None
        """io requests read rate of the disk volume"""
        self.diskIopsReadRate = None
        """io requests write rate of the disk volume"""
        self.diskIopsWriteRate = None
        """the display text of the disk offering"""
        self.diskofferingdisplaytext = None
        """ID of the disk offering"""
        self.diskofferingid = None
        """name of the disk offering"""
        self.diskofferingname = None
        """an optional field whether to the display the volume to the end user or not."""
        self.displayvolume = None
        """the domain associated with the disk volume"""
        self.domain = None
        """the ID of the domain associated with the disk volume"""
        self.domainid = None
        """Hypervisor the volume belongs to"""
        self.hypervisor = None
        """true if the volume is extractable, false otherwise"""
        self.isextractable = None
        """max iops of the disk volume"""
        self.maxiops = None
        """min iops of the disk volume"""
        self.miniops = None
        """name of the disk volume"""
        self.name = None
        """The path of the volume"""
        self.path = None
        """the project name of the vpn"""
        self.project = None
        """the project id of the vpn"""
        self.projectid = None
        """need quiesce vm or not when taking snapshot"""
        self.quiescevm = None
        """the display text of the service offering for root disk"""
        self.serviceofferingdisplaytext = None
        """ID of the service offering for root disk"""
        self.serviceofferingid = None
        """name of the service offering for root disk"""
        self.serviceofferingname = None
        """size of the disk volume"""
        self.size = None
        """ID of the snapshot from which this volume was created"""
        self.snapshotid = None
        """the state of the disk volume"""
        self.state = None
        """the status of the volume"""
        self.status = None
        """name of the primary storage hosting the disk volume"""
        self.storage = None
        """id of the primary storage hosting the disk volume; returned to admin user only"""
        self.storageid = None
        """shared or local storage"""
        self.storagetype = None
        """type of the disk volume (ROOT or DATADISK)"""
        self.type = None
        """id of the virtual machine"""
        self.virtualmachineid = None
        """display name of the virtual machine"""
        self.vmdisplayname = None
        """name of the virtual machine"""
        self.vmname = None
        """state of the virtual machine"""
        self.vmstate = None
        """ID of the availability zone"""
        self.zoneid = None
        """name of the availability zone"""
        self.zonename = None
        """the list of resource tags associated with volume"""
        self.tags = []
        """the ID of the latest async job acting on this object"""
        self.jobid = None
        """the current status of the latest async job acting on this object"""
        self.jobstatus = None

class tags:
    def __init__(self):
        """"the account associated with the tag"""
        self.account = None
        """"customer associated with the tag"""
        self.customer = None
        """"the domain associated with the tag"""
        self.domain = None
        """"the ID of the domain associated with the tag"""
        self.domainid = None
        """"tag key name"""
        self.key = None
        """"the project name where tag belongs to"""
        self.project = None
        """"the project id the tag belongs to"""
        self.projectid = None
        """"id of the resource"""
        self.resourceid = None
        """"resource type"""
        self.resourcetype = None
        """"tag value"""
        self.value = None

