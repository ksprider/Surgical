package com.github.ksprider.surgical.node;

import com.github.ksprider.surgical.TreeHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NodeTreeHandler implements TreeHandler<Node> {

    private final char COMMA = ',';
    private final char OPEN_PAREN = '(';
    private final char CLOSE_PAREN = ')';

    @Override
    public Node treefy(String treeStr) {
        Node root = new Node("root");
        doTreefy(treeStr, root);
        return root;
    }

    private int doTreefy(String treeStr, Node root) {
        int resultIndex = 0;

        for (int nextSymbolIndex = getNextSymbolIndex(treeStr); nextSymbolIndex >= 0; nextSymbolIndex = getNextSymbolIndex(treeStr)) {
            resultIndex += nextSymbolIndex + 1;

            char nextSymbol = treeStr.charAt(nextSymbolIndex);
            switch (nextSymbol) {
                case COMMA: {
                    root.getPropertySet().add(treeStr.substring(0, nextSymbolIndex));
                    treeStr = treeStr.substring(nextSymbolIndex + 1);
                    break;
                }
                case OPEN_PAREN: {
                    Node node = new Node(treeStr.substring(0, nextSymbolIndex));
                    int subIndex = doTreefy(treeStr.substring(nextSymbolIndex + 1), node);
                    root.getNodes().put(node.getName(), node);

                    resultIndex += subIndex;
                    treeStr = treeStr.substring(nextSymbolIndex + 1 + subIndex);
                    break;
                }
                case CLOSE_PAREN: {
                    root.getPropertySet().add(treeStr.substring(0, nextSymbolIndex));
                    return resultIndex;
                }
                default: {
                    root.getPropertySet().add(treeStr);
                    treeStr = "";
                    break;
                }
            }
        }
        return -1;
    }

    private int getNextSymbolIndex(String str) {
        if (Objects.isNull(str)) return Integer.MIN_VALUE;

        int min = str.length() - 1;

        if (str.indexOf(COMMA) >= 0) {
            min = Math.min(min, str.indexOf(COMMA));
        }
        if (str.indexOf(OPEN_PAREN) >= 0) {
            min = Math.min(min, str.indexOf(OPEN_PAREN));
        }
        if (str.indexOf(CLOSE_PAREN) >= 0) {
            min = Math.min(min, str.indexOf(CLOSE_PAREN));
        }

        return min;
    }
}
