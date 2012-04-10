package parser.common;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 02.03.12
 */
public class JarClassLoaderTest {

    @Test
    public void testPositiveReadJUnit() throws IOException {
        JarClassLoader loader = new JarClassLoader("src/test/resources/parser/common/junit-3.8.1.jar");
//        JarClassLoader loader = new JarClassLoader("/home/al1/IdeaProjects/ModelXmlParser/parser/src/test/resources/parser/common/junit-3.8.1.jar");

        List<Class> classes = loader.getClasses();
        List<String> classNames = getJUnitJarClassesList();
        Iterator<String> namesItr = classNames.iterator();
        for (Class clazz : classes) {
            if (namesItr.hasNext() && !clazz.getName().contains(namesItr.next())) {
                fail();
            }
        }
    }

    @Test
    public void testNegativeEmptyJar() throws IOException {
        JarClassLoader loader = new JarClassLoader("src/test/resources/parser/common/junit-empty.jar");
//        JarClassLoader loader = new JarClassLoader("/home/al1/IdeaProjects/ModelXmlParser/parser/src/test/resources/parser/common/junit-empty.jar");

        List<Class> classes = loader.getClasses();
        assertTrue(classes.isEmpty());
    }

    @Test(expected = LogicException.class)
    public void testNegativePathNameEmpty() throws IOException {
        JarClassLoader loader = new JarClassLoader("");
        loader.getClasses();
    }

    @Test(expected = LogicException.class)
    public void testNegativePathNameNull() throws IOException {
        JarClassLoader loader = new JarClassLoader(null);
        loader.getClasses();
    }

    private List<String> getJUnitJarClassesList() {
        return new ArrayList<String>(Arrays.asList(
                "junit.awtui.AboutDialog$1",
                "junit.awtui.AboutDialog$2",
                "junit.awtui.AboutDialog",
                "junit.awtui.Logo",
                "junit.awtui.ProgressBar",
                "junit.awtui.TestRunner$1",
                "junit.awtui.TestRunner$10",
                "junit.awtui.TestRunner$2",
                "junit.awtui.TestRunner$3",
                "junit.awtui.TestRunner$4",
                "junit.awtui.TestRunner$5",
                "junit.awtui.TestRunner$6",
                "junit.awtui.TestRunner$7",
                "junit.awtui.TestRunner$8",
                "junit.awtui.TestRunner$9",
                "junit.awtui.TestRunner",
                "junit.extensions.ActiveTestSuite$1",
                "junit.extensions.ActiveTestSuite",
                "junit.extensions.ExceptionTestCase",
                "junit.extensions.RepeatedTest",
                "junit.extensions.TestDecorator",
                "junit.extensions.TestSetup$1",
                "junit.extensions.TestSetup",
                "junit.framework.Assert",
                "junit.framework.AssertionFailedError",
                "junit.framework.ComparisonFailure",
                "junit.framework.Protectable",
                "junit.framework.Test",
                "junit.framework.TestCase",
                "junit.framework.TestFailure",
                "junit.framework.TestListener",
                "junit.framework.TestResult$1",
                "junit.framework.TestResult",
                "junit.framework.TestSuite$1",
                "junit.framework.TestSuite",
                "junit.runner.BaseTestRunner",
                "junit.runner.ClassPathTestCollector",
                "junit.runner.FailureDetailView",
                "junit.runner.LoadingTestCollector",
                "junit.runner.ReloadingTestSuiteLoader",
                "junit.runner.SimpleTestCollector",
                "junit.runner.Sorter$Swapper",
                "junit.runner.Sorter",
                "junit.runner.StandardTestSuiteLoader",
                "junit.runner.TestCaseClassLoader",
                "junit.runner.TestCollector",
                "junit.runner.TestRunListener",
                "junit.runner.TestSuiteLoader",
                "junit.runner.Version",
                "junit.swingui.AboutDialog$1",
                "junit.swingui.AboutDialog$2",
                "junit.swingui.AboutDialog",
                "junit.swingui.CounterPanel",
                "junit.swingui.DefaultFailureDetailView$StackEntryRenderer",
                "junit.swingui.DefaultFailureDetailView$StackTraceListModel",
                "junit.swingui.DefaultFailureDetailView",
                "junit.swingui.FailureRunView$1",
                "junit.swingui.FailureRunView$FailureListCellRenderer",
                "junit.swingui.FailureRunView",
                "junit.swingui.ProgressBar",
                "junit.swingui.StatusLine",
                "junit.swingui.TestHierarchyRunView$1",
                "junit.swingui.TestHierarchyRunView",
                "junit.swingui.TestRunContext",
                "junit.swingui.TestRunner$1",
                "junit.swingui.TestRunner$10",
                "junit.swingui.TestRunner$11",
                "junit.swingui.TestRunner$12",
                "junit.swingui.TestRunner$13",
                "junit.swingui.TestRunner$14",
                "junit.swingui.TestRunner$15",
                "junit.swingui.TestRunner$16",
                "junit.swingui.TestRunner$17",
                "junit.swingui.TestRunner$18",
                "junit.swingui.TestRunner$19",
                "junit.swingui.TestRunner$2",
                "junit.swingui.TestRunner$3",
                "junit.swingui.TestRunner$4",
                "junit.swingui.TestRunner$5",
                "junit.swingui.TestRunner$6",
                "junit.swingui.TestRunner$7",
                "junit.swingui.TestRunner$8",
                "junit.swingui.TestRunner$9",
                "junit.swingui.TestRunner",
                "junit.swingui.TestRunView",
                "junit.swingui.TestSelector$1",
                "junit.swingui.TestSelector$2",
                "junit.swingui.TestSelector$3",
                "junit.swingui.TestSelector$4",
                "junit.swingui.TestSelector$DoubleClickListener",
                "junit.swingui.TestSelector$KeySelectListener",
                "junit.swingui.TestSelector$ParallelSwapper",
                "junit.swingui.TestSelector$TestCellRenderer",
                "junit.swingui.TestSelector",
                "junit.swingui.TestSuitePanel$1",
                "junit.swingui.TestSuitePanel$TestTreeCellRenderer",
                "junit.swingui.TestSuitePanel",
                "junit.swingui.TestTreeModel",
                "junit.textui.ResultPrinter",
                "junit.textui.TestRunner"));
    }
}
