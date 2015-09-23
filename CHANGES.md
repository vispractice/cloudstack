Apache CloudStack CHANGES
=========================

Full release notes for each release are located in the project's documentation website:  
http://docs.cloudstack.apache.org

Version 4.3.2
-------------

This is a bug fix release. The following issues were fixed:

    CLOUDSTACK-6738: Add configs in developer prefill to avoid restart mgmt server
    CLOUDSTACK-7517: loading ftp modules in VR
    CLOUDSTACK-7633: fix "Provides" in most LSB headers
    CLOUDSTACK-7658: Upgrading debian packages as part of system vm template build
    CLOUDSTACK-7674 throw an exception when encountered
    CLOUDSTACK-7679: bump up the RabbitMQ AMQP java client version from 2.8.7 to 3.3.5
    CLOUDSTACK-2625, CLOUDSTACK-3401: Usage records are ordered by start_date which is not unique. While listing large datasets or when page size is small this will result in duplicates
    CLOUDSTACK-7855: NIC3 should set MTU and not NIC1 for storage network nic
    CLOUDSTACK-7871: allow VM and template details update using update APIs
    CLOUDSTACK-6438, CLOUDSTACK-6442: XAPI plugins must be copied to XS master first
    CLOUDSTACK-6805: UI > create account > fix a bug that account creation failed when password contains # character.
    CLOUDSTACK-6892: use lowercase noredist, as package.sh lowercases build type
    CLOUDSTACK-6371: Set snapshot size in copycommand answer during snapshot backup
    CLOUDSTACK-6432: Blocking DHCP server to service DNS outside network
    CLOUDSTACK-6761: Fixed removing proxy arp rule on deleting static nat or PF rule on ip
    CLOUDSTACK-7250: [vCenter 5.5] SourceNAT,StaticNAT and Portfowrding is not working with Vmware DVS in vCenter 5.5
    CLOUDSTACK-6652: CLONE - [Automation] Vmware-  System's StartCommand failed with "NumberFormatException" while using VMware DVS
    CLOUDSTACK-5785: VM display name cell not updated upon detaching volume from VM
    CLOUDSTACK-6011: When detach is called on a deleted volume, avoid the NPE and throw an appropriate exception instead
    CLOUDSTACK-7752: Fixed deployment planner stuck in infinite loop. If we create VM with shared service offering and attach disk with local disk offering, and one of storage pool is full
    CLOUDSTACK-7937: CloudStack accepts unauthenticated LDAP binds
    CLOUDSTACK-7822: Fixed SSL Cert Tests and relaxed chain validation
    CLOUDSTACK-7952: Remove private key from SslCertResponse (listSslCerts)
    CLOUDSTACK-3383: Fetch CPU utilization more reliable.
    CLOUDSTACK-7415. Host remains in Alert after vCenter restart.
    CLOUDSTACK-6261: remove the forceful timeout setting when login to NetScaler.
    CLOUDSTACK-7954: ListTags API is ignoring the resourceID and displaying
    CLOUDSTACK-6647: appending instance name with custom supplied info that contains - character can break vmsync.
    CLOUDSTACK-6463: password is not set for VMs created from password enabled template
    CLOUDSTACK-5992: default values of configuraiton parameters in configuration table are set NULL on fresh setup Some configuration parameters have Component names different from fresh
    CLOUDSTACK-6859:Management Server PermGen run out of memory after some time due to class leak
    CLOUDSTACK-6970: Protect event interpretion from causing NPE exception
    CLOUDSTACK-6466: cpu and ram is not getting updated correctly in usage_vm_instance table for usage type 2
    CLOUDSTACK-6669: Support volume resize in usage server
    CLOUDSTACK-6669: Fix support resize in usage server
    CLOUDSTACK-6743: Use edge-triggering in MessageDetector to handle bogus wakeup gracefully. Level triggering plus bogus wakeup can cause a tight loop to spin
    CLOUDSTACK-6075: Increase the ram size for router service offering
    CLOUDSTACK-7966: remove snapshot_store_ref entry, in which role is Primary, during storage GC
    CLOUDSTACK-7917: Validating Load Balancer Rule when updating LB + unit test
    CLOUDSTACK-7951: Limit amount of memory used by cloudstack-agent jsvc
    CLOUDSTACK-7886: cloudstackoperations like deployvm,deleteNW are failing if CS fail to contact rabbit mq server. This is happening in case of Async API calls.
    CLOUDSTACK-7884: Cloudstack MS is not responding (happening randomly) after some restart.
    CLOUDSTACK-7877: The NET.IPRELEASE events are not added to usage_event on IP range deletion from Physical Networks.
    CLOUDSTACK-7872: network getting shutdown inspite of running VM's in the network
    CLOUDSTACK-7869: Add simulator support for findHostsForMigration API
    CLOUDSTACK-7849: Sorting projects alphabetically in drop down menu
    CLOUDSTACK-7837: [UI] Make the Source CIDR column wide enough to fit the CIDR value without ellipsizing
    CLOUDSTACK-7700: Volume Snapshot Async Job returns Success for a failed operation
    CLOUDSTACK-7679: Bump rabbitmq client library to latest 3.4.1
    CLOUDSTACK-7589: VM not Starting and always stuck in Stopped state after management server restarts.
    CLOUDSTACK-7581: Empty 'ID' parameters allowed in API calls
    CLOUDSTACK-7463: UI: Domain Admin UI shows 'Add LDAP Users' button (should not be shown)
    CLOUDSTACK-7319: dd with direct io is less impacting on Dom0 kernel resources
    CLOUDSTACK-7302: UI: Remove Hover Interaction from breadcrumbs at top page
    CLOUDSTACK-7293: UI: Fixed localization issues on the login page
    CLOUDSTACK-7191: On restartNetwork destroy the VR immediatley, instead of cleanup the rules then destroy
    CLOUDSTACK-7144: No GSLB provider is available during assigning load balancing rule
    CLOUDSTACK-7129: Non-admin user can use deleteNetwork with shared networks
    CLOUDSTACK-6996: Adding cluster to legacy zone failed
    CLOUDSTACK-6989: Add 3 strikes rule for RvR freezing detection
    CLOUDSTACK-6908: Enable IPv6 in sysctl when only necessary
    CLOUDSTACK-6869: SSH Public key content is overridden by template's meta data when you create a instance
    CLOUDSTACK-6783: Return a proper LibvirtStoragePool object after creating the pool
    CLOUDSTACK-6714: monitor script echo service command is added with quotes
    CLOUDSTACK-6578: Fixed issue in delete remote access vpn command
    CLOUDSTACK-6577: Disable service monitoring in RVR
    CLOUDSTACK-6516: In 4.3, SSL was turned off by default.
    CLOUDSTACK-6467: Add pre/post-state transition status to messages published on
    CLOUDSTACK-5907, CLOUDSTACK-6396: KVM/RBD & KVM/CLVM volumes mistakenly shown as OVM, disables snapshotting
    CLOUDSTACK-6376: Return empty list when network tier has no ACL list associated.
    CLOUDSTACK-6374: Remove entries from lb vm map when lb rule apply fails
    CLOUDSTACK-6365: support virtual host and ssl in rabbitMQ event bus
    CLOUDSTACK-6328: run.sh check if an existing java process is running, before spawining new ones
    CLOUDSTACK-6322: Don't allow service instance creation with empty or null service-instance "name".
    CLOUDSTACK-6269: [Simulator] Exception "Unable to send command"
    CLOUDSTACK-6236: Negative ref_cnt of template(snapshot/volume)_store_ref results in out-of-range error in Mysql
    CLOUDSTACK-6210: LDAP:listLdapUsers api throws exception when we click on "Add LDAP Account"
    CLOUDSTACK-6192: Return failure on StartCommand and PrepareForMigrationCommand when connectPhysicalDisk fails
    CLOUDSTACK-6172: Volume is not retaining same uuid when migrating from one storage to another.
    CLOUDSTACK-6020: ipv4 address can be a larger number then Interger.MAX_VALUE
    CLOUDSTACK-5962: Value of Global parameter "custom.diskoffering.size.min" is not reflected in UI during new instance creation.
    CLOUDSTACK-5870: API support for retrieving user data
    CLOUDSTACK-5865: Unable to use login API if domainId parameter is id and not uuid
    CLOUDSTACK-5839: return canEnableIndividualService in listNetworkServiceProvidersResponse
    CLOUDSTACK-5821: systemvmiso is locked by systevmvm in hyperv
    CLOUDSTACK-5762: VM wizard, custom compute offering: Fix error label
    CLOUDSTACK-5719: UI > Network > Add Guest Network > when Physical Network dropdown is changed, refresh Network Offering dropdown
    CLOUDSTACK-5576: UI > IP Address > EnableVPN, DisableVPN: change label.
    CLOUDSTACK-5501: Allow one vpn customer gateway with multiple connections
    CLOUDSTACK-5446: delete all the leftover snapshots on primary storage in case of snapshot errors
    CLOUDSTACK-2823: Loop through cmdline when patching routers
    CLOUDSTACK-7412: Can't create proper template from VM on S3 secondary storage environment
    CLOUDSTACK-7903: Decreased minimal usage aggregation range value
    CLOUDSTACK-5834: got VBD statistics from RRD
    CLOUDSTACK-7595: Remove unnecessary multiply factor for job expiry
    CLOUDSTACK-7360: [vmware] Add host to existing cluster fails if the cluster is using Nexus 1000v as backend for atleast one traffic type
    CLOUDSTACK-5812: pass podId information when request for secondary ip address in Basic zone guest network
    CLOUDSTACK-5997: Template state changes side affects
    CLOUDSTACK-5685: reboot VR if a out-of-band power-on event is detected
    CLOUDSTACK-7994: Network rules are not configured in VR after out-of-band movement due to host crash
    CLOUDSTACK-5923: CS doesn't do master switch for XS any more, CS will depend on XS HA to do master switch, XS HA needs to be enabled.
    CLOUDSTACK-7688, CLOUDSTACK-7747: restricted various operations for VM with VM snapshots which breaks VM snapshots.
    CLOUDSTACK-7950: AttachIsoCmd shoud give correct messge when trying to attach vmwaretools installer iso on non supported guestvm deployed by ISO
    CLOUDSTACK-7986: [F5 LB] Failed to execute IPAssocCommand due to com.cloud.utils.exception.ExecutionException
    CLOUDSTACK-7572: Fix regression from 156bd9b
    CLOUDSTACK-7974: remove old hostname entry for a VM when adding a VM
    CLOUDSTACK-8014: Fix NPE searching including removed templates
    CLOUDSTACK-8070: during 4.3.1 to 4.3.2 upgrade encrypt config that are hidden

