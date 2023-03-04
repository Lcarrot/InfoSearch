package ru.tyshchenko.infosearch.expression;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BooleanExpression<T> {

    private final Map<String, Set<T>> keyMap;

    public Set<T> resolve(String expr) {
        return getFilesRecursively(RuleSet.simplify(ExprParser.parse(expr)));
    }

    private Set<T> getFilesRecursively(Expression<String> expression) {
        Set<T> leftSet = new HashSet<>();
        Set<T> rightSet = new HashSet<>();
        if (expression.getChildren().size() == 2) {
            leftSet.addAll(getFilesRecursively(expression.getChildren().get(0)));
            rightSet.addAll(getFilesRecursively(expression.getChildren().get(1)));
        }
        if (expression.getChildren().size() == 1) {
            leftSet.addAll(getFilesRecursively(expression.getChildren().get(0)));
        }
        switch (expression.getExprType().toUpperCase()) {
            case "AND" -> {
                return leftSet.stream().filter(rightSet::contains).collect(Collectors.toSet());
            }
            case "OR" -> {
                leftSet.addAll(rightSet);
                return leftSet;
            }
            case "NOT" -> {
                var res = keyMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
                res.removeAll(leftSet);
                return res;
            }
            case "VARIABLE" -> {
                return keyMap.get(expression.getAllK().iterator().next());
            }
            default -> throw new IllegalStateException("Unpredictable operation");
        }
    }
}
