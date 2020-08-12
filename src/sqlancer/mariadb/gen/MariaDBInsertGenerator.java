package sqlancer.mariadb.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.Query;
import sqlancer.common.query.QueryAdapter;
import sqlancer.mariadb.MariaDBErrors;
import sqlancer.mariadb.MariaDBSchema;
import sqlancer.mariadb.MariaDBSchema.MariaDBTable;
import sqlancer.mariadb.ast.MariaDBVisitor;

public final class MariaDBInsertGenerator {

    private MariaDBInsertGenerator() {
    }

    public static Query insert(MariaDBSchema s, Randomly r) {
        MariaDBTable randomTable = s.getRandomTable();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(randomTable.getName());
        sb.append(" VALUES (");
        for (int i = 0; i < randomTable.getColumns().size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            if (Randomly.getBooleanWithSmallProbability()) {
                sb.append(MariaDBVisitor.asString(MariaDBExpressionGenerator.getRandomConstant(r)));
            } else {
                sb.append(MariaDBVisitor.asString(MariaDBExpressionGenerator.getRandomConstant(r,
                        randomTable.getColumns().get(i).getColumnType())));
            }
        }
        sb.append(")");
        ExpectedErrors errors = new ExpectedErrors();
        MariaDBErrors.addInsertErrors(errors);
        return new QueryAdapter(sb.toString(), errors);
    }

}