Version 4.3.1
-------------

This is a bug fix release. The following issues were fixed:

    CLOUDSTACK-6099: live migration is failing for vm deployed using dynaic compute offerings with NPE
    CLOUDSTACK-7528: More verbose logging when sending alert fails
    CLOUDSTACK-6624: set specifyIpRanges to true if specifyVlan is set to true
    CLOUDSTACK-7404: Failed to start an instance when originating template has been deleted 
    CLOUDSTACK-6531: Stopping the router in case of command failures
    CLOUDSTACK-6115: TravisCI configuration
    CLOUDSTACK-7405: allowing VR meta-data to be accessed without trailing slash
    CLOUDSTACK-7260: Management server not responding after some time for Vmware due to Oom (cannot create native thread).
    CLOUDSTACK-7038: Add mysql client dependency for mgmt server pkg for debian
    CLOUDSTACK-6892: Create separate package for the mysql HA component
    CLOUDSTACK-7038: Add mysql client dependency for mgmt server/rpms
    CLOUDSTACK-7193: handle domain ID being an int
    CLOUDSTACK-7309: using findProjectByProjectAccountIdIncludingRemoved
    CLOUDSTACK-6886: Fixed the issue created by the SSL feature with the SDX:
    CLOUDSTACK-6508: impossible to list projects from API with domainid set
    CLOUDSTACK-4725: if storage pool has different path, but the uuid is the same, then treat them as the same storage pool
    CLOUDSTACK-7087: Latest OS X VPN client not working Downgrading openswan version to 1:2.6.37-3
    CLOUDSTACK-6926: removed hard coded jdk dirs and setting java home using readlink and dirname
    CLOUDSTACK-6665: A fix for vpc routers not releasing dhcp leases.
    CLOUDSTACK-6665: A fix for vpc routers not releasing dhcp leases.
    CLOUDSTACK-7006: Restore template ID in ROOT volume usages
    CLOUDSTACK-6747: call a more forgiving test on the supplied peer cidr-list
    CLOUDSTACK-6747: test for test to allow all cidrs on other end of a vpc
    CLOUDSTACK-6747: test to allow all cidrs on other end of vpc, public or
    CLOUDSTACK-6272: Fix icons for recover/restore VM
    CLOUDSTACK-6272: UI > Instance > actions > replace internal action name "restore" with "recover", "reset" with "reinstall".
    CLOUDSTACK-6272: Fix recover/restore VM actions
    CLOUDSTACK-6927: store virsh list in list instead of querying libvirt for each chain to speedup the function
    CLOUDSTACK-6317: [VMware] Tagged VLAN support broken for Management/Control/Storage traffic
    CLOUDSTACK-5891: [VMware] If a template has been registered and "cpu.corespersocket=X" template details have been added for it,
    CLOUDSTACK-6478: Failed to download Template when having 3 SSVM's in one
    CLOUDSTACK-6464: if guest network type is vlan://untagged, and traffic label is used, kvm agent needs to honor traffic label
    CLOUDSTACK-6816: bugfix: cloudstack-setup-management make /root directory's permission 0777 improperly
    CLOUDSTACK-6204: applying missed patch
    CLOUDSTACK-6472: (4.3 specific) listUsageRecords: Pull information from removed items as well, fixing NPEs/Null UUIDs with usage API calls.
    CLOUDSTACK-5976: Typo in "ssh_keypairs" table's foreign key constraints on the Upgraded Setup
    CLOUDSTACK-6240: Fixed updating advanced SG rules for vm nic secondary ip
    CLOUDSTACK-6509: Cannot import multiple LDAP/AD users into a cloudstack account
    CLOUDSTACK-6485: private gateway network should not be associated with vpc
    CLOUDSTACK-6156: removing rampart maven dependencies from awsapi
    CLOUDSTACK-6433: Don't return success if only one of RvR successfully created
    CLOUDSTACK-6285: Fix savepassword.sh script for clear out old entries
    CLOUDSTACK-4665: Check if a snapshot is protected before trying to unprotect
    CLOUDSTACK-4665: Depend on rados-java 0.1.4
    CLOUDSTACK-6375: suppress the prompt while installing libssl
    CLOUDSTACK-6375: suppress the prompt while installing openssl
    CLOUDSTACK-4665: Check if a snapshot is protected before trying to unprotect
    CLOUDSTACK-4665: Depend on rados-java 0.1.4
    CLOUDSTACK-6375: suppress the prompt while installing libssl
    CLOUDSTACK-6360: adding mysql-connector to class path and it will be loaded while starting cloudstack-usage
    CLOUDSTACK-6326: Fixed password visible in plain text in some of commands in Hyper-v Agent logs.
    CLOUDSTACK-6325: [hyper-v] fixed cleaning of bin and obj directories when building with mono, they were not cleaning up and resulting in use of stale dlls in some cases
    CLOUDSTACK-6285: Fix savepassword.sh script for clear out old entries
    CLOUDSTACK-6299: Fixed apidoc info with base64 encoded
    CLOUDSTACK-5743: The link generated for downlading a volume or a template was giving a permission error on hyper-v.

