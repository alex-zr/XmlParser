package parser.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: a.roshchupkin
 * Date: 2/29/12
 */
public class NestedSetTreeTest {
    private NestedSetTree<Integer> tree;

    @Before
    public void setUp() {
        tree = new NestedSetTree<Integer>();
    }

    @Test
    public void testPositiveAdd2CheckSize() {
        tree.add(null, 1);
        tree.add(1L, 2);
        assertEquals(2, tree.size());
    }

    @Test
    public void testPositiveAdd2CheckElements() {
        tree.add(null, 1);
        tree.add(1L, 2);

        assertEquals(new Long(1L), tree.getById(1L).getId());
        assertEquals(new Long(1L), tree.getById(1L).getLeft());
        assertEquals(new Long(4L), tree.getById(1L).getRight());
        assertEquals(new Long(1L), tree.getById(1L).getLevel());

        assertEquals(new Long(2L), tree.getById(2L).getId());
        assertEquals(new Long(2L), tree.getById(2L).getLeft());
        assertEquals(new Long(3L), tree.getById(2L).getRight());
        assertEquals(new Long(2L), tree.getById(2L).getLevel());
    }

    @Test
    public void testPositiveAddToNext3LevelCheckElements() {
        tree.add(null, 1);
        tree.add(1L, 2);
        tree.add(2L, 3);

        assertEquals(new Long(1L), tree.getById(1L).getId());
        assertEquals(new Long(1L), tree.getById(1L).getLeft());
        assertEquals(new Long(6L), tree.getById(1L).getRight());
        assertEquals(new Long(1L), tree.getById(1L).getLevel());

        assertEquals(new Long(2L), tree.getById(2L).getId());
        assertEquals(new Long(2L), tree.getById(2L).getLeft());
        assertEquals(new Long(5L), tree.getById(2L).getRight());
        assertEquals(new Long(2L), tree.getById(2L).getLevel());

        assertEquals(new Long(3L), tree.getById(3L).getId());
        assertEquals(new Long(3L), tree.getById(3L).getLeft());
        assertEquals(new Long(4L), tree.getById(3L).getRight());
        assertEquals(new Long(3L), tree.getById(3L).getLevel());
    }

    @Test
    public void testPositiveAdd1MoreTo2LevelCheckElements() {
        tree.add(null, 1);
        tree.add(1L, 2);
        tree.add(1L, 3);

        System.out.println(tree);

        assertEquals(new Long(1L), tree.getById(1L).getId());
        assertEquals(new Long(1L), tree.getById(1L).getLeft());
        assertEquals(new Long(6L), tree.getById(1L).getRight());
        assertEquals(new Long(1L), tree.getById(1L).getLevel());

        assertEquals(new Long(2L), tree.getById(2L).getId());
        assertEquals(new Long(4L), tree.getById(2L).getLeft());
        assertEquals(new Long(5L), tree.getById(2L).getRight());
        assertEquals(new Long(2L), tree.getById(2L).getLevel());

        assertEquals(new Long(3L), tree.getById(3L).getId());
        assertEquals(new Long(2L), tree.getById(3L).getLeft());
        assertEquals(new Long(3L), tree.getById(3L).getRight());
        assertEquals(new Long(2L), tree.getById(3L).getLevel());

    }

    //TODO add to existing level (to existing siblings)

}
