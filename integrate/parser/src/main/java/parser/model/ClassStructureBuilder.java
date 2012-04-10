package parser.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.common.JarClassLoader;
import parser.common.LogicException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 02.03.12
 */
public class ClassStructureBuilder {
    final static Logger logger = LoggerFactory.getLogger(ClassStructureBuilder.class);
    private List<Class> modelClasses;

    public ClassStructureBuilder(List<Class> modelClasses) {
        if (modelClasses == null) {
            throw new LogicException("Classes list can't be null");
        }
        this.modelClasses = modelClasses;
    }

    public List<NestedSetTree<Class>> buildClassesForest() {
        ArrayList<NestedSetTree<Class>> forest = new ArrayList<NestedSetTree<Class>>(modelClasses.size());

        for (Class clazz : modelClasses) {
            NestedSetTree<Class> classTree = new NestedSetTree<Class>();
            addClass(classTree, null, clazz);
            forest.add(classTree);
            logger.info(classTree.toString());
        }
        return forest;
    }
//
//    private NestedSetTree<Class> buildClassTree(Class clazz) {
//        NestedSetTree<Class> classTree = new NestedSetTree<Class>();
//        Field[] fields = clazz.getDeclaredFields();
//        for(Field field : fields) {
//            Class<?> type = field.getType();
//            String name = field.getName();
//            System.out.println(type + ", " + name);
//        }
//        return classTree;
//    }

    public Long addClass(NestedSetTree<Class> classTree, Long parentId, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Long nodeId = classTree.add(parentId, clazz);
        if (fields.length == 0 || !modelClasses.contains(clazz)) {
            return nodeId;
        }
        for (Field field : fields) {
            addClass(classTree, nodeId, field.getType());
//            System.out.println(field.getType() + ", " + field.getName());
        }

        return nodeId;
    }

//    private ClassData getClassData(Class clazz) {
//        return new ClassData(clazz.getName(), makeFieldsMap(clazz));
//    }
//
//    private Map<String, FieldType> makeFieldsMap(Class clazz) {
//        // TODO implement
//        HashMap<String, FieldType> fieldsMap = new HashMap<String, FieldType>();
//        Field[] fields = clazz.getDeclaredFields();
//        for(Field field : fields) {
//            fieldsMap.put(field.getName(), resolveFieldType(field));
//        }
//        return fieldsMap;
//
//    }
//
//    private FieldType resolveFieldType(Field field) {
//        return null;
//    }

    public static void main(String[] args) {
//        JarClassLoader loader = new JarClassLoader("src/test/resources/parser/common/junit-empty.jar");
        JarClassLoader loader = new JarClassLoader("/home/al1/IdeaProjects/ModelXmlParser/parser/src/test/resources/parser/common/junit-3.8.1.jar");
        ClassStructureBuilder builder = new ClassStructureBuilder(loader.getClasses());

        List<NestedSetTree<Class>> trees = builder.buildClassesForest();
        //System.out.println(trees );
    }
}