Version 4.3.0
-------------
Please refer to release notes for list of changes 

Version 4.2.0
-------------
Released on October 1 2013. Please refer to Release Notes for list of changes

Version 4.1.0
-------------

This is the second major release of CloudStack from within the Apache Software Foundation, and the
first major release as a Top-Level Project (TLP). 

Build Tool Changes:

 * The project now uses Maven 3 exclusively to build. 

New Features:
* CLOUDSTACK-101: OVS support in KVM
* CLOUDSTACK-132: Mash up marvin into an interactive auto-completing API shell for CloudStack
* CLOUDSTACK-241: AWS Style Regions
* CLOUDSTACK-297: Reset SSH Key to access VM (similar to reset password)
* CLOUDSTACK-299: Egress firewall rules for guest network
* CLOUDSTACK-306: Support SRX & F5 inline mode
* CLOUDSTACK-618: API request throttling to avoid malicious attacks on MS per account through frequent API request.
* CLOUDSTACK-637: AutoScale
* CLOUDSTACK-644: Resize volumes feature
* CLOUDSTACK-706: Persistent Networks without running a VM
* CLOUDSTACK-726: Implement L3 Router functionality in Nicira Nvp Plugin
* CLOUDSTACK-780: Additional VMX Settings
* CLOUDSTACK-926: ApiDiscoverService: Implement a plugin mechanism that exposes the list of APIs through a discovery service on the management server

Bug Fixes:

