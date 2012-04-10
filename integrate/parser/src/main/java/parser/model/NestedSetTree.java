package parser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 02.03.12
 */
public class NestedSetTree<T> {
    private long nodeId;

    private long newId() {
        return ++nodeId;
    }

    private List<NSTNode<T>> tree;

    public NestedSetTree() {
        tree = new ArrayList<NSTNode<T>>();
    }

    public Long add(Long parentId, T nodeValue) {
        if (parentId == null) {
            // add root element
            NSTNode holder = new NSTNode<T>(newId(), nodeValue, 1L, 2L, 1L);
            tree.add(holder);
            return holder.getId();
        }

        NSTNode parentNSTNode = getById(parentId);
        if (parentNSTNode == null) {
            return null;
        }

        NSTNode newNSTNode = createNodeForAddLeft(nodeValue, parentNSTNode);

        recalculateIndexesOfRightElements(newNSTNode);
        recalculateIndexesOfParentElements(newNSTNode);

        tree.add(newNSTNode);
//        System.out.println(toString());
//        System.out.println("       ............");

        return newNSTNode.getId();
    }

    private NSTNode createNodeForAddLeft(T nodeValue, NSTNode parentNSTNode) {
        NSTNode leftSibling = getLeftSibling(parentNSTNode);
        NSTNode newNSTNode = null;

        if (leftSibling == null) {   // no siblings
            Long newLeft = parentNSTNode.getLeft() + 1;
            newNSTNode = new NSTNode(newId(), nodeValue, newLeft, newLeft + 1, parentNSTNode.getLevel() + 1);
        } else {    // add to righter of the right sibling
            Long newLeft = leftSibling.getLeft();
            newNSTNode = new NSTNode(newId(), nodeValue, leftSibling.getLeft(), leftSibling.getRight(), leftSibling.getLevel());
        }

        return newNSTNode;
    }

//    private NSTNode getRightSibling(NSTNode parentNSTNode) {
//        NSTNode maxRightSibling = null;
//        long maxRightIndex = 0;
//        for(NSTNode currNSTNode : tree) {
//            if(currNSTNode.getLevel() == parentNSTNode.getLevel() + 1 &&
//                    currNSTNode.getLeft() > parentNSTNode.getLeft() &&
//                    currNSTNode.getRight() < parentNSTNode.getRight() &&
//                    currNSTNode.getRight() > maxRightIndex) {
//                maxRightIndex = currNSTNode.getRight();
//                maxRightSibling = currNSTNode;
//            }
//        }
//
//        return maxRightSibling;
//    }

    private NSTNode getLeftSibling(NSTNode parentNSTNode) {
        NSTNode minLeftSibling = null;
        if (parentNSTNode.getRight() - parentNSTNode.getLeft() == 1) { // check no siblings
            return null;
        }
        long minLeftIndex = parentNSTNode.getRight();
        for (NSTNode currNSTNode : tree) {
            boolean isChild = isChild(parentNSTNode, currNSTNode);
            boolean isNextLevel = currNSTNode.getLevel() == parentNSTNode.getLevel() + 1;
            if (isChild && isNextLevel && currNSTNode.getLeft() < minLeftIndex) {
                minLeftIndex = currNSTNode.getLeft();
                minLeftSibling = currNSTNode;
            }
        }

        return minLeftSibling;
    }

    private boolean isChild(NSTNode parent, NSTNode examine) {
        return examine.getLeft() > parent.getLeft() &&
                examine.getRight() < parent.getRight();
    }

    private void recalculateIndexesOfRightElements(NSTNode newNSTNode) {
        for (NSTNode holder : tree) {
            if (holder.getLeft() >= newNSTNode.getLeft() &&
                    holder.getRight() >= newNSTNode.getRight()) {
                holder.setRight(holder.getRight() + 2);
                holder.setLeft(holder.getLeft() + 2);
            }
        }
    }

    private void recalculateIndexesOfParentElements(NSTNode newNSTNode) {
        for (NSTNode holder : tree) {
            if (holder.getLeft() < newNSTNode.getLeft() &&
                    newNSTNode.getRight() >= holder.getLeft()) {
                holder.setRight(holder.getRight() + 2);
            }
        }
    }

    public void remove() {

    }

    public NSTNode<T> getById(long id) {
        for (NSTNode<T> holder : tree) {
            if (holder.getId() == id) {
                return holder;
            }
        }

        return null;
    }

    public T getByIndex(int index) {
        return tree.get(index).getValue();
    }
    
    public T getRoot() {
        return getById(1L).getValue();
    }

    public int size() {
        return tree.size();
    }


    private StringBuffer toStringChildren(NSTNode holder) {
        List<NSTNode<T>> children = getChildren(holder);
        StringBuffer sb = new StringBuffer();
        if (children.isEmpty()) {
            for (int i = 0; i < holder.getLevel(); i++) {
                sb.append('\t');
            }
            sb.append(holder);
            sb.append('\n');
            return sb;
        } else {
            for (NSTNode child : children) {
                sb.append(toStringChildren(child));
            }
        }


        return sb;
    }

    private List<NSTNode<T>> getChildren(NSTNode parent) {
        ArrayList<NSTNode<T>> chidren = new ArrayList<NSTNode<T>>();
        for (NSTNode<T> node : tree) {
            if (isChild(parent, node)) {
                chidren.add(node);
            }
        }

        return chidren;
    }

    public static List<NSTNode> sortByLeft(List<NSTNode> tree) {
        Collections.sort(tree, new Comparator<NSTNode>() {
            @Override
            public int compare(NSTNode o1, NSTNode o2) {
                if (o1.getLeft() > o2.getLeft()) {
                    return 1;
                }
                if (o1.getLeft() < o2.getLeft()) {
                    return -1;
                }
                return 0;
            }
        });
        return tree;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();//toStringChildren(getById(1L));
        List<NSTNode> sTree = sortByLeft(new ArrayList<NSTNode>(tree));
        for (NSTNode holder : sTree) {
            for (int i = 0; i < holder.getLevel(); i++) {
                sb.append('\t');
            }
            sb.append(holder);
            sb.append('\n');
        }

        return sb.toString();
    }

    static class NSTNode<T> {
        private Long id;
        private Long left;
        private Long right;
        private Long level;
        private T value;

        public NSTNode(Long id, T value, Long left, Long right, long level) {
            this.id = id;
            this.left = left;
            this.right = right;
            this.level = level;
            this.value = value;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getLeft() {
            return left;
        }

        public void setLeft(Long left) {
            this.left = left;
        }

        public Long getRight() {
            return right;
        }

        public void setRight(Long right) {
            this.right = right;
        }

        public Long getLevel() {
            return level;
        }

        public void setLevel(Long level) {
            this.level = level;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "NSTNode{" +
                    "id=" + id +
                    ", left=" + left +
                    ", right=" + right +
                    ", level=" + level +
                    ", value=" + value +
                    '}';
        }
    }
}
