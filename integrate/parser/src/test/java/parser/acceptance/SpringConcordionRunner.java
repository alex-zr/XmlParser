package parser.acceptance;

import org.concordion.api.ResultSummary;
import org.concordion.internal.FixtureRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 25.04.12
 */
public class SpringConcordionRunner extends SpringJUnit4ClassRunner {



        private final Description fixtureDescription;
        private final FrameworkMethod fakeMethod;
        private ResultSummary result;

        public SpringConcordionRunner(final Class<?> fixtureClass) throws InitializationError {
            super(fixtureClass);
            String testDescription = ("[Concordion Specification for '" + fixtureClass.getSimpleName()).replaceAll("Test$", "']");
            fixtureDescription = Description.createTestDescription(fixtureClass, testDescription);
            try {
                fakeMethod = new FakeFrameworkMethod();
            } catch (Exception e) {
                throw new InitializationError("Failed to initialize ConcordionRunner");
            }
        }

        static class FakeFrameworkMethod extends FrameworkMethod {

            public FakeFrameworkMethod() {
                super(null);
            }

            @Override
            public String getName() {
                return "[Concordion Specification]";
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
                return null;
            }

            @Override
            public int hashCode() {
                return 1;
            }

            public void specs() {
                System.out.println("specs");
            }

            @Override
            public Method getMethod() {
                try {
                    return getClass().getMethod("specs", new Class<?>[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            List<FrameworkMethod> children = new ArrayList<FrameworkMethod>();
            children.addAll(super.getChildren());
            children.add(fakeMethod);
            return children;
        }

        @Override
        protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
            if (method == fakeMethod) {
                return specExecStatement(test);
            }
            return super.methodInvoker(method, test);
        }

        @Override
        protected Description describeChild(final FrameworkMethod method) {
            if (method == fakeMethod) {
                return fixtureDescription;
            }
            return super.describeChild(method);
        }

        @Override
        protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
            super.runChild(method, notifier);
            if (result != null && result.getIgnoredCount() > 0) {
                notifier.fireTestIgnored(fixtureDescription);
            }
        }

        protected Statement specExecStatement(final Object fixture) {
            return new Statement() {

                @Override
                public void evaluate() throws Throwable {
                    result = new FixtureRunner().run(fixture);
                }
            };
        }

        @Override
        protected void validateInstanceMethods(final List<Throwable> errors) {
            validatePublicVoidNoArgMethods(After.class, false, errors);
            validatePublicVoidNoArgMethods(Before.class, false, errors);
            validateTestMethods(errors);
        }

        @Override
        protected boolean isTestMethodIgnored(final FrameworkMethod frameworkMethod) {
            return frameworkMethod != fakeMethod && super.isTestMethodIgnored(frameworkMethod);
        }

    }