* CLOUDSTACK-1600 Typo in dpkg-buildpackage command
* CLOUDSTACK-1574 updateResourceCount API is failed saying to specify valida resource type even after parsing the valid resource type
* CLOUDSTACK-1562 Replace the short-cut solution of supportting @DB with the formal one
* CLOUDSTACK-1541 NPE while deleting snapshot :Unexpected exception while executing org.apache.cloudstack.api.command.user.snapshot.DeleteSnapshotCmd
* CLOUDSTACK-1521 Redundant router: Services are not stopped when switch to BACKUP state
* CLOUDSTACK-1509 Failed to implement network elements and resources while provisioning for persistent network(createVlanIpRange to an account]
* CLOUDSTACK-1496 List API Performance: listAccounts failing with OOME for high values of pagesize (>1000 )
* CLOUDSTACK-1487 cloudstack-setup-agent fails to set private.network.device on KVM host add
* CLOUDSTACK-1485 Add Baremetal Provider back to 4.1 branch
* CLOUDSTACK-1484 "API Throttling : api.throttling.enabled, Global setting missing"
* CLOUDSTACK-1473 deleteDomain is failing with NPE
* CLOUDSTACK-1470 unhandled exception executing api command: deployVirtualMachine
* CLOUDSTACK-1469 kvm agent: agent service fails to start up
* CLOUDSTACK-1465 List Zones returns null under create instance when logged is as user
* CLOUDSTACK-1449 listAccounts and listProjectAccounts API lists all the users not account-specific users for each account returned
* CLOUDSTACK-1447 [UI]Persistent Status is not displayed for VPC Tiers
* CLOUDSTACK-1436 4.1 management server fails to start from RPM build artifacts
* CLOUDSTACK-1429 single account is unable to use same vnet across multiple physical networks
* CLOUDSTACK-1425 unhandled exception executing api command: migrateVirtualMachine & recoverVirtualMachine
* CLOUDSTACK-1420 Ensure trademarks are properly attributed in publican brand.
* CLOUDSTACK-1419 Apache-ify and apply trademark logos in the UI
* CLOUDSTACK-1418 "As regular user , we are not allowed to deploy VM on a shared network."
* CLOUDSTACK-1417 "When invalid values are passed to createNetwork() , error message does not indicate the parameter name that has invalid values."
* CLOUDSTACK-1414 Redundant router: BACKUP switch cancelled due to lock timeout after a glitch in network
* CLOUDSTACK-1403 Storage and console-proxy related error
* CLOUDSTACK-1402 listRouters API response doesn't return linklocal IP and public IP details
* CLOUDSTACK-1399 Unhandled exception executing api command: stopVirtualMachine
* CLOUDSTACK-1397 Static Nat configuration is failing with NPE
* CLOUDSTACK-1391 EventBus is not getting injected after javelin merge
* CLOUDSTACK-1383 Deploying basic zone on 4.1 fails in NPE
* CLOUDSTACK-1382 "vm deploy fails with Error ""cannot find DeployPlannerSelector for vm"""
* CLOUDSTACK-1375 deploydb failing with acs master
* CLOUDSTACK-1369 "Ipv6 - In dual Stack network , guest VM does not have the Ipv6 address of the router programmed in /etc/resolv.conf for DNS resolution."
* CLOUDSTACK-1367 NPE noticed in logs while AgentMonitor is monitoring the host ping interval
* CLOUDSTACK-1357 "Autoscale: Provisioned VMs from Netscaler not being added to lb vserver, provserver fails with provserver_err_asynctaskpoll"
* CLOUDSTACK-1350 Management server Stop and start causes previously downloaded ISOs and templates to redownload & reinstall
* CLOUDSTACK-1347 "Not able to delete network. Error - ""Unable to insert queue item into database, DB is full?"""
* CLOUDSTACK-1346 "Check to see if external devices are used in the network, is hardcoded for specific devices"
* CLOUDSTACK-1345 BigSwitch plugin introduces 'VNS' isolation in UI without backend implementation
* CLOUDSTACK-1344 Typo in use.external.dns setting description
* CLOUDSTACK-1343 Porting Baremetal related UI changes to ACS
* CLOUDSTACK-1341 URL for the KEYs file is wrong in the installation guide
* CLOUDSTACK-1339 ASF 4.1: Management server becomes unresponsive
* CLOUDSTACK-1338 Deploy VM failed using ISO
* CLOUDSTACK-1334 vmware.root.disk.controller doesn't work.
* CLOUDSTACK-1332 IPV6 - Router and guest Vms should be able to use an IPV6 address for external DNS entry.
* CLOUDSTACK-1331 Upgrade fails for a 2.2.14 Zone having multiple guest networks using network_tags and Public Vlan
* CLOUDSTACK-1330 ec2-run-instances - When -n option is used to deploy multiple Vms API returns error even though few of the Vms have been deployed successfully.
* CLOUDSTACK-1320 Routers naming convention is changed to hostname
* CLOUDSTACK-1319 createCustomerVpnGateway response gives TypeError: json.createvpncustomergatewayresponse is undefined
* CLOUDSTACK-1315 [F5-SRX-InlineMode] Network implement failed with Run time Exception during network upgrade from VR to SRX-F5
* CLOUDSTACK-1313 Working with Volumes Section Is Missing
* CLOUDSTACK-1312 "Fix rolling upgrades from 4.0 to 4.1 in 4.1 release, fix db schemas to be same as 4.0"
* CLOUDSTACK-1307 Noticed NPE when we put host in maintenance mode in clustered management setup
* CLOUDSTACK-1303 Ipv6 - java.lang.NullPointerException when executing listnetworks() and deployVirtualMachine() after extending the Ipv4 range of a dual stack network.
* CLOUDSTACK-1300 section in wrong order in installation guide
* CLOUDSTACK-1299 Errors in 4.5.5 section of installation guide
* CLOUDSTACK-1295 NPE in usage parsers due to missing @Component inject
* CLOUDSTACK-1289 [F5-SRX-InlineMode] Usage stats are not generated for Juniper SRX Firewall in inlinemode
* CLOUDSTACK-1288 [F5-SRX-InlineMode] classCastException during network restart with cleanup option true
* CLOUDSTACK-1277 ApiResponseHelper.createUserVmResponse failed to populate password field set from UserVm object
* CLOUDSTACK-1272 Autoscale: createAutoScaleVmProfile fails due to unable to retrieve Service Offering id
* CLOUDSTACK-1267 KVM's cloudstack-agent service doesn't log (log4j)
* CLOUDSTACK-1265 logrotate dnsmasq configuration is wrong
* CLOUDSTACK-1262 "Failed to Prepare Secondary Storage in VMware,"
* CLOUDSTACK-1251 Baremetal zone doesn't need primary/secondary storage in UI wizard
* CLOUDSTACK-1243 Failed to cleanup account :java.lang.NullPointerException
* CLOUDSTACK-1242 [F5-SRX-InlineMode] Failed to create LB rule with F5-SRX inlinemode deployement
* CLOUDSTACK-1241 Network apply rules logic is broken
* CLOUDSTACK-1237 "Register Template fails with ""Cannot find template adapter for XenServer"""
* CLOUDSTACK-1234 Unable to start KVM agent with 4.1 build
* CLOUDSTACK-1233 Veewee configuration files are inappropriately identified as ASLv2 licensed files
* CLOUDSTACK-1232 "Ipv6 - Guest Vms are not able to get Ipaddress when executing dhclient command when using ""/96"" network."
* CLOUDSTACK-1226 Error while running Cloudstack-setup-databases
* CLOUDSTACK-1223 Exception while starting jetty server: org.springframework.beans.factory.BeanCreationException Error creating bean with name 'apiServer':
* CLOUDSTACK-1222 API rate limit configs: removed double quote in upgrade script
* CLOUDSTACK-1220 Ipv6 - Better error message when deploy Vm fails to get a free Ip address.
* CLOUDSTACK-1219 Ipv6 - Provide better error messages when deploying a Vm with Ip an address that is outside the network's ip range / if the ip address already is assigned to another Vm.
* CLOUDSTACK-1216 UUID is null for admin and failed to register user key with 4.1
* CLOUDSTACK-1210 Make all pluggable services return list of api cmd classes
* CLOUDSTACK-1206 Failure in Copy of System templates
* CLOUDSTACK-1205 Ipv6 - Ubuntu 12.10 guest Vms looses default route (after it expiration time ~ 30 mts) when ipv6.autoconfig parameters are disabled except for net.ipv6.conf.lo.autoconf which is enabled.
* CLOUDSTACK-1204 Fail to create advance zone due to fail to add host
* CLOUDSTACK-1201 "Failed to create ssh key for user ""cloud"" /var/lib/cloud/management/.ssh/id_rsa and failed to start management server"
* CLOUDSTACK-1190 Make APIChecker interface throw a single sensible exception
* CLOUDSTACK-1181 mvn deploy db failing with NPE
* CLOUDSTACK-1176 Issue with snapshots(create/list)
* CLOUDSTACK-1174 Snapshots related SQL error
* CLOUDSTACK-1173 ConsoleProxyResource instantiation exception
* CLOUDSTACK-1168 Create firewall rule broken
* CLOUDSTACK-1163 Failed with NPE while creating firewall rule
* CLOUDSTACK-1161 Differences between 4.1 and master in ongoing-config-of-external-firewalls-lb.xml
* CLOUDSTACK-1154 Account/Users related API failed due to RegionService inject exception
* CLOUDSTACK-1153 "Ipv6 - Vm deployment fails with ""n must be positive"" error."
* CLOUDSTACK-1152 Missing tag in host-add.xml
* CLOUDSTACK-1141 "Ipv6 - After network restart (and reboot router) , we do not see the existing vms dnsentries not being programmed in the router."
* CLOUDSTACK-1138 "Providing invalid values for gateway, netmask etc in the zoneWizard blocks the VLAN container to load , throwing an error"
* CLOUDSTACK-1123 ListStoragePools API broken by refactor
* CLOUDSTACK-1113 "Ipv6 - Not able to deploy a new VM in this network because of ""Unable to allocate Unique Ipv6 address"""
* CLOUDSTACK-1112 "Errors in ""Prepare the System VM Template"""
* CLOUDSTACK-1111 Ipv6 - listRouters() does not return guestipaddress/
* CLOUDSTACK-1109 "Ipv6 - Unable to expunge User Vms that are ""Destroyed""."
* CLOUDSTACK-1108 Ipv6 - Not able to restart Networks.
* CLOUDSTACK-1107 Ipv6 - Unable to extend Ip range for a Ipv6 network using craeteVlanIpRange() command - Error code 530 returned.
* CLOUDSTACK-1105 "IpV6 - listVirtualMachines() does not return netmask ,gateway,ipaddress."
* CLOUDSTACK-1104 Ipv6 - listVlanIpRanges() returns error 530.
* CLOUDSTACK-1103 "IpV6 - listNetwork() command does not retrun gateway,netmask,cidr"
* CLOUDSTACK-1095 Ipv6 - dhclient command needs to be run manually on the Vms to get the Ipv6 address.
* CLOUDSTACK-1088 EnableStaticNat error will clear the data in database
* CLOUDSTACK-1087 Update the Developer Guide for ASFCS 4.1 Release
* CLOUDSTACK-1083 listUsageRecords api: removed project results in NPE
* CLOUDSTACK-1082 UI doesn't throw any error message when trying to delete ip range from a network that is in use
* CLOUDSTACK-1079 Deploying AWSAPI with mvn -pl :cloud-awsapi jetty:run fails
* CLOUDSTACK-1070 javelin: NPE on executing registerIso API
* CLOUDSTACK-1064 A type error occurs when trying to add account/register template....
* CLOUDSTACK-1063 "SG Enabled Advanced Zone - ""Add Guest Networks"" - When user tries to add a guest Network with scope as ""Account"" , he should NOT be presented with ""Offering for shared security group enabled"""
* CLOUDSTACK-1057 regression of changeServiceForVirtualMachine API - fails to find service offering by serviceOfferingId parameter
* CLOUDSTACK-1056 S3 secondary storage fails to upload systemvm template due to KVMHA directory
* CLOUDSTACK-1055 "The overlay still exists when the ""Recurring Snapshots"" dialog is canceled by pressing esc key."
* CLOUDSTACK-1051 API dispatcher unable to find objectVO corresponding to DeleteTemplatecmd
* CLOUDSTACK-1050 No Documentation on Adding a Load Balancer Rule
* CLOUDSTACK-1037 "Make cloudmonkey awesome-er: Online help docs and api discovery, better colored output, parameter value autocompletion"
* CLOUDSTACK-1029 Enter the token to specified project is malfunctioned
* CLOUDSTACK-1027 """Update SSL certificate"" button should properly reflect it's functionality"
* CLOUDSTACK-1024 Regression: Unable to add Xenserver host with latest build
* CLOUDSTACK-1021 the vlan is not creat to right nic. when i creat multi guest network.
* CLOUDSTACK-1016 Not able to deploy VM.
* CLOUDSTACK-1014 Merge ManagementServer and ManagementServerExt
* CLOUDSTACK-1013 running cloudstack overwrites default public/private ssh keys
* CLOUDSTACK-1011 KVM host getting disconnected in cluster environment
* CLOUDSTACK-1010 Host count and Secondary storage count always shows 1 in UI
* CLOUDSTACK-1002 Not able to start VM.
* CLOUDSTACK-995  Not able to add the KVM host.
* CLOUDSTACK-993  """admin"" user is not getting created when management server is started."
* CLOUDSTACK-987  Sections missing in Working With Snapshots
* CLOUDSTACK-985  Different MAC address for RvR caused issue in short term network outrage
* CLOUDSTACK-978  TypeError: instance.displayname is undefined while adding VM's to the LB rule.
* CLOUDSTACK-968  marvin: vlan should be an attribute of the physical_network and not the zone
* CLOUDSTACK-959  Missing sub-sections in document section System Service Offerings
* CLOUDSTACK-938  s2s VPN trouble
* CLOUDSTACK-928  [Simulator] Latency for Agent Commands - change unit of wait from seconds to milliseconds
* CLOUDSTACK-863  Non-printable characters (ASCII control character) such as %00 or %0025 are getting stored in raw/non encoded form in the database.
* CLOUDSTACK-819  Create Account/User API logging password in access logs
* CLOUDSTACK-799  [Load Test] Check router statistics falls behind in gathering stats by more than 2 times the set value
* CLOUDSTACK-798  Move usage related cmd classes from cloud-server to cloud-api
* CLOUDSTACK-736  Integration smoke tests: Fix check for vm name for the deployvm smoke test
* CLOUDSTACK-734  api_refactoring: CreateAccountCmd fails to send response due to NPE in service layer
* CLOUDSTACK-725  UI: Error when the Egress rules tab is selected for a network
* CLOUDSTACK-721  Bytes sent/received in user statistics is empty (CloudStack 4.0)
* CLOUDSTACK-720  Fail to load a png image when accessing the web console
* CLOUDSTACK-717  cloudmonkey fails to parse/print response
* CLOUDSTACK-693  Adding a VPC virtual router to a NiciraNVP enabled network fails
* CLOUDSTACK-691  A warning dialog box shows after reloading the welcome page
* CLOUDSTACK-689  RVR: Stop pending flag is not cleared when user start the disconnected router from another host
* CLOUDSTACK-683  Image Is Missing in the Accessing VM Section
* CLOUDSTACK-660  Network Traffic Labels are not functional in Marvin
* CLOUDSTACK-648  The normal users could change their own login password
* CLOUDSTACK-639  API Refactoring: Adapters for ACL
* CLOUDSTACK-617  Unable to edit a Sub domain
* CLOUDSTACK-614  "ListTemplates API is not returning ""Enable SSH Key"" attribute for any given template"
* CLOUDSTACK-606  Starting VM fails with 'ConcurrentOperationException' in a clustered MS scenario
* CLOUDSTACK-605  Host physical CPU is incorrectly calculated for Vmware hosts
* CLOUDSTACK-599  DhcpEntryCommand fails on Router VM on CS4.0 and vSphere5 with Advanced Network Zone
* CLOUDSTACK-596  DeployVM command takes a lot of time to return job id
* CLOUDSTACK-584  "typos in ""Apache_CloudStack-4.0.0-incubating-CloudStack_Nicira_NVP_Guide-en-US"""
* CLOUDSTACK-573  "NPE at ""com.cloud.network.NetworkManagerImpl.networkOfferingIsConfiguredForExternalNetworking(NetworkManagerImpl.java:4345)"" when create network from the network offering having NULL provider for the service"
* CLOUDSTACK-572  SG Enabled Advanced Zone - Not able to deploy a VM in an account specific shared network.
* CLOUDSTACK-560  Usage server doesn't work in 4.0.0 due to missing db changes
* CLOUDSTACK-556  Erratic window behavior in Quick View tooltip
* CLOUDSTACK-553  "SRX - When adding SRX device make ""Public Network"" - default to ""untrusted"" and ""Private Network"" - default to ""trusted"" as un-editable fields."
* CLOUDSTACK-552  ]Quick view details for a volume displays scroll bar in place of name of the volume when the name of the volume has more no of characters
* CLOUDSTACK-539  Cropped Text in UI under Quick View
* CLOUDSTACK-536  remove citrix cloudpatform from 4.0 build - CloudStack is ASF project
* CLOUDSTACK-527  List API performance optimization by using DB views and removing UUID conversion.
* CLOUDSTACK-522  Log requests in cloudmonkey's log file
* CLOUDSTACK-520  Dependency jar names mismatch with install-non-oss.sh
* CLOUDSTACK-518  API refactoring -- change @Parameter annotation and remove the @IdentityMapper annotation
* CLOUDSTACK-514  Marvin and Cloudmonkey don't work when an API target uses https or an alternate path
* CLOUDSTACK-510  Add button not visible when adding public IPs to physical network
* CLOUDSTACK-508  CLVM copies template to primary storage unnecessarily
* CLOUDSTACK-507  fix api docs for listSSHKeyPairs
* CLOUDSTACK-504  Duplicate guest password scripts in codebase
* CLOUDSTACK-501  Apidocs and marvin does not know how to handle Autoscaling docs
* CLOUDSTACK-500  Passwd-server iptables rules are dropped on domr on fresh start or on reboot
* CLOUDSTACK-499  cloudmonkey CLI can't accept complex parameters
* CLOUDSTACK-493  2.2.x-3.0 DB upgrade support for Advance SG enabled networks
* CLOUDSTACK-481  Installation Guide Doc Error
* CLOUDSTACK-467  Developer's Guide points to cloud.com for API reference
* CLOUDSTACK-465  French language file quotes are dropping javascript syntax errors
* CLOUDSTACK-464  "Regression in AWSAPI docs, entire sections removed"
* CLOUDSTACK-462  A few corrections to make to the 4.0.0 installation guide
* CLOUDSTACK-459  [Optional Public IP assignment for EIP with Basic Zone] Associate IP Checkbox in Create Network Offering Dialog is Displayed When Elastic LB is Selected
* CLOUDSTACK-456  License tag in SPEC isn't what RPM is expecting
* CLOUDSTACK-448  SSVM bootstrap failure on XenServer hosts with E3 CPU
* CLOUDSTACK-446  "Host going to alert state, if you are adding already added host"
* CLOUDSTACK-441  Running mgmt server using jetty fails to start api server
* CLOUDSTACK-435  Vmware network labels are ignored when creating a Zone using basic networking
* CLOUDSTACK-427  Change hardcoded step number references to dynamic links
* CLOUDSTACK-424  Updated userdata not propagating to the VR.
* CLOUDSTACK-417  Handle password server securely to run on port 8080 on VR
* CLOUDSTACK-416  XCP 1.6beta2 (61002c) - can't add a host
* CLOUDSTACK-404  Update docs on the usage of cloud-setup-databases
* CLOUDSTACK-398  Install Guide: Section 11.17.3 (Using VPN with Mac OSX): Not complete?
* CLOUDSTACK-397  Install Guide: Section 11.1 (Guest Traffic): Diagram is the wrong diagram
* CLOUDSTACK-390  Install Guide: Section 4.5.7 (Prepare the System VM Template): Links go to cloud.com
* CLOUDSTACK-378  mavenize marvin on master
* CLOUDSTACK-377  provide deployment config access to marvin's testcase
* CLOUDSTACK-373  "static NAT and Firewall is not working on external firewall device SRX, it needs to be implemented"
* CLOUDSTACK-369  ASF 4.0 - unable to support XenServer 6.1 host
* CLOUDSTACK-364  Docs point to download.cloud.com for AWS API script
* CLOUDSTACK-361  Wrong creation of guest networks on a KVM host in Multiple Physical Networks with guest traffic
* CLOUDSTACK-359  PropagateResourceEventCommand failes in cluster configuration
* CLOUDSTACK-357  "ISOs can be deleted while still attached to a running VM, and they subsequently cannot be detached from a running VM"
* CLOUDSTACK-355  "Fix ""count"" in a bunch of API commands"
* CLOUDSTACK-348  deleteNetwork does not clean up network resource count correctly
* CLOUDSTACK-347  listNetworks API: return vlan information only when the caller is ROOT admin
* CLOUDSTACK-346  Cannot add Vmware cluster with class loader conflict exception
* CLOUDSTACK-335  KVM VPC load balancer not working
* CLOUDSTACK-333  When Datacenter name in VCenter has spaces Primary Storage (VMFS) discovery will fail
* CLOUDSTACK-332  """count"" property in list* API response should be equal to how many entries in database, not how many objects in API response"
* CLOUDSTACK-318  Adding XenServer Host Fails - 6.0.2 fails with 4.0.0
* CLOUDSTACK-304  Add synchronization for createSnapshot command per host basis
* CLOUDSTACK-293  "We do awful, hacky things in our spec file for client"
* CLOUDSTACK-290  3.0.0 template also needed for 2.2.14 to 3.0.5 direct upgrade.
* CLOUDSTACK-284  listVirtualMachines does not return deleted machines when zone is specified
* CLOUDSTACK-279  deleteProject fails when executed by the regular user (works fine for root/domain admin)
* CLOUDSTACK-274  Two error codes mapped to same value in API
* CLOUDSTACK-271  updatePhysicalNetwork dies with an NPE when the vlan range is empty
* CLOUDSTACK-256  "vpn:As an admin user , not able to delete VPN user which is present in a regular user's network."
* CLOUDSTACK-250  Incorrect description of maintenance mode in admin guide
* CLOUDSTACK-249  Add host id to failed VM deploy alerts
* CLOUDSTACK-235  Network rate can be set in 2 places. Clarify docs on how this works.
* CLOUDSTACK-232  Zone infrastructure chart -- disable resource total display
* CLOUDSTACK-228  UI provides an option to reconnect a disconnected host - ServerApiException is thrown on an attempt
* CLOUDSTACK-227  ReconnectHostCmd: NullPointerException: Unable to get host Information for XenServer 6.0.2 host - on intentionally changing the traffic labels on the physical network
* CLOUDSTACK-226  UpdatePhysicalNetworkcommand failed due to java.sql.BatchUpdateException ; Tried to extend the existing Guest VLAN Range of one physical network into the Guest VLAN range of the other physical network
* CLOUDSTACK-225  API Docs: Request params repeated with different descriptions
* CLOUDSTACK-222  Admin UI prompts to restart Management server with cancel edit operation
* CLOUDSTACK-178  Expose name parameter of VM in list Vm view.
* CLOUDSTACK-130  Clarify docs on tags parameter in API reference
* CLOUDSTACK-119  Move Agent-Simulator in to the hypervisor plugin model
* CLOUDSTACK-118  "Status of host resorce stuck in ""ErrorInMaintenance"""
* CLOUDSTACK-95   IP address allocation not working when a user tries to allocate IP addresses in a Project.
* CLOUDSTACK-70   Improve Network Restart Behaviour for Basic Zone: Restarting Network Fails
* CLOUDSTACK-46   Remnants of mycloud remain

