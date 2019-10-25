package com.skt.classic.web.template.common;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.regex.Pattern;

public class MariaDialect extends Dialect {

    final static String PAGING_SQL = "select * from ( %s ) limit %s, %s";
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

        long offset = pageable.getOffset(), // 페이지당 가져올 ROW 수
                pageSize = pageable.getPageSize(); // 페이지 번호

        return String.format(PAGING_SQL, body, offset + pageSize, offset);
    }

}
