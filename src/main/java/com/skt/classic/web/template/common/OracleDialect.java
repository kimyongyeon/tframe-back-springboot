package com.skt.classic.web.template.common;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OracleDialect extends Dialect {

    final static String PAGING_SQL = "select * from ( select row__.*, rownum rownum__ from ( %s ) row__ where rownum <= %s ) where rownum__ > %s";
    final static String PAGING_SQL_FIRST = "select * from ( %s ) where rownum <= %s";
    final static Pattern PTN_FORUPDATE = Pattern.compile("for\\s+update", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    final static Pattern PTN_ORDERBY = Pattern.compile("order\\s+by", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    @Override
    public String getPagingSQL(final String sql, final Pageable pageable) {
        String body = StringUtils.trim(sql);
        body = PTN_FORUPDATE.matcher(body).replaceAll(StringUtils.EMPTY);

        boolean hasOrderby = PTN_ORDERBY.matcher(body).find();
        Sort sort = pageable.getSort();
        if(hasOrderby == false && sort != null) {

            Optional<String> sortSQL = sort.stream()
                    .map(s -> String.format("%s %s",
                            StringEscapeUtils.escapeSql(s.getProperty()),
                            StringEscapeUtils.escapeSql(s.getDirection().name())))
                    .reduce((r, s) ->  r + ", " + s);
            if(sortSQL.isPresent()) {
                body += " order by " + sortSQL.get();
            }
        }

        long offset = pageable.getOffset(),
                pageSize = pageable.getPageSize();
        if(pageable.getOffset() == 0) {
            return String.format(PAGING_SQL_FIRST, body, pageSize);
        }

        return String.format(PAGING_SQL, body, offset + pageSize, offset);
    }

}
