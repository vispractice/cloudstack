-- Andrew ling add the file to the cloudstack`s mysql db,for the develop of the cloudstack in the vispractice company.

use cloud;

-- for the multiline part

CREATE TABLE `multiline` (
`id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
`uuid`  varchar(40)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`label`  varchar(100)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'the multi line label',
`name`  varchar(1024)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'the multi line operators  name',
`is_default`  int(1) NOT NULL DEFAULT 0 COMMENT '1 if multi line label is default',
`route_rule`  varchar(1024)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'this multi line label route rule script',
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2
ROW_FORMAT=COMPACT;

alter table user_ip_address add multiline_label varchar(100) default 'ctcc' comment 'the multiline label';

alter table portable_ip_address add multiline_label varchar(100) default 'ctcc' comment 'the multiline label';

-- for the bandwidth control part



