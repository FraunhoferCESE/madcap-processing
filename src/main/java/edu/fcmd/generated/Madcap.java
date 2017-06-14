/*
 * This file is generated by jOOQ.
*/
package edu.fcmd.generated;


import edu.fcmd.generated.tables.Foregroundbackgroundentry;
import edu.fcmd.generated.tables.Msmsentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Madcap extends SchemaImpl {

    private static final long serialVersionUID = 1358614060;

    /**
     * The reference instance of <code>madcap</code>
     */
    public static final Madcap MADCAP = new Madcap();

    /**
     * The table <code>madcap.foregroundbackgroundentry</code>.
     */
    public final Foregroundbackgroundentry FOREGROUNDBACKGROUNDENTRY = edu.fcmd.generated.tables.Foregroundbackgroundentry.FOREGROUNDBACKGROUNDENTRY;

    /**
     * The table <code>madcap.msmsentry</code>.
     */
    public final Msmsentry MSMSENTRY = edu.fcmd.generated.tables.Msmsentry.MSMSENTRY;

    /**
     * No further instances allowed
     */
    private Madcap() {
        super("madcap", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Foregroundbackgroundentry.FOREGROUNDBACKGROUNDENTRY,
            Msmsentry.MSMSENTRY);
    }
}