Security Fixes:

 * CVE-2012-4501: Apache CloudStack configuration vulnerability


Version 4.0.2
------------------------

This is a maintenance release for the Apache CloudStack 4.0.x series, with no new features.

Issues fixed in this release:

* CLOUDSTACK-354: Display of storage statistics is wrong.
* CLOUDSTACK-397: Install Guide: Section 11.1 (Guest Traffic): Diagram is the wrong diagram
* CLOUDSTACK-398: Install Guide: Section 11.17.3 (Using VPN with Mac OSX): Not complete?
* CLOUDSTACK-462: A few corrections to make to the 4.0.0 installation guide
* CLOUDSTACK-524: http proxy used by ssvm (secstorage.proxy) NOT working
* CLOUDSTACK-587: MEMORY_CONSTRAINT_VIOLATIONMemory limits must satisfy:
* CLOUDSTACK-803: HA gets triggered even when the host investigator is unable to determine the state of the host
* CLOUDSTACK-810: Make DirectAgent thread pool size configurable
* CLOUDSTACK-976: unable to start cloudstack (error: "java.lang.NoSuchMethodError: org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString([B)Ljava/lang/String;")
* CLOUDSTACK-988: HV version must be updated in hypervisor_version column of host table
* CLOUDSTACK-990: Documentation issue with libvirtd.conf tcp_port configuration
* CLOUDSTACK-1088: EnableStaticNat error will clear the data in database
* CLOUDSTACK-1106: Missing documentation for cloud-setup-databases
* CLOUDSTACK-1110: Documentation missing "Management Server Load Balancing"
* CLOUDSTACK-1112: Errors in "Prepare the System VM Template"
* CLOUDSTACK-1137: Force reconnect of a disconnected state complains about the state of the host.
* CLOUDSTACK-1150: Documentation for libvirt on Ubuntu 12.04
* CLOUDSTACK-1151: vmware systemVm template upgrade is missing in 4.0 upgrade
* CLOUDSTACK-1211: Network operations are Blocked for the Read-only file system of Virtual Router
* CLOUDSTACK-1265: logrotate dnsmasq configuration is wrong
* CLOUDSTACK-1291: duplicate arguments in commands.xml prevents cloudapis.py to run to completion
* CLOUDSTACK-1298: typo in deb package setup
* CLOUDSTACK-1299: Errors in 4.5.5 section of installation guide
* CLOUDSTACK-1300: section in wrong order in installation guide
* CLOUDSTACK-1341: URL for the KEYs file is wrong in the installation guide
* CLOUDSTACK-1419: Apache-ify and apply trademark logos in the UI
* CLOUDSTACK-1420: Ensure trademarks are properly attributed in publican brand.
* CLOUDSTACK-1589: Ubuntu 4.0 packages depend on non-existent chkconfig
* CLOUDSTACK-1629: Need to move location or conflict with antlr
* CLOUDSTACK-1642: Add support CentOS 6.4
* CLOUDSTACK-1648: Unable to add KVM host
* CLOUDSTACK-1652: /etc/hosts error in virtual router when deploy instance with the name same to previous instances
* CLOUDSTACK-1666: KVM VPC NetworkUsage doesnot work
* CLOUDSTACK-1668: IP conflict in VPC tier
* CLOUDSTACK-1761: Available local storage disk capacity incorrectly reported in KVM to manager.
* CLOUDSTACK-1845: KVM - storage migration often fails
* CLOUDSTACK-1846: KVM - storage pools can silently fail to be unregistered, leading to failure to register later
* CLOUDSTACK-2003: Deleting domain while deleted account is cleaning up leaves VMs expunging forever due to 'Failed to update resource count'
* CLOUDSTACK-2090: Upgrade from version 4.0.1 to version 4.0.2 triggers the 4.0.0 to 4.0.1. 
* CLOUDSTACK-2091: Error in API documentation for 4.0.x.


