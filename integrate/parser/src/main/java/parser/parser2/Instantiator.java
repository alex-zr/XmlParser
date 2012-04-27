package parser.parser2;

import parser.common.ParseException;
import parser.types.Type;
import parser.types.TypeFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 25.04.12
 */
public class Instantiator {
    private Map<String, Class> classesMap;

    public Instantiator(Map<String, Class> classesMap) {
        this.classesMap = classesMap;
    }

    /**
     * Recursive build object by class name and object content
     *
     * @param record
     * @return
     */
    public Object instanceObject(Record record, Object object) throws InstantiationException, IllegalAccessException {
        if (record instanceof FieldRecord) {
            String fieldName = record.getName();
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                Type fieldType = TypeFactory.getType(field.getType().getName());
                fieldType.setValue(object, field, record.getValue().getContent());
            } catch (NoSuchFieldException e) {
                throw new ParseException("Field " + fieldName + " not found", e);
            }
        }  else if (record instanceof ListRecord) {
            ListRecord listRecord = (ListRecord)record;
            ArrayList list = new ArrayList();
            for(Record rec : listRecord.getValues()) {
                if(rec instanceof ClassRecord) { // include list and set
                    list.add(instanceObject(rec, null));
                } else {
                    throw new ParseException("List contains the illegal element " + rec.getName());
                }
            }
            return list;
        } else if (record instanceof SetRecord) {

        } else if (record instanceof ClassRecord) {
            Class clazz = classesMap.get(record.getName());
            if (clazz != null) {
                Object obj = clazz.newInstance();
                for(Record rec : ((ClassRecord) record).getValues()) {
                    if(!(rec instanceof FieldRecord)) {
                        throw new ParseException("Class contains not a field " + rec.getName());
                    }
                    instanceObject(rec, obj);
                }
                return obj;
            }
            throw new InstantiationException("Can't find " + record.getName() + " class");
        } /*else if(record instanceof ValueRecord) {

        }*/
        return object;
        //throw new ParseException("Unknown record " + record);

    }
}
