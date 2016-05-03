-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.

-- ;
-- Schema upgrade from 4.3.2 to 4.3.3;
-- ;

-- vispractice add db script

SET foreign_key_checks = 0;
use cloud;

-- add songfor AD machine
alter table load_balancing_rules add service_type varchar(20) default 'VS_MODE_L4' comment 'virtual service mode type,default VS_MODE_L4 /VS_MODE_L7';

-- mutiline
DROP TABLE IF EXISTS `cloud`.`multiline`;

CREATE TABLE `cloud`.`multiline` (
`id`    bigint UNSIGNED NOT NULL AUTO_INCREMENT, 
`uuid`  varchar(40)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , 
`label`  varchar(100)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'the multi line label', 
`name`  varchar(1024)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'the multi line operators  name', 
`is_default`  int(1) NOT NULL DEFAULT 0 COMMENT '1 if multi line label is default', 
`route_rule`  varchar(1024)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'this multiline label route rule script', 
PRIMARY KEY (`id`)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci 
AUTO_INCREMENT=2 
ROW_FORMAT=COMPACT;

INSERT INTO `configuration` VALUES ('Network', 'DEFAULT', 'NetworkManager', 'allow.networks.multiline', 'false', 'Allow networks to use multiline.', 'false', null, 'Global', 1); 

alter table user_ip_address  add multiline_label varchar(100) comment 'the multiline label', add is_default_static_nat int(1) not null default 0 comment 'is default the static nat'; 

alter table portable_ip_address add multiline_label varchar(100) comment 'the multiline label'; 

alter table vlan add multiline_label varchar(100) comment 'the multiline label'; 

alter table portable_ip_range add multiline_label varchar(100) comment 'the multiline label'; 

update user_ip_address set multiline_label=(SELECT label from multiline where is_default=1 limit 1) where multiline_label='' or multiline_label is null; 

update vlan set multiline_label=(SELECT label from multiline where is_default=1 limit 1) where multiline_label='' or multiline_label is null; 

update portable_ip_range set multiline_label=(SELECT label from multiline where is_default=1 limit 1) where multiline_label='' or multiline_label is null;

update portable_ip_address set multiline_label=(SELECT label from multiline where is_default=1 limit 1) where multiline_label='' or multiline_label is null;


INSERT INTO `multiline` VALUES (1,uuid(), 'ctcc', 'dianxin', 1, '27.192.0.0/11,39.64.0.0/11,112.224.0.0/11,120.0.0.0/12,113.224.0.0/12,114.240.0.0/12,115.48.0.0/12,42.224.0.0/12,182.112.0.0/12,123.112.0.0/12,175.160.0.0/12,119.176.0.0/12,110.240.0.0/12,111.192.0.0/12,120.80.0.0/13,171.120.0.0/13,175.16.0.0/13,119.48.0.0/13,221.216.0.0/13,113.0.0.0/13,117.8.0.0/13,125.40.0.0/13,123.152.0.0/13,123.128.0.0/13,123.8.0.0/13,101.64.0.0/13,121.16.0.0/13,112.88.0.0/13,112.80.0.0/13,119.112.0.0/13,220.200.0.0/13,60.16.0.0/13,1.24.0.0/13,27.40.0.0/13,42.176.0.0/13,222.136.0.0/13,27.8.0.0/13,122.136.0.0/13,1.56.0.0/13,60.0.0.0/13,220.248.0.0/14,42.84.0.0/14'); 

INSERT INTO `multiline` VALUES (2,uuid(), 'cucc', 'liantong', 0, '10.204.10.0/24,27.192.0.0/11,39.64.0.0/11,112.224.0.0/11,120.0.0.0/12,113.224.0.0/12,114.240.0.0/12,115.48.0.0/12,42.224.0.0/12,182.112.0.0/12,123.112.0.0/12,175.160.0.0/12,119.176.0.0/12,110.240.0.0/12,111.192.0.0/12,120.80.0.0/13,171.120.0.0/13,175.16.0.0/13,119.48.0.0/13,221.216.0.0/13,113.0.0.0/13,117.8.0.0/13,125.40.0.0/13,123.152.0.0/13,123.128.0.0/13,123.8.0.0/13,101.64.0.0/13,121.16.0.0/13,112.88.0.0/13,112.80.0.0/13,119.112.0.0/13,220.200.0.0/13,60.16.0.0/13,1.24.0.0/13,27.40.0.0/13,42.176.0.0/13,222.136.0.0/13,27.8.0.0/13,122.136.0.0/13,1.56.0.0/13,60.0.0.0/13,220.248.0.0/14,42.84.0.0/14'); 


-- bandwidth

DROP TABLE IF EXISTS `cloud`.`bandwidth_ip_port_map`;
DROP TABLE IF EXISTS `cloud`.`bandwidth_rules`;
DROP TABLE IF EXISTS `cloud`.`bandwidth`;
DROP TABLE IF EXISTS `cloud`.`bandwidth_offering`;

CREATE TABLE `cloud`.`bandwidth` (
  `id` bigint unsigned NOT NULL auto_increment,
  `uuid` varchar(40),
  `multiline_id` bigint unsigned NOT NULL,
  `data_center_id` bigint unsigned NOT NULL,
  `in_traffic` int(10) unsigned COMMENT 'network in traffic rate throttle kbits/s',
  `out_traffic` int(10) unsigned COMMENT 'network out traffic rate throttle kbits/s',
  PRIMARY KEY  (`id`),
  CONSTRAINT `fk_bandwidth__multiline_id` FOREIGN KEY(`multiline_id`) REFERENCES `multiline`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bandwidth__data_center_id` FOREIGN KEY(`data_center_id`) REFERENCES `data_center`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cloud`.`bandwidth_offering` (
  `id` bigint unsigned NOT NULL auto_increment,
  `uuid` varchar(40),
  `data_center_id` bigint unsigned NOT NULL,
  `name` varchar(64) COMMENT 'name of the bandwidth offering',
  `unique_name` varchar(64) UNIQUE COMMENT 'unique name of the bandwidth offering',
  `display_text` varchar(255) NOT NULL COMMENT 'text to display to users',
  `rate` int(10) unsigned COMMENT 'network traffic the lower throttle kbits/s',
  `ceil` int(10) unsigned COMMENT 'network traffic the higher throttle kbits/s',
  `created` datetime NOT NULL COMMENT 'time the entry was created',
  `removed` datetime DEFAULT NULL COMMENT 'time the entry was removed',
  `state` varchar(32) NOT NULL COMMENT 'state of the bandwidth offering',
  PRIMARY KEY  (`id`),
  INDEX `i_bandwidth_offering__removed`(`removed`),
  CONSTRAINT `uc_bandwidth_offering__uuid` UNIQUE (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cloud`.`bandwidth_rules` (
  `id` bigint unsigned NOT NULL auto_increment,
  `uuid` varchar(40),
  `bandwidth_id` bigint unsigned NOT NULL,
  `networks_id` bigint unsigned NOT NULL,
  `bandwidth_offering_id` bigint unsigned,
  `domain_id` bigint unsigned NOT NULL,
  `account_id` bigint unsigned NOT NULL,
  `type` varchar(32) NOT NULL COMMENT 'the traffic type in_traffic and out_traffic',
  `prio` smallint unsigned NOT NULL,
  `rate` int(10) unsigned NOT NULL COMMENT 'network traffic the lower throttle kbits/s',
  `ceil` int(10) unsigned NOT NULL COMMENT 'network traffic the higher throttle kbits/s',
  `traffic_rule_id` int(10) unsigned NOT NULL COMMENT 'The traffic rule id in the VR traffic control tool used.',
  PRIMARY KEY  (`id`),
  CONSTRAINT `fk_bandwidth_rules__bandwidth_id` FOREIGN KEY(`bandwidth_id`) REFERENCES `bandwidth`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bandwidth_rules__networks_id` FOREIGN KEY(`networks_id`) REFERENCES `networks`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bandwidth_rules__bandwidth_offering_id` FOREIGN KEY(`bandwidth_offering_id`) REFERENCES `bandwidth_offering`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cloud`.`bandwidth_ip_port_map` (
  `id` bigint unsigned NOT NULL auto_increment,
  `uuid` varchar(40),
  `bandwidth_rules_id` bigint unsigned NOT NULL,
  `ip_address` char(40) NOT NULL COMMENT 'the ip_address for the bandwidth rule',
  `protocol` char(16) COMMENT 'protocol to open these ports for',
  `start_port` int(10) COMMENT 'starting port of a port range',
  `end_port` int(10) COMMENT 'end port of a port range',
  PRIMARY KEY  (`id`),
  UNIQUE KEY (`bandwidth_rules_id`, `ip_address`, `protocol`, `start_port`, `end_port`),
  CONSTRAINT `fk_bandwidth_ip_port_map__bandwidth_rules_id` FOREIGN KEY(`bandwidth_rules_id`) REFERENCES `bandwidth_rules`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `configuration` VALUES ('Network', 'DEFAULT', 'management-server', 'network.bandwidth.traffic.id.maximum', '9999', 'The bandwidth traffic rule id max number can be use for any one of the virtual router, now only support the max number can up to 9999.', '9999', null, 'Global', 1);


-- shared network with public service

alter table `data_center` add `is_public_service_in_sg_enabled` tinyint NOT NULL DEFAULT 0 COMMENT '1: enabled, 0: not';

DROP VIEW IF EXISTS `cloud`.`data_center_view`;
CREATE VIEW `cloud`.`data_center_view` AS
    select
        data_center.id,
        data_center.uuid,
        data_center.name,
        data_center.is_security_group_enabled,
        data_center.is_public_service_in_sg_enabled,
        data_center.is_local_storage_enabled,
        data_center.description,
        data_center.dns1,
        data_center.dns2,
        data_center.ip6_dns1,
        data_center.ip6_dns2,
        data_center.internal_dns1,
        data_center.internal_dns2,
        data_center.guest_network_cidr,
        data_center.domain,
        data_center.networktype,
        data_center.allocation_state,
        data_center.zone_token,
        data_center.dhcp_provider,
        data_center.removed,
        domain.id domain_id,
        domain.uuid domain_uuid,
        domain.name domain_name,
        domain.path domain_path,
                dedicated_resources.affinity_group_id,
                dedicated_resources.account_id,
                affinity_group.uuid affinity_group_uuid
    from
        `cloud`.`data_center`
            left join
        `cloud`.`domain` ON data_center.domain_id = domain.id
                        left join
        `cloud`.`dedicated_resources` ON data_center.id = dedicated_resources.data_center_id
                        left join
        `cloud`.`affinity_group` ON dedicated_resources.affinity_group_id = affinity_group.id;
