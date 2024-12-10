package org.examples.utils;

import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;

public class Heartbeats {


    public static void checkTable(Query query, String table) throws Exception {

        Long count = query.select("SELECT COUNT(*) FROM  " + table ).singleResult(Mappers.singleLong());

    }

}
