/*
 * This file is generated by jOOQ.
*/
package edu.fcmd.generated;


import edu.fcmd.generated.tables.Msmsentry;
import edu.fcmd.generated.tables.records.MsmsentryRecord;

import javax.annotation.Generated;

import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>madcap</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<MsmsentryRecord> KEY_MSMSENTRY_PRIMARY = UniqueKeys0.KEY_MSMSENTRY_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<MsmsentryRecord> KEY_MSMSENTRY_PRIMARY = createUniqueKey(Msmsentry.MSMSENTRY, "KEY_msmsentry_PRIMARY", Msmsentry.MSMSENTRY.NAMEID);
    }
}