Version 4.0.1-incubating
------------------------

This is a bugfix release for Apache CloudStack 4.0.0-incubating, with no new features. 

Security Fixes:

* CVE-2012-5616: Local Information Disclosure Vulnerability (See CLOUDSTACK-505)

Bugs fixed in this release:

* CLOUDSTACK-359: PropagateResourceEventCommand fails in cluster configuration
* CLOUDSTACK-374: When running cloud-setup-databases, it auto chooses the highest priority nic (lowest number ie: eth0)
* CLOUDSTACK-389: Install Guide: Section 4.5.5 (Prepare NFS Shares): Confusing statement about iSCSI
* CLOUDSTACK-395: Primary Storage and Secondary Storage sections missing sub-sections
* CLOUDSTACK-411: Add another step during kvm agent installation on Ubuntu machine
* CLOUDSTACK-415: restartNetwork call causes VM to be unreachable when Nicira based SDN is used.
* CLOUDSTACK-422: XSL files missing license header.
* CLOUDSTACK-426: SetVPCStaticNatRules unimplemented for KVM.
* CLOUDSTACK-448: SSVM bootstrap failure on XenServer hosts with E3 CPU.
* CLOUDSTACK-465: French language file quotes are dropping javascript syntax errors.
* CLOUDSTACK-473: API Doc for uploadCustomCertificate doesn't explain how to use the optional parameters well.
* CLOUDSTACK-480: Installation Documentation error: Section 4.5.5.2 needs to mention nfs-kernel-server.
* CLOUDSTACK-481: Installation Guide Doc Error
* CLOUDSTACK-498: Missing dependency in RPM of KVM Agent.
* CLOUDSTACK-502: VPC router needs to resolve its hostname.
* CLOUDSTACK-505: cloudstack logs the private key in plaintext.
* CLOUDSTACK-507: fix api docs for listSSHKeyPairs.
* CLOUDSTACK-515: NVP installation.
* CLOUDSTACK-536: remove citrix cloudpatform from 4.0 build - CloudStack is ASF project.
* CLOUDSTACK-560: Usage server doesn't work in 4.0.0 due to missing db changes.
* CLOUDSTACK-580: Packages are named with 4.0 with 4.0.1 build.
* CLOUDSTACK-591: Wrong vnet in iptables on KVM hypervisors after VM reboot.
* CLOUDSTACK-595: Recreate root volume scenarios doesn't work in VMware
* CLOUDSTACK-603: Upgrade from 4.0 to 4.0.1 is not enabled.
* CLOUDSTACK-605: Host physical CPU is incorrectly calculated for VMware host
* CLOUDSTACK-622: In the add primary storage dialog in the ui the RBD fields don't disappear when changing from RBD to another protocol.
* CLOUDSTACK-683: Image is missing in the Accessing VM Section
* CLOUDSTACK-685: CloudStack 4.0 Network Usage is ZERO
* CLOUDSTACK-938: s2s VPN trouble
* CLOUDSTACK-961: Installation docs don't detail dependencies for building RPMs
* CLOUDSTACK-995: Not able to add the KVM host


Version 4.0.0-incubating
------------------------

This is the first release of CloudStack from within the Apache Software Foundation.

Build Tool Changes:

 * The project now uses a combination of maven3 and ant for building
 * License header auditing is now implemented via the Apache RAT Maven plugin
 * Some integrations have been disabled in the default build, due to the license types of our dependencies (See README.md for details on how to build with the optional capabilities)

New Features:

 * Inter-VLAN Routing (VPC)
 * Site-to-Site VPN
 * Local Storage Support for Data Volumes
 * Virtual Resource Tagging
 * Secure Console Access on XenServer
 * Added the ability to create a VM without immediately starting it (via API)
 * Upload an Existing Volume to a Virtual Machine
 * Dedicated High-Availability Hosts
 * Support for Amazon Web Services API (formerly a separate package)
 * AWS API Extensions to include Tagging
 * Support for Nicira NVP (L2)
 * Ceph RBD Support for KVM
 * Support for Caringo as Secondary Storage
 * KVM Hypervisor support upgraded to work with Ubuntu 12.04 and RHEL 6.3

Security Fixes:

 * CVE-2012-4501: Apache CloudStack configuration vulnerability
