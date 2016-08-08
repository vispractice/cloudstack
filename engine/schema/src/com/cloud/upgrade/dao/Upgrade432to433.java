package com.cloud.upgrade.dao;

import java.io.File;
import java.sql.Connection;

import org.apache.log4j.Logger;

import com.cloud.utils.exception.CloudRuntimeException;
import com.cloud.utils.script.Script;
/**
 * andrew ling add, the vispractice company develop the cloudstack branch 4.3.3 db update
 * **/
public class Upgrade432to433  implements DbUpgrade {
    final static Logger s_logger = Logger.getLogger(Upgrade432to433.class);
    @Override
    public String[] getUpgradableVersionRange() {
        return new String[] {"4.3.2", "4.3.3"};
    }

    @Override
    public String getUpgradedVersion() {
        return "4.3.3";
    }

    @Override
    public boolean supportsRollingUpgrade() {
        return false;
    }

    @Override
    public File[] getPrepareScripts() {
        String script = Script.findScript("", "db/schema-432to433.sql");
        if (script == null) {
            throw new CloudRuntimeException("Unable to find db/schema-432to433.sql");
        }

        return new File[] {new File(script)};
    }

    @Override
    public void performDataMigration(Connection conn) {

    }

    @Override
    public File[] getCleanupScripts() {
        return null;
    }

}
